package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;
import java.util.Set;

public abstract class zzca extends zzak {
    private static final String zzbiQ = zzae.ARG0.toString();
    private static final String zzbjO = zzae.ARG1.toString();

    public zzca(String str) {
        super(str, zzbiQ, zzbjO);
    }

    public boolean zzFW() {
        return true;
    }

    public /* bridge */ /* synthetic */ String zzGB() {
        return super.zzGB();
    }

    public /* bridge */ /* synthetic */ Set zzGC() {
        return super.zzGC();
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        for (zzag.zza zza : map.values()) {
            if (zza == zzdf.zzHF()) {
                return zzdf.zzR(false);
            }
        }
        zzag.zza zza2 = map.get(zzbiQ);
        zzag.zza zza3 = map.get(zzbjO);
        return zzdf.zzR(Boolean.valueOf((zza2 == null || zza3 == null) ? false : zza(zza2, zza3, map)));
    }

    /* access modifiers changed from: protected */
    public abstract boolean zza(zzag.zza zza, zzag.zza zza2, Map<String, zzag.zza> map);
}
