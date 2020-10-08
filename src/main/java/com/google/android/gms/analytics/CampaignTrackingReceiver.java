package com.google.android.gms.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import com.google.android.gms.analytics.internal.zzaf;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzrp;

public class CampaignTrackingReceiver extends BroadcastReceiver {
    static zzrp zzOM;
    static Boolean zzON;
    static Object zzqy = new Object();

    public static boolean zzY(Context context) {
        zzx.zzz(context);
        if (zzON != null) {
            return zzON.booleanValue();
        }
        boolean zza = zzam.zza(context, (Class<? extends BroadcastReceiver>) CampaignTrackingReceiver.class, true);
        zzON = Boolean.valueOf(zza);
        return zza;
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public void onReceive(Context context, Intent intent) {
        zzf zzaa = zzf.zzaa(context);
        zzaf zzjm = zzaa.zzjm();
        String stringExtra = intent.getStringExtra("referrer");
        String action = intent.getAction();
        zzjm.zza("CampaignTrackingReceiver received", action);
        if (!"com.android.vending.INSTALL_REFERRER".equals(action) || TextUtils.isEmpty(stringExtra)) {
            zzjm.zzbg("CampaignTrackingReceiver received unexpected intent without referrer extra");
            return;
        }
        boolean zzZ = CampaignTrackingService.zzZ(context);
        if (!zzZ) {
            zzjm.zzbg("CampaignTrackingService not registered or disabled. Installation tracking not possible. See http://goo.gl/8Rd3yj for instructions.");
        }
        zzaV(stringExtra);
        if (zzaa.zzjn().zzkr()) {
            zzjm.zzbh("Received unexpected installation campaign on package side");
            return;
        }
        Class<? extends CampaignTrackingService> zziB = zziB();
        zzx.zzz(zziB);
        Intent intent2 = new Intent(context, zziB);
        intent2.putExtra("referrer", stringExtra);
        synchronized (zzqy) {
            context.startService(intent2);
            if (zzZ) {
                try {
                    if (zzOM == null) {
                        zzOM = new zzrp(context, 1, "Analytics campaign WakeLock");
                        zzOM.setReferenceCounted(false);
                    }
                    zzOM.acquire(1000);
                } catch (SecurityException e) {
                    zzjm.zzbg("CampaignTrackingService service at risk of not starting. For more reliable installation campaign reports, add the WAKE_LOCK permission to your manifest. See http://goo.gl/8Rd3yj for instructions.");
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzaV(String str) {
    }

    /* access modifiers changed from: protected */
    public Class<? extends CampaignTrackingService> zziB() {
        return CampaignTrackingService.class;
    }
}
