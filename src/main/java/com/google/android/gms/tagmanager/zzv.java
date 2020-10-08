package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzv extends zzak {
    private static final String ID = zzad.CUSTOM_VAR.toString();
    private static final String NAME = zzae.NAME.toString();
    private static final String zzbiA = zzae.DEFAULT_VALUE.toString();
    private final DataLayer zzbhN;

    public zzv(DataLayer dataLayer) {
        super(ID, NAME);
        this.zzbhN = dataLayer;
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        Object obj = this.zzbhN.get(zzdf.zzg(map.get(NAME)));
        if (obj != null) {
            return zzdf.zzR(obj);
        }
        zzag.zza zza = map.get(zzbiA);
        return zza != null ? zza : zzdf.zzHF();
    }
}
