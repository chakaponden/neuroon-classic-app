package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

public class ReceivedLogEvent {
    private final byte[] logData;

    public ReceivedLogEvent(byte[] characteristic) {
        this.logData = characteristic;
    }

    public ReceivedLogEvent(BluetoothGattCharacteristic characteristic) {
        this(characteristic.getValue());
    }

    public byte[] getLogData() {
        return this.logData;
    }
}
