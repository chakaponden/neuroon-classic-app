package com.google.android.gms.analytics.internal;

public abstract class zzd extends zzc {
    private boolean zzQk;
    private boolean zzQl;

    protected zzd(zzf zzf) {
        super(zzf);
    }

    public boolean isInitialized() {
        return this.zzQk && !this.zzQl;
    }

    public void zza() {
        zziJ();
        this.zzQk = true;
    }

    /* access modifiers changed from: protected */
    public abstract void zziJ();

    /* access modifiers changed from: protected */
    public void zzjv() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
