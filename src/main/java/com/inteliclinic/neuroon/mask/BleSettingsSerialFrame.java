package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsSerialFrame extends FrameContent {
    private byte[] mBytes;

    public BleSettingsSerialFrame(byte[] bytes) {
        this.mBytes = bytes;
    }

    public byte[] toBytes() {
        return this.mBytes;
    }

    public String getSerialNumber() {
        return String.format("%02x%02x%02x%02x%02x%02x%02x", new Object[]{Integer.valueOf(this.mBytes[3] & 255), Integer.valueOf(this.mBytes[2] & 255), Integer.valueOf(this.mBytes[1] & 255), Integer.valueOf(this.mBytes[8] & 255), Integer.valueOf(this.mBytes[7] & 255), Integer.valueOf(this.mBytes[6] & 255), Integer.valueOf(this.mBytes[5] & 255)});
    }
}
