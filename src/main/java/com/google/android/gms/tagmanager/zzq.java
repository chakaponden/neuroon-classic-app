package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzq extends zzak {
    private static final String ID = zzad.CONTAINER_VERSION.toString();
    private final String zzadc;

    public zzq(String str) {
        super(ID, new String[0]);
        this.zzadc = str;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return this.zzadc == null ? zzdf.zzHF() : zzdf.zzR(this.zzadc);
    }
}
