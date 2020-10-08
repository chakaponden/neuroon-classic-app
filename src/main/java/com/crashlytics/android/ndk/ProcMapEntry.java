package com.crashlytics.android.ndk;

class ProcMapEntry {
    public final long address;
    public final String path;
    public final long size;

    public ProcMapEntry(long address2, long size2, String path2) {
        this.address = address2;
        this.size = size2;
        this.path = path2;
    }
}
