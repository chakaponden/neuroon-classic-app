package com.google.android.gms.internal;

import com.google.android.gms.measurement.zze;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class zzkb extends zze<zzkb> {
    private Map<Integer, String> zzPL = new HashMap(4);

    public String toString() {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : this.zzPL.entrySet()) {
            hashMap.put("dimension" + next.getKey(), next.getValue());
        }
        return zzF(hashMap);
    }

    public void zza(zzkb zzkb) {
        zzkb.zzPL.putAll(this.zzPL);
    }

    public Map<Integer, String> zziP() {
        return Collections.unmodifiableMap(this.zzPL);
    }
}
