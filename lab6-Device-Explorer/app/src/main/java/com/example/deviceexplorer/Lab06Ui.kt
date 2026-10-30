package com.example.deviceexplorer

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun DeviceExplorerApp() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = HOME) {
        composable(HOME) { entry ->
            ObserveDestination(entry)
            HomeScreen(onOpen = { id ->
                nav.navigate("details/$id") { launchSingleTop = true }
            })
        }
        composable(
            route = DETAILS,
            arguments = listOf(navArgument("deviceId") {
                type = NavType.StringType
            })
        ) { entry ->
            ObserveDestination(entry)
            val id = entry.arguments?.getString("deviceId")
            DetailsScreen(
                device = findDevice(id),
                onBack = { nav.popBackStack() },
                onSummary = { deviceId, reviewed ->
                    nav.navigate("summary/$deviceId?reviewed=$reviewed") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = SUMMARY,
            arguments = listOf(
                navArgument("deviceId") { type = NavType.StringType },
                navArgument("reviewed") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { entry ->
            ObserveDestination(entry)
            SummaryScreen(
                device = findDevice(entry.arguments?.getString("deviceId")),
                reviewed = entry.arguments?.getBoolean("reviewed") ?: false,
                onBack = { nav.popBackStack() },
                onDone = { nav.popBackStack(HOME, inclusive = false) }
            )
        }
    }
}

@Composable
fun LabScreen(title: String, content: @Composable ColumnScope.() -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            content()
        }
    }
}

@Composable
fun HomeScreen(onOpen: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    val matches = devices.filter {
        it.name.contains(query.trim(), ignoreCase = true)
    }
    LabScreen(stringResource(R.string.home_title)) {
        OutlinedTextField(
            value = query, onValueChange = { query = it },
            label = { Text(stringResource(R.string.search_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("filter")
        )
        if (matches.isEmpty()) Text(stringResource(R.string.no_matches))
        matches.forEach { device ->
            Button(
                onClick = { onOpen(device.id) },
                modifier = Modifier.fillMaxWidth()
                    .testTag("open_${device.id}")
            ) { Text(device.name) }
        }
    }
}

@Composable
fun DetailsScreen(
    device: Device?, onBack: () -> Unit,
    onSummary: (String, Boolean) -> Unit
) {
    if (device == null) {
        LabScreen(stringResource(R.string.details_title)) {
            Text(stringResource(R.string.unknown_device))
            Button(onClick = onBack) {
                Text(stringResource(R.string.back_label))
            }
        }
        return
    }
    var note by rememberSaveable(device.id) { mutableStateOf("") }
    var reviewed by rememberSaveable(device.id) { mutableStateOf(false) }
    LabScreen(stringResource(R.string.details_title)) {
        Text(device.name)
        Text(stringResource(R.string.battery_value, device.battery))
        OutlinedTextField(
            value = note, onValueChange = { note = it.take(200) },
            label = { Text(stringResource(R.string.note_label)) },
            modifier = Modifier.fillMaxWidth().testTag("note")
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = reviewed, onCheckedChange = { reviewed = it },
                modifier = Modifier.testTag("reviewed")
            )
            Text(stringResource(R.string.reviewed_label))
        }
        Button(
            onClick = { onSummary(device.id, reviewed) },
            modifier = Modifier.testTag("summary")
        ) { Text(stringResource(R.string.open_summary)) }
        OutlinedButton(onClick = onBack) {
            Text(stringResource(R.string.back_label))
        }
    }
}

@Composable
fun SummaryScreen(
    device: Device?, reviewed: Boolean,
    onBack: () -> Unit, onDone: () -> Unit
) {
    LabScreen(stringResource(R.string.summary_title)) {
        if (device == null) {
            Text(stringResource(R.string.unknown_device))
        } else {
            Text(device.name)
            Text(stringResource(R.string.battery_value, device.battery))
            Text(stringResource(
                if (reviewed) R.string.review_yes else R.string.review_no
            ))
        }
        OutlinedButton(onClick = onBack) {
            Text(stringResource(R.string.back_label))
        }
        Button(onClick = onDone, modifier = Modifier.testTag("done")) {
            Text(stringResource(R.string.done_label))
        }
    }
}

@Composable
fun ObserveDestination(entry: NavBackStackEntry) {
    DisposableEffect(entry) {
        val label = entry.destination.route ?: "unknown"
        val observer = LifecycleEventObserver { _, event ->
            Log.d("Lab06Destination", "$label: $event")
        }
        entry.lifecycle.addObserver(observer)
        onDispose {
            entry.lifecycle.removeObserver(observer)
            Log.d("Lab06Destination", "$label: observer removed")
        }
    }
}