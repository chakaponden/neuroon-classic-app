package com.google.android.gms.analytics.internal;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;

public class zzg {
    private final Context zzQC;
    private final Context zzsa;

    public zzg(Context context) {
        zzx.zzz(context);
        Context applicationContext = context.getApplicationContext();
        zzx.zzb(applicationContext, (Object) "Application context can't be null");
        this.zzsa = applicationContext;
        this.zzQC = applicationContext;
    }

    public Context getApplicationContext() {
        return this.zzsa;
    }

    /* access modifiers changed from: protected */
    public zzu zza(zzf zzf) {
        return new zzu(zzf);
    }

    /* access modifiers changed from: protected */
    public com.google.android.gms.measurement.zzg zzab(Context context) {
        return com.google.android.gms.measurement.zzg.zzaS(context);
    }

    /* access modifiers changed from: protected */
    public zzk zzb(zzf zzf) {
        return new zzk(zzf);
    }

    /* access modifiers changed from: protected */
    public zza zzc(zzf zzf) {
        return new zza(zzf);
    }

    /* access modifiers changed from: protected */
    public zzn zzd(zzf zzf) {
        return new zzn(zzf);
    }

    /* access modifiers changed from: protected */
    public zzan zze(zzf zzf) {
        return new zzan(zzf);
    }

    /* access modifiers changed from: protected */
    public zzaf zzf(zzf zzf) {
        return new zzaf(zzf);
    }

    /* access modifiers changed from: protected */
    public zzr zzg(zzf zzf) {
        return new zzr(zzf);
    }

    /* access modifiers changed from: protected */
    public zzmq zzh(zzf zzf) {
        return zzmt.zzsc();
    }

    /* access modifiers changed from: protected */
    public GoogleAnalytics zzi(zzf zzf) {
        return new GoogleAnalytics(zzf);
    }

    /* access modifiers changed from: package-private */
    public zzl zzj(zzf zzf) {
        return new zzl(zzf, this);
    }

    public Context zzjx() {
        return this.zzQC;
    }

    /* access modifiers changed from: package-private */
    public zzag zzk(zzf zzf) {
        return new zzag(zzf);
    }

    /* access modifiers changed from: protected */
    public zzb zzl(zzf zzf) {
        return new zzb(zzf, this);
    }

    public zzj zzm(zzf zzf) {
        return new zzj(zzf);
    }

    public zzah zzn(zzf zzf) {
        return new zzah(zzf);
    }

    public zzi zzo(zzf zzf) {
        return new zzi(zzf);
    }

    public zzv zzp(zzf zzf) {
        return new zzv(zzf);
    }

    public zzai zzq(zzf zzf) {
        return new zzai(zzf);
    }
}
