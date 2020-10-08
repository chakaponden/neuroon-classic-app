package com.crashlytics.android.core.internal.models;

public class SessionEventData {
    public final BinaryImageData[] binaryImages;
    public final CustomAttributeData[] customAttributes;
    public final DeviceData deviceData;
    public final SignalData signal;
    public final ThreadData[] threads;
    public final long timestamp;

    public SessionEventData(long timestamp2, SignalData signal2, ThreadData[] threads2, BinaryImageData[] binaryImages2, CustomAttributeData[] customAttributes2, DeviceData deviceData2) {
        this.timestamp = timestamp2;
        this.signal = signal2;
        this.threads = threads2;
        this.binaryImages = binaryImages2;
        this.customAttributes = customAttributes2;
        this.deviceData = deviceData2;
    }
}
