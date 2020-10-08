package com.google.android.gms.analytics.internal;

import android.app.Activity;
import java.util.HashMap;
import java.util.Map;

public class zzal implements zzp {
    public String zzOV;
    public double zzTo = -1.0d;
    public int zzTp = -1;
    public int zzTq = -1;
    public int zzTr = -1;
    public int zzTs = -1;
    public Map<String, String> zzTt = new HashMap();

    public int getSessionTimeout() {
        return this.zzTp;
    }

    public String getTrackingId() {
        return this.zzOV;
    }

    public String zzbr(String str) {
        String str2 = this.zzTt.get(str);
        return str2 != null ? str2 : str;
    }

    public boolean zzlT() {
        return this.zzOV != null;
    }

    public boolean zzlU() {
        return this.zzTo >= 0.0d;
    }

    public double zzlV() {
        return this.zzTo;
    }

    public boolean zzlW() {
        return this.zzTp >= 0;
    }

    public boolean zzlX() {
        return this.zzTq != -1;
    }

    public boolean zzlY() {
        return this.zzTq == 1;
    }

    public boolean zzlZ() {
        return this.zzTr != -1;
    }

    public boolean zzma() {
        return this.zzTr == 1;
    }

    public boolean zzmb() {
        return this.zzTs == 1;
    }

    public String zzo(Activity activity) {
        return zzbr(activity.getClass().getCanonicalName());
    }
}
