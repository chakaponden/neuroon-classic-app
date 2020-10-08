package com.inteliclinic.neuroon.activities;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.PairMaskStartFragment;
import com.inteliclinic.neuroon.fragments.loading.LoadingFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskNotFoundFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskPickerFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskSearchFragment;
import com.inteliclinic.neuroon.fragments.mask.TurnOnBluetoothFragment;
import com.inteliclinic.neuroon.fragments.mask.TurnOnMaskFragment;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.mask.bluetooth.BluetoothAdapterStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceFoundEvent;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import de.greenrobot.event.EventBus;
import java.util.HashSet;
import java.util.Set;

public class PairMaskActivity extends BaseActivity implements PairMaskStartFragment.OnFragmentInteractionListener, MaskSearchFragment.OnFragmentInteractionListener, MaskNotFoundFragment.OnFragmentInteractionListener, MaskPickerFragment.OnFragmentInteractionListener, TurnOnBluetoothFragment.OnFragmentInteractionListener {
    private boolean mBTEnabling;
    /* access modifiers changed from: private */
    public Set<BluetoothDevice> mDevices = new HashSet();
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mScanning;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_pair_mask);
        BleManager.getInstance().stopScan();
        this.mHandler = new Handler(Looper.getMainLooper());
        if (this.mDevices.size() > 0) {
            updatePickerDevices();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.container, PairMaskStartFragment.newInstance()).commit();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.getDefault().registerSticky(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void closeFragments() {
        finish();
    }

    private void scanForMask() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            getFragmentManager().beginTransaction().replace(R.id.container, TurnOnBluetoothFragment.newInstance()).addToBackStack((String) null).commit();
            return;
        }
        this.mDevices.clear();
        this.mScanning = true;
        BleManager.getInstance().scanForDevices();
        getFragmentManager().beginTransaction().replace(R.id.container, MaskSearchFragment.newInstance()).commit();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                boolean unused = PairMaskActivity.this.mScanning = false;
                BleManager.getInstance().stopScan();
                if (PairMaskActivity.this.mDevices.isEmpty()) {
                    PairMaskActivity.this.getTracker().send(new HitBuilders.EventBuilder().setCategory("searching").setAction("mask_not_found").setLabel("mask_not_found").build());
                    if (!PairMaskActivity.this.isDestroyed() && !PairMaskActivity.this.isFinishing()) {
                        PairMaskActivity.this.getFragmentManager().beginTransaction().replace(R.id.container, MaskNotFoundFragment.newInstance()).commitAllowingStateLoss();
                    }
                }
            }
        }, 15000);
    }

    public void onEventMainThread(DeviceFoundEvent event) {
        if (this.mDevices != null && this.mScanning && this.mDevices.add(event.getDevice())) {
            if (this.mDevices.size() == 1) {
                getTracker().send(new HitBuilders.EventBuilder().setCategory("searching").setAction("mask_found").setLabel("mask_found").build());
            } else if (this.mDevices.size() == 2) {
                getTracker().send(new HitBuilders.EventBuilder().setCategory("searching").setAction("mask_select").setLabel("mask_select").build());
            }
            updatePickerDevices();
        }
    }

    public void onEventMainThread(DeviceStateEvent event) {
        if (MaskManager.getInstance().pairedWithDevice() && event.isConnected()) {
            setResult(-1);
            finish();
        }
    }

    private void updatePickerDevices() {
        Fragment fragmentById = getFragmentManager().findFragmentById(R.id.container);
        if (fragmentById == null || !(fragmentById instanceof MaskPickerFragment)) {
            getFragmentManager().beginTransaction().replace(R.id.container, MaskPickerFragment.newInstance(this.mDevices)).addToBackStack((String) null).commitAllowingStateLoss();
        } else {
            ((MaskPickerFragment) fragmentById).fillWithDevices(this.mDevices);
        }
    }

    public void onCancelSearch() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        getFragmentManager().beginTransaction().replace(R.id.container, PairMaskStartFragment.newInstance()).commit();
    }

    public void pickDevice(final String deviceAddress) {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("searching").setAction("mask_paired").setLabel("mask_paired").build());
        this.mScanning = false;
        setProgress(true);
        waitForEndBlinking(new Runnable() {
            public void run() {
                BleManager.getInstance().stopScan();
                BleManager.getInstance().resetAll();
                PairMaskActivity.this.mHandler.removeCallbacksAndMessages((Object) null);
                MaskManager.getInstance().saveMask(deviceAddress);
                PairMaskActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        BleManager.getInstance().scanForDevices();
                    }
                }, 1000);
            }
        });
    }

    /* access modifiers changed from: protected */
    @NonNull
    public LoadingFragment getLoadingFragment() {
        return TurnOnMaskFragment.newInstance((String) null);
    }

    /* access modifiers changed from: private */
    public void waitForEndBlinking(final Runnable runnable) {
        if (MaskManager.getInstance().isBlinking()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    PairMaskActivity.this.waitForEndBlinking(runnable);
                }
            }, 500);
        } else {
            runnable.run();
        }
    }

    public void onTryAgainSearch() {
        scanForMask();
    }

    public void pairMask() {
        scanForMask();
    }

    public boolean goBack() {
        Fragment fragmentById = getFragmentManager().findFragmentById(R.id.container);
        if (fragmentById == null || !(fragmentById instanceof PairMaskStartFragment)) {
            return super.goBack();
        }
        finish();
        return true;
    }

    public void onBuyNowClick() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.buy_mask_uri))));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(getWindow().getDecorView(), (CharSequence) "No application can handle this request. Please install a web browser", 0).show();
            e.printStackTrace();
        }
    }

    public void cancelBluetoothTurningOn() {
        getFragmentManager().popBackStack();
    }

    public void enableBluetooth() {
        this.mBTEnabling = true;
        BluetoothAdapter.getDefaultAdapter().enable();
    }

    public void onEvent(BluetoothAdapterStateEvent event) {
        if (event.isEnabled() && this.mBTEnabling) {
            this.mBTEnabling = false;
            getFragmentManager().popBackStack();
            scanForMask();
        }
    }
}
