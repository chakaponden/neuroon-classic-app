package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzaw extends zzak {
    private static final String ID = zzad.INSTALL_REFERRER.toString();
    private static final String zzbhD = zzae.COMPONENT.toString();
    private final Context context;

    public zzaw(Context context2) {
        super(ID, new String[0]);
        this.context = context2;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String zzm = zzax.zzm(this.context, map.get(zzbhD) != null ? zzdf.zzg(map.get(zzbhD)) : null);
        return zzm != null ? zzdf.zzR(zzm) : zzdf.zzHF();
    }
}
