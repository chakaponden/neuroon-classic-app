package com.google.android.gms.tagmanager;

import android.os.Build;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzbx extends zzak {
    private static final String ID = zzad.OS_VERSION.toString();

    public zzbx() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return zzdf.zzR(Build.VERSION.RELEASE);
    }
}
