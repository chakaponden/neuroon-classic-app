package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class zzdj {
    private static zzbw<zzag.zza> zza(zzbw<zzag.zza> zzbw) {
        try {
            return new zzbw<>(zzdf.zzR(zzgA(zzdf.zzg(zzbw.getObject()))), zzbw.zzGP());
        } catch (UnsupportedEncodingException e) {
            zzbg.zzb("Escape URI: unsupported encoding", e);
            return zzbw;
        }
    }

    private static zzbw<zzag.zza> zza(zzbw<zzag.zza> zzbw, int i) {
        if (!zzn(zzbw.getObject())) {
            zzbg.e("Escaping can only be applied to strings.");
            return zzbw;
        }
        switch (i) {
            case 12:
                return zza(zzbw);
            default:
                zzbg.e("Unsupported Value Escaping: " + i);
                return zzbw;
        }
    }

    static zzbw<zzag.zza> zza(zzbw<zzag.zza> zzbw, int... iArr) {
        for (int zza : iArr) {
            zzbw = zza(zzbw, zza);
        }
        return zzbw;
    }

    static String zzgA(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, HttpRequest.CHARSET_UTF8).replaceAll("\\+", "%20");
    }

    private static boolean zzn(zzag.zza zza) {
        return zzdf.zzl(zza) instanceof String;
    }
}
