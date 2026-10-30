# CSC303 Lab 05: Jetpack Compose UI State, Validation, and Accessibility

## 1. Validation and Readiness Rules
The completed application follows these defined rules:
* **Device Name:** Required after trimming; blank input is invalid[cite: 1].
* **Battery Percentage:** A whole number from 0 through 100; missing, non-numeric, and out-of-range inputs are invalid[cite: 1].
* **Network Available:** Boolean selected through a Material Switch[cite: 1].
* **Ready:** Valid form, battery at least 30, and network available[cite: 1].
* **Needs Attention:** Valid form, but battery below 30 or network unavailable[cite: 1].
* **Stale Result Prevention:** Any change to input fields or switches immediately hides the previous result card so it cannot describe outdated values[cite: 1].

## 2. State and Event Map
| State | Displayed by | Changed by |
| :--- | :--- | :--- |
| `deviceName` | Device name field and result card[cite: 1] | `onDeviceNameChange` and `Clear`[cite: 1] |
| `batteryText` | Battery field, validation check, and progress indicator[cite: 1] | `onBatteryChange` and `Clear`[cite: 1] |
| `networkAvailable` | Switch and readiness evaluation logic[cite: 1] | `onNetworkChange` and `Clear`[cite: 1] |
| `submitted` | Error visibility and result card visibility[cite: 1] | `Evaluate`, any text/switch edit, and `Clear`[cite: 1] |

## 3. Verification Table (T1 - T8)
| ID | Input or Action | Expected Behaviour | Actual Behaviour | Pass/Fail |
| :--- | :--- | :--- | :--- | :--- |
| **T1** | Clean build and launch[cite: 1] | Material 3 screen opens with labelled fields, switch, and actions[cite: 1]. | App opens correctly with UI components rendered[cite: 1]. | Pass |
| **T2** | Evaluate blank form[cite: 1] | Both visible validation messages appear; no result card is shown[cite: 1]. | Validation messages displayed; card hidden[cite: 1]. | Pass |
| **T3** | Battery text, -1 or 101[cite: 1] | Battery error appears and the form remains editable[cite: 1]. | Error shown, form editable[cite: 1]. | Pass |
| **T4** | Battery 0, 29, 30 and 100 with network on[cite: 1] | Boundaries are valid; 0/29 need attention and 30/100 are ready[cite: 1]. | Correct statuses evaluated per boundary[cite: 1]. | Pass |
| **T5** | Battery 80; toggle network off[cite: 1] | Result changes to needs attention only after Evaluate is selected again[cite: 1]. | Result updates correctly upon re-evaluation[cite: 1]. | Pass |
| **T6** | Evaluate valid values, then edit a field[cite: 1] | The old result card disappears so stale information is not displayed[cite: 1]. | Result card hides instantly upon editing[cite: 1]. | Pass |
| **T7** | Enter values and recreate the activity[cite: 1] | Supported form values are restored through rememberSaveable[cite: 1]. | State successfully preserved on rotation/recreation[cite: 1]. | Pass |
| **T8** | Increase font size and use accessibility focus[cite: 1] | Content remains reachable; labels, switch, and result are announced meaningfully[cite: 1]. | Semantics tree and font scale verified[cite: 1]. | Pass |

## 4. Accessibility Review
* **Visible Labels:** Every field retains a visible text label; content descriptions do not replace form labels[cite: 1].
* **Switch Semantics:** The Material switch exposes role, checked state, and a resource-backed content description (`network_switch_description`)[cite: 1].
* **Merged Descendants:** The result card uses `semantics(mergeDescendants = true)` with a complete spoken description (`result_accessibility`) to prevent redundant or confusing screen reader announcements[cite: 1].
* **Non-Colour Cues:** Readiness and validation errors are explicitly communicated via text statuses and error messages rather than color alone[cite: 1].

## 5. UI Test Results
* `blankForm_showsBothValidationMessages`: Verified that clicking Evaluate on an empty form triggers both device name and battery validation error messages[cite: 1].
* `validForm_showsReadyResult`: Verified that entering valid input ("Sensor-A", "80", network enabled) displays the expected ready result card[cite: 1].