package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzn extends zzak {
    private static final String ID = zzad.CONSTANT.toString();
    private static final String VALUE = zzae.VALUE.toString();

    public zzn() {
        super(ID, VALUE);
    }

    public static String zzFZ() {
        return ID;
    }

    public static String zzGa() {
        return VALUE;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        return map.get(VALUE);
    }
}
