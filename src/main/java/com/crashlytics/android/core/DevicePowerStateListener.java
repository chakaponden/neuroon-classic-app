package com.crashlytics.android.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.concurrent.atomic.AtomicBoolean;

class DevicePowerStateListener {
    private static final IntentFilter FILTER_BATTERY_CHANGED = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    private static final IntentFilter FILTER_POWER_CONNECTED = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
    private static final IntentFilter FILTER_POWER_DISCONNECTED = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
    private final Context context;
    /* access modifiers changed from: private */
    public boolean isPowerConnected;
    private final BroadcastReceiver powerConnectedReceiver;
    private final BroadcastReceiver powerDisconnectedReceiver;
    private final AtomicBoolean receiversRegistered;

    public DevicePowerStateListener(Context context2) {
        int status = -1;
        this.context = context2;
        Intent statusIntent = context2.registerReceiver((BroadcastReceiver) null, FILTER_BATTERY_CHANGED);
        status = statusIntent != null ? statusIntent.getIntExtra("status", -1) : status;
        this.isPowerConnected = status == 2 || status == 5;
        this.powerConnectedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean unused = DevicePowerStateListener.this.isPowerConnected = true;
            }
        };
        this.powerDisconnectedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean unused = DevicePowerStateListener.this.isPowerConnected = false;
            }
        };
        context2.registerReceiver(this.powerConnectedReceiver, FILTER_POWER_CONNECTED);
        context2.registerReceiver(this.powerDisconnectedReceiver, FILTER_POWER_DISCONNECTED);
        this.receiversRegistered = new AtomicBoolean(true);
    }

    public boolean isPowerConnected() {
        return this.isPowerConnected;
    }

    public void dispose() {
        if (this.receiversRegistered.getAndSet(false)) {
            this.context.unregisterReceiver(this.powerConnectedReceiver);
            this.context.unregisterReceiver(this.powerDisconnectedReceiver);
        }
    }
}
