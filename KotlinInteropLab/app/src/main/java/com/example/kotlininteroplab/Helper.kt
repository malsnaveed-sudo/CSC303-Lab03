package com.example.kotlininteroplab

class Helper {
    fun runTask6Verification(): String {
        val testCases = listOf(
            Pair(" Sensor-A ", 15),
            Pair(null, null),
            Pair("   ", -1),
            Pair("Gateway-E", 140)
        )

        val sb = StringBuilder()
        sb.append("Task 6 Test Output:\n\n")

        for ((name, battery) in testCases) {
            val normName = DeviceAnalyzer.normalizeName(name)
            val battText = DeviceAnalyzer.formatBattery(battery)
            sb.append("Input: name=\"$name\", battery=$battery\n")
            sb.append(" -> Output: Name=\"$normName\" | Battery=\"$battText\"\n\n")
        }

        return sb.toString().trim()
    }
}