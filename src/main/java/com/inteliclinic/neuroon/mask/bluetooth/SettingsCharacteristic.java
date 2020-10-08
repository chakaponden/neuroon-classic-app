package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import com.inteliclinic.neuroon.mask.BleSettingsSerialFrame;
import com.inteliclinic.neuroon.mask.BleSettingsSleepFrame;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;

public class SettingsCharacteristic {
    public static final byte APP_CONFIG_MODE_BLT_MASK = 8;
    public static final byte APP_CONFIG_MODE_DEVICE_STATE_MASK = 1;
    public static final byte APP_CONFIG_MODE_DOWNLOAD_DATA_MASK = 64;
    public static final byte APP_CONFIG_MODE_JETLAG_SLEEP_MASK = 16;
    public static final byte APP_CONFIG_MODE_NAP_MASK = 4;
    public static final byte APP_CONFIG_MODE_SHUTDOWN_MASK = Byte.MIN_VALUE;
    public static final byte APP_CONFIG_MODE_SLEEP_MASK = 2;
    public static final byte APP_CONFIG_MODE_TIME_MASK = 32;
    public static final byte BLE_CMD_DEVSEL = 51;
    public static final byte BLE_CMD_EPOCH_CLEAR = 33;
    public static final byte BLE_CMD_GOTO_DFU = 49;
    public static final byte BLE_CMD_GOTO_TEST = 48;
    public static final byte BLE_CMD_RAW_CLEAR = 66;
    public static final byte BLE_CMD_RAW_START = 64;
    public static final byte BLE_CMD_RAW_STOP = 65;
    public static final byte BLE_CMD_STANDBY = 1;
    public static final byte BLE_CMD_TASK_BLT = 19;
    public static final byte BLE_CMD_TASK_JETLAG = 17;
    public static final byte BLE_CMD_TASK_NAP = 18;
    public static final byte BLE_CMD_TASK_SLEEP = 16;
    public static final byte BLE_CMD_UART_TEST = 52;
    public static final byte BLE_INTERNAL_CMD_ERROR = -113;
    public static final byte BLE_INTERNAL_CMD_GET_SERIAL = -125;
    public static final byte BLE_INTERNAL_CMD_SET_USERID = -126;
    public static final byte BLE_INTERNAL_CMD_START = Byte.MIN_VALUE;
    public static final byte BLE_INTERNAL_CMD_STATUS = -127;
    public static final byte FRIDAY = 32;
    public static final byte MONDAY = 2;
    public static final byte SATURDAY = 64;
    public static final byte SINGLE = 1;
    public static final byte SUNDAY = Byte.MIN_VALUE;
    public static final byte THURSDAY = 16;
    public static final byte TUESDAY = 4;
    public static final byte WEDNESDAY = 8;
    private boolean correct;
    private byte mCommand;
    private FrameContent mContent;

    public SettingsCharacteristic(BluetoothGattCharacteristic gattCharacteristic) {
        byte[] value = gattCharacteristic.getValue();
        if (value.length == 20) {
            this.mCommand = value[0];
            switch (this.mCommand) {
                case -127:
                    this.mContent = new BleSettingsStatusFrame(gattCharacteristic);
                    break;
                case -125:
                    this.mContent = new BleSettingsSerialFrame(value);
                    break;
                case -113:
                    this.mContent = new BleSettingsErrorFrame(gattCharacteristic);
                    break;
                case 16:
                    this.mContent = new BleSettingsSleepFrame(value);
                    break;
            }
            this.correct = true;
            return;
        }
        this.correct = false;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public byte[] toBytes() {
        return this.mContent.toBytes();
    }

    public int getCommand() {
        return this.mCommand;
    }

    public FrameContent getContent() {
        return this.mContent;
    }
}
