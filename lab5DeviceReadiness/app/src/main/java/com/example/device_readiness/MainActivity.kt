package com.example.device_readiness


import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.testTag
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.device_readiness.ui.theme.Device_ReadinessTheme
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun DeviceReadinessApp() {
    var deviceName by rememberSaveable { mutableStateOf("") }
    var batteryText by rememberSaveable { mutableStateOf("") }
    var networkAvailable by rememberSaveable { mutableStateOf(true) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val battery = batteryText.toIntOrNull()
    val nameError = submitted && deviceName.trim().isEmpty()
    val batteryError = submitted && (battery == null || battery !in 0..100)
    val valid = submitted && !nameError && !batteryError

    val resultReady = if (valid) {
        battery!! >= 30 && networkAvailable
    } else {
        null
    }

    // This calls the stateless screen (which we will build next in Task 5)
    DeviceReadinessScreen(
        deviceName = deviceName,
        batteryText = batteryText,
        networkAvailable = networkAvailable,
        nameError = nameError,
        batteryError = batteryError,
        resultReady = resultReady,
        onDeviceNameChange = { deviceName = it; submitted = false },
        onBatteryChange = { batteryText = it; submitted = false },
        onNetworkChange = { networkAvailable = it; submitted = false },
        onEvaluate = { submitted = true },
        onClear = {
            deviceName = ""
            batteryText = ""
            networkAvailable = true
            submitted = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceReadinessScreen(
    deviceName: String,
    batteryText: String,
    networkAvailable: Boolean,
    nameError: Boolean,
    batteryError: Boolean,
    resultReady: Boolean?,
    onDeviceNameChange: (String) -> Unit,
    onBatteryChange: (String) -> Unit,
    onNetworkChange: (Boolean) -> Unit,
    onEvaluate: () -> Unit,
    onClear: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.screen_title)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.screen_intro),
                style = MaterialTheme.typography.bodyLarge
            )
            // We will add the fields, switch, buttons, and result card here in Tasks 6-8.
            // Device Name Field
            OutlinedTextField(
                value = deviceName,
                onValueChange = onDeviceNameChange,
                label = { Text(stringResource(R.string.device_name_label)) },
                singleLine = true,
                isError = nameError,
                supportingText = {
                    if (nameError) Text(stringResource(R.string.error_device_name_required))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deviceNameInput")
            )

            // Battery Percentage Field
            OutlinedTextField(
                value = batteryText,
                onValueChange = onBatteryChange,
                label = { Text(stringResource(R.string.battery_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = batteryError,
                supportingText = {
                    Text(
                        if (batteryError) stringResource(R.string.error_battery_invalid)
                        else stringResource(R.string.battery_support)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("batteryInput")
            )
            // Task 7: Network Switch Row
            val networkDescription = stringResource(R.string.network_switch_description)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.network_label),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = networkAvailable,
                    onCheckedChange = onNetworkChange,
                    modifier = Modifier
                        .semantics { contentDescription = networkDescription }
                        .testTag("networkSwitch")
                )
            }

            // Task 7: Action Buttons Row (Evaluate & Clear)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEvaluate,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("evaluateButton")
                ) {
                    Text(stringResource(R.string.evaluate))
                }

                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.clear))
                }
                // Task 8: Display readiness result conditionally
                if (resultReady != null) {
                    ReadinessResultCard(
                        deviceName = deviceName.trim(),
                        batteryPercent = batteryText.toInt(),
                        networkAvailable = networkAvailable,
                        ready = resultReady
                    )
                }
            }
        }
    }
}

@Composable
fun ReadinessResultCard(
    deviceName: String,
    batteryPercent: Int,
    networkAvailable: Boolean,
    ready: Boolean
) {
    val status = stringResource(
        if (ready) R.string.status_ready else R.string.status_attention
    )
    val network = stringResource(
        if (networkAvailable) R.string.network_available
        else R.string.network_unavailable
    )
    val message = stringResource(
        if (ready) R.string.result_ready else R.string.result_attention,
        deviceName
    )
    val resultDescription = stringResource(
        R.string.result_accessibility,
        deviceName, batteryPercent, network, status
    )
    val batteryDescription = stringResource(
        R.string.battery_progress_description, batteryPercent
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = resultDescription
            }
            .testTag("resultCard")
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(status, style = MaterialTheme.typography.titleLarge)
            Text(message)
            LinearProgressIndicator(
                progress = { batteryPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = batteryDescription }
            )
        }
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Device_ReadinessTheme {
                DeviceReadinessApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Device_ReadinessTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceReadinessReadyPreview() {
    Device_ReadinessTheme {
        DeviceReadinessScreen(
            deviceName = "Sensor-A",
            batteryText = "68",
            networkAvailable = true,
            nameError = false,
            batteryError = false,
            resultReady = true,
            onDeviceNameChange = {},
            onBatteryChange = {},
            onNetworkChange = {},
            onEvaluate = {},
            onClear = {}
        )
    }
}