package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsCmdFrame extends FrameContent {
    private final byte mCmd;

    public BleSettingsCmdFrame(byte cmd) {
        this.mCmd = cmd;
    }

    public byte[] toBytes() {
        return new byte[]{this.mCmd, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
