package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzan extends zzbv {
    private static final String ID = zzad.GREATER_THAN.toString();

    public zzan() {
        super(ID);
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzde zzde, zzde zzde2, Map<String, zzag.zza> map) {
        return zzde.compareTo(zzde2) > 0;
    }
}
