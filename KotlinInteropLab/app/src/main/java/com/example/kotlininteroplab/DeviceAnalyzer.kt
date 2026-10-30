package com.example.kotlininteroplab

object DeviceAnalyzer {

    @JvmStatic
    fun normalizeName(value: String?): String =
        value?.trim()?.takeIf { it.isNotEmpty() } ?: "Unnamed device"

    @JvmStatic
    fun formatBattery(value: Int?): String = when {
        value == null -> "unknown"
        value in 0..100 -> "$value%"
        else -> "invalid ($value)"
    }

    @JvmStatic
    fun summarize(devices: List<DeviceReading>): DeviceSummary {
        val validBatteries = devices.mapNotNull { device ->
            device.batteryPercent?.takeIf { value -> value in 0..100 }
        }

        val average = if (validBatteries.isEmpty()) {
            0.0
        } else {
            validBatteries.average()
        }

        return DeviceSummary(
            total = devices.size,
            validBatteryCount = validBatteries.size,
            lowBatteryCount = validBatteries.count { it < 20 },
            invalidOrMissingCount = devices.size - validBatteries.size,
            averageBattery = average
        )
    }

    @JvmStatic
    fun loadAndBuildReport(): String {
        val devices = LegacyDeviceRepository.loadDevices()
        val summary = summarize(devices)

        val lines = devices
            .map { device ->
                val safeName = normalizeName(device.name)
                val alias = LegacyDeviceRepository.findAlias(safeName)
                val label = alias?.let { "$safeName ($it)" } ?: safeName
                "$label | ${device.category} | " +
                        formatBattery(device.batteryPercent)
            }
            .sorted()
            .joinToString(separator = "\n")

        return buildString {
            appendLine("DEVICE REPORT")
            appendLine(lines)
            appendLine()
            appendLine("Total: ${summary.total}")
            appendLine("Valid batteries: ${summary.validBatteryCount}")
            appendLine("Low batteries: ${summary.lowBatteryCount}")
            appendLine("Invalid or missing: ${summary.invalidOrMissingCount}")
            append("Average: %.1f%%".format(summary.averageBattery))
        }
    }
}