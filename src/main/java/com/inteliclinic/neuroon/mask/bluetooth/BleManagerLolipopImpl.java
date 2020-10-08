package com.inteliclinic.neuroon.mask.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.lucid.IMaskUserManager;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

@TargetApi(21)
class BleManagerLolipopImpl extends BleManagerBase {
    /* access modifiers changed from: private */
    public static final String TAG = BleManagerLolipopImpl.class.getSimpleName();
    /* access modifiers changed from: private */
    public static boolean mScanning;
    private BluetoothLeScanner mLeScanner;
    private ScanCallback mScanCallback = new MyScanCallback();
    private List<ScanFilter> mScanFilters;
    private ScanSettings mScanSettings;

    BleManagerLolipopImpl(Context context, IMaskUserManager userManager) {
        super(context, userManager);
    }

    /* access modifiers changed from: protected */
    public void getBluetooth() {
        super.getBluetooth();
        if (getBluetoothAdapter() != null) {
            this.mLeScanner = getBluetoothAdapter().getBluetoothLeScanner();
            this.mScanSettings = new ScanSettings.Builder().setScanMode(0).build();
            this.mScanFilters = new ArrayList();
            this.mScanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID_SERVICE)).build());
        }
    }

    /* access modifiers changed from: protected */
    public void scanLeDevice(boolean enable) {
        if (this.mLeScanner != null && getBluetoothAdapter() != null && getBluetoothAdapter().getState() == 12) {
            if (!enable) {
                setScanning(false);
                try {
                    this.mLeScanner.stopScan(this.mScanCallback);
                } catch (Exception ex) {
                    if (Fabric.isInitialized()) {
                        Crashlytics.logException(ex);
                    }
                }
            } else if (isScanning()) {
                scanLeDevice(false);
                getHandler().postDelayed(new Runnable() {
                    public void run() {
                        BleManagerLolipopImpl.this.scanLeDevice(true);
                    }
                }, 500);
            } else {
                setScanning(true);
                try {
                    this.mLeScanner.startScan(this.mScanFilters, this.mScanSettings, this.mScanCallback);
                } catch (Exception ex2) {
                    if (Fabric.isInitialized()) {
                        Crashlytics.logException(ex2);
                    }
                }
            }
        }
    }

    private synchronized boolean isScanning() {
        return mScanning;
    }

    private synchronized void setScanning(boolean scanning) {
        mScanning = scanning;
    }

    private static class MyScanCallback extends ScanCallback {
        private MyScanCallback() {
        }

        public void onScanResult(int callbackType, ScanResult result) {
            EventBus.getDefault().post(new DeviceFoundEvent(result.getDevice()));
        }

        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i(BleManagerLolipopImpl.TAG + "ScanResult - Results", sr.toString());
                EventBus.getDefault().post(new DeviceFoundEvent(sr.getDevice()));
            }
        }

        public void onScanFailed(int errorCode) {
            Log.e(BleManagerLolipopImpl.TAG + "Scan Failed", "Error Code: " + errorCode);
            boolean unused = BleManagerLolipopImpl.mScanning = false;
        }
    }
}
