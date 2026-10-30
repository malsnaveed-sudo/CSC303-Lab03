package com.example.kotlininteroplab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class LegacyDeviceRepository {
    private LegacyDeviceRepository() { }

    @NonNull
    public static List<DeviceReading> loadDevices() {
        return Arrays.asList(
                new DeviceReading("Sensor-A", 15, "Sensor"),
                new DeviceReading(null, 85, "Gateway"),
                new DeviceReading("Camera-C", null, "Camera"),
                new DeviceReading("Tracker-D", 40, "Tracker"),
                new DeviceReading("Gateway-E", 140, "Gateway")
        );
    }

    @Nullable // Restored @Nullable annotation
    public static String findAlias(@NonNull String name) {
        if (name.equals("Sensor-A")) return "Laboratory sensor";
        if (name.equals("Camera-C")) return "Entrance camera";
        return null;
    }
}