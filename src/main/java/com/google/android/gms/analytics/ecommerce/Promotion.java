package com.google.android.gms.analytics.ecommerce;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;
import java.util.Map;

public class Promotion {
    public static final String ACTION_CLICK = "click";
    public static final String ACTION_VIEW = "view";
    Map<String, String> zzPU = new HashMap();

    /* access modifiers changed from: package-private */
    public void put(String name, String value) {
        zzx.zzb(name, (Object) "Name should be non-null");
        this.zzPU.put(name, value);
    }

    public Promotion setCreative(String value) {
        put("cr", value);
        return this;
    }

    public Promotion setId(String value) {
        put("id", value);
        return this;
    }

    public Promotion setName(String value) {
        put("nm", value);
        return this;
    }

    public Promotion setPosition(String value) {
        put("ps", value);
        return this;
    }

    public String toString() {
        return zze.zzO(this.zzPU);
    }

    public Map<String, String> zzba(String str) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : this.zzPU.entrySet()) {
            hashMap.put(str + ((String) next.getKey()), next.getValue());
        }
        return hashMap;
    }
}
