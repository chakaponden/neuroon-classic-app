package com.inteliclinic.neuroon.models.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class BleDataFrame extends FrameContent {
    private Integer mAccEvents;
    private Integer mAccMax;
    private int mAppVersion;
    private Integer mCrc1;
    private Integer mCrc2;
    int mFrameNumber;
    private Integer mHighAlpha;
    private Integer mHighBeta;
    private Integer mHighDelta;
    private Integer mLowAlpha;
    private Integer mLowBeta;
    private Integer mLowDelta;
    int mPacketCount;
    private Integer mPowNoDelta;
    private Integer mPower;
    private Integer mPrv;
    private Integer mPulse;
    byte[] mRawData;
    private Integer mSignalQuality;
    private int mSleepId;
    private int mSleepLength;
    private int mSleepType;
    private Integer mSpindles;
    private Integer mSpindlesStd;
    private Integer mTemperature;
    private Integer mTheta;
    private int mTimestamp;
    private byte[] mUserId;

    public BleDataFrame(int sleepNum, byte[] frame) {
        this(frame);
        this.mSleepId = sleepNum;
    }

    public BleDataFrame(int sleepNum) {
        this.mSleepId = sleepNum;
    }

    public BleDataFrame(byte[] frame) {
        this.mRawData = frame;
        if (frame.length == 20) {
            this.mFrameNumber = getUShort(frame, 0);
            if (this.mFrameNumber == 0) {
                headerFrame(frame);
            } else if (this.mFrameNumber == 1) {
                userIdFrame(frame);
            } else if (this.mFrameNumber % 2 == 0) {
                epochMod0(frame);
            } else {
                epochMod1(frame);
            }
        }
    }

    public BleDataFrame(int sleepNum, int frameNumber) {
        this.mSleepId = sleepNum;
        this.mFrameNumber = frameNumber;
    }

    public BleDataFrame(int sleepNum, int frameNumber, int packetCount) {
        this(sleepNum, frameNumber);
        this.mPacketCount = packetCount;
    }

    public int getFrameNumber() {
        return this.mFrameNumber;
    }

    public Integer getTimeStamp() {
        if (this.mFrameNumber == 0) {
            return Integer.valueOf(this.mTimestamp);
        }
        throw new UnsupportedOperationException("No timestamp in this frame");
    }

    public int getSleepId() {
        return this.mSleepId;
    }

    public int getSleepLength() {
        return this.mSleepLength;
    }

    public int getSleepType() {
        return this.mSleepType;
    }

    public int getTimestamp() {
        return this.mTimestamp;
    }

    public int getAppVersion() {
        return this.mAppVersion;
    }

    public byte[] getUserId() {
        return this.mUserId;
    }

    public Integer getPulse() {
        return this.mPulse;
    }

    public Integer getPrv() {
        return this.mPrv;
    }

    public Integer getAccEvents() {
        return this.mAccEvents;
    }

    public Integer getAccMax() {
        return this.mAccMax;
    }

    public Integer getLowDelta() {
        return this.mLowDelta;
    }

    public Integer getHighDelta() {
        return this.mHighDelta;
    }

    public Integer getTheta() {
        return this.mTheta;
    }

    public Integer getLowAlpha() {
        return this.mLowAlpha;
    }

    public Integer getSpindles() {
        return this.mSpindles;
    }

    public Integer getSpindlesStd() {
        return this.mSpindlesStd;
    }

    public Integer getCrc1() {
        return this.mCrc1;
    }

    public Integer getSignalQuality() {
        return this.mSignalQuality;
    }

    public Integer getHighAlpha() {
        return this.mHighAlpha;
    }

    public Integer getLowBeta() {
        return this.mLowBeta;
    }

    public Integer getHighBeta() {
        return this.mHighBeta;
    }

    public Integer getPower() {
        return this.mPower;
    }

    public Integer getPowNoDelta() {
        return this.mPowNoDelta;
    }

    public Integer getTemperature() {
        return this.mTemperature;
    }

    public Integer getCrc2() {
        return this.mCrc2;
    }

    private void headerFrame(byte[] frame) {
        this.mSleepId = getUShort(frame, 2);
        this.mSleepLength = getUShort(frame, 4);
        this.mSleepType = frame[6];
        this.mTimestamp = getUInt32(frame, 7);
        this.mAppVersion = getUInt32(frame, 11);
    }

    private void headerFrame(BluetoothGattCharacteristic gattCharacteristic) {
        this.mSleepId = gattCharacteristic.getIntValue(18, 2).intValue();
        this.mSleepLength = gattCharacteristic.getIntValue(18, 4).intValue();
        this.mSleepType = gattCharacteristic.getIntValue(17, 6).intValue();
        this.mTimestamp = gattCharacteristic.getIntValue(20, 7).intValue();
        this.mAppVersion = gattCharacteristic.getIntValue(20, 11).intValue();
    }

    private void userIdFrame(BluetoothGattCharacteristic gattCharacteristic) {
        this.mUserId = new byte[16];
        System.arraycopy(gattCharacteristic.getValue(), 2, this.mUserId, 0, 16);
    }

    private void userIdFrame(byte[] frame) {
        this.mUserId = new byte[16];
        System.arraycopy(frame, 2, this.mUserId, 0, 16);
    }

    private void epochMod0(BluetoothGattCharacteristic gattCharacteristic) {
        this.mPulse = gattCharacteristic.getIntValue(17, 2);
        this.mPrv = gattCharacteristic.getIntValue(17, 3);
        this.mAccEvents = gattCharacteristic.getIntValue(17, 4);
        this.mAccMax = gattCharacteristic.getIntValue(18, 5);
        this.mLowDelta = gattCharacteristic.getIntValue(18, 7);
        this.mHighDelta = gattCharacteristic.getIntValue(18, 9);
        this.mTheta = gattCharacteristic.getIntValue(18, 11);
        this.mLowAlpha = gattCharacteristic.getIntValue(18, 13);
        this.mSpindles = gattCharacteristic.getIntValue(18, 15);
        this.mSpindlesStd = gattCharacteristic.getIntValue(18, 17);
        this.mCrc1 = gattCharacteristic.getIntValue(17, 18);
    }

    private void epochMod0(byte[] frame) {
        this.mPulse = Integer.valueOf(unsignedByteToInt(frame[2]));
        this.mPrv = Integer.valueOf(unsignedByteToInt(frame[3]));
        this.mAccEvents = Integer.valueOf(unsignedByteToInt(frame[4]));
        this.mAccMax = Integer.valueOf(getUShort(frame, 5));
        this.mLowDelta = Integer.valueOf(getUShort(frame, 7));
        this.mHighDelta = Integer.valueOf(getUShort(frame, 9));
        this.mTheta = Integer.valueOf(getUShort(frame, 11));
        this.mLowAlpha = Integer.valueOf(getUShort(frame, 13));
        this.mSpindles = Integer.valueOf(getUShort(frame, 15));
        this.mSpindlesStd = Integer.valueOf(getUShort(frame, 17));
        this.mCrc1 = Integer.valueOf(unsignedByteToInt(frame[18]));
    }

    private void epochMod1(BluetoothGattCharacteristic gattCharacteristic) {
        this.mSignalQuality = gattCharacteristic.getIntValue(17, 2);
        this.mHighAlpha = gattCharacteristic.getIntValue(18, 3);
        this.mLowBeta = gattCharacteristic.getIntValue(18, 5);
        this.mHighBeta = gattCharacteristic.getIntValue(18, 7);
        this.mPower = gattCharacteristic.getIntValue(20, 9);
        this.mPowNoDelta = gattCharacteristic.getIntValue(20, 13);
        this.mTemperature = gattCharacteristic.getIntValue(18, 17);
        this.mCrc2 = gattCharacteristic.getIntValue(17, 19);
    }

    private void epochMod1(byte[] frame) {
        this.mSignalQuality = Integer.valueOf(unsignedByteToInt(frame[2]));
        this.mHighAlpha = Integer.valueOf(getUShort(frame, 3));
        this.mLowBeta = Integer.valueOf(getUShort(frame, 5));
        this.mHighBeta = Integer.valueOf(getUShort(frame, 7));
        this.mPower = Integer.valueOf(getUInt32(frame, 9));
        this.mPowNoDelta = Integer.valueOf(getUInt32(frame, 13));
        this.mTemperature = Integer.valueOf(getUShort(frame, 17));
        this.mCrc2 = Integer.valueOf(unsignedByteToInt(frame[19]));
    }

    public byte[] toBytes() {
        byte[] data = new byte[20];
        data[0] = (byte) this.mSleepId;
        data[1] = (byte) (this.mSleepId >> 8);
        data[2] = (byte) this.mFrameNumber;
        data[3] = (byte) (this.mFrameNumber >> 8);
        data[4] = (byte) this.mPacketCount;
        data[5] = (byte) (this.mPacketCount >> 8);
        return data;
    }

    public byte[] getRawDataWithoutPacketNumber() {
        return Arrays.copyOfRange(this.mRawData, 2, 20);
    }

    public int getSleepCount() {
        if (this.mFrameNumber == 0) {
            return this.mSleepId;
        }
        throw new NoSuchElementException();
    }

    public String toString() {
        return "BleDataFrame{mRawData=" + Arrays.toString(this.mRawData) + ", mFrameNumber=" + this.mFrameNumber + ", mSleepId=" + this.mSleepId + ", mSleepLength=" + this.mSleepLength + ", mSleepType=" + this.mSleepType + ", mTimestamp=" + this.mTimestamp + ", mAppVersion=" + this.mAppVersion + ", mUserId=" + Arrays.toString(this.mUserId) + ", mPulse=" + this.mPulse + ", mPrv=" + this.mPrv + ", mAccEvents=" + this.mAccEvents + ", mAccMax=" + this.mAccMax + ", mLowDelta=" + this.mLowDelta + ", mHighDelta=" + this.mHighDelta + ", mTheta=" + this.mTheta + ", mLowAlpha=" + this.mLowAlpha + ", mSpindles=" + this.mSpindles + ", mSpindlesStd=" + this.mSpindlesStd + ", mCrc1=" + this.mCrc1 + ", mSignalQuality=" + this.mSignalQuality + ", mHighAlpha=" + this.mHighAlpha + ", mLowBeta=" + this.mLowBeta + ", mHighBeta=" + this.mHighBeta + ", mPower=" + this.mPower + ", mPowNoDelta=" + this.mPowNoDelta + ", mTemperature=" + this.mTemperature + ", mCrc2=" + this.mCrc2 + '}';
    }

    public BleDataFrame incrementFrameId() {
        this.mFrameNumber++;
        return this;
    }

    public void decrementFrameId() {
        this.mFrameNumber--;
    }

    public byte[] getRawData() {
        return this.mRawData;
    }

    public void setFrameNumber(int frameNumber) {
        this.mFrameNumber = frameNumber;
    }
}
