package com.inteliclinic.neuroon.mask;

public class BatteryLevelReceivedEvent {
    private short level;

    public BatteryLevelReceivedEvent(short level2) {
        this.level = level2;
    }

    public short getLevel() {
        return this.level;
    }
}
