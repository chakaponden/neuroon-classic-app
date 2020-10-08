package com.google.android.gms.analytics.internal;

public enum zzo {
    NONE,
    GZIP;

    public static zzo zzbn(String str) {
        return "GZIP".equalsIgnoreCase(str) ? GZIP : NONE;
    }
}
