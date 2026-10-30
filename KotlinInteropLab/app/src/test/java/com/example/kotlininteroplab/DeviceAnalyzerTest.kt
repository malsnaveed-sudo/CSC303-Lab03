package com.example.kotlininteroplab

import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceAnalyzerTest {

    @Test
    fun normalizeName_handlesNullBlankAndWhitespace() {
        assertEquals("Unnamed device", DeviceAnalyzer.normalizeName(null))
        assertEquals("Unnamed device", DeviceAnalyzer.normalizeName("   "))
        assertEquals("Sensor-A", DeviceAnalyzer.normalizeName(" Sensor-A "))
    }

    @Test
    fun summarize_ignoresMissingAndOutOfRangeBatteries() {
        val devices = listOf(
            DeviceReading("A", 10, "Sensor"),
            DeviceReading("B", 80, "Gateway"),
            DeviceReading(null, null, "Camera"),
            DeviceReading("C", 120, "Tracker")
        )

        val result = DeviceAnalyzer.summarize(devices)
        assertEquals(4, result.total)
        assertEquals(2, result.validBatteryCount)
        assertEquals(1, result.lowBatteryCount)
        assertEquals(2, result.invalidOrMissingCount)
        assertEquals(45.0, result.averageBattery, 0.001)
    }
}