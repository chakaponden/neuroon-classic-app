package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.ParcelUuid;
import android.os.SystemClock;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanRecord;
import com.inteliclinic.neuroon.mask.bluetooth.scanning.ScanResult;
import de.greenrobot.event.EventBus;
import java.util.List;

class BleManagerJBImpl extends BleManagerBase implements BluetoothAdapter.LeScanCallback {
    private static final String TAG = BleManagerJBImpl.class.getSimpleName();
    int tryCount = 0;

    BleManagerJBImpl(Context context, IMaskUserManager userManager) {
        super(context, userManager);
    }

    /* access modifiers changed from: protected */
    public void scanLeDevice(boolean enable) {
        if (!enable) {
            getBluetoothAdapter().stopLeScan(this);
        } else if (!getBluetoothAdapter().startLeScan(this)) {
            if (getBluetoothAdapter() != null) {
                getBluetoothAdapter().stopLeScan(this);
            }
            this.tryCount++;
            if (this.tryCount < 4) {
                getHandler().postDelayed(new Runnable() {
                    public void run() {
                        BleManagerJBImpl.this.scanLeDevice(true);
                    }
                }, (long) (this.tryCount * 1000));
            }
        }
    }

    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        List<ParcelUuid> serviceUuids;
        ScanResult scanResult = new ScanResult(device, ScanRecord.parseFromBytes(scanRecord), rssi, SystemClock.elapsedRealtimeNanos());
        if (scanResult.getScanRecord() != null && (serviceUuids = scanResult.getScanRecord().getServiceUuids()) != null && serviceUuids.contains(new ParcelUuid(UUID_SERVICE))) {
            EventBus.getDefault().post(new DeviceFoundEvent(device));
        }
    }
}
