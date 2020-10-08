package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.measurement.internal.zzm;

public class zzo extends zzj<zzm> {
    public zzo(Context context, Looper looper, zzf zzf, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 93, zzf, connectionCallbacks, onConnectionFailedListener);
    }

    /* renamed from: zzdo */
    public zzm zzW(IBinder iBinder) {
        return zzm.zza.zzdn(iBinder);
    }

    /* access modifiers changed from: protected */
    public String zzgu() {
        return "com.google.android.gms.measurement.START";
    }

    /* access modifiers changed from: protected */
    public String zzgv() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }
}
