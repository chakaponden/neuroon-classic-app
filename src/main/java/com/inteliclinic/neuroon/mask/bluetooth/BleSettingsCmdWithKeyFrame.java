package com.inteliclinic.neuroon.mask.bluetooth;

import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsCmdWithKeyFrame extends FrameContent {
    private final byte mCmd;
    private final int mKey;

    public BleSettingsCmdWithKeyFrame(byte cmd, int key) {
        this.mCmd = cmd;
        this.mKey = key;
    }

    public byte[] toBytes() {
        return new byte[]{this.mCmd, (byte) this.mKey, (byte) (this.mKey >> 8), (byte) (this.mKey >> 16), (byte) (this.mKey >> 24), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
