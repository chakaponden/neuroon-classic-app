package com.google.android.gms.analytics.internal;

import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zznf;
import java.util.HashSet;
import java.util.Set;

public class zzr {
    private final zzf zzOK;
    private Set<Integer> zzRA;
    private volatile Boolean zzRy;
    private String zzRz;

    protected zzr(zzf zzf) {
        zzx.zzz(zzf);
        this.zzOK = zzf;
    }

    public long zzkA() {
        return zzy.zzRV.get().longValue();
    }

    public long zzkB() {
        return zzy.zzRW.get().longValue();
    }

    public int zzkC() {
        return zzy.zzRX.get().intValue();
    }

    public int zzkD() {
        return zzy.zzRY.get().intValue();
    }

    public long zzkE() {
        return (long) zzy.zzSl.get().intValue();
    }

    public String zzkF() {
        return zzy.zzSa.get();
    }

    public String zzkG() {
        return zzy.zzRZ.get();
    }

    public String zzkH() {
        return zzy.zzSb.get();
    }

    public String zzkI() {
        return zzy.zzSc.get();
    }

    public zzm zzkJ() {
        return zzm.zzbm(zzy.zzSe.get());
    }

    public zzo zzkK() {
        return zzo.zzbn(zzy.zzSf.get());
    }

    public Set<Integer> zzkL() {
        String str = zzy.zzSk.get();
        if (this.zzRA == null || this.zzRz == null || !this.zzRz.equals(str)) {
            String[] split = TextUtils.split(str, ",");
            HashSet hashSet = new HashSet();
            for (String parseInt : split) {
                try {
                    hashSet.add(Integer.valueOf(Integer.parseInt(parseInt)));
                } catch (NumberFormatException e) {
                }
            }
            this.zzRz = str;
            this.zzRA = hashSet;
        }
        return this.zzRA;
    }

    public long zzkM() {
        return zzy.zzSt.get().longValue();
    }

    public long zzkN() {
        return zzy.zzSu.get().longValue();
    }

    public long zzkO() {
        return zzy.zzSx.get().longValue();
    }

    public int zzkP() {
        return zzy.zzRO.get().intValue();
    }

    public int zzkQ() {
        return zzy.zzRQ.get().intValue();
    }

    public String zzkR() {
        return "google_analytics_v4.db";
    }

    public String zzkS() {
        return "google_analytics2_v4.db";
    }

    public long zzkT() {
        return 86400000;
    }

    public int zzkU() {
        return zzy.zzSn.get().intValue();
    }

    public int zzkV() {
        return zzy.zzSo.get().intValue();
    }

    public long zzkW() {
        return zzy.zzSp.get().longValue();
    }

    public long zzkX() {
        return zzy.zzSy.get().longValue();
    }

    public boolean zzkr() {
        return zzd.zzakE;
    }

    public boolean zzks() {
        if (this.zzRy == null) {
            synchronized (this) {
                if (this.zzRy == null) {
                    ApplicationInfo applicationInfo = this.zzOK.getContext().getApplicationInfo();
                    String zzi = zznf.zzi(this.zzOK.getContext(), Process.myPid());
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzRy = Boolean.valueOf(str != null && str.equals(zzi));
                    }
                    if ((this.zzRy == null || !this.zzRy.booleanValue()) && "com.google.android.gms.analytics".equals(zzi)) {
                        this.zzRy = Boolean.TRUE;
                    }
                    if (this.zzRy == null) {
                        this.zzRy = Boolean.TRUE;
                        this.zzOK.zzjm().zzbh("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzRy.booleanValue();
    }

    public boolean zzkt() {
        return zzy.zzRK.get().booleanValue();
    }

    public int zzku() {
        return zzy.zzSd.get().intValue();
    }

    public int zzkv() {
        return zzy.zzSh.get().intValue();
    }

    public int zzkw() {
        return zzy.zzSi.get().intValue();
    }

    public int zzkx() {
        return zzy.zzSj.get().intValue();
    }

    public long zzky() {
        return zzy.zzRS.get().longValue();
    }

    public long zzkz() {
        return zzy.zzRR.get().longValue();
    }
}
