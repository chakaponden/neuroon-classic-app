package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzbc extends zzbv {
    private static final String ID = zzad.LESS_EQUALS.toString();

    public zzbc() {
        super(ID);
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzde zzde, zzde zzde2, Map<String, zzag.zza> map) {
        return zzde.compareTo(zzde2) <= 0;
    }
}
