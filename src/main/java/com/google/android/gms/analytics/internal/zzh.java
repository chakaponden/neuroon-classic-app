package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzx;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class zzh {
    private final String zzPO;
    private final long zzQD;
    private final String zzQE;
    private final boolean zzQF;
    private long zzQG;
    private final Map<String, String> zzxA;

    public zzh(long j, String str, String str2, boolean z, long j2, Map<String, String> map) {
        zzx.zzcM(str);
        zzx.zzcM(str2);
        this.zzQD = j;
        this.zzPO = str;
        this.zzQE = str2;
        this.zzQF = z;
        this.zzQG = j2;
        if (map != null) {
            this.zzxA = new HashMap(map);
        } else {
            this.zzxA = Collections.emptyMap();
        }
    }

    public String getClientId() {
        return this.zzPO;
    }

    public long zzjD() {
        return this.zzQD;
    }

    public String zzjE() {
        return this.zzQE;
    }

    public boolean zzjF() {
        return this.zzQF;
    }

    public long zzjG() {
        return this.zzQG;
    }

    public Map<String, String> zzn() {
        return this.zzxA;
    }

    public void zzn(long j) {
        this.zzQG = j;
    }
}
