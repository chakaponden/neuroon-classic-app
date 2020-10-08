package com.inteliclinic.neuroon.mask.bluetooth;

public class FirmwareSentEvent {
    private float procentage;
    private boolean success;

    public FirmwareSentEvent(float procentage2, boolean success2) {
        this.procentage = procentage2;
        this.success = success2;
    }
}
