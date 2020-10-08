package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.measurement.internal.zzw;

public class AppMeasurement {
    private final zzw zzaTV;

    public interface zza {
        @WorkerThread
        void zza(String str, String str2, Bundle bundle, long j);
    }

    public AppMeasurement(zzw scion) {
        zzx.zzz(scion);
        this.zzaTV = scion;
    }

    public static AppMeasurement getInstance(Context context) {
        return zzw.zzaT(context).zzCV();
    }

    public void setMeasurementEnabled(boolean enabled) {
        this.zzaTV.zzCf().setMeasurementEnabled(enabled);
    }

    public void zzd(String str, String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.zzaTV.zzCf().zze(str, str2, bundle);
    }
}
