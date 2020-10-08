package com.google.android.gms.analytics.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.android.gms.common.internal.zzx;

class zzag extends BroadcastReceiver {
    static final String zzSZ = zzag.class.getName();
    private final zzf zzQj;
    private boolean zzTa;
    private boolean zzTb;

    zzag(zzf zzf) {
        zzx.zzz(zzf);
        this.zzQj = zzf;
    }

    private Context getContext() {
        return this.zzQj.getContext();
    }

    private zzb zziH() {
        return this.zzQj.zziH();
    }

    private zzaf zzjm() {
        return this.zzQj.zzjm();
    }

    private void zzlz() {
        zzjm();
        zziH();
    }

    public boolean isConnected() {
        if (!this.zzTa) {
            this.zzQj.zzjm().zzbg("Connectivity unknown. Receiver not registered");
        }
        return this.zzTb;
    }

    public boolean isRegistered() {
        return this.zzTa;
    }

    public void onReceive(Context ctx, Intent intent) {
        zzlz();
        String action = intent.getAction();
        this.zzQj.zzjm().zza("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            boolean zzlB = zzlB();
            if (this.zzTb != zzlB) {
                this.zzTb = zzlB;
                zziH().zzJ(zzlB);
            }
        } else if (!"com.google.analytics.RADIO_POWERED".equals(action)) {
            this.zzQj.zzjm().zzd("NetworkBroadcastReceiver received unknown action", action);
        } else if (!intent.hasExtra(zzSZ)) {
            zziH().zzjg();
        }
    }

    public void unregister() {
        if (isRegistered()) {
            this.zzQj.zzjm().zzbd("Unregistering connectivity change receiver");
            this.zzTa = false;
            this.zzTb = false;
            try {
                getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                zzjm().zze("Failed to unregister the network broadcast receiver", e);
            }
        }
    }

    public void zzlA() {
        if (Build.VERSION.SDK_INT > 10) {
            Context context = getContext();
            Intent intent = new Intent("com.google.analytics.RADIO_POWERED");
            intent.addCategory(context.getPackageName());
            intent.putExtra(zzSZ, true);
            context.sendOrderedBroadcast(intent, (String) null);
        }
    }

    /* access modifiers changed from: protected */
    public boolean zzlB() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (SecurityException e) {
            return false;
        }
    }

    public void zzly() {
        zzlz();
        if (!this.zzTa) {
            Context context = getContext();
            context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            IntentFilter intentFilter = new IntentFilter("com.google.analytics.RADIO_POWERED");
            intentFilter.addCategory(context.getPackageName());
            context.registerReceiver(this, intentFilter);
            this.zzTb = zzlB();
            this.zzQj.zzjm().zza("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzTb));
            this.zzTa = true;
        }
    }
}
