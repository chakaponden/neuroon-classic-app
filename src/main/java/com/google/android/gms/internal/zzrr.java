package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.zzrs;
import java.util.HashMap;
import java.util.Map;

public class zzrr {
    private final Context mContext;
    private String zzbio;
    private final zzrt zzbmf;
    Map<String, Object<zzrs.zzc>> zzbmg;
    private final Map<String, Object> zzbmh;
    private final zzmq zzqW;

    public zzrr(Context context) {
        this(context, new HashMap(), new zzrt(context), zzmt.zzsc());
    }

    zzrr(Context context, Map<String, Object> map, zzrt zzrt, zzmq zzmq) {
        this.zzbio = null;
        this.zzbmg = new HashMap();
        this.mContext = context;
        this.zzqW = zzmq;
        this.zzbmf = zzrt;
        this.zzbmh = map;
    }

    public void zzgB(String str) {
        this.zzbio = str;
    }
}
