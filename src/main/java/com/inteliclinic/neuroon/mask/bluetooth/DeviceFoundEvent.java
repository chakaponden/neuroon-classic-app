package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothDevice;

public class DeviceFoundEvent {
    private BluetoothDevice device;

    public DeviceFoundEvent(BluetoothDevice device2) {
        this.device = device2;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }
}
