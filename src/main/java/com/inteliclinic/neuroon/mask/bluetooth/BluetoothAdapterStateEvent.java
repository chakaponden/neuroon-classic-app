package com.inteliclinic.neuroon.mask.bluetooth;

public class BluetoothAdapterStateEvent {
    private final boolean enabled;

    public BluetoothAdapterStateEvent(boolean enabled2) {
        this.enabled = enabled2;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
