package com.example.deviceexplorer

data class Device(val id: String, val name: String, val battery: Int)

val devices = listOf(
    Device("d1", "Sensor-A", 80),
    Device("d2", "Camera-B", 45),
    Device("d3", "Gateway-C", 20)
)

fun findDevice(id: String?): Device? = devices.find { it.id == id }

const val HOME = "home"
const val DETAILS = "details/{deviceId}"
const val SUMMARY = "summary/{deviceId}?reviewed={reviewed}"