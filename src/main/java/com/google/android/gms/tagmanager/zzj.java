package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class zzj extends zzdd {
    private static final String ID = zzad.ARBITRARY_PIXEL.toString();
    private static final String URL = zzae.URL.toString();
    private static final String zzbhF = zzae.ADDITIONAL_PARAMS.toString();
    private static final String zzbhG = zzae.UNREPEATABLE.toString();
    static final String zzbhH = ("gtm_" + ID + "_unrepeatable");
    private static final Set<String> zzbhI = new HashSet();
    private final Context mContext;
    private final zza zzbhJ;

    public interface zza {
        zzar zzFX();
    }

    public zzj(final Context context) {
        this(context, new zza() {
            public zzar zzFX() {
                return zzz.zzaX(context);
            }
        });
    }

    zzj(Context context, zza zza2) {
        super(ID, URL);
        this.zzbhJ = zza2;
        this.mContext = context;
    }

    private synchronized boolean zzfL(String str) {
        boolean z = true;
        synchronized (this) {
            if (!zzfN(str)) {
                if (zzfM(str)) {
                    zzbhI.add(str);
                } else {
                    z = false;
                }
            }
        }
        return z;
    }

    public void zzR(Map<String, zzag.zza> map) {
        String zzg = map.get(zzbhG) != null ? zzdf.zzg(map.get(zzbhG)) : null;
        if (zzg == null || !zzfL(zzg)) {
            Uri.Builder buildUpon = Uri.parse(zzdf.zzg(map.get(URL))).buildUpon();
            zzag.zza zza2 = map.get(zzbhF);
            if (zza2 != null) {
                Object zzl = zzdf.zzl(zza2);
                if (!(zzl instanceof List)) {
                    zzbg.e("ArbitraryPixel: additional params not a list: not sending partial hit: " + buildUpon.build().toString());
                    return;
                }
                for (Object next : (List) zzl) {
                    if (!(next instanceof Map)) {
                        zzbg.e("ArbitraryPixel: additional params contains non-map: not sending partial hit: " + buildUpon.build().toString());
                        return;
                    }
                    for (Map.Entry entry : ((Map) next).entrySet()) {
                        buildUpon.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
            String uri = buildUpon.build().toString();
            this.zzbhJ.zzFX().zzgc(uri);
            zzbg.v("ArbitraryPixel: url = " + uri);
            if (zzg != null) {
                synchronized (zzj.class) {
                    zzbhI.add(zzg);
                    zzcv.zzb(this.mContext, zzbhH, zzg, "true");
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean zzfM(String str) {
        return this.mContext.getSharedPreferences(zzbhH, 0).contains(str);
    }

    /* access modifiers changed from: package-private */
    public boolean zzfN(String str) {
        return zzbhI.contains(str);
    }
}
