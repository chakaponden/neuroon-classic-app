package com.inteliclinic.neuroon.mask.bluetooth.scanning;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.Arrays;

public final class ScanResult implements Parcelable {
    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel source) {
            return new ScanResult(source);
        }

        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };
    private BluetoothDevice mDevice;
    private int mRssi;
    @Nullable
    private ScanRecord mScanRecord;
    private long mTimestampNanos;

    public ScanResult(BluetoothDevice device, ScanRecord scanRecord, int rssi, long timestampNanos) {
        this.mDevice = device;
        this.mScanRecord = scanRecord;
        this.mRssi = rssi;
        this.mTimestampNanos = timestampNanos;
    }

    private ScanResult(Parcel in) {
        readFromParcel(in);
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.mDevice != null) {
            dest.writeInt(1);
            this.mDevice.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (this.mScanRecord != null) {
            dest.writeInt(1);
            dest.writeByteArray(this.mScanRecord.getBytes());
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.mRssi);
        dest.writeLong(this.mTimestampNanos);
    }

    private void readFromParcel(Parcel in) {
        if (in.readInt() == 1) {
            this.mDevice = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(in);
        }
        if (in.readInt() == 1) {
            this.mScanRecord = ScanRecord.parseFromBytes(in.createByteArray());
        }
        this.mRssi = in.readInt();
        this.mTimestampNanos = in.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    @Nullable
    public ScanRecord getScanRecord() {
        return this.mScanRecord;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public long getTimestampNanos() {
        return this.mTimestampNanos;
    }

    public int hashCode() {
        return hash(this.mDevice, Integer.valueOf(this.mRssi), this.mScanRecord, Long.valueOf(this.mTimestampNanos));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScanResult other = (ScanResult) obj;
        if (this.mDevice != null ? this.mDevice.equals(other.mDevice) : other.mDevice == null) {
            if (this.mRssi == other.mRssi && (this.mScanRecord != null ? this.mScanRecord.equals(other.mScanRecord) : other.mScanRecord == null) && this.mTimestampNanos == other.mTimestampNanos) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        String scanRecord;
        StringBuilder append = new StringBuilder().append("ScanResult{mDevice=").append(this.mDevice).append(", mScanRecord=");
        if (this.mScanRecord == null) {
            scanRecord = "null";
        } else {
            scanRecord = this.mScanRecord.toString();
        }
        return append.append(scanRecord).append(", mRssi=").append(this.mRssi).append(", mTimestampNanos=").append(this.mTimestampNanos).append('}').toString();
    }
}
