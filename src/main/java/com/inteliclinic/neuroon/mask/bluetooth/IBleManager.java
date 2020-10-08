package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface IBleManager {
    void clearMaskData();

    void clearMaskRawData();

    void connectWithDevice(BluetoothDevice bluetoothDevice);

    void connectWithDfuDevice();

    void disconnectDevice();

    void downloadSleep(int i);

    boolean isMaskUpdating();

    void resetAll();

    void scanForDevices();

    void sendData(byte[] bArr);

    void sendFirmwareData(byte[] bArr, int i, int i2, int i3);

    void sendSettings(BluetoothDevice bluetoothDevice, byte[] bArr);

    void sendSettings(byte[] bArr);

    void startBluetooth();

    void stopBluetooth();

    void stopDownloadSleep();

    void stopScan();
}
