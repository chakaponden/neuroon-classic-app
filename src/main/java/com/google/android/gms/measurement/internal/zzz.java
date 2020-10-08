package com.google.android.gms.measurement.internal;

abstract class zzz extends zzy {
    private boolean zzQk;
    private boolean zzQl;
    private boolean zzaYC;

    zzz(zzw zzw) {
        super(zzw);
        this.zzaTV.zzb(this);
    }

    /* access modifiers changed from: package-private */
    public boolean isInitialized() {
        return this.zzQk && !this.zzQl;
    }

    /* access modifiers changed from: package-private */
    public boolean zzDi() {
        return this.zzaYC;
    }

    public final void zza() {
        if (this.zzQk) {
            throw new IllegalStateException("Can't initialize twice");
        }
        zziJ();
        this.zzaTV.zzDg();
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
