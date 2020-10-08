package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzda extends zzak {
    private static final String ID = zzad.TIME.toString();

    public zzda() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return zzdf.zzR(Long.valueOf(System.currentTimeMillis()));
    }
}
