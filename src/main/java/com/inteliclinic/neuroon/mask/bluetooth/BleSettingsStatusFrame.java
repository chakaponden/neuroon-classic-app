package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import com.inteliclinic.neuroon.mask.BatteryLevelReceivedEvent;
import com.inteliclinic.neuroon.models.bluetooth.FrameContent;
import de.greenrobot.event.EventBus;

public class BleSettingsStatusFrame extends FrameContent {
    private int mAppVersion;
    private byte mBatteryState;
    private short mBootVersion;
    private int mDfuVersion;
    private int mFreePackets;
    private short mSleepNumber;

    public BleSettingsStatusFrame(BluetoothGattCharacteristic gatt) {
        this.mAppVersion = gatt.getIntValue(20, 1).intValue();
        this.mDfuVersion = gatt.getIntValue(20, 5).intValue();
        this.mSleepNumber = gatt.getIntValue(18, 9).shortValue();
        this.mFreePackets = gatt.getIntValue(20, 11).intValue();
        this.mBatteryState = gatt.getValue()[15];
        this.mBootVersion = gatt.getIntValue(18, 16).shortValue();
        EventBus.getDefault().postSticky(new BatteryLevelReceivedEvent((short) this.mBatteryState));
    }

    public BleSettingsStatusFrame() {
    }

    public short getSleepNumber() {
        return this.mSleepNumber;
    }

    public int getAppVersion() {
        return this.mAppVersion;
    }

    public int getDfuVersion() {
        return this.mDfuVersion;
    }

    public short getBootVersion() {
        return this.mBootVersion;
    }

    public int getFreePackets() {
        return this.mFreePackets;
    }

    public byte getBatteryState() {
        return this.mBatteryState;
    }

    public byte[] toBytes() {
        return new byte[]{SettingsCharacteristic.BLE_INTERNAL_CMD_STATUS, (byte) (this.mAppVersion << 8), (byte) this.mAppVersion, (byte) (this.mDfuVersion << 8), (byte) this.mDfuVersion, (byte) (this.mSleepNumber << 8), (byte) this.mSleepNumber, (byte) (this.mFreePackets << 24), (byte) (this.mFreePackets << 16), (byte) (this.mFreePackets << 8), (byte) this.mFreePackets, this.mBatteryState, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
