package com.example.kotlininteroplab

data class DeviceSummary(
    val total: Int,
    val validBatteryCount: Int,
    val lowBatteryCount: Int,
    val invalidOrMissingCount: Int,
    val averageBattery: Double
)