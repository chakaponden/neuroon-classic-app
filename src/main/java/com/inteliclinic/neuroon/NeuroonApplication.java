package com.inteliclinic.neuroon;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.lucid.LucidConfig;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.StatsManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.context.ContextManager;
import com.inteliclinic.neuroon.managers.network.NetworkManager;
import com.inteliclinic.neuroon.managers.therapy.ITherapyTracker;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.tip.TipManager;
import com.inteliclinic.neuroon.mask.IUserAlarmManager;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.service.NeuroonService;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import io.fabric.sdk.android.Fabric;
import io.intercom.android.sdk.Intercom;
import java.util.Date;

public class NeuroonApplication extends Application {
    private Tracker mTracker;

    public synchronized Tracker getDefaultTracker() {
        if (this.mTracker == null) {
            this.mTracker = GoogleAnalytics.getInstance(this).newTracker((int) R.xml.global_tracker);
        }
        return this.mTracker;
    }

    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        LucidConfig.init((Context) this);
        DataManager.temporaryFixForRestoreAirportDB(this);
        FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
        DataManager.getInstance(this);
        NetworkManager.getInstance();
        AccountManager.getInstance(this);
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.initialize(this, "android_sdk-3d50a5445d6b41e4d1414bdb1b497c935e9bd658", "aexywely");
        }
        ContextManager.getInstance(this);
        TherapyManager.getInstance(this, new ITherapyTracker() {
            public void setHalfTherapyProgress() {
                NeuroonApplication.this.getDefaultTracker().send(new HitBuilders.EventBuilder().setCategory("therapy_progress").setAction("50%").setLabel("50%").build());
            }

            public void setEndTherapyProgress() {
                NeuroonApplication.this.getDefaultTracker().send(new HitBuilders.EventBuilder().setCategory("therapy_progress").setAction("100%").setLabel("100%").build());
            }
        });
        StatsManager.getInstance(this);
        TipManager.getInstance(this);
        IMaskUserManager iMaskUserManager = new IMaskUserManager() {
            public byte[] getUserMaskHash() {
                return AccountManager.getInstance().getUserMaskHash();
            }
        };
        BleManager.getInstance(this, iMaskUserManager);
        MaskManager.getInstance(this, iMaskUserManager, new IUserAlarmManager() {
            public Date getNextAlarmTime() {
                return AccountManager.getInstance().getNextAlarmTime();
            }
        });
        startService(new Intent(this, NeuroonService.class));
    }
}
