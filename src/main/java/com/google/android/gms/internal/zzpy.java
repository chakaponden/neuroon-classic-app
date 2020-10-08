package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzpy extends zze<zzpy> {
    public String mCategory;
    public String zzaUO;
    public String zzaVa;
    public long zzaVb;

    public String getLabel() {
        return this.zzaUO;
    }

    public long getTimeInMillis() {
        return this.zzaVb;
    }

    public void setTimeInMillis(long milliseconds) {
        this.zzaVb = milliseconds;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("variableName", this.zzaVa);
        hashMap.put("timeInMillis", Long.valueOf(this.zzaVb));
        hashMap.put("category", this.mCategory);
        hashMap.put("label", this.zzaUO);
        return zzF(hashMap);
    }

    public String zzAZ() {
        return this.mCategory;
    }

    public String zzBh() {
        return this.zzaVa;
    }

    public void zza(zzpy zzpy) {
        if (!TextUtils.isEmpty(this.zzaVa)) {
            zzpy.zzeL(this.zzaVa);
        }
        if (this.zzaVb != 0) {
            zzpy.setTimeInMillis(this.zzaVb);
        }
        if (!TextUtils.isEmpty(this.mCategory)) {
            zzpy.zzeE(this.mCategory);
        }
        if (!TextUtils.isEmpty(this.zzaUO)) {
            zzpy.zzeG(this.zzaUO);
        }
    }

    public void zzeE(String str) {
        this.mCategory = str;
    }

    public void zzeG(String str) {
        this.zzaUO = str;
    }

    public void zzeL(String str) {
        this.zzaVa = str;
    }
}
