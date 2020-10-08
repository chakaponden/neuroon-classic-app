package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzdh extends zzak {
    private static final String ID = zzad.UPPERCASE_STRING.toString();
    private static final String zzbiQ = zzae.ARG0.toString();

    public zzdh() {
        super(ID, zzbiQ);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return zzdf.zzR(zzdf.zzg(map.get(zzbiQ)).toUpperCase());
    }
}
