package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import com.google.android.gms.internal.zzrs;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class zzaz {
    private static zzag.zza zzK(Object obj) throws JSONException {
        return zzdf.zzR(zzL(obj));
    }

    static Object zzL(Object obj) throws JSONException {
        if (obj instanceof JSONArray) {
            throw new RuntimeException("JSONArrays are not supported");
        } else if (JSONObject.NULL.equals(obj)) {
            throw new RuntimeException("JSON nulls are not supported");
        } else if (!(obj instanceof JSONObject)) {
            return obj;
        } else {
            JSONObject jSONObject = (JSONObject) obj;
            HashMap hashMap = new HashMap();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                hashMap.put(next, zzL(jSONObject.get(next)));
            }
            return hashMap;
        }
    }

    public static zzrs.zzc zzgi(String str) throws JSONException {
        zzag.zza zzK = zzK(new JSONObject(str));
        zzrs.zzd zzHK = zzrs.zzc.zzHK();
        for (int i = 0; i < zzK.zzjz.length; i++) {
            zzHK.zzc(zzrs.zza.zzHH().zzb(zzae.INSTANCE_NAME.toString(), zzK.zzjz[i]).zzb(zzae.FUNCTION.toString(), zzdf.zzgt(zzn.zzFZ())).zzb(zzn.zzGa(), zzK.zzjA[i]).zzHJ());
        }
        return zzHK.zzHN();
    }
}
