package com.google.android.gms.analytics.internal;

import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import com.google.android.gms.measurement.zzg;
import java.lang.Thread;

public class zzf {
    private static zzf zzQn;
    private final Context mContext;
    private final zzu zzQA;
    private final Context zzQo;
    private final zzr zzQp;
    private final zzaf zzQq;
    private final zzg zzQr;
    private final zzb zzQs;
    private final zzv zzQt;
    private final zzan zzQu;
    private final zzai zzQv;
    private final GoogleAnalytics zzQw;
    private final zzn zzQx;
    private final zza zzQy;
    private final zzk zzQz;
    private final zzmq zzqW;

    protected zzf(zzg zzg) {
        Context applicationContext = zzg.getApplicationContext();
        zzx.zzb(applicationContext, (Object) "Application context can't be null");
        zzx.zzb(applicationContext instanceof Application, (Object) "getApplicationContext didn't return the application");
        Context zzjx = zzg.zzjx();
        zzx.zzz(zzjx);
        this.mContext = applicationContext;
        this.zzQo = zzjx;
        this.zzqW = zzg.zzh(this);
        this.zzQp = zzg.zzg(this);
        zzaf zzf = zzg.zzf(this);
        zzf.zza();
        this.zzQq = zzf;
        if (zzjn().zzkr()) {
            zzjm().zzbf("Google Analytics " + zze.VERSION + " is starting up.");
        } else {
            zzjm().zzbf("Google Analytics " + zze.VERSION + " is starting up. " + "To enable debug logging on a device run:\n" + "  adb shell setprop log.tag.GAv4 DEBUG\n" + "  adb logcat -s GAv4");
        }
        zzai zzq = zzg.zzq(this);
        zzq.zza();
        this.zzQv = zzq;
        zzan zze = zzg.zze(this);
        zze.zza();
        this.zzQu = zze;
        zzb zzl = zzg.zzl(this);
        zzn zzd = zzg.zzd(this);
        zza zzc = zzg.zzc(this);
        zzk zzb = zzg.zzb(this);
        zzu zza = zzg.zza(this);
        zzg zzab = zzg.zzab(applicationContext);
        zzab.zza(zzjw());
        this.zzQr = zzab;
        GoogleAnalytics zzi = zzg.zzi(this);
        zzd.zza();
        this.zzQx = zzd;
        zzc.zza();
        this.zzQy = zzc;
        zzb.zza();
        this.zzQz = zzb;
        zza.zza();
        this.zzQA = zza;
        zzv zzp = zzg.zzp(this);
        zzp.zza();
        this.zzQt = zzp;
        zzl.zza();
        this.zzQs = zzl;
        if (zzjn().zzkr()) {
            zzjm().zzb("Device AnalyticsService version", zze.VERSION);
        }
        zzi.zza();
        this.zzQw = zzi;
        zzl.start();
    }

    private void zza(zzd zzd) {
        zzx.zzb(zzd, (Object) "Analytics service not created/initialized");
        zzx.zzb(zzd.isInitialized(), (Object) "Analytics service not initialized");
    }

    public static zzf zzaa(Context context) {
        zzx.zzz(context);
        if (zzQn == null) {
            synchronized (zzf.class) {
                if (zzQn == null) {
                    zzmq zzsc = zzmt.zzsc();
                    long elapsedRealtime = zzsc.elapsedRealtime();
                    zzf zzf = new zzf(new zzg(context.getApplicationContext()));
                    zzQn = zzf;
                    GoogleAnalytics.zziF();
                    long elapsedRealtime2 = zzsc.elapsedRealtime() - elapsedRealtime;
                    long longValue = zzy.zzSz.get().longValue();
                    if (elapsedRealtime2 > longValue) {
                        zzf.zzjm().zzc("Slow initialization (ms)", Long.valueOf(elapsedRealtime2), Long.valueOf(longValue));
                    }
                }
            }
        }
        return zzQn;
    }

    public Context getContext() {
        return this.mContext;
    }

    public zzb zziH() {
        zza(this.zzQs);
        return this.zzQs;
    }

    public zzan zziI() {
        zza(this.zzQu);
        return this.zzQu;
    }

    public zzai zzjA() {
        if (this.zzQv == null || !this.zzQv.isInitialized()) {
            return null;
        }
        return this.zzQv;
    }

    public zza zzjB() {
        zza(this.zzQy);
        return this.zzQy;
    }

    public zzn zzjC() {
        zza(this.zzQx);
        return this.zzQx;
    }

    public void zzjk() {
        zzg.zzjk();
    }

    public zzmq zzjl() {
        return this.zzqW;
    }

    public zzaf zzjm() {
        zza(this.zzQq);
        return this.zzQq;
    }

    public zzr zzjn() {
        return this.zzQp;
    }

    public zzg zzjo() {
        zzx.zzz(this.zzQr);
        return this.zzQr;
    }

    public zzv zzjp() {
        zza(this.zzQt);
        return this.zzQt;
    }

    public zzai zzjq() {
        zza(this.zzQv);
        return this.zzQv;
    }

    public zzk zzjt() {
        zza(this.zzQz);
        return this.zzQz;
    }

    public zzu zzju() {
        return this.zzQA;
    }

    /* access modifiers changed from: protected */
    public Thread.UncaughtExceptionHandler zzjw() {
        return new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable error) {
                zzaf zzjy = zzf.this.zzjy();
                if (zzjy != null) {
                    zzjy.zze("Job execution failed", error);
                }
            }
        };
    }

    public Context zzjx() {
        return this.zzQo;
    }

    public zzaf zzjy() {
        return this.zzQq;
    }

    public GoogleAnalytics zzjz() {
        zzx.zzz(this.zzQw);
        zzx.zzb(this.zzQw.isInitialized(), (Object) "Analytics instance not initialized");
        return this.zzQw;
    }
}
