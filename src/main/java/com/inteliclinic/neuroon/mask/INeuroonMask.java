package com.inteliclinic.neuroon.mask;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedStateEvent;
import com.inteliclinic.neuroon.models.data.Event;

public interface INeuroonMask {
    void blink(BluetoothDevice bluetoothDevice);

    void cancelEvent();

    void connectWithDevice(BluetoothDevice bluetoothDevice);

    void downloadSleep(int i);

    Event getCurrentEvent();

    void handleDeviceState(DeviceStateEvent deviceStateEvent);

    void handleSettings(ReceivedStateEvent receivedStateEvent);

    void handleSleepEvent(ReceivedSleepEvent receivedSleepEvent);

    boolean isConnected();

    boolean isDownloadingSleeps();

    boolean isFullyConnected();

    void sendEventToDevice(@NonNull Event event);

    void setMaskDownloadingSleep(boolean z);

    void stopDownloadingSleep();

    void updateFirmwareOnDevice(byte[] bArr, int i, int i2, int i3);
}
