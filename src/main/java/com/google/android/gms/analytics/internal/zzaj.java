package com.google.android.gms.analytics.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;

class zzaj {
    private long zzCv;
    private final zzmq zzqW;

    public zzaj(zzmq zzmq) {
        zzx.zzz(zzmq);
        this.zzqW = zzmq;
    }

    public zzaj(zzmq zzmq, long j) {
        zzx.zzz(zzmq);
        this.zzqW = zzmq;
        this.zzCv = j;
    }

    public void clear() {
        this.zzCv = 0;
    }

    public void start() {
        this.zzCv = this.zzqW.elapsedRealtime();
    }

    public boolean zzv(long j) {
        return this.zzCv == 0 || this.zzqW.elapsedRealtime() - this.zzCv > j;
    }
}
