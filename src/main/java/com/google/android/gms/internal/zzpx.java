package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzpx extends zze<zzpx> {
    public String zzSU;
    public String zzaUY;
    public String zzaUZ;

    public String getAction() {
        return this.zzSU;
    }

    public String getTarget() {
        return this.zzaUZ;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("network", this.zzaUY);
        hashMap.put("action", this.zzSU);
        hashMap.put("target", this.zzaUZ);
        return zzF(hashMap);
    }

    public String zzBg() {
        return this.zzaUY;
    }

    public void zza(zzpx zzpx) {
        if (!TextUtils.isEmpty(this.zzaUY)) {
            zzpx.zzeJ(this.zzaUY);
        }
        if (!TextUtils.isEmpty(this.zzSU)) {
            zzpx.zzeF(this.zzSU);
        }
        if (!TextUtils.isEmpty(this.zzaUZ)) {
            zzpx.zzeK(this.zzaUZ);
        }
    }

    public void zzeF(String str) {
        this.zzSU = str;
    }

    public void zzeJ(String str) {
        this.zzaUY = str;
    }

    public void zzeK(String str) {
        this.zzaUZ = str;
    }
}
