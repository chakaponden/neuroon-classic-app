package com.google.android.gms.tagmanager;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.analytics.CampaignTrackingService;

public final class InstallReferrerReceiver extends CampaignTrackingReceiver {
    /* access modifiers changed from: protected */
    public void zzaV(String str) {
        zzax.zzgh(str);
    }

    /* access modifiers changed from: protected */
    public Class<? extends CampaignTrackingService> zziB() {
        return InstallReferrerService.class;
    }
}
