package com.crashlytics.android.core.internal.models;

public class SignalData {
    public final String code;
    public final long faultAddress;
    public final String name;

    public SignalData(String name2, String code2, long faultAddress2) {
        this.name = name2;
        this.code = code2;
        this.faultAddress = faultAddress2;
    }
}
