package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsBltFrame extends FrameContent {
    private final byte mMinutes;

    public BleSettingsBltFrame(short minutes) {
        this.mMinutes = (byte) minutes;
    }

    public byte[] toBytes() {
        return new byte[]{SettingsCharacteristic.BLE_CMD_TASK_BLT, this.mMinutes, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
