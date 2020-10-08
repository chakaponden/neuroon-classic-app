package com.google.android.gms.internal;

import android.support.v7.widget.ActivityChooserView;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zze;
import java.util.HashMap;
import java.util.UUID;

public final class zzpw extends zze<zzpw> {
    private String zzaUQ;
    private int zzaUR;
    private int zzaUS;
    private String zzaUT;
    private String zzaUU;
    private boolean zzaUV;
    private boolean zzaUW;
    private boolean zzaUX;

    public zzpw() {
        this(false);
    }

    public zzpw(boolean z) {
        this(z, zzBb());
    }

    public zzpw(boolean z, int i) {
        zzx.zzbV(i);
        this.zzaUR = i;
        this.zzaUW = z;
    }

    static int zzBb() {
        UUID randomUUID = UUID.randomUUID();
        int leastSignificantBits = (int) (randomUUID.getLeastSignificantBits() & 2147483647L);
        if (leastSignificantBits != 0) {
            return leastSignificantBits;
        }
        int mostSignificantBits = (int) (randomUUID.getMostSignificantBits() & 2147483647L);
        if (mostSignificantBits != 0) {
            return mostSignificantBits;
        }
        Log.e("GAv4", "UUID.randomUUID() returned 0.");
        return ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    }

    private void zzBf() {
        if (this.zzaUX) {
            throw new IllegalStateException("ScreenViewInfo is immutable");
        }
    }

    public void setScreenName(String screenName) {
        zzBf();
        this.zzaUQ = screenName;
    }

    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("screenName", this.zzaUQ);
        hashMap.put("interstitial", Boolean.valueOf(this.zzaUV));
        hashMap.put("automatic", Boolean.valueOf(this.zzaUW));
        hashMap.put("screenId", Integer.valueOf(this.zzaUR));
        hashMap.put("referrerScreenId", Integer.valueOf(this.zzaUS));
        hashMap.put("referrerScreenName", this.zzaUT);
        hashMap.put("referrerUri", this.zzaUU);
        return zzF(hashMap);
    }

    public String zzBc() {
        return this.zzaUQ;
    }

    public int zzBd() {
        return this.zzaUR;
    }

    public String zzBe() {
        return this.zzaUU;
    }

    public void zza(zzpw zzpw) {
        if (!TextUtils.isEmpty(this.zzaUQ)) {
            zzpw.setScreenName(this.zzaUQ);
        }
        if (this.zzaUR != 0) {
            zzpw.zziF(this.zzaUR);
        }
        if (this.zzaUS != 0) {
            zzpw.zziG(this.zzaUS);
        }
        if (!TextUtils.isEmpty(this.zzaUT)) {
            zzpw.zzeH(this.zzaUT);
        }
        if (!TextUtils.isEmpty(this.zzaUU)) {
            zzpw.zzeI(this.zzaUU);
        }
        if (this.zzaUV) {
            zzpw.zzaq(this.zzaUV);
        }
        if (this.zzaUW) {
            zzpw.zzap(this.zzaUW);
        }
    }

    public void zzap(boolean z) {
        zzBf();
        this.zzaUW = z;
    }

    public void zzaq(boolean z) {
        zzBf();
        this.zzaUV = z;
    }

    public void zzeH(String str) {
        zzBf();
        this.zzaUT = str;
    }

    public void zzeI(String str) {
        zzBf();
        if (TextUtils.isEmpty(str)) {
            this.zzaUU = null;
        } else {
            this.zzaUU = str;
        }
    }

    public void zziF(int i) {
        zzBf();
        this.zzaUR = i;
    }

    public void zziG(int i) {
        zzBf();
        this.zzaUS = i;
    }
}
