package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothDevice;

public class DeviceStateEvent {
    private boolean connected;
    private BluetoothDevice device;

    public DeviceStateEvent(BluetoothDevice device2, int status, int newState) {
        this.device = device2;
        if (status == 133) {
            this.connected = false;
        } else if (newState == 2) {
            this.connected = true;
        } else if (newState == 0) {
            this.connected = false;
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }
}
