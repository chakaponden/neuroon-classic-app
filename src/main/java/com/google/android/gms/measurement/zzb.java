package com.google.android.gms.measurement;

import android.net.Uri;
import android.text.TextUtils;
import android.util.LogPrinter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class zzb implements zzi {
    private static final Uri zzaUf;
    private final LogPrinter zzaUg = new LogPrinter(4, "GA/LogCatTransport");

    static {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("uri");
        builder.authority("local");
        zzaUf = builder.build();
    }

    public void zzb(zzc zzc) {
        ArrayList<zze> arrayList = new ArrayList<>(zzc.zzAv());
        Collections.sort(arrayList, new Comparator<zze>() {
            /* renamed from: zza */
            public int compare(zze zze, zze zze2) {
                return zze.getClass().getCanonicalName().compareTo(zze2.getClass().getCanonicalName());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (zze obj : arrayList) {
            String obj2 = obj.toString();
            if (!TextUtils.isEmpty(obj2)) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(obj2);
            }
        }
        this.zzaUg.println(sb.toString());
    }

    public Uri zziA() {
        return zzaUf;
    }
}
