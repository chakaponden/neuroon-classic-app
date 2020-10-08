package com.inteliclinic.neuroon.mask;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleSettingsStatusFrame;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceFoundEvent;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.inteliclinic.neuroon.mask.events.MaskSerialReceived;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.old_guava.Strings;
import com.inteliclinic.neuroon.settings.UserConfig;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.Date;

public final class MaskManager extends BaseManager implements IMaskManager {
    private static final String TAG = MaskManager.class.getSimpleName();
    private static IMaskManager mInstance;
    private boolean mBlinking;
    private BluetoothDevice mBlinkingDevice;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private INeuroonMask mNeuroonMask;

    private MaskManager(Context context, IMaskUserManager userManager, IUserAlarmManager alarmManager) {
        this.mContext = context;
        this.mNeuroonMask = new NeuroonMask2(userManager, alarmManager);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new ManagerStartedEvent(this));
    }

    public static IMaskManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException();
    }

    public static IMaskManager getInstance(Context context, IMaskUserManager userManager, IUserAlarmManager alarmManager) {
        if (mInstance == null) {
            mInstance = new MaskManager(context, userManager, alarmManager);
        }
        return mInstance;
    }

    public void startBluetooth() {
        BleManager.getInstance().startBluetooth();
    }

    public boolean illuminateDevice(BluetoothDevice device) {
        this.mNeuroonMask.blink(device);
        EventBus.getDefault().post(new MaskBlinkedEvent());
        return true;
    }

    public void connectWithDevice(BluetoothDevice device) {
        try {
            this.mNeuroonMask.connectWithDevice(device);
        } catch (Exception ex) {
            if (Crashlytics.getInstance() != null) {
                Crashlytics.logException(ex);
            }
        }
    }

    public void startEvent(Event event) {
        this.mNeuroonMask.sendEventToDevice(event);
    }

    public void cancelEvent() {
        this.mNeuroonMask.cancelEvent();
    }

    public void updateFirmwareOnDevice(byte[] data, int firmwareType, int firmwareSize, int firmwareCrc) {
        this.mNeuroonMask.updateFirmwareOnDevice(data, firmwareType, firmwareSize, firmwareCrc);
    }

    public void downloadSleep(int which) {
        Log.d(TAG, "Downloading sleep:" + which);
        this.mNeuroonMask.downloadSleep(which);
    }

    public boolean pairedWithDevice() {
        return !Strings.isNullOrEmpty((String) lucidRead(String.class, UserConfig.USER_DEVICE));
    }

    public void unPairMask() {
        BleManager.getInstance().disconnectDevice();
        lucidSet(String.class, UserConfig.USER_DEVICE, "");
        lucidSet(String.class, UserConfig.MASK_SERIAL, "");
        lucidSet(Integer.class, UserConfig.SAVED_SLEEP, 0);
        lucidSet(Integer.class, UserConfig.MASK_FIRMWARE_VER, -1);
        lucidSet(Integer.class, UserConfig.MASK_DFU_VER, -1);
        lucidSet(Integer.class, UserConfig.MASK_BOOT_VER, -1);
        lucidSet(Integer.class, UserConfig.MASK_SLEEP_NUMBER, -1);
        lucidSet(Integer.class, UserConfig.MASK_FREE_PACKETS, -1);
        lucidSet(Integer.class, UserConfig.MASK_BATTERY_LEVEL, -1);
        lucidSave(this.mContext);
    }

    public void saveMask(String deviceAddress) {
        Date date = new Date();
        lucidSet(String.class, UserConfig.USER_DEVICE, deviceAddress);
        lucidSet(Integer.class, UserConfig.USER_DEVICE_PAIR_DATE, Integer.valueOf((int) (date.getTime() / 1000)));
        lucidSave(this.mContext);
        DataManager.getInstance().saveEvent(Event.pairMaskEvent(date));
    }

    public boolean isMaskUpdating() {
        return BleManager.getInstance().isMaskUpdating();
    }

    public Date getPairDate() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.OLD_USER_DEVICE_PAIR_DATE);
        if (integer != null) {
            return new Date(((long) integer.intValue()) * 1000);
        }
        Integer integer2 = (Integer) lucidRead(Integer.class, UserConfig.USER_DEVICE_PAIR_DATE);
        if (integer2 != null) {
            return new Date(((long) integer2.intValue()) * 1000);
        }
        return null;
    }

    public Date getSavedNadir() {
        Double nadirValue = (Double) lucidRead(Double.class, UserConfig.USER_NADIR);
        if (nadirValue != null) {
            Date date = DateUtils.resetDateToToday(DateUtils.timeInUtc(Integer.valueOf(nadirValue.intValue())));
            if (DateUtils.compareToNow(date) < 0) {
                return DateUtils.dateAddSeconds(date, 86400);
            }
            return date;
        }
        Date date2 = DateUtils.resetDateToToday(new Date(18000000));
        return DateUtils.compareToNow(date2) < 0 ? DateUtils.dateAddSeconds(date2, 86400) : date2;
    }

    public boolean isConnected() {
        return this.mNeuroonMask.isConnected();
    }

    public void saveMaskInfo(BleSettingsStatusFrame content) {
        lucidSet(Integer.class, UserConfig.MASK_FIRMWARE_VER, Integer.valueOf(content.getAppVersion()));
        lucidSet(Integer.class, UserConfig.MASK_DFU_VER, Integer.valueOf(content.getDfuVersion()));
        lucidSet(Integer.class, UserConfig.MASK_BOOT_VER, Integer.valueOf(content.getBootVersion()));
        lucidSet(Integer.class, UserConfig.MASK_SLEEP_NUMBER, Integer.valueOf(content.getSleepNumber()));
        lucidSet(Integer.class, UserConfig.MASK_FREE_PACKETS, Integer.valueOf(content.getFreePackets()));
        lucidSet(Integer.class, UserConfig.MASK_BATTERY_LEVEL, Integer.valueOf(content.getBatteryState()));
        if (this.mContext != null) {
            lucidSave(this.mContext);
        }
        EventBus.getDefault().post(new MaskInfoUpdatedEvent());
    }

    public int getBatteryLevel() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.MASK_BATTERY_LEVEL);
        if (integer == null) {
            return -1;
        }
        return integer.intValue();
    }

    public String getDeviceName() {
        return (String) lucidRead(String.class, UserConfig.USER_DEVICE);
    }

    public int getAppVersion() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.MASK_FIRMWARE_VER);
        if (integer == null) {
            return 0;
        }
        return integer.intValue();
    }

    public void setAppVersion(int version) {
        lucidSet(Integer.class, UserConfig.MASK_FIRMWARE_VER, Integer.valueOf(version));
    }

    public String getAppVersionAsString() {
        return integerAsVersionString((Integer) lucidRead(Integer.class, UserConfig.MASK_FIRMWARE_VER));
    }

    public int getDfuVersion() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.MASK_DFU_VER);
        if (integer == null) {
            return 0;
        }
        return integer.intValue();
    }

    public String getDfuVersionAsString() {
        return integerAsVersionString((Integer) lucidRead(Integer.class, UserConfig.MASK_DFU_VER));
    }

    private String integerAsVersionString(Integer integer) {
        if (integer == null || integer.intValue() == -1) {
            return "0.0.0.0";
        }
        byte fourth = integer.byteValue();
        byte third = (byte) (integer.intValue() >> 8);
        return String.format("%d.%d.%d.%d", new Object[]{Byte.valueOf((byte) (integer.intValue() >> 24)), Byte.valueOf((byte) (integer.intValue() >> 16)), Byte.valueOf(third), Byte.valueOf(fourth)});
    }

    public synchronized void onEvent(DeviceFoundEvent event) {
        Log.i(TAG, "Found device: " + event.getDevice().getName() + " with address: " + event.getDevice().getAddress());
        try {
            if (event.getDevice().getAddress().equals(lucidRead(String.class, UserConfig.USER_DEVICE))) {
                connectWithDevice(event.getDevice());
            }
        } catch (IllegalStateException ex) {
            Log.w(TAG, "Not connected with device", ex);
        }
        return;
    }

    public void onEvent(DeviceStateEvent event) {
        Log.i(TAG, "Connected with device: " + event.isConnected());
        if (!this.mBlinking) {
            this.mNeuroonMask.handleDeviceState(event);
        }
    }

    public void onEventBackgroundThread(ReceivedSleepEvent event) {
        Log.i(TAG, "Received sleep");
        lucidSet(Integer.class, UserConfig.SAVED_SLEEP, Integer.valueOf(event.getSleepNum()));
        lucidSave(this.mContext);
        if (!event.shouldSkip()) {
            this.mNeuroonMask.handleSleepEvent(event);
            DataManager.getInstance().revalidateMaskEvents();
        }
    }

    public void onEvent(ReceivedStateEvent event) {
        this.mNeuroonMask.handleSettings(event);
    }

    public int getLastSavedSleep() {
        Integer lastSavedSleep = (Integer) lucidRead(Integer.class, UserConfig.SAVED_SLEEP);
        if (lastSavedSleep == null) {
            return 0;
        }
        return lastSavedSleep.intValue();
    }

    public void setSavedSleep(int i) {
        lucidSet(Integer.class, UserConfig.SAVED_SLEEP, Integer.valueOf(i));
        lucidSave(this.mContext);
    }

    public void retrieveSerialNumber() {
        if (isConnected() && isFullyConnected() && !isMaskUpdating()) {
            BleManager.getInstance().sendSettings(new BleSettingsCmdFrame(SettingsCharacteristic.BLE_INTERNAL_CMD_GET_SERIAL).toBytes());
        }
    }

    public String getMaskSerial() {
        return (String) lucidRead(String.class, UserConfig.MASK_SERIAL);
    }

    public void saveMaskSerial(String serialNumber) {
        lucidSet(String.class, UserConfig.MASK_SERIAL, serialNumber);
        EventBus.getDefault().post(new MaskSerialReceived());
    }

    public int getMaskSleepCount() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.MASK_SLEEP_NUMBER);
        if (integer == null) {
            return 0;
        }
        return integer.intValue();
    }

    public void setMaskDownloadingSleep(boolean is) {
        this.mNeuroonMask.setMaskDownloadingSleep(is);
    }

    public boolean isDownloadingSleeps() {
        return this.mNeuroonMask.isDownloadingSleeps();
    }

    public void setUseArtificialDawn(boolean use) {
        lucidSet(Boolean.class, UserConfig.USE_ARTIFICIAL_DAWN, Boolean.valueOf(use));
        lucidSave(this.mContext);
    }

    public boolean useArtificialDawn() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.USE_ARTIFICIAL_DAWN);
        return a == null || a.booleanValue();
    }

    public void setAutomaticSleepStart(boolean use) {
        lucidSet(Boolean.class, UserConfig.AUTOMATIC_SLEEP_START_ACTIVE, Boolean.valueOf(use));
        lucidSave(this.mContext);
    }

    public boolean isAutomaticSleepStartActive() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.AUTOMATIC_SLEEP_START_ACTIVE);
        return a == null || a.booleanValue();
    }

    public int getArtificialDawnIntensity() {
        Integer a = (Integer) lucidRead(Integer.class, UserConfig.ARTIFICIAL_DAWN_INTENSITY);
        if (a == null) {
            return 2;
        }
        return a.intValue();
    }

    public void setArtificialDawnIntensity(int intensity) {
        lucidSet(Integer.class, UserConfig.ARTIFICIAL_DAWN_INTENSITY, Integer.valueOf(intensity));
        lucidSave(this.mContext);
    }

    public Event getActiveEvent() {
        return this.mNeuroonMask.getCurrentEvent();
    }

    public boolean isMultiplePacketsAvailable() {
        return false;
    }

    public boolean isBlinking() {
        return this.mBlinking;
    }

    public void stopDownloadingSleep() {
        this.mNeuroonMask.stopDownloadingSleep();
    }

    public boolean isFullyConnected() {
        return this.mNeuroonMask.isFullyConnected();
    }

    public String getLucidDelegateKey() {
        return "mask-manager";
    }
}
