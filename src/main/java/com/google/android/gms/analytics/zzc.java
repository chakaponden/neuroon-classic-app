package com.google.android.gms.analytics;

import com.google.android.gms.analytics.internal.zzae;

public final class zzc {
    public static String zzT(int i) {
        return zzb("&cd", i);
    }

    public static String zzU(int i) {
        return zzb("cd", i);
    }

    public static String zzV(int i) {
        return zzb("&cm", i);
    }

    public static String zzW(int i) {
        return zzb("cm", i);
    }

    public static String zzX(int i) {
        return zzb("&pr", i);
    }

    public static String zzY(int i) {
        return zzb("pr", i);
    }

    public static String zzZ(int i) {
        return zzb("&promo", i);
    }

    public static String zzaa(int i) {
        return zzb("promo", i);
    }

    public static String zzab(int i) {
        return zzb("pi", i);
    }

    public static String zzac(int i) {
        return zzb("&il", i);
    }

    public static String zzad(int i) {
        return zzb("il", i);
    }

    public static String zzae(int i) {
        return zzb("cd", i);
    }

    public static String zzaf(int i) {
        return zzb("cm", i);
    }

    private static String zzb(String str, int i) {
        if (i >= 1) {
            return str + i;
        }
        zzae.zzf("index out of range for prefix", str);
        return "";
    }
}
