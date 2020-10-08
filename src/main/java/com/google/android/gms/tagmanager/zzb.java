package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzb extends zzak {
    private static final String ID = zzad.ADVERTISER_ID.toString();
    private final zza zzbhC;

    public zzb(Context context) {
        this(zza.zzaW(context));
    }

    zzb(zza zza) {
        super(ID, new String[0]);
        this.zzbhC = zza;
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String zzFQ = this.zzbhC.zzFQ();
        return zzFQ == null ? zzdf.zzHF() : zzdf.zzR(zzFQ);
    }
}
