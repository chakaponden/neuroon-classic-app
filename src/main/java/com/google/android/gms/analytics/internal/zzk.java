package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zzpq;

public class zzk extends zzd {
    private final zzpq zzQX = new zzpq();

    zzk(zzf zzf) {
        super(zzf);
    }

    public void zziE() {
        zzan zziI = zziI();
        String zzlg = zziI.zzlg();
        if (zzlg != null) {
            this.zzQX.setAppName(zzlg);
        }
        String zzli = zziI.zzli();
        if (zzli != null) {
            this.zzQX.setAppVersion(zzli);
        }
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        zzjo().zzAH().zza(this.zzQX);
        zziE();
    }

    public zzpq zzjS() {
        zzjv();
        return this.zzQX;
    }
}
