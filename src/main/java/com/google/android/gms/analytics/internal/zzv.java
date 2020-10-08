package com.google.android.gms.analytics.internal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.analytics.AnalyticsReceiver;
import com.google.android.gms.common.internal.zzx;

public class zzv extends zzd {
    private boolean zzRG;
    private boolean zzRH;
    private AlarmManager zzRI = ((AlarmManager) getContext().getSystemService("alarm"));

    protected zzv(zzf zzf) {
        super(zzf);
    }

    private PendingIntent zzld() {
        Intent intent = new Intent(getContext(), AnalyticsReceiver.class);
        intent.setAction("com.google.android.gms.analytics.ANALYTICS_DISPATCH");
        return PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    }

    public void cancel() {
        zzjv();
        this.zzRH = false;
        this.zzRI.cancel(zzld());
    }

    public boolean zzbw() {
        return this.zzRH;
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        ActivityInfo receiverInfo;
        try {
            this.zzRI.cancel(zzld());
            if (zzjn().zzkA() > 0 && (receiverInfo = getContext().getPackageManager().getReceiverInfo(new ComponentName(getContext(), AnalyticsReceiver.class), 2)) != null && receiverInfo.enabled) {
                zzbd("Receiver registered. Using alarm for local dispatch.");
                this.zzRG = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public boolean zzlb() {
        return this.zzRG;
    }

    public void zzlc() {
        zzjv();
        zzx.zza(zzlb(), (Object) "Receiver not registered");
        long zzkA = zzjn().zzkA();
        if (zzkA > 0) {
            cancel();
            long elapsedRealtime = zzjl().elapsedRealtime() + zzkA;
            this.zzRH = true;
            this.zzRI.setInexactRepeating(2, elapsedRealtime, 0, zzld());
        }
    }
}
