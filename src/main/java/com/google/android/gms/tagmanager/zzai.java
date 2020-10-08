package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzai extends zzak {
    private static final String ID = zzad.EVENT.toString();
    private final zzcp zzbhO;

    public zzai(zzcp zzcp) {
        super(ID, new String[0]);
        this.zzbhO = zzcp;
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String zzHe = this.zzbhO.zzHe();
        return zzHe == null ? zzdf.zzHF() : zzdf.zzR(zzHe);
    }
}
