package com.google.android.gms.tagmanager;

import android.os.Build;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzcr extends zzak {
    private static final String ID = zzad.SDK_VERSION.toString();

    public zzcr() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return zzdf.zzR(Integer.valueOf(Build.VERSION.SDK_INT));
    }
}
