package com.google.android.gms.analytics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import com.google.android.gms.analytics.internal.zzad;
import com.google.android.gms.analytics.internal.zzae;
import com.google.android.gms.analytics.internal.zzak;
import com.google.android.gms.analytics.internal.zzal;
import com.google.android.gms.analytics.internal.zzan;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzy;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GoogleAnalytics extends zza {
    private static List<Runnable> zzPe = new ArrayList();
    private boolean zzPf;
    private Set<zza> zzPg = new HashSet();
    private boolean zzPh;
    private boolean zzPi;
    private volatile boolean zzPj;
    private boolean zzPk;
    private boolean zzqA;

    interface zza {
        void zzl(Activity activity);

        void zzm(Activity activity);
    }

    @TargetApi(14)
    class zzb implements Application.ActivityLifecycleCallbacks {
        zzb() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityStarted(Activity activity) {
            GoogleAnalytics.this.zzj(activity);
        }

        public void onActivityStopped(Activity activity) {
            GoogleAnalytics.this.zzk(activity);
        }
    }

    public GoogleAnalytics(zzf context) {
        super(context);
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public static GoogleAnalytics getInstance(Context context) {
        return zzf.zzaa(context).zzjz();
    }

    public static void zziF() {
        synchronized (GoogleAnalytics.class) {
            if (zzPe != null) {
                for (Runnable run : zzPe) {
                    run.run();
                }
                zzPe = null;
            }
        }
    }

    private com.google.android.gms.analytics.internal.zzb zziH() {
        return zzix().zziH();
    }

    private zzan zziI() {
        return zzix().zziI();
    }

    public void dispatchLocalHits() {
        zziH().zzjd();
    }

    @TargetApi(14)
    public void enableAutoActivityReports(Application application) {
        if (Build.VERSION.SDK_INT >= 14 && !this.zzPh) {
            application.registerActivityLifecycleCallbacks(new zzb());
            this.zzPh = true;
        }
    }

    public boolean getAppOptOut() {
        return this.zzPj;
    }

    public String getClientId() {
        zzx.zzcE("getClientId can not be called from the main thread");
        return zzix().zzjC().zzkk();
    }

    @Deprecated
    public Logger getLogger() {
        return zzae.getLogger();
    }

    public boolean isDryRunEnabled() {
        return this.zzPi;
    }

    public boolean isInitialized() {
        return this.zzqA && !this.zzPf;
    }

    public Tracker newTracker(int configResId) {
        Tracker tracker;
        zzal zzal;
        synchronized (this) {
            tracker = new Tracker(zzix(), (String) null, (zzad) null);
            if (configResId > 0 && (zzal = (zzal) new zzak(zzix()).zzah(configResId)) != null) {
                tracker.zza(zzal);
            }
            tracker.zza();
        }
        return tracker;
    }

    public Tracker newTracker(String trackingId) {
        Tracker tracker;
        synchronized (this) {
            tracker = new Tracker(zzix(), trackingId, (zzad) null);
            tracker.zza();
        }
        return tracker;
    }

    public void reportActivityStart(Activity activity) {
        if (!this.zzPh) {
            zzj(activity);
        }
    }

    public void reportActivityStop(Activity activity) {
        if (!this.zzPh) {
            zzk(activity);
        }
    }

    public void setAppOptOut(boolean optOut) {
        this.zzPj = optOut;
        if (this.zzPj) {
            zziH().zzjc();
        }
    }

    public void setDryRun(boolean dryRun) {
        this.zzPi = dryRun;
    }

    public void setLocalDispatchPeriod(int dispatchPeriodInSeconds) {
        zziH().setLocalDispatchPeriod(dispatchPeriodInSeconds);
    }

    @Deprecated
    public void setLogger(Logger logger) {
        zzae.setLogger(logger);
        if (!this.zzPk) {
            Log.i(zzy.zzRL.get(), "GoogleAnalytics.setLogger() is deprecated. To enable debug logging, please run:\nadb shell setprop log.tag." + zzy.zzRL.get() + " DEBUG");
            this.zzPk = true;
        }
    }

    public void zza() {
        zziE();
        this.zzqA = true;
    }

    /* access modifiers changed from: package-private */
    public void zza(zza zza2) {
        this.zzPg.add(zza2);
        Context context = zzix().getContext();
        if (context instanceof Application) {
            enableAutoActivityReports((Application) context);
        }
    }

    /* access modifiers changed from: package-private */
    public void zzb(zza zza2) {
        this.zzPg.remove(zza2);
    }

    /* access modifiers changed from: package-private */
    public void zziE() {
        Logger logger;
        zzan zziI = zziI();
        if (zziI.zzlj()) {
            getLogger().setLogLevel(zziI.getLogLevel());
        }
        if (zziI.zzln()) {
            setDryRun(zziI.zzlo());
        }
        if (zziI.zzlj() && (logger = zzae.getLogger()) != null) {
            logger.setLogLevel(zziI.getLogLevel());
        }
    }

    /* access modifiers changed from: package-private */
    public void zziG() {
        zziH().zzje();
    }

    /* access modifiers changed from: package-private */
    public void zzj(Activity activity) {
        for (zza zzl : this.zzPg) {
            zzl.zzl(activity);
        }
    }

    /* access modifiers changed from: package-private */
    public void zzk(Activity activity) {
        for (zza zzm : this.zzPg) {
            zzm.zzm(activity);
        }
    }
}
