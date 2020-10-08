package com.google.android.gms.analytics.internal;

import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.internal.zzx;

abstract class zzt {
    private static volatile Handler zzRC;
    /* access modifiers changed from: private */
    public final zzf zzQj;
    /* access modifiers changed from: private */
    public volatile long zzRD;
    /* access modifiers changed from: private */
    public boolean zzRE;
    private final Runnable zzx = new Runnable() {
        public void run() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                zzt.this.zzQj.zzjo().zzf(this);
                return;
            }
            boolean zzbw = zzt.this.zzbw();
            long unused = zzt.this.zzRD = 0;
            if (zzbw && !zzt.this.zzRE) {
                zzt.this.run();
            }
        }
    };

    zzt(zzf zzf) {
        zzx.zzz(zzf);
        this.zzQj = zzf;
    }

    private Handler getHandler() {
        Handler handler;
        if (zzRC != null) {
            return zzRC;
        }
        synchronized (zzt.class) {
            if (zzRC == null) {
                zzRC = new Handler(this.zzQj.getContext().getMainLooper());
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

    public long zzkY() {
        if (this.zzRD == 0) {
            return 0;
        }
        return Math.abs(this.zzQj.zzjl().currentTimeMillis() - this.zzRD);
    }

    public void zzt(long j) {
        cancel();
        if (j >= 0) {
            this.zzRD = this.zzQj.zzjl().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzx, j)) {
                this.zzQj.zzjm().zze("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }

    public void zzu(long j) {
        long j2 = 0;
        if (zzbw()) {
            if (j < 0) {
                cancel();
                return;
            }
            long abs = j - Math.abs(this.zzQj.zzjl().currentTimeMillis() - this.zzRD);
            if (abs >= 0) {
                j2 = abs;
            }
            getHandler().removeCallbacks(this.zzx);
            if (!getHandler().postDelayed(this.zzx, j2)) {
                this.zzQj.zzjm().zze("Failed to adjust delayed post. time", Long.valueOf(j2));
            }
        }
    }
}
