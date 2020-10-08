package com.google.android.gms.analytics.internal;

import android.util.Log;
import com.google.android.gms.common.internal.zzx;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.Map;

public class zzaf extends zzd {
    private static String zzSW = "3";
    private static String zzSX = "01VDIWEA?";
    private static zzaf zzSY;

    public zzaf(zzf zzf) {
        super(zzf);
    }

    public static zzaf zzlx() {
        return zzSY;
    }

    public void zza(int i, String str, Object obj, Object obj2, Object obj3) {
        String str2 = zzy.zzRL.get();
        if (Log.isLoggable(str2, i)) {
            Log.println(i, str2, zzc(str, obj, obj2, obj3));
        }
        if (i >= 5) {
            zzb(i, str, obj, obj2, obj3);
        }
    }

    public void zza(zzab zzab, String str) {
        if (str == null) {
            str = "no reason provided";
        }
        zzd("Discarding hit. " + str, zzab != null ? zzab.toString() : "no hit data");
    }

    public synchronized void zzb(int i, String str, Object obj, Object obj2, Object obj3) {
        int i2 = 0;
        synchronized (this) {
            zzx.zzz(str);
            if (i >= 0) {
                i2 = i;
            }
            String str2 = zzSW + zzSX.charAt(i2 >= zzSX.length() ? zzSX.length() - 1 : i2) + (zzjn().zzks() ? zzjn().zzkr() ? 'P' : 'C' : zzjn().zzkr() ? 'p' : 'c') + zze.VERSION + ":" + zzc(str, zzl(obj), zzl(obj2), zzl(obj3));
            if (str2.length() > 1024) {
                str2 = str2.substring(0, 1024);
            }
            zzai zzjA = zzji().zzjA();
            if (zzjA != null) {
                zzjA.zzlK().zzbq(str2);
            }
        }
    }

    public void zzh(Map<String, String> map, String str) {
        String str2;
        if (str == null) {
            str = "no reason provided";
        }
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry next : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append((String) next.getKey());
                sb.append('=');
                sb.append((String) next.getValue());
            }
            str2 = sb.toString();
        } else {
            str2 = "no hit data";
        }
        zzd("Discarding hit. " + str, str2);
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        synchronized (zzaf.class) {
            zzSY = this;
        }
    }

    /* access modifiers changed from: protected */
    public String zzl(Object obj) {
        if (obj == null) {
            return null;
        }
        Object l = obj instanceof Integer ? new Long((long) ((Integer) obj).intValue()) : obj;
        if (!(l instanceof Long)) {
            return l instanceof Boolean ? String.valueOf(l) : l instanceof Throwable ? l.getClass().getCanonicalName() : Condition.Operation.MINUS;
        }
        if (Math.abs(((Long) l).longValue()) < 100) {
            return String.valueOf(l);
        }
        String str = String.valueOf(l).charAt(0) == '-' ? Condition.Operation.MINUS : "";
        String valueOf = String.valueOf(Math.abs(((Long) l).longValue()));
        return str + Math.round(Math.pow(10.0d, (double) (valueOf.length() - 1))) + "..." + str + Math.round(Math.pow(10.0d, (double) valueOf.length()) - 1.0d);
    }
}
