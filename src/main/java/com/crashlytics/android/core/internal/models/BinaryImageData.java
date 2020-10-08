package com.crashlytics.android.core.internal.models;

public class BinaryImageData {
    public final long baseAddress;
    public final String id;
    public final String path;
    public final long size;

    public BinaryImageData(long baseAddress2, long size2, String path2, String id2) {
        this.baseAddress = baseAddress2;
        this.size = size2;
        this.path = path2;
        this.id = id2;
    }
}
