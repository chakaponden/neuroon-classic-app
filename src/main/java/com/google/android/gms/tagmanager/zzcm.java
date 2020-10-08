package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.tagmanager.zzp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class zzcm implements zzp.zze {
    private boolean mClosed;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final String zzbhM;
    private String zzbij;
    private zzbf<zzaf.zzj> zzbkg;
    private zzs zzbkh;
    private final ScheduledExecutorService zzbkj;
    private final zza zzbkk;
    private ScheduledFuture<?> zzbkl;

    interface zza {
        zzcl zza(zzs zzs);
    }

    interface zzb {
        ScheduledExecutorService zzHb();
    }

    public zzcm(Context context, String str, zzs zzs) {
        this(context, str, zzs, (zzb) null, (zza) null);
    }

    zzcm(Context context, String str, zzs zzs, zzb zzb2, zza zza2) {
        this.zzbkh = zzs;
        this.mContext = context;
        this.zzbhM = str;
        this.zzbkj = (zzb2 == null ? new zzb() {
            public ScheduledExecutorService zzHb() {
                return Executors.newSingleThreadScheduledExecutor();
            }
        } : zzb2).zzHb();
        if (zza2 == null) {
            this.zzbkk = new zza() {
                public zzcl zza(zzs zzs) {
                    return new zzcl(zzcm.this.mContext, zzcm.this.zzbhM, zzs);
                }
            };
        } else {
            this.zzbkk = zza2;
        }
    }

    private synchronized void zzHa() {
        if (this.mClosed) {
            throw new IllegalStateException("called method after closed");
        }
    }

    private zzcl zzgm(String str) {
        zzcl zza2 = this.zzbkk.zza(this.zzbkh);
        zza2.zza(this.zzbkg);
        zza2.zzfW(this.zzbij);
        zza2.zzgl(str);
        return zza2;
    }

    public synchronized void release() {
        zzHa();
        if (this.zzbkl != null) {
            this.zzbkl.cancel(false);
        }
        this.zzbkj.shutdown();
        this.mClosed = true;
    }

    public synchronized void zza(zzbf<zzaf.zzj> zzbf) {
        zzHa();
        this.zzbkg = zzbf;
    }

    public synchronized void zzf(long j, String str) {
        zzbg.v("loadAfterDelay: containerId=" + this.zzbhM + " delay=" + j);
        zzHa();
        if (this.zzbkg == null) {
            throw new IllegalStateException("callback must be set before loadAfterDelay() is called.");
        }
        if (this.zzbkl != null) {
            this.zzbkl.cancel(false);
        }
        this.zzbkl = this.zzbkj.schedule(zzgm(str), j, TimeUnit.MILLISECONDS);
    }

    public synchronized void zzfW(String str) {
        zzHa();
        this.zzbij = str;
    }
}
