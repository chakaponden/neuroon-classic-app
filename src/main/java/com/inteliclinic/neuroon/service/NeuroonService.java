package com.inteliclinic.neuroon.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.lucid.LucidConfig;
import com.inteliclinic.lucid.LucidConfiguration;
import com.inteliclinic.neuroon.events.TriggerAnalyzeSleeps;
import com.inteliclinic.neuroon.events.TriggerConfigUpdate;
import com.inteliclinic.neuroon.events.TriggerSleepUpdate;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.managers.StatsManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.AccessTokenUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.InvalidRefreshTokenEvent;
import com.inteliclinic.neuroon.managers.account.events.UserConfigUpdatedEvent;
import com.inteliclinic.neuroon.managers.network.NetworkManager;
import com.inteliclinic.neuroon.managers.tip.TipManager;
import com.inteliclinic.neuroon.mask.MaskEventStarted;
import com.inteliclinic.neuroon.mask.MaskEventStopped;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.NewSleepAvailableEvent;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.events.MaskSerialReceived;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.OldSleep;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.network.SleepRecordingMeta;
import com.inteliclinic.neuroon.old_guava.Strings;
import com.inteliclinic.neuroon.utils.NetworkUtils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import retrofit.Callback;

public class NeuroonService extends Service {
    public static final boolean LOCAL_WORK = false;
    private static final String TAG = NeuroonService.class.getSimpleName();
    private boolean isAnalyze;
    private ScreenBleDeviceStateReceiver mBle2DeviceReceiver;
    private boolean mDoubleSleepAnalyze;
    private Handler mHandler = new Handler(Looper.myLooper());
    private ScheduledExecutorService mScheduler;

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        EventBus.getDefault().postSticky(new ServiceStartingEvent());
        LucidConfig.init((Context) this);
        this.mBle2DeviceReceiver = new ScreenBleDeviceStateReceiver();
        registerReceiver(this.mBle2DeviceReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));
        this.mScheduler = Executors.newSingleThreadScheduledExecutor();
        new Thread() {
            public void run() {
                super.run();
                TipManager.getInstance().start(NeuroonService.this);
                MaskManager.getInstance().startBluetooth();
                NotificationsWatcher.startWatcher(NeuroonService.this);
                EventBus.getDefault().removeStickyEvent(ServiceStartingEvent.class);
                EventBus.getDefault().postSticky(new ServiceStartedEvent());
                NeuroonService.this.scheduleDailyTasks();
                NeuroonService.this.startOneTimeTask();
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void startOneTimeTask() {
        this.mHandler.post(new Runnable() {
            public void run() {
                NeuroonService.this.migrateOldSleepsToNewModel();
                NeuroonService.this.fixSleepTypes();
            }
        });
    }

    /* access modifiers changed from: private */
    public void syncTips() {
        if (AccountManager.getInstance().isLogged() && NetworkUtils.isWiFiOn(getApplicationContext())) {
            AccountManager.getInstance().synchronizeTips((Callback) null);
        }
    }

    /* access modifiers changed from: private */
    public void migrateOldSleepsToNewModel() {
        for (OldSleep sleep : DataManager.getInstance().getAllOldSleeps()) {
            Sleep.convertFromOld(sleep, MaskManager.getInstance().getAppVersion()).save();
        }
        DataManager.getInstance().deleteAllOldSleeps();
    }

    /* access modifiers changed from: private */
    public void fixSleepTypes() {
        List<Sleep> toDelete = new ArrayList<>();
        for (Sleep next : DataManager.getInstance().getSleepsWithNoType()) {
            if (next.getSleepType() == 0) {
                next.setAsDeleted();
                toDelete.add(next);
            }
        }
        if (toDelete.size() > 0) {
            Crashlytics.logException(new UnknownError("Sleeps with no type:" + String.valueOf(toDelete.size())));
            DataManager.getInstance().saveSleeps((Sleep[]) toDelete.toArray(new Sleep[toDelete.size()]));
        }
        DataManager.getInstance().revalidateMaskEvents();
    }

    /* access modifiers changed from: private */
    public void scheduleDailyTasks() {
        this.mScheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (AccountManager.getInstance().isLogged()) {
                    NeuroonService.this.syncConfigs();
                    NeuroonService.this.checkForMaskUpdate();
                    if (!Strings.isNullOrEmpty(MaskManager.getInstance().getMaskSerial())) {
                        AccountManager.getInstance().checkMask(MaskManager.getInstance().getMaskSerial());
                    }
                    NotificationsWatcher.check();
                    NeuroonService.this.syncTips();
                    NeuroonService.this.syncSleeps();
                }
            }
        }, 0, 8, TimeUnit.HOURS);
    }

    /* access modifiers changed from: private */
    public void syncSleeps() {
        SleepRecordingMeta l;
        if (AccountManager.getInstance().isLogged()) {
            for (SleepRecordingMeta sleepMeta : AccountManager.getInstance().getSleepMetas()) {
                if (DataManager.getInstance().getSleepIdByDate(sleepMeta.getSleepDate()) <= 0 && sleepMeta.isDataUploaded()) {
                    SleepRecordingMeta meta = AccountManager.getInstance().getSleepMeta(sleepMeta.getId());
                    byte[] data = AccountManager.getInstance().getSleepData(sleepMeta.getId());
                    DataManager.getInstance().saveSleeps(new Sleep[]{Sleep.fromData(meta, data)});
                    EventBus.getDefault().post(new TriggerAnalyzeSleeps());
                }
                DataManager.getInstance().revalidateMaskEvents();
            }
            try {
                List<Sleep> sleepsToSend = DataManager.getInstance().getSleepsToSend();
                int appConfigVersion = LucidConfiguration.getAppConfigVersion(getApplicationContext());
                for (Sleep sleep : sleepsToSend) {
                    if (!(sleep.getStartDate() == null || (l = AccountManager.getInstance().uploadSleepMeta(SleepRecordingMeta.fromSleep(sleep, MaskManager.getInstance().getAppVersionAsString(), appConfigVersion))) == null)) {
                        sleep.setSyncState(l.getId());
                        if (l.isDataUploaded()) {
                            sleep.setAsDataSynced();
                        }
                        sleep.save();
                    }
                }
                for (Sleep sleep2 : DataManager.getInstance().getSleepsWithoutUpload()) {
                    int i = AccountManager.getInstance().uploadSleepData(sleep2);
                    if (i == 1) {
                        sleep2.setAsDataSynced();
                        sleep2.save();
                    } else if (i == -1) {
                        sleep2.setSyncState(-1);
                        sleep2.save();
                    }
                }
            } catch (Exception ex) {
                Crashlytics.logException(ex);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkForMaskUpdate() {
        if (AccountManager.getInstance().isLogged()) {
            AccountManager.getInstance().checkMaskFirmware();
        }
    }

    /* access modifiers changed from: private */
    public void syncConfigs() {
        if (AccountManager.getInstance().isLogged() && AccountManager.getInstance().shouldConfigBeUpdated()) {
            AccountManager.getInstance().synchronizeConfigs();
        }
    }

    public void onEvent(TriggerAnalyzeSleeps event) {
        analyzeSleeps();
    }

    public void onEventBackgroundThread(TriggerSleepUpdate event) {
        syncSleeps();
    }

    public void onEventBackgroundThread(MaskEventStarted event) {
        if (event.getEvent().getType() == Event.EventType.ETSleep) {
            NotificationsWatcher.scheduleAlarm(event.getEvent().getEndDate());
        }
    }

    public void onEventBackgroundThread(MaskEventStopped event) {
        if (event.getEvent().getType() == Event.EventType.ETSleep) {
            NotificationsWatcher.removeScheduledAlarm();
        }
    }

    public void onEventBackgroundThread(MaskSerialReceived event) {
        AccountManager.getInstance().checkMask(MaskManager.getInstance().getMaskSerial());
    }

    private void analyzeSleeps() {
        if (isUnderAnalyze()) {
            setDoubleSleepAnalyze(true);
            return;
        }
        setAnalyze(true);
        analyzeSleepInt();
        while (isDoubleSleepAnalyze()) {
            setDoubleSleepAnalyze(false);
            analyzeSleepInt();
        }
        setAnalyze(false);
    }

    private void analyzeSleepInt() {
        Sleep sleep;
        Iterator<Sleep> it = DataManager.getInstance().getAllSleepsWithoutHypnogram().iterator();
        while (it.hasNext() && (sleep = it.next()) != null && !sleep.isDeleted()) {
            if (!sleep.hasHypnogram() && ((sleep.getSleepType() == 1 || sleep.getSleepType() == 2) && sleep.getSleepDuration() > 600 && new Date().after(sleep.getStartDate()))) {
                StatsManager.analyzeSleep(sleep);
                DataManager.getInstance().saveSleeps(new Sleep[]{sleep});
            } else if (!sleep.hasHypnogram() && (sleep.getSleepType() == 1 || sleep.getSleepType() == 2)) {
                sleep.setAsDeleted();
                DataManager.getInstance().saveSleeps(new Sleep[]{sleep});
            }
        }
    }

    private synchronized void setAnalyze(boolean is) {
        this.isAnalyze = is;
    }

    private synchronized boolean isUnderAnalyze() {
        return this.isAnalyze;
    }

    public void onEvent(TriggerConfigUpdate event) {
        syncConfigs();
    }

    public void onEvent(UserConfigUpdatedEvent event) {
        if (AccountManager.getInstance().getUserId() == -1) {
            AccountManager.getInstance().syncUserId();
        }
    }

    public void onEvent(ManagerStartedEvent event) {
    }

    public void onEvent(InvalidRefreshTokenEvent event) {
        AccountManager.getInstance().signOut();
        Crashlytics.logException(new UnsupportedOperationException("Refresh token"));
    }

    public void onEvent(AccessTokenUpdatedEvent event) {
        if (event.getRequestKey() != null) {
            if (event.getRequestKey().equals(NetworkManager.RequestKey.APP_CONFIG) || event.getRequestKey().equals(NetworkManager.RequestKey.USER_CONFIG)) {
                AccountManager.getInstance().synchronizeConfigs();
            }
        }
    }

    public void onEvent(NewSleepAvailableEvent event) {
        MaskManager.getInstance().downloadSleep(MaskManager.getInstance().getLastSavedSleep() + 1);
    }

    public void onEventBackgroundThread(ReceivedSleepEvent event) {
        int sleepNum = event.getSleepNum();
        if (sleepNum < MaskManager.getInstance().getMaskSleepCount() && sleepNum != -1) {
            MaskManager.getInstance().downloadSleep(sleepNum + 1);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        unregisterReceiver(this.mBle2DeviceReceiver);
        this.mBle2DeviceReceiver = null;
        BleManager.getInstance().stopBluetooth();
        AccountManager.getInstance().getLucid().saveConfig(getApplicationContext());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Started neuroon service with startId: " + startId);
        return 1;
    }

    private synchronized boolean isDoubleSleepAnalyze() {
        return this.mDoubleSleepAnalyze;
    }

    private synchronized void setDoubleSleepAnalyze(boolean b) {
        this.mDoubleSleepAnalyze = b;
    }

    private class ScreenBleDeviceStateReceiver extends BroadcastReceiver {
        private ScreenBleDeviceStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                return;
            }
            if (!MaskManager.getInstance().pairedWithDevice() || (MaskManager.getInstance().isConnected() && MaskManager.getInstance().isFullyConnected())) {
                BleManager.getInstance().stopScan();
            } else {
                BleManager.getInstance().scanForDevices();
            }
        }
    }
}
