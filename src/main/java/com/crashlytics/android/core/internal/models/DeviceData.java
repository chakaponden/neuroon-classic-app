package com.crashlytics.android.core.internal.models;

public class DeviceData {
    public final long availableInternalStorage;
    public final long availablePhysicalMemory;
    public final int batteryCapacity;
    public final int batteryVelocity;
    public final int orientation;
    public final boolean proximity;
    public final long totalInternalStorage;
    public final long totalPhysicalMemory;

    public DeviceData(int orientation2, long totalPhysicalMemory2, long totalInternalStorage2, long availablePhysicalMemory2, long availableInternalStorage2, int batteryCapacity2, int batteryVelocity2, boolean proximity2) {
        this.orientation = orientation2;
        this.totalPhysicalMemory = totalPhysicalMemory2;
        this.totalInternalStorage = totalInternalStorage2;
        this.availablePhysicalMemory = availablePhysicalMemory2;
        this.availableInternalStorage = availableInternalStorage2;
        this.batteryCapacity = batteryCapacity2;
        this.batteryVelocity = batteryVelocity2;
        this.proximity = proximity2;
    }
}
