package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.HashMap;
import java.util.Map;

class zzt extends zzak {
    private static final String ID = zzad.FUNCTION_CALL.toString();
    private static final String zzbhF = zzae.ADDITIONAL_PARAMS.toString();
    private static final String zzbip = zzae.FUNCTION_CALL_NAME.toString();
    private final zza zzbiq;

    public interface zza {
        Object zzc(String str, Map<String, Object> map);
    }

    public zzt(zza zza2) {
        super(ID, zzbip);
        this.zzbiq = zza2;
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String zzg = zzdf.zzg(map.get(zzbip));
        HashMap hashMap = new HashMap();
        zzag.zza zza2 = map.get(zzbhF);
        if (zza2 != null) {
            Object zzl = zzdf.zzl(zza2);
            if (!(zzl instanceof Map)) {
                zzbg.zzaK("FunctionCallMacro: expected ADDITIONAL_PARAMS to be a map.");
                return zzdf.zzHF();
            }
            for (Map.Entry entry : ((Map) zzl).entrySet()) {
                hashMap.put(entry.getKey().toString(), entry.getValue());
            }
        }
        try {
            return zzdf.zzR(this.zzbiq.zzc(zzg, hashMap));
        } catch (Exception e) {
            zzbg.zzaK("Custom macro/tag " + zzg + " threw exception " + e.getMessage());
            return zzdf.zzHF();
        }
    }
}
