package com.inteliclinic.neuroon.mask;

import android.bluetooth.BluetoothDevice;
import com.inteliclinic.neuroon.mask.bluetooth.BleSettingsStatusFrame;
import com.inteliclinic.neuroon.models.data.Event;
import java.util.Date;

public interface IMaskManager {
    void cancelEvent();

    void connectWithDevice(BluetoothDevice bluetoothDevice);

    void downloadSleep(int i);

    Event getActiveEvent();

    int getAppVersion();

    String getAppVersionAsString();

    int getArtificialDawnIntensity();

    int getBatteryLevel();

    String getDeviceName();

    int getDfuVersion();

    String getDfuVersionAsString();

    int getLastSavedSleep();

    String getMaskSerial();

    int getMaskSleepCount();

    Date getPairDate();

    Date getSavedNadir();

    boolean illuminateDevice(BluetoothDevice bluetoothDevice);

    boolean isAutomaticSleepStartActive();

    boolean isBlinking();

    boolean isConnected();

    boolean isDownloadingSleeps();

    boolean isFullyConnected();

    boolean isMaskUpdating();

    boolean isMultiplePacketsAvailable();

    boolean pairedWithDevice();

    void retrieveSerialNumber();

    void saveMask(String str);

    void saveMaskInfo(BleSettingsStatusFrame bleSettingsStatusFrame);

    void saveMaskSerial(String str);

    void setAppVersion(int i);

    void setArtificialDawnIntensity(int i);

    void setAutomaticSleepStart(boolean z);

    void setMaskDownloadingSleep(boolean z);

    void setSavedSleep(int i);

    void setUseArtificialDawn(boolean z);

    void startBluetooth();

    void startEvent(Event event);

    void stopDownloadingSleep();

    void unPairMask();

    void updateFirmwareOnDevice(byte[] bArr, int i, int i2, int i3);

    boolean useArtificialDawn();
}
