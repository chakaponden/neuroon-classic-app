package com.google.android.gms.measurement;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpq;
import com.google.android.gms.internal.zzps;
import java.lang.Thread;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzg {
    private static volatile zzg zzaUv;
    private final Context mContext;
    private volatile zzpq zzQX;
    /* access modifiers changed from: private */
    public final List<zzh> zzaUw = new CopyOnWriteArrayList();
    private final zzb zzaUx = new zzb();
    private final zza zzaUy = new zza();
    /* access modifiers changed from: private */
    public Thread.UncaughtExceptionHandler zzaUz;

    private class zza extends ThreadPoolExecutor {
        public zza() {
            super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
            setThreadFactory(new zzb());
        }

        /* access modifiers changed from: protected */
        public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
            return new FutureTask<T>(runnable, value) {
                /* access modifiers changed from: protected */
                public void setException(Throwable error) {
                    Thread.UncaughtExceptionHandler zzb = zzg.this.zzaUz;
                    if (zzb != null) {
                        zzb.uncaughtException(Thread.currentThread(), error);
                    } else if (Log.isLoggable("GAv4", 6)) {
                        Log.e("GAv4", "MeasurementExecutor: job failed with " + error);
                    }
                    super.setException(error);
                }
            };
        }
    }

    private static class zzb implements ThreadFactory {
        private static final AtomicInteger zzaUD = new AtomicInteger();

        private zzb() {
        }

        public Thread newThread(Runnable target) {
            return new zzc(target, "measurement-" + zzaUD.incrementAndGet());
        }
    }

    private static class zzc extends Thread {
        zzc(Runnable runnable, String str) {
            super(runnable, str);
        }

        public void run() {
            Process.setThreadPriority(10);
            super.run();
        }
    }

    zzg(Context context) {
        Context applicationContext = context.getApplicationContext();
        zzx.zzz(applicationContext);
        this.mContext = applicationContext;
    }

    public static zzg zzaS(Context context) {
        zzx.zzz(context);
        if (zzaUv == null) {
            synchronized (zzg.class) {
                if (zzaUv == null) {
                    zzaUv = new zzg(context);
                }
            }
        }
        return zzaUv;
    }

    /* access modifiers changed from: private */
    public void zzb(zzc zzc2) {
        zzx.zzcE("deliver should be called from worker thread");
        zzx.zzb(zzc2.zzAz(), (Object) "Measurement must be submitted");
        List<zzi> zzAw = zzc2.zzAw();
        if (!zzAw.isEmpty()) {
            HashSet hashSet = new HashSet();
            for (zzi next : zzAw) {
                Uri zziA = next.zziA();
                if (!hashSet.contains(zziA)) {
                    hashSet.add(zziA);
                    next.zzb(zzc2);
                }
            }
        }
    }

    public static void zzjk() {
        if (!(Thread.currentThread() instanceof zzc)) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public zzpq zzAH() {
        if (this.zzQX == null) {
            synchronized (this) {
                if (this.zzQX == null) {
                    zzpq zzpq = new zzpq();
                    PackageManager packageManager = this.mContext.getPackageManager();
                    String packageName = this.mContext.getPackageName();
                    zzpq.setAppId(packageName);
                    zzpq.setAppInstallerId(packageManager.getInstallerPackageName(packageName));
                    String str = null;
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(this.mContext.getPackageName(), 0);
                        if (packageInfo != null) {
                            CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                            if (!TextUtils.isEmpty(applicationLabel)) {
                                packageName = applicationLabel.toString();
                            }
                            str = packageInfo.versionName;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e("GAv4", "Error retrieving package info: appName set to " + packageName);
                    }
                    zzpq.setAppName(packageName);
                    zzpq.setAppVersion(str);
                    this.zzQX = zzpq;
                }
            }
        }
        return this.zzQX;
    }

    public zzps zzAI() {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        zzps zzps = new zzps();
        zzps.setLanguage(zzam.zza(Locale.getDefault()));
        zzps.zziB(displayMetrics.widthPixels);
        zzps.zziC(displayMetrics.heightPixels);
        return zzps;
    }

    public void zza(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.zzaUz = uncaughtExceptionHandler;
    }

    public <V> Future<V> zzc(Callable<V> callable) {
        zzx.zzz(callable);
        if (!(Thread.currentThread() instanceof zzc)) {
            return this.zzaUy.submit(callable);
        }
        FutureTask futureTask = new FutureTask(callable);
        futureTask.run();
        return futureTask;
    }

    /* access modifiers changed from: package-private */
    public void zze(zzc zzc2) {
        if (zzc2.zzAD()) {
            throw new IllegalStateException("Measurement prototype can't be submitted");
        } else if (zzc2.zzAz()) {
            throw new IllegalStateException("Measurement can only be submitted once");
        } else {
            final zzc zzAu = zzc2.zzAu();
            zzAu.zzAA();
            this.zzaUy.execute(new Runnable() {
                public void run() {
                    zzAu.zzAB().zza(zzAu);
                    for (zzh zza : zzg.this.zzaUw) {
                        zza.zza(zzAu);
                    }
                    zzg.this.zzb(zzAu);
                }
            });
        }
    }

    public void zzf(Runnable runnable) {
        zzx.zzz(runnable);
        this.zzaUy.submit(runnable);
    }
}
