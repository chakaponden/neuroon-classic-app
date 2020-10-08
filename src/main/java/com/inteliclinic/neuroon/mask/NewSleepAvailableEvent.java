package com.inteliclinic.neuroon.mask;

public class NewSleepAvailableEvent {
    private final int lastSavedSleep;
    private final int lastSleepIndex;

    public NewSleepAvailableEvent(int lastSavedSleep2, int lastSleepIndex2) {
        this.lastSavedSleep = lastSavedSleep2;
        this.lastSleepIndex = lastSleepIndex2;
    }

    public int getLastSavedSleep() {
        return this.lastSavedSleep;
    }

    public int getLastSleep() {
        return this.lastSleepIndex;
    }
}
