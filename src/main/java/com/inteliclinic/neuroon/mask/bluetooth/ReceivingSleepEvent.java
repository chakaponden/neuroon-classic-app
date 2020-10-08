package com.inteliclinic.neuroon.mask.bluetooth;

public class ReceivingSleepEvent {
    private int actualFrame;
    private int allFrames;
    private int sleepNum;

    public ReceivingSleepEvent(int sleepNum2, int actualFrame2, int allFrames2) {
        this.sleepNum = sleepNum2;
        this.actualFrame = actualFrame2;
        this.allFrames = allFrames2;
    }

    public int getSleepNum() {
        return this.sleepNum;
    }

    public int getAllFrames() {
        if (this.allFrames == -1) {
            return this.actualFrame;
        }
        return this.allFrames;
    }

    public int getActualFrame() {
        return this.actualFrame;
    }
}
