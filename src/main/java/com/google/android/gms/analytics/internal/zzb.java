package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.content.Intent;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.analytics.AnalyticsService;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.zzg;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class zzb extends zzd {
    /* access modifiers changed from: private */
    public final zzl zzQb;

    public zzb(zzf zzf, zzg zzg) {
        super(zzf);
        zzx.zzz(zzg);
        this.zzQb = zzg.zzj(zzf);
    }

    /* access modifiers changed from: package-private */
    public void onServiceConnected() {
        zzjk();
        this.zzQb.onServiceConnected();
    }

    public void setLocalDispatchPeriod(final int dispatchPeriodInSeconds) {
        zzjv();
        zzb("setLocalDispatchPeriod (sec)", Integer.valueOf(dispatchPeriodInSeconds));
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zzs(((long) dispatchPeriodInSeconds) * 1000);
            }
        });
    }

    public void start() {
        this.zzQb.start();
    }

    public void zzJ(final boolean z) {
        zza("Network connectivity status changed", Boolean.valueOf(z));
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zzJ(z);
            }
        });
    }

    public long zza(zzh zzh) {
        zzjv();
        zzx.zzz(zzh);
        zzjk();
        long zza = this.zzQb.zza(zzh, true);
        if (zza == 0) {
            this.zzQb.zzc(zzh);
        }
        return zza;
    }

    public void zza(final zzab zzab) {
        zzx.zzz(zzab);
        zzjv();
        zzb("Hit delivery requested", zzab);
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zza(zzab);
            }
        });
    }

    public void zza(final zzw zzw) {
        zzjv();
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zzb(zzw);
            }
        });
    }

    public void zza(final String str, final Runnable runnable) {
        zzx.zzh(str, "campaign param can't be empty");
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zzbl(str);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        this.zzQb.zza();
    }

    public void zzjc() {
        zzjv();
        zzjj();
        zzjo().zzf(new Runnable() {
            public void run() {
                zzb.this.zzQb.zzjc();
            }
        });
    }

    public void zzjd() {
        zzjv();
        Context context = getContext();
        if (!AnalyticsReceiver.zzY(context) || !AnalyticsService.zzZ(context)) {
            zza((zzw) null);
            return;
        }
        Intent intent = new Intent(context, AnalyticsService.class);
        intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
        context.startService(intent);
    }

    public boolean zzje() {
        zzjv();
        try {
            zzjo().zzc(new Callable<Void>() {
                /* renamed from: zzdt */
                public Void call() throws Exception {
                    zzb.this.zzQb.zzka();
                    return null;
                }
            }).get(4, TimeUnit.SECONDS);
            return true;
        } catch (InterruptedException e) {
            zzd("syncDispatchLocalHits interrupted", e);
            return false;
        } catch (ExecutionException e2) {
            zze("syncDispatchLocalHits failed", e2);
            return false;
        } catch (TimeoutException e3) {
            zzd("syncDispatchLocalHits timed out", e3);
            return false;
        }
    }

    public void zzjf() {
        zzjv();
        zzg.zzjk();
        this.zzQb.zzjf();
    }

    public void zzjg() {
        zzbd("Radio powered up");
        zzjd();
    }

    /* access modifiers changed from: package-private */
    public void zzjh() {
        zzjk();
        this.zzQb.zzjh();
    }
}
