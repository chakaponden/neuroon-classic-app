package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsUserIdFrame extends FrameContent {
    private byte[] mUsernameHashArr;

    public BleSettingsUserIdFrame(byte[] userhash) {
        this.mUsernameHashArr = userhash;
    }

    public byte[] toBytes() {
        return new byte[]{SettingsCharacteristic.BLE_INTERNAL_CMD_SET_USERID, this.mUsernameHashArr[0], this.mUsernameHashArr[1], this.mUsernameHashArr[2], this.mUsernameHashArr[3], this.mUsernameHashArr[4], this.mUsernameHashArr[5], this.mUsernameHashArr[6], this.mUsernameHashArr[7], this.mUsernameHashArr[8], this.mUsernameHashArr[9], this.mUsernameHashArr[10], this.mUsernameHashArr[11], this.mUsernameHashArr[12], this.mUsernameHashArr[13], this.mUsernameHashArr[14], this.mUsernameHashArr[15], 0, 0, 0};
    }
}
