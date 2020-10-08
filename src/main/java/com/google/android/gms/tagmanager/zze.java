package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zze extends zzak {
    private static final String ID = zzad.ADWORDS_CLICK_REFERRER.toString();
    private static final String zzbhD = zzae.COMPONENT.toString();
    private static final String zzbhE = zzae.CONVERSION_ID.toString();
    private final Context context;

    public zze(Context context2) {
        super(ID, zzbhE);
        this.context = context2;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        zzag.zza zza = map.get(zzbhE);
        if (zza == null) {
            return zzdf.zzHF();
        }
        String zzg = zzdf.zzg(zza);
        zzag.zza zza2 = map.get(zzbhD);
        String zzf = zzax.zzf(this.context, zzg, zza2 != null ? zzdf.zzg(zza2) : null);
        return zzf != null ? zzdf.zzR(zzf) : zzdf.zzHF();
    }
}
