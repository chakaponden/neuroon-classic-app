package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzpu extends zze<zzpu> {
    private String mCategory;
    private String zzSU;
    private long zzaDV;
    private String zzaUO;

    public String getAction() {
        return this.zzSU;
    }

    public String getLabel() {
        return this.zzaUO;
    }

    public long getValue() {
        return this.zzaDV;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("category", this.mCategory);
        hashMap.put("action", this.zzSU);
        hashMap.put("label", this.zzaUO);
        hashMap.put("value", Long.valueOf(this.zzaDV));
        return zzF(hashMap);
    }

    public String zzAZ() {
        return this.mCategory;
    }

    public void zzN(long j) {
        this.zzaDV = j;
    }

    public void zza(zzpu zzpu) {
        if (!TextUtils.isEmpty(this.mCategory)) {
            zzpu.zzeE(this.mCategory);
        }
        if (!TextUtils.isEmpty(this.zzSU)) {
            zzpu.zzeF(this.zzSU);
        }
        if (!TextUtils.isEmpty(this.zzaUO)) {
            zzpu.zzeG(this.zzaUO);
        }
        if (this.zzaDV != 0) {
            zzpu.zzN(this.zzaDV);
        }
    }

    public void zzeE(String str) {
        this.mCategory = str;
    }

    public void zzeF(String str) {
        this.zzSU = str;
    }

    public void zzeG(String str) {
        this.zzaUO = str;
    }
}
