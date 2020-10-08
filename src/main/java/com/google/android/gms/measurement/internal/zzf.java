package com.google.android.gms.measurement.internal;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.internal.zzx;

abstract class zzf {
    private static volatile Handler zzRC;
    /* access modifiers changed from: private */
    public volatile long zzRD;
    /* access modifiers changed from: private */
    public final zzw zzaTV;
    /* access modifiers changed from: private */
    public boolean zzaVI = true;
    private final Runnable zzx = new Runnable() {
        public void run() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                zzf.this.zzaTV.zzCn().zzg(this);
                return;
            }
            boolean zzbw = zzf.this.zzbw();
            long unused = zzf.this.zzRD = 0;
            if (zzbw && zzf.this.zzaVI) {
                zzf.this.run();
            }
        }
    };

    zzf(zzw zzw) {
        zzx.zzz(zzw);
        this.zzaTV = zzw;
    }

    private Handler getHandler() {
        Handler handler;
        if (zzRC != null) {
            return zzRC;
        }
        synchronized (zzf.class) {
            if (zzRC == null) {
                zzRC = new Handler(this.zzaTV.getContext().getMainLooper());
            }
            handler = zzRC;
        }
        return handler;
    }

    public void cancel() {
        this.zzRD = 0;
        getHandler().removeCallbacks(this.zzx);
    }

    public abstract void run();

    public boolean zzbw() {
        return this.zzRD != 0;
    }

    public void zzt(long j) {
        cancel();
        if (j >= 0) {
            this.zzRD = this.zzaTV.zzjl().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzx, j)) {
                this.zzaTV.zzAo().zzCE().zzj("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }
}
