package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsStartFrame extends FrameContent {
    private final int mTimestamp;

    public BleSettingsStartFrame(int timestamp) {
        this.mTimestamp = timestamp;
    }

    public byte[] toBytes() {
        return new byte[]{Byte.MIN_VALUE, (byte) this.mTimestamp, (byte) (this.mTimestamp >> 8), (byte) (this.mTimestamp >> 16), (byte) (this.mTimestamp >> 24), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
