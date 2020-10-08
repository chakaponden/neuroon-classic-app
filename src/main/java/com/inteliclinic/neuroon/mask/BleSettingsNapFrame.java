package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsNapFrame extends FrameContent {
    private final byte mAccEventStartLevel;
    private final byte mAccEventStopLevel;
    private final byte mAccEventTailETime;
    private final byte mArtificialDawn;
    private final byte mArtificialDawnIntensity;
    private final byte mAsleepMaxWndTimerDelay;
    private final byte mAsleepWndEStart;
    private final byte mAsleepWndEStop;
    private final byte mContactLevel;
    private final byte mMaxNapLength;
    private final byte mNapLength;
    private final short mSpindlesStartLevel;
    private final byte mSpindlesStdMALength;
    private final short mSpindlesStopLevel;
    private final byte mSpindlesTailETime;

    public BleSettingsNapFrame(byte napLength, byte maxNapLength, byte artificialDawn, byte artificialDawnIntensity) {
        this(napLength, maxNapLength, artificialDawn, (byte) 0, (byte) 0, (byte) 0, (byte) 0, 0, 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, artificialDawnIntensity);
    }

    private BleSettingsNapFrame(byte mNapLength2, byte mMaxNapLength2, byte mArtificialDawn2, byte mContactLevel2, byte mAccEventStartLevel2, byte mAccEventStopLevel2, byte mAccEventTailETime2, short mSpindlesStartLevel2, short mSpindlesStopLevel2, byte mSpindlesTailETime2, byte mAsleepWndEStart2, byte mAsleepWndEStop2, byte mAsleepMaxWndTimerDelay2, byte mSpindlesStdMALength2, byte artificialDawnIntensity) {
        this.mNapLength = mNapLength2;
        this.mMaxNapLength = mMaxNapLength2;
        this.mArtificialDawn = mArtificialDawn2;
        this.mContactLevel = mContactLevel2;
        this.mAccEventStartLevel = mAccEventStartLevel2;
        this.mAccEventStopLevel = mAccEventStopLevel2;
        this.mAccEventTailETime = mAccEventTailETime2;
        this.mSpindlesStartLevel = mSpindlesStartLevel2;
        this.mSpindlesStopLevel = mSpindlesStopLevel2;
        this.mSpindlesTailETime = mSpindlesTailETime2;
        this.mAsleepWndEStart = mAsleepWndEStart2;
        this.mAsleepWndEStop = mAsleepWndEStop2;
        this.mAsleepMaxWndTimerDelay = mAsleepMaxWndTimerDelay2;
        this.mSpindlesStdMALength = mSpindlesStdMALength2;
        this.mArtificialDawnIntensity = artificialDawnIntensity;
    }

    public static BleSettingsNapFrame withDefaultSettings(byte napLength, byte maxNapLength, byte artificialDawn, byte artificialDawnIntensity) {
        return new BleSettingsNapFrame(napLength, maxNapLength, artificialDawn, (byte) 20, (byte) 25, (byte) 35, (byte) 8, 115, 95, (byte) 5, (byte) 20, (byte) 40, (byte) 10, (byte) 3, artificialDawnIntensity);
    }

    public byte[] toBytes() {
        return new byte[]{SettingsCharacteristic.BLE_CMD_TASK_NAP, this.mNapLength, this.mMaxNapLength, this.mArtificialDawn, this.mContactLevel, this.mAccEventStartLevel, this.mAccEventStopLevel, this.mAccEventTailETime, (byte) this.mSpindlesStartLevel, (byte) (this.mSpindlesStartLevel >> 8), (byte) this.mSpindlesStopLevel, (byte) (this.mSpindlesStopLevel >> 8), this.mSpindlesTailETime, this.mAsleepWndEStart, this.mAsleepWndEStop, this.mAsleepMaxWndTimerDelay, this.mSpindlesStdMALength, this.mArtificialDawnIntensity, 0, 0};
    }
}
