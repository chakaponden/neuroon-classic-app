package com.inteliclinic.neuroon.mask.bluetooth;

public class DfuProgressEvent {
    private final int packageCount;
    private final int packagesSent;

    public DfuProgressEvent(int packagesSent2, int packageCount2) {
        this.packagesSent = packagesSent2;
        this.packageCount = packageCount2;
    }

    public int getProgress(int max) {
        return (int) ((((float) this.packagesSent) / ((float) this.packageCount)) * ((float) max));
    }
}
