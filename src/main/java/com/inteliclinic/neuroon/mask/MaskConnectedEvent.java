package com.inteliclinic.neuroon.mask;

public class MaskConnectedEvent {
    private final boolean connected;

    public MaskConnectedEvent(boolean connected2) {
        this.connected = connected2;
    }

    public boolean isConnected() {
        return this.connected;
    }
}
