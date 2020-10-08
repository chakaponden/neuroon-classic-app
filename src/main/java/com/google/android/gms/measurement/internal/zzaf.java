package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;

class zzaf {
    private long zzCv;
    private final zzmq zzqW;

    public zzaf(zzmq zzmq) {
        zzx.zzz(zzmq);
        this.zzqW = zzmq;
    }

    public void clear() {
        this.zzCv = 0;
    }

    public void start() {
        this.zzCv = this.zzqW.elapsedRealtime();
    }

    public boolean zzv(long j) {
        return this.zzCv == 0 || this.zzqW.elapsedRealtime() - this.zzCv >= j;
    }
}
