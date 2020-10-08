package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

public class ReceivedStateEvent {
    private SettingsCharacteristic builder;
    private byte[] rawData;

    public ReceivedStateEvent(BluetoothGattCharacteristic rawData2) {
        this.rawData = rawData2.getValue();
        this.builder = new SettingsCharacteristic(rawData2);
    }

    public SettingsCharacteristic getSettingsCharacteristic() {
        return this.builder;
    }

    public byte[] getRawData() {
        return this.rawData;
    }

    public boolean isGood() {
        return this.builder.isCorrect();
    }
}
