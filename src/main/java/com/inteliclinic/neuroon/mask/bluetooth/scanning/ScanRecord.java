package com.inteliclinic.neuroon.mask.bluetooth.scanning;

import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import java.util.List;
import java.util.Map;

public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA = 22;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    @Nullable
    private final List<ParcelUuid> mServiceUuids;
    private final int mTxPowerLevel;

    private ScanRecord(List<ParcelUuid> serviceUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] bytes) {
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mBytes = bytes;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r8v2, types: [byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord parseFromBytes(byte[] r30) {
        /*
            if (r30 != 0) goto L_0x0004
            r3 = 0
        L_0x0003:
            return r3
        L_0x0004:
            r18 = 0
            r7 = -1
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            r9 = 0
            r8 = -2147483648(0xffffffff80000000, float:-0.0)
            android.util.SparseArray r5 = new android.util.SparseArray
            r5.<init>()
            java.util.HashMap r6 = new java.util.HashMap
            r6.<init>()
            r19 = r18
        L_0x001b:
            r0 = r30
            int r3 = r0.length     // Catch:{ Exception -> 0x008b }
            r0 = r19
            if (r0 >= r3) goto L_0x00fc
            int r18 = r19 + 1
            byte r3 = r30[r19]     // Catch:{ Exception -> 0x003b }
            r0 = r3 & 255(0xff, float:3.57E-43)
            r23 = r0
            if (r23 != 0) goto L_0x0069
        L_0x002c:
            boolean r3 = r4.isEmpty()     // Catch:{ Exception -> 0x003b }
            if (r3 == 0) goto L_0x0033
            r4 = 0
        L_0x0033:
            com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord r3 = new com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord     // Catch:{ Exception -> 0x003b }
            r10 = r30
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)     // Catch:{ Exception -> 0x003b }
            goto L_0x0003
        L_0x003b:
            r21 = move-exception
        L_0x003c:
            java.lang.String r3 = "ScanRecord"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "unable to parse scan record: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = java.util.Arrays.toString(r30)
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            android.util.Log.e(r3, r10)
            com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord r10 = new com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = -1
            r15 = -2147483648(0xffffffff80000000, float:-0.0)
            r16 = 0
            r17 = r30
            r10.<init>(r11, r12, r13, r14, r15, r16, r17)
            r3 = r10
            goto L_0x0003
        L_0x0069:
            int r20 = r23 + -1
            int r19 = r18 + 1
            byte r3 = r30[r18]     // Catch:{ Exception -> 0x008b }
            r0 = r3 & 255(0xff, float:3.57E-43)
            r22 = r0
            switch(r22) {
                case 1: goto L_0x007b;
                case 2: goto L_0x0080;
                case 3: goto L_0x0080;
                case 4: goto L_0x008f;
                case 5: goto L_0x008f;
                case 6: goto L_0x009a;
                case 7: goto L_0x009a;
                case 8: goto L_0x00a6;
                case 9: goto L_0x00a6;
                case 10: goto L_0x00b6;
                case 22: goto L_0x00b9;
                case 255: goto L_0x00db;
                default: goto L_0x0076;
            }     // Catch:{ Exception -> 0x008b }
        L_0x0076:
            int r18 = r19 + r20
            r19 = r18
            goto L_0x001b
        L_0x007b:
            byte r3 = r30[r19]     // Catch:{ Exception -> 0x008b }
            r7 = r3 & 255(0xff, float:3.57E-43)
            goto L_0x0076
        L_0x0080:
            r3 = 2
            r0 = r30
            r1 = r19
            r2 = r20
            parseServiceUuid(r0, r1, r2, r3, r4)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x008b:
            r21 = move-exception
            r18 = r19
            goto L_0x003c
        L_0x008f:
            r3 = 4
            r0 = r30
            r1 = r19
            r2 = r20
            parseServiceUuid(r0, r1, r2, r3, r4)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x009a:
            r3 = 16
            r0 = r30
            r1 = r19
            r2 = r20
            parseServiceUuid(r0, r1, r2, r3, r4)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x00a6:
            java.lang.String r9 = new java.lang.String     // Catch:{ Exception -> 0x008b }
            r0 = r30
            r1 = r19
            r2 = r20
            byte[] r3 = extractBytes(r0, r1, r2)     // Catch:{ Exception -> 0x008b }
            r9.<init>(r3)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x00b6:
            byte r8 = r30[r19]     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x00b9:
            r29 = 2
            r0 = r30
            r1 = r19
            r2 = r29
            byte[] r28 = extractBytes(r0, r1, r2)     // Catch:{ Exception -> 0x008b }
            android.os.ParcelUuid r27 = com.inteliclinic.neuroon.mask.bluetooth.scanning.BluetoothUuid.parseUuidFrom(r28)     // Catch:{ Exception -> 0x008b }
            int r3 = r19 + r29
            int r10 = r20 - r29
            r0 = r30
            byte[] r26 = extractBytes(r0, r3, r10)     // Catch:{ Exception -> 0x008b }
            r0 = r27
            r1 = r26
            r6.put(r0, r1)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x00db:
            int r3 = r19 + 1
            byte r3 = r30[r3]     // Catch:{ Exception -> 0x008b }
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r3 = r3 << 8
            byte r10 = r30[r19]     // Catch:{ Exception -> 0x008b }
            r10 = r10 & 255(0xff, float:3.57E-43)
            int r25 = r3 + r10
            int r3 = r19 + 2
            int r10 = r20 + -2
            r0 = r30
            byte[] r24 = extractBytes(r0, r3, r10)     // Catch:{ Exception -> 0x008b }
            r0 = r25
            r1 = r24
            r5.put(r0, r1)     // Catch:{ Exception -> 0x008b }
            goto L_0x0076
        L_0x00fc:
            r18 = r19
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord.parseFromBytes(byte[]):com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord");
    }

    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            serviceUuids.add(BluetoothUuid.parseUuidFrom(extractBytes(scanRecord, currentPos, uuidLength)));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    @Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return this.mManufacturerSpecificData.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    @Nullable
    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return this.mServiceData.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    @Nullable
    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + this.mAdvertiseFlags + ", mServiceUuids=" + this.mServiceUuids + ", mManufacturerSpecificData=" + BluetoothLeUtils.toString(this.mManufacturerSpecificData) + ", mServiceData=" + BluetoothLeUtils.toString(this.mServiceData) + ", mTxPowerLevel=" + this.mTxPowerLevel + ", mDeviceName=" + this.mDeviceName + "]";
    }
}
