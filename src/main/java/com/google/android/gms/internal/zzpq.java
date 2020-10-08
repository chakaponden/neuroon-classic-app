package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzpq extends zze<zzpq> {
    private String zzSE;
    private String zzSF;
    private String zzaUE;
    private String zzaUa;

    public void setAppId(String value) {
        this.zzaUa = value;
    }

    public void setAppInstallerId(String value) {
        this.zzaUE = value;
    }

    public void setAppName(String value) {
        this.zzSE = value;
    }

    public void setAppVersion(String value) {
        this.zzSF = value;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("appName", this.zzSE);
        hashMap.put("appVersion", this.zzSF);
        hashMap.put("appId", this.zzaUa);
        hashMap.put("appInstallerId", this.zzaUE);
        return zzF(hashMap);
    }

    public String zzAJ() {
        return this.zzaUE;
    }

    public void zza(zzpq zzpq) {
        if (!TextUtils.isEmpty(this.zzSE)) {
            zzpq.setAppName(this.zzSE);
        }
        if (!TextUtils.isEmpty(this.zzSF)) {
            zzpq.setAppVersion(this.zzSF);
        }
        if (!TextUtils.isEmpty(this.zzaUa)) {
            zzpq.setAppId(this.zzaUa);
        }
        if (!TextUtils.isEmpty(this.zzaUE)) {
            zzpq.setAppInstallerId(this.zzaUE);
        }
    }

    public String zzlg() {
        return this.zzSE;
    }

    public String zzli() {
        return this.zzSF;
    }

    public String zzwK() {
        return this.zzaUa;
    }
}
