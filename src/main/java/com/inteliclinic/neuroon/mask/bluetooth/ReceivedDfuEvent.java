package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

public class ReceivedDfuEvent {
    private final byte[] data;

    ReceivedDfuEvent(BluetoothGattCharacteristic characteristic) {
        this.data = characteristic.getValue();
    }

    public byte[] getData() {
        return this.data;
    }
}
