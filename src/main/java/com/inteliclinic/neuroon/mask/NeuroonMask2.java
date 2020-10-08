package com.inteliclinic.neuroon.mask;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.neuroon.events.TriggerAnalyzeSleeps;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleSettingsStatusFrame;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.Date;

public class NeuroonMask2 implements INeuroonMask {
    private static final String TAG = NeuroonMask2.class.getSimpleName();
    private Event mActualSendingEvent;
    private IUserAlarmManager mAlarmManager;
    private boolean mDownloadingSleep;
    private boolean mForceStopDownloadingSleep;
    private boolean mIsEventGoing;
    private boolean mIsEventStarting;
    private Date mLastAutoStartTime;
    private boolean mMaskFullyConnected;
    private boolean notConnected = true;
    private IMaskUserManager userManager;

    public NeuroonMask2(IMaskUserManager userManager2, IUserAlarmManager alarmManager) {
        this.userManager = userManager2;
        this.mAlarmManager = alarmManager;
        setMaskConnected(false);
    }

    public void handleDeviceState(DeviceStateEvent event) {
        if (!event.isConnected()) {
            if (this.mDownloadingSleep) {
                this.mDownloadingSleep = false;
                EventBus.getDefault().post(new MaskDownloadStoppedEvent());
            }
            if (this.mIsEventStarting) {
                this.mActualSendingEvent.setEndDate(DateUtils.dateAddSeconds(this.mActualSendingEvent.getStartDate(), this.mActualSendingEvent.getDuration()));
                DataManager.getInstance().saveEvent(this.mActualSendingEvent);
                EventBus.getDefault().post(new MaskEventStarted(this.mActualSendingEvent));
                this.mIsEventStarting = false;
                this.mIsEventGoing = true;
            }
            setMaskConnected(false);
            this.notConnected = true;
            if (MaskManager.getInstance().pairedWithDevice()) {
                BleManager.getInstance().scanForDevices();
                return;
            }
            return;
        }
        cancelEventIfExist();
        BleManager.getInstance().sendSettings(new BleSettingsStatusFrame().toBytes());
    }

    private void cancelEventIfExist() {
        if (this.mIsEventGoing) {
            this.mIsEventGoing = false;
            if (this.mActualSendingEvent != null) {
                this.mActualSendingEvent.setEndDate(new Date());
                this.mActualSendingEvent.setCompleted(true);
                DataManager.getInstance().saveEvent(this.mActualSendingEvent);
                EventBus.getDefault().post(new MaskEventStopped(this.mActualSendingEvent));
                this.mActualSendingEvent = null;
            }
        }
    }

    private void setMaskConnected(boolean connected) {
        this.mMaskFullyConnected = connected;
        EventBus.getDefault().postSticky(new MaskConnectedEvent(this.mMaskFullyConnected));
    }

    public void handleSettings(ReceivedStateEvent event) {
        int sleepNum;
        int lastSavedSleep;
        if (event.isGood()) {
            SettingsCharacteristic settingsCharacteristic = event.getSettingsCharacteristic();
            if (settingsCharacteristic.getCommand() == -113) {
                askForMaskState();
                return;
            }
            Date startDate = new Date();
            switch (settingsCharacteristic.getCommand()) {
                case -128:
                    BleManager.getInstance().disconnectDevice();
                    return;
                case -127:
                    setMaskConnected(true);
                    MaskManager.getInstance().saveMaskInfo((BleSettingsStatusFrame) settingsCharacteristic.getContent());
                    sendUserHash(this.userManager.getUserMaskHash());
                    MaskManager.getInstance().retrieveSerialNumber();
                    if (!sendSleepIfShould() && !this.mIsEventGoing && !this.mIsEventStarting && (settingsCharacteristic.getContent() instanceof BleSettingsStatusFrame) && (sleepNum = ((BleSettingsStatusFrame) settingsCharacteristic.getContent()).getSleepNumber()) > (lastSavedSleep = MaskManager.getInstance().getLastSavedSleep())) {
                        EventBus.getDefault().postSticky(new NewSleepAvailableEvent(lastSavedSleep, sleepNum));
                        return;
                    }
                    return;
                case -125:
                    MaskManager.getInstance().saveMaskSerial(((BleSettingsSerialFrame) settingsCharacteristic.getContent()).getSerialNumber());
                    return;
                case 16:
                    if (this.mActualSendingEvent == null) {
                    }
                    this.mActualSendingEvent.setStartDate(startDate);
                    BleManager.getInstance().sendSettings(new BleSettingsStartFrame((int) (startDate.getTime() / 1000)).toBytes());
                    this.mIsEventStarting = true;
                    return;
                case 18:
                    if (this.mActualSendingEvent == null) {
                    }
                    this.mActualSendingEvent.setStartDate(startDate);
                    BleManager.getInstance().sendSettings(new BleSettingsStartFrame((int) (startDate.getTime() / 1000)).toBytes());
                    this.mIsEventStarting = true;
                    return;
                case 19:
                    if (this.mActualSendingEvent == null) {
                    }
                    this.mActualSendingEvent.setStartDate(startDate);
                    BleManager.getInstance().sendSettings(new BleSettingsStartFrame((int) (startDate.getTime() / 1000)).toBytes());
                    this.mIsEventStarting = true;
                    return;
                case 49:
                    BleManager.getInstance().connectWithDfuDevice();
                    return;
                default:
                    return;
            }
        }
    }

    private boolean sendSleepIfShould() {
        if (MaskManager.getInstance().isAutomaticSleepStartActive() && (this.mLastAutoStartTime == null || DateUtils.compareToNow(DateUtils.dateAddSeconds(this.mLastAutoStartTime, 60)) < 0)) {
            this.mLastAutoStartTime = null;
            Date nextAlarmTime = this.mAlarmManager.getNextAlarmTime();
            if (new Date().after(DateUtils.dateAddSeconds(nextAlarmTime, -36000)) && new Date().before(DateUtils.dateAddSeconds(nextAlarmTime, -18000))) {
                this.mLastAutoStartTime = new Date();
                Event event = Event.sleepEvent(nextAlarmTime);
                DataManager.getInstance().saveEvents(new Event[]{event});
                sendEventToDevice(event);
                EventBus.getDefault().post(new AutomaticSleepStartedEvent());
                return true;
            }
        }
        return false;
    }

    private void sendUserHash(byte[] username) {
        BleManager.getInstance().sendSettings(new BleSettingsUserIdFrame(username).toBytes());
    }

    public void handleSleepEvent(ReceivedSleepEvent event) {
        if (!event.shouldSkip()) {
            Sleep sleep = event.getSleep();
            if (!DataManager.getInstance().isSleepStartDateExist(sleep)) {
                DataManager.getInstance().saveSleeps(new Sleep[]{sleep});
                EventBus.getDefault().post(new TriggerAnalyzeSleeps());
            }
        }
    }

    public synchronized void sendEventToDevice(@NonNull Event event) {
        byte artificialDawnValue = 10;
        synchronized (this) {
            if (this.mDownloadingSleep) {
                stopDownloadingSleep();
            }
            this.mActualSendingEvent = event;
            int artificialDawnIntensity = MaskManager.getInstance().getArtificialDawnIntensity();
            if (artificialDawnIntensity != 0) {
                artificialDawnValue = artificialDawnIntensity == 1 ? (byte) 50 : 100;
            }
            switch (event.getType()) {
                case ETSleep:
                    BleManager.getInstance().sendSettings(new BleSettingsSleepFrame(DateUtils.minutesTo(event.getEndDate()), MaskManager.getInstance().useArtificialDawn() ? (byte) 30 : 0, artificialDawnValue).toBytes());
                    break;
                case ETBLT:
                    BleManager.getInstance().sendSettings(new BleSettingsBltFrame(DateUtils.minutesTo(event.getEndDate())).toBytes());
                    break;
                case ETNapPower:
                    BleManager.getInstance().sendSettings(new BleSettingsNapFrame((byte) DateUtils.minutesTo(event.getEndDate()), (byte) DateUtils.minutesTo(event.getEndDate()), (byte) 3, artificialDawnValue).toBytes());
                    break;
                case ETNapBody:
                    BleManager.getInstance().sendSettings(new BleSettingsNapFrame((byte) DateUtils.minutesTo(event.getEndDate()), (byte) DateUtils.minutesTo(event.getEndDate()), (byte) 5, artificialDawnValue).toBytes());
                    break;
                case ETNapRem:
                    BleManager.getInstance().sendSettings(new BleSettingsNapFrame((byte) DateUtils.minutesTo(event.getEndDate()), (byte) DateUtils.minutesTo(event.getEndDate()), (byte) 10, artificialDawnValue).toBytes());
                    break;
                case ETNapUltimate:
                    BleManager.getInstance().sendSettings(new BleSettingsNapFrame((byte) DateUtils.minutesTo(event.getEndDate()), (byte) DateUtils.minutesTo(event.getEndDate()), (byte) 20, artificialDawnValue).toBytes());
                    break;
            }
        }
    }

    public void downloadSleep(int sleepNum) {
        if (!this.mDownloadingSleep) {
            this.mDownloadingSleep = true;
            BleManager.getInstance().downloadSleep(sleepNum);
        }
    }

    public void cancelEvent() {
        cancelEventIfExist();
    }

    public boolean isConnected() {
        return !this.notConnected;
    }

    public void updateFirmwareOnDevice(byte[] data, int firmwareType, int firmwareSize, int firmwareCrc) {
        BleManager.getInstance().sendFirmwareData(data, firmwareType, firmwareSize, firmwareCrc);
    }

    public void connectWithDevice(BluetoothDevice device) {
        if (this.notConnected) {
            this.notConnected = false;
            BleManager.getInstance().connectWithDevice(device);
        }
    }

    public void blink(BluetoothDevice device) {
        BleManager.getInstance().sendSettings(device, new BleSettingsCmdFrame(SettingsCharacteristic.BLE_CMD_DEVSEL).toBytes());
    }

    public void stopDownloadingSleep() {
        this.mDownloadingSleep = false;
        BleManager.getInstance().stopDownloadSleep();
    }

    public boolean isFullyConnected() {
        return this.mMaskFullyConnected;
    }

    public void setMaskDownloadingSleep(boolean is) {
        this.mDownloadingSleep = is;
    }

    public boolean isDownloadingSleeps() {
        return this.mDownloadingSleep;
    }

    public Event getCurrentEvent() {
        return this.mActualSendingEvent;
    }

    private void askForMaskState() {
        BleManager.getInstance().sendSettings(new BleSettingsStatusFrame().toBytes());
    }
}
