package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzcc extends zzak {
    private static final String ID = zzad.RANDOM.toString();
    private static final String zzbjY = zzae.MIN.toString();
    private static final String zzbjZ = zzae.MAX.toString();

    public zzcc() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        double d;
        double d2;
        zzag.zza zza = map.get(zzbjY);
        zzag.zza zza2 = map.get(zzbjZ);
        if (!(zza == null || zza == zzdf.zzHF() || zza2 == null || zza2 == zzdf.zzHF())) {
            zzde zzh = zzdf.zzh(zza);
            zzde zzh2 = zzdf.zzh(zza2);
            if (!(zzh == zzdf.zzHD() || zzh2 == zzdf.zzHD())) {
                double doubleValue = zzh.doubleValue();
                d = zzh2.doubleValue();
                if (doubleValue <= d) {
                    d2 = doubleValue;
                    return zzdf.zzR(Long.valueOf(Math.round(((d - d2) * Math.random()) + d2)));
                }
            }
        }
        d = 2.147483647E9d;
        d2 = 0.0d;
        return zzdf.zzR(Long.valueOf(Math.round(((d - d2) * Math.random()) + d2)));
    }
}
