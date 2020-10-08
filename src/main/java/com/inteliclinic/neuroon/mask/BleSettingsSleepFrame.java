package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.bluetooth.FrameContent;
import java.nio.ByteBuffer;

public class BleSettingsSleepFrame extends FrameContent {
    private final byte mArtificialDawnIntensity;
    private final byte mArtificialDawnMinutes;
    private final short mMinutes;

    public BleSettingsSleepFrame(byte[] bytes) {
        this.mMinutes = ByteBuffer.wrap(bytes, 1, 2).getShort();
        this.mArtificialDawnMinutes = bytes[3];
        this.mArtificialDawnIntensity = bytes[4];
    }

    public BleSettingsSleepFrame(short sleepMinutes, byte artificialDawnMinutes, byte artificialDawnIntensity) {
        this.mMinutes = sleepMinutes;
        this.mArtificialDawnMinutes = artificialDawnMinutes;
        this.mArtificialDawnIntensity = artificialDawnIntensity;
    }

    public byte[] toBytes() {
        return new byte[]{16, (byte) this.mMinutes, (byte) (this.mMinutes >> 8), this.mArtificialDawnMinutes, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
