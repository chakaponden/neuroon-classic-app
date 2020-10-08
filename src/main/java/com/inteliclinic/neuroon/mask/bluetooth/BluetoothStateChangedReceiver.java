package com.inteliclinic.neuroon.mask.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import de.greenrobot.event.EventBus;

public class BluetoothStateChangedReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
        if (intExtra == 10) {
            try {
                BleManager.getInstance().resetAll();
                EventBus.getDefault().postSticky(new BluetoothAdapterStateEvent(false));
            } catch (NullPointerException e) {
            }
        } else if (intExtra == 12) {
            BleManager.getInstance().startBluetooth();
            EventBus.getDefault().postSticky(new BluetoothAdapterStateEvent(true));
        }
    }
}
