package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzc extends zzak {
    private static final String ID = zzad.ADVERTISING_TRACKING_ENABLED.toString();
    private final zza zzbhC;

    public zzc(Context context) {
        this(zza.zzaW(context));
    }

    zzc(zza zza) {
        super(ID, new String[0]);
        this.zzbhC = zza;
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return zzdf.zzR(Boolean.valueOf(!this.zzbhC.isLimitAdTrackingEnabled()));
    }
}
