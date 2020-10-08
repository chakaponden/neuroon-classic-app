package com.google.android.gms.internal;

import com.google.android.gms.measurement.zze;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class zzkc extends zze<zzkc> {
    private Map<Integer, Double> zzPM = new HashMap(4);

    public String toString() {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : this.zzPM.entrySet()) {
            hashMap.put("metric" + next.getKey(), next.getValue());
        }
        return zzF(hashMap);
    }

    public void zza(zzkc zzkc) {
        zzkc.zzPM.putAll(this.zzPM);
    }

    public Map<Integer, Double> zziQ() {
        return Collections.unmodifiableMap(this.zzPM);
    }
}
