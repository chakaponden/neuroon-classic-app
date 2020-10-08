package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

public class ReceivedDfuSettingsEvent {
    public static final int DFU_CRC_ERROR_FLAG = 34;
    public static final int DFU_CRC_OK_FLAG = 33;
    public static final int DFU_RESET_COMMAND = 18;
    public static final int FLASH_READY_COMMAND = 17;
    private final byte[] data;

    public ReceivedDfuSettingsEvent(BluetoothGattCharacteristic characteristic) {
        this.data = characteristic.getValue();
    }

    public int getStatus() {
        return this.data[0];
    }

    public byte[] getData() {
        return this.data;
    }
}
