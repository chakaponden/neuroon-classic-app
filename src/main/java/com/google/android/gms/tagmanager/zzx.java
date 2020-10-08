package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.List;
import java.util.Map;

class zzx extends zzdd {
    private static final String ID = zzad.DATA_LAYER_WRITE.toString();
    private static final String VALUE = zzae.VALUE.toString();
    private static final String zzbiL = zzae.CLEAR_PERSISTENT_DATA_LAYER_PREFIX.toString();
    private final DataLayer zzbhN;

    public zzx(DataLayer dataLayer) {
        super(ID, VALUE);
        this.zzbhN = dataLayer;
    }

    private void zza(zzag.zza zza) {
        String zzg;
        if (zza != null && zza != zzdf.zzHz() && (zzg = zzdf.zzg(zza)) != zzdf.zzHE()) {
            this.zzbhN.zzfX(zzg);
        }
    }

    private void zzb(zzag.zza zza) {
        if (zza != null && zza != zzdf.zzHz()) {
            Object zzl = zzdf.zzl(zza);
            if (zzl instanceof List) {
                for (Object next : (List) zzl) {
                    if (next instanceof Map) {
                        this.zzbhN.push((Map) next);
                    }
                }
            }
        }
    }

    public void zzR(Map<String, zzag.zza> map) {
        zzb(map.get(VALUE));
        zza(map.get(zzbiL));
    }
}
