package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class BleSettingsErrorFrame extends FrameContent {
    public static final byte BLE_INTERNAL_ERROR_CMD_INVALID = 3;
    public static final byte BLE_INTERNAL_ERROR_CMD_KEY_INVALID = 5;
    public static final byte BLE_INTERNAL_ERROR_CMD_RAW_STOP_INVALID_USE = 4;
    public static final byte BLE_INTERNAL_ERROR_CMD_TASK_INVALID = 2;
    public static final byte BLE_INTERNAL_ERROR_PREV_CMD_NOT_FINISHED = 1;
    private final int mErrorCode;

    public BleSettingsErrorFrame(BluetoothGattCharacteristic gattCharacteristic) {
        this.mErrorCode = gattCharacteristic.getIntValue(17, 1).intValue();
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public byte[] toBytes() {
        return new byte[0];
    }
}
