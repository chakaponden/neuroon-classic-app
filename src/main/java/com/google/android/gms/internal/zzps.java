package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;

public final class zzps extends zze<zzps> {
    public int zzDC;
    public int zzDD;
    public int zzaUL;
    public int zzaUM;
    public int zzaUN;
    private String zzaaL;

    public String getLanguage() {
        return this.zzaaL;
    }

    public void setLanguage(String language) {
        this.zzaaL = language;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("language", this.zzaaL);
        hashMap.put("screenColors", Integer.valueOf(this.zzaUL));
        hashMap.put("screenWidth", Integer.valueOf(this.zzDC));
        hashMap.put("screenHeight", Integer.valueOf(this.zzDD));
        hashMap.put("viewportWidth", Integer.valueOf(this.zzaUM));
        hashMap.put("viewportHeight", Integer.valueOf(this.zzaUN));
        return zzF(hashMap);
    }

    public int zzAQ() {
        return this.zzaUL;
    }

    public int zzAR() {
        return this.zzDC;
    }

    public int zzAS() {
        return this.zzDD;
    }

    public int zzAT() {
        return this.zzaUM;
    }

    public int zzAU() {
        return this.zzaUN;
    }

    public void zza(zzps zzps) {
        if (this.zzaUL != 0) {
            zzps.zziA(this.zzaUL);
        }
        if (this.zzDC != 0) {
            zzps.zziB(this.zzDC);
        }
        if (this.zzDD != 0) {
            zzps.zziC(this.zzDD);
        }
        if (this.zzaUM != 0) {
            zzps.zziD(this.zzaUM);
        }
        if (this.zzaUN != 0) {
            zzps.zziE(this.zzaUN);
        }
        if (!TextUtils.isEmpty(this.zzaaL)) {
            zzps.setLanguage(this.zzaaL);
        }
    }

    public void zziA(int i) {
        this.zzaUL = i;
    }

    public void zziB(int i) {
        this.zzDC = i;
    }

    public void zziC(int i) {
        this.zzDD = i;
    }

    public void zziD(int i) {
        this.zzaUM = i;
    }

    public void zziE(int i) {
        this.zzaUN = i;
    }
}
