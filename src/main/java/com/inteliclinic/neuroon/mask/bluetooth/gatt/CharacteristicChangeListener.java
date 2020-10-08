package com.inteliclinic.neuroon.mask.bluetooth.gatt;

import android.bluetooth.BluetoothGattCharacteristic;

public interface CharacteristicChangeListener {
    void onCharacteristicChanged(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr);
}
