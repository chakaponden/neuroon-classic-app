package com.google.android.gms.analytics;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.measurement.zzc;
import com.google.android.gms.measurement.zzf;
import com.google.android.gms.measurement.zzi;
import java.util.ListIterator;

public class zza extends zzf<zza> {
    private final com.google.android.gms.analytics.internal.zzf zzOK;
    private boolean zzOL;

    public zza(com.google.android.gms.analytics.internal.zzf zzf) {
        super(zzf.zzjo(), zzf.zzjl());
        this.zzOK = zzf;
    }

    public void enableAdvertisingIdCollection(boolean enable) {
        this.zzOL = enable;
    }

    /* access modifiers changed from: protected */
    public void zza(zzc zzc) {
        zzke zzke = (zzke) zzc.zzf(zzke.class);
        if (TextUtils.isEmpty(zzke.getClientId())) {
            zzke.setClientId(this.zzOK.zzjC().zzkk());
        }
        if (this.zzOL && TextUtils.isEmpty(zzke.zziT())) {
            com.google.android.gms.analytics.internal.zza zzjB = this.zzOK.zzjB();
            zzke.zzaY(zzjB.zziY());
            zzke.zzH(zzjB.zziU());
        }
    }

    public void zzaS(String str) {
        zzx.zzcM(str);
        zzaT(str);
        zzAG().add(new zzb(this.zzOK, str));
    }

    public void zzaT(String str) {
        Uri zzaU = zzb.zzaU(str);
        ListIterator<zzi> listIterator = zzAG().listIterator();
        while (listIterator.hasNext()) {
            if (zzaU.equals(listIterator.next().zziA())) {
                listIterator.remove();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public com.google.android.gms.analytics.internal.zzf zzix() {
        return this.zzOK;
    }

    public zzc zziy() {
        zzc zzAu = zzAF().zzAu();
        zzAu.zzb(this.zzOK.zzjt().zzjS());
        zzAu.zzb(this.zzOK.zzju().zzkZ());
        zzd(zzAu);
        return zzAu;
    }
}
