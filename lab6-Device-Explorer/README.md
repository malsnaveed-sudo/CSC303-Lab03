# Lab 06: Navigation, Lifecycle, and State Restoration

## Student & Environment Details
- **Student Name:** Maliha Naveed
- **Registration Number:** FA23-BCE-048
- **Course:** CSC303 - Mobile Application Development
- **Android Studio Version:** [e.g., Ladybug | 2024.2.1]
- **Compile SDK / Target SDK:** 34 / 35
- **Android Gradle Plugin (AGP) Version:** [e.g., 8.5.0]
- **Kotlin Version:** [e.g., 2.0.0]
- **Navigation Compose Version:** 2.9.8
- **Device / Emulator Tested:** Pixel 6 Pro (API 34)
- **Git Commit Hash:** 

---

## 1. Route Contract and Navigation Structure

| Destination | Route String | Route Arguments | Description / Behavior |
| :--- | :--- | :--- | :--- |
| **Home** | `home` | None | Lists available devices with a search filter. Filter state uses `rememberSaveable`. |
| **Details** | `details/{deviceId}` | `deviceId` (StringType) | Displays device details, note input field, and reviewed checkbox. Note and reviewed state use `rememberSaveable(device.id)`. |
| **Summary** | `summary/{deviceId}?reviewed={reviewed}` | `deviceId` (StringType), `reviewed` (BoolType, default=false) | Displays device name, battery, and review completion status. Contains Back and Done buttons. |

---

## 2. Navigation Back-Stack Predictions & Verification

### Action Sequence & Stack History:
1. **Launch App:**
    - **Visible Screen:** Home Screen
    - **Back Stack (Bottom to Top):** `[Home]`
2. **Select Device `d1` (Sensor-A):**
    - **Visible Screen:** Details Screen (`d1`)
    - **Back Stack (Bottom to Top):** `[Home, Details(d1)]`
3. **Click "Open Summary":**
    - **Visible Screen:** Summary Screen (`d1`)
    - **Back Stack (Bottom to Top):** `[Home, Details(d1), Summary(d1)]`
4. **Press System Back or In-App Back:**
    - **Visible Screen:** Details Screen (`d1`)
    - **Back Stack (Bottom to Top):** `[Home, Details(d1)]`
5. **Click "Open Summary" then Click "Done":**
    - **Visible Screen:** Home Screen
    - **Back Stack (Bottom to Top):** `[Home]` *(Details and Summary are popped using `popBackStack(HOME, inclusive = false)`)*
6. **Select Device `d1` again from Home:**
    - **Visible Screen:** Details Screen (`d1`) *(New Instance with blank note/checkbox)*
    - **Back Stack (Bottom to Top):** `[Home, Details(d1)]`

---

## 3. Verification Matrix (T1 – T12)

| Test ID | Input / Action | Expected Result | Actual Result | Status |
| :---: | :--- | :--- | :--- | :---: |
| **T1** | Clean build and launch app | Home shows three records (`Sensor-A`, `Camera-B`, `Gateway-C`) and filter field. | As expected. App displays all 3 records. | **PASS** |
| **T2** | Filter "Sensor", open `d1`, press Back | "Sensor" filter remains in search field; existing Home entry is reused. | Filter retained. | **PASS** |
| **T3** | Open each device (`d1`, `d2`, `d3`) | Correct ID resolves to correct name and battery percentage. | Displays matching device data. | **PASS** |
| **T4** | Navigate to `details/missing` | Error text "Device not found" displayed with working Back button; no crash. | Error shown, Back button returns safely. | **PASS** |
| **T5** | Type note + check Reviewed on Details, go to Summary, press Back | Details note text and checkbox state are fully preserved. | State restored upon back navigation. | **PASS** |
| **T6** | Toggle Reviewed true/false, open Summary on new visits | Summary screen accurately reflects the Boolean value passed on each visit. | Summary shows correct review status. | **PASS** |
| **T7** | Rotate screen on Details screen with a note entered | Destination, entered note, and checkbox state are restored across activity recreation. | Retained using `rememberSaveable`. | **PASS** |
| **T8** | Rotate screen on Summary screen | Summary `deviceId` and `reviewed` boolean arguments are restored. | Summary restored correctly. | **PASS** |
| **T9** | Click Done on Summary, reopen `d1` from Home | Home filter remains; newly opened Details screen has blank note and unchecked box. | Previous entry removed, fresh state shown. | **PASS** |
| **T10** | Compare System Back and In-App Back buttons | Both pop back to the expected prior destination consistently. | Identical navigation back behavior. | **PASS** |
| **T11** | Navigate, background app, return, and rotate | Activity and Destination lifecycle logs captured in Logcat and verified. | Lifecycle events logged as expected. | **PASS** |
| **T12** | Test large text and accessibility navigation | Filter controls reachable; single labelled toggle announced properly. | Accessible with accessible toggle target. | **PASS** |

---

## 4. Lifecycle Observations & Interpretation

### Logcat Filter Excerpts:
- **Activity Tag:** `Lab06Activity`
- **Destination Tag:** `Lab06Destination`

### Key Observations:
1. **Destination Transitions vs. Activity Lifecycle:**
    - Navigating between screens (`Home` $\rightarrow$ `Details` $\rightarrow$ `Summary`) triggers `ON_PAUSE`, `ON_STOP`, and `ON_DESTROY` events for the individual `NavBackStackEntry` destinations, while `Lab06Activity` remains in the **`ON_RESUME`** state throughout.
2. **Screen Rotation:**
    - Screen rotation causes `Lab06Activity` to undergo a full recreation cycle (`ON_PAUSE` $\rightarrow$ `ON_STOP` $\rightarrow$ `ON_DESTROY` $\rightarrow$ `ON_CREATE` $\rightarrow$ `ON_START` $\rightarrow$ `ON_RESUME`). The UI states (`query`, `note`, `reviewed`) survive this configuration change because they are stored via `rememberSaveable`.

---

## 5. Independent Exercises & Modifications

1. **Accessible Toggle Row (Exercise 1):**
    - Updated the `Reviewed` row in `DetailsScreen` to use `Modifier.toggleable(value = reviewed, role = Role.Checkbox, onValueChange = { reviewed = it })` with `Checkbox(..., onCheckedChange = null)`. This merges the Checkbox and Text label into a single clickable target for screen readers.
2. **Fourth Device Record (Exercise 2):**
    - Added `Device("d4", "Display-D", 60)` to `DeviceData.kt`. Verified that navigation to `details/d4` and summary review work automatically without modifying the `NavHost` graph.
3. **State Restoration Experiment (Exercise 3):**
    - Temporarily replaced `rememberSaveable` with `remember` for the `note` field in `DetailsScreen`. Upon screen rotation, the note was cleared. Restored `rememberSaveable` to ensure persistent UI state across rotations.