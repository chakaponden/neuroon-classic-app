package com.google.android.gms.measurement;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.zzf;
import java.util.ArrayList;
import java.util.List;

public abstract class zzf<T extends zzf> {
    private final zzg zzaUs;
    protected final zzc zzaUt;
    private final List<zzd> zzaUu = new ArrayList();

    protected zzf(zzg zzg, zzmq zzmq) {
        zzx.zzz(zzg);
        this.zzaUs = zzg;
        zzc zzc = new zzc(this, zzmq);
        zzc.zzAE();
        this.zzaUt = zzc;
    }

    /* access modifiers changed from: protected */
    public zzg zzAC() {
        return this.zzaUs;
    }

    public zzc zzAF() {
        return this.zzaUt;
    }

    public List<zzi> zzAG() {
        return this.zzaUt.zzAw();
    }

    /* access modifiers changed from: protected */
    public void zza(zzc zzc) {
    }

    /* access modifiers changed from: protected */
    public void zzd(zzc zzc) {
        for (zzd zza : this.zzaUu) {
            zza.zza(this, zzc);
        }
    }

    public zzc zziy() {
        zzc zzAu = this.zzaUt.zzAu();
        zzd(zzAu);
        return zzAu;
    }
}
