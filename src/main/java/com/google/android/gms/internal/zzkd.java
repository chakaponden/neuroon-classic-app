package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zze;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class zzkd extends zze<zzkd> {
    private final Map<String, Object> zzxA = new HashMap();

    private String zzaW(String str) {
        zzx.zzcM(str);
        if (str != null && str.startsWith("&")) {
            str = str.substring(1);
        }
        zzx.zzh(str, "Name can not be empty or \"&\"");
        return str;
    }

    public void set(String name, String value) {
        this.zzxA.put(zzaW(name), value);
    }

    public String toString() {
        return zzF(this.zzxA);
    }

    public void zza(zzkd zzkd) {
        zzx.zzz(zzkd);
        zzkd.zzxA.putAll(this.zzxA);
    }

    public Map<String, Object> zziR() {
        return Collections.unmodifiableMap(this.zzxA);
    }
}
