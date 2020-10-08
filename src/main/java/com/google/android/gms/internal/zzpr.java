package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzpr extends zze<zzpr> {
    private String mName;
    private String zzaPI;
    private String zzaUF;
    private String zzaUG;
    private String zzaUH;
    private String zzaUI;
    private String zzaUJ;
    private String zzaUK;
    private String zzxG;
    private String zzyv;

    public String getContent() {
        return this.zzxG;
    }

    public String getId() {
        return this.zzyv;
    }

    public String getName() {
        return this.mName;
    }

    public String getSource() {
        return this.zzaPI;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("name", this.mName);
        hashMap.put("source", this.zzaPI);
        hashMap.put("medium", this.zzaUF);
        hashMap.put("keyword", this.zzaUG);
        hashMap.put("content", this.zzxG);
        hashMap.put("id", this.zzyv);
        hashMap.put("adNetworkId", this.zzaUH);
        hashMap.put("gclid", this.zzaUI);
        hashMap.put("dclid", this.zzaUJ);
        hashMap.put("aclid", this.zzaUK);
        return zzF(hashMap);
    }

    public String zzAK() {
        return this.zzaUF;
    }

    public String zzAL() {
        return this.zzaUG;
    }

    public String zzAM() {
        return this.zzaUH;
    }

    public String zzAN() {
        return this.zzaUI;
    }

    public String zzAO() {
        return this.zzaUJ;
    }

    public String zzAP() {
        return this.zzaUK;
    }

    public void zza(zzpr zzpr) {
        if (!TextUtils.isEmpty(this.mName)) {
            zzpr.setName(this.mName);
        }
        if (!TextUtils.isEmpty(this.zzaPI)) {
            zzpr.zzev(this.zzaPI);
        }
        if (!TextUtils.isEmpty(this.zzaUF)) {
            zzpr.zzew(this.zzaUF);
        }
        if (!TextUtils.isEmpty(this.zzaUG)) {
            zzpr.zzex(this.zzaUG);
        }
        if (!TextUtils.isEmpty(this.zzxG)) {
            zzpr.zzey(this.zzxG);
        }
        if (!TextUtils.isEmpty(this.zzyv)) {
            zzpr.zzez(this.zzyv);
        }
        if (!TextUtils.isEmpty(this.zzaUH)) {
            zzpr.zzeA(this.zzaUH);
        }
        if (!TextUtils.isEmpty(this.zzaUI)) {
            zzpr.zzeB(this.zzaUI);
        }
        if (!TextUtils.isEmpty(this.zzaUJ)) {
            zzpr.zzeC(this.zzaUJ);
        }
        if (!TextUtils.isEmpty(this.zzaUK)) {
            zzpr.zzeD(this.zzaUK);
        }
    }

    public void zzeA(String str) {
        this.zzaUH = str;
    }

    public void zzeB(String str) {
        this.zzaUI = str;
    }

    public void zzeC(String str) {
        this.zzaUJ = str;
    }

    public void zzeD(String str) {
        this.zzaUK = str;
    }

    public void zzev(String str) {
        this.zzaPI = str;
    }

    public void zzew(String str) {
        this.zzaUF = str;
    }

    public void zzex(String str) {
        this.zzaUG = str;
    }

    public void zzey(String str) {
        this.zzxG = str;
    }

    public void zzez(String str) {
        this.zzyv = str;
    }
}
