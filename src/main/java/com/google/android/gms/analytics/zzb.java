package com.google.android.gms.analytics;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzc;
import com.google.android.gms.analytics.internal.zze;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzkb;
import com.google.android.gms.internal.zzkc;
import com.google.android.gms.internal.zzkd;
import com.google.android.gms.internal.zzke;
import com.google.android.gms.internal.zzpq;
import com.google.android.gms.internal.zzpr;
import com.google.android.gms.internal.zzps;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.internal.zzpu;
import com.google.android.gms.internal.zzpv;
import com.google.android.gms.internal.zzpw;
import com.google.android.gms.internal.zzpx;
import com.google.android.gms.internal.zzpy;
import com.google.android.gms.measurement.zzi;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zzb extends zzc implements zzi {
    private static DecimalFormat zzOU;
    private final zzf zzOK;
    private final String zzOV;
    private final Uri zzOW;
    private final boolean zzOX;
    private final boolean zzOY;

    public zzb(zzf zzf, String str) {
        this(zzf, str, true, false);
    }

    public zzb(zzf zzf, String str, boolean z, boolean z2) {
        super(zzf);
        zzx.zzcM(str);
        this.zzOK = zzf;
        this.zzOV = str;
        this.zzOX = z;
        this.zzOY = z2;
        this.zzOW = zzaU(this.zzOV);
    }

    private static String zzH(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : map.entrySet()) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append((String) next.getKey());
            sb.append(Condition.Operation.EQUALS);
            sb.append((String) next.getValue());
        }
        return sb.toString();
    }

    private static void zza(Map<String, String> map, String str, double d) {
        if (d != 0.0d) {
            map.put(str, zzb(d));
        }
    }

    private static void zza(Map<String, String> map, String str, int i, int i2) {
        if (i > 0 && i2 > 0) {
            map.put(str, i + "x" + i2);
        }
    }

    private static void zza(Map<String, String> map, String str, boolean z) {
        if (z) {
            map.put(str, "1");
        }
    }

    static Uri zzaU(String str) {
        zzx.zzcM(str);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("uri");
        builder.authority("google-analytics.com");
        builder.path(str);
        return builder.build();
    }

    static String zzb(double d) {
        if (zzOU == null) {
            zzOU = new DecimalFormat("0.######");
        }
        return zzOU.format(d);
    }

    private static void zzb(Map<String, String> map, String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            map.put(str, str2);
        }
    }

    public static Map<String, String> zzc(com.google.android.gms.measurement.zzc zzc) {
        HashMap hashMap = new HashMap();
        zzkd zzkd = (zzkd) zzc.zze(zzkd.class);
        if (zzkd != null) {
            for (Map.Entry next : zzkd.zziR().entrySet()) {
                String zzi = zzi(next.getValue());
                if (zzi != null) {
                    hashMap.put(next.getKey(), zzi);
                }
            }
        }
        zzke zzke = (zzke) zzc.zze(zzke.class);
        if (zzke != null) {
            zzb(hashMap, "t", zzke.zziS());
            zzb(hashMap, "cid", zzke.getClientId());
            zzb(hashMap, "uid", zzke.getUserId());
            zzb(hashMap, "sc", zzke.zziV());
            zza((Map<String, String>) hashMap, "sf", zzke.zziX());
            zza((Map<String, String>) hashMap, "ni", zzke.zziW());
            zzb(hashMap, "adid", zzke.zziT());
            zza((Map<String, String>) hashMap, "ate", zzke.zziU());
        }
        zzpw zzpw = (zzpw) zzc.zze(zzpw.class);
        if (zzpw != null) {
            zzb(hashMap, "cd", zzpw.zzBc());
            zza((Map<String, String>) hashMap, "a", (double) zzpw.zzBd());
            zzb(hashMap, "dr", zzpw.zzBe());
        }
        zzpu zzpu = (zzpu) zzc.zze(zzpu.class);
        if (zzpu != null) {
            zzb(hashMap, "ec", zzpu.zzAZ());
            zzb(hashMap, "ea", zzpu.getAction());
            zzb(hashMap, "el", zzpu.getLabel());
            zza((Map<String, String>) hashMap, "ev", (double) zzpu.getValue());
        }
        zzpr zzpr = (zzpr) zzc.zze(zzpr.class);
        if (zzpr != null) {
            zzb(hashMap, "cn", zzpr.getName());
            zzb(hashMap, "cs", zzpr.getSource());
            zzb(hashMap, "cm", zzpr.zzAK());
            zzb(hashMap, "ck", zzpr.zzAL());
            zzb(hashMap, "cc", zzpr.getContent());
            zzb(hashMap, "ci", zzpr.getId());
            zzb(hashMap, "anid", zzpr.zzAM());
            zzb(hashMap, "gclid", zzpr.zzAN());
            zzb(hashMap, "dclid", zzpr.zzAO());
            zzb(hashMap, "aclid", zzpr.zzAP());
        }
        zzpv zzpv = (zzpv) zzc.zze(zzpv.class);
        if (zzpv != null) {
            zzb(hashMap, "exd", zzpv.getDescription());
            zza((Map<String, String>) hashMap, "exf", zzpv.zzBa());
        }
        zzpx zzpx = (zzpx) zzc.zze(zzpx.class);
        if (zzpx != null) {
            zzb(hashMap, "sn", zzpx.zzBg());
            zzb(hashMap, "sa", zzpx.getAction());
            zzb(hashMap, "st", zzpx.getTarget());
        }
        zzpy zzpy = (zzpy) zzc.zze(zzpy.class);
        if (zzpy != null) {
            zzb(hashMap, "utv", zzpy.zzBh());
            zza((Map<String, String>) hashMap, "utt", (double) zzpy.getTimeInMillis());
            zzb(hashMap, "utc", zzpy.zzAZ());
            zzb(hashMap, "utl", zzpy.getLabel());
        }
        zzkb zzkb = (zzkb) zzc.zze(zzkb.class);
        if (zzkb != null) {
            for (Map.Entry next2 : zzkb.zziP().entrySet()) {
                String zzU = zzc.zzU(((Integer) next2.getKey()).intValue());
                if (!TextUtils.isEmpty(zzU)) {
                    hashMap.put(zzU, next2.getValue());
                }
            }
        }
        zzkc zzkc = (zzkc) zzc.zze(zzkc.class);
        if (zzkc != null) {
            for (Map.Entry next3 : zzkc.zziQ().entrySet()) {
                String zzW = zzc.zzW(((Integer) next3.getKey()).intValue());
                if (!TextUtils.isEmpty(zzW)) {
                    hashMap.put(zzW, zzb(((Double) next3.getValue()).doubleValue()));
                }
            }
        }
        zzpt zzpt = (zzpt) zzc.zze(zzpt.class);
        if (zzpt != null) {
            ProductAction zzAV = zzpt.zzAV();
            if (zzAV != null) {
                for (Map.Entry next4 : zzAV.build().entrySet()) {
                    if (((String) next4.getKey()).startsWith("&")) {
                        hashMap.put(((String) next4.getKey()).substring(1), next4.getValue());
                    } else {
                        hashMap.put(next4.getKey(), next4.getValue());
                    }
                }
            }
            int i = 1;
            for (Promotion zzba : zzpt.zzAY()) {
                hashMap.putAll(zzba.zzba(zzc.zzaa(i)));
                i++;
            }
            int i2 = 1;
            for (Product zzba2 : zzpt.zzAW()) {
                hashMap.putAll(zzba2.zzba(zzc.zzY(i2)));
                i2++;
            }
            int i3 = 1;
            for (Map.Entry next5 : zzpt.zzAX().entrySet()) {
                String zzad = zzc.zzad(i3);
                int i4 = 1;
                for (Product zzba3 : (List) next5.getValue()) {
                    hashMap.putAll(zzba3.zzba(zzad + zzc.zzab(i4)));
                    i4++;
                }
                if (!TextUtils.isEmpty((CharSequence) next5.getKey())) {
                    hashMap.put(zzad + "nm", next5.getKey());
                }
                i3++;
            }
        }
        zzps zzps = (zzps) zzc.zze(zzps.class);
        if (zzps != null) {
            zzb(hashMap, "ul", zzps.getLanguage());
            zza((Map<String, String>) hashMap, "sd", (double) zzps.zzAQ());
            zza(hashMap, "sr", zzps.zzAR(), zzps.zzAS());
            zza(hashMap, "vp", zzps.zzAT(), zzps.zzAU());
        }
        zzpq zzpq = (zzpq) zzc.zze(zzpq.class);
        if (zzpq != null) {
            zzb(hashMap, "an", zzpq.zzlg());
            zzb(hashMap, "aid", zzpq.zzwK());
            zzb(hashMap, "aiid", zzpq.zzAJ());
            zzb(hashMap, "av", zzpq.zzli());
        }
        return hashMap;
    }

    private static String zzi(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return str;
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            if (d.doubleValue() != 0.0d) {
                return zzb(d.doubleValue());
            }
            return null;
        } else if (!(obj instanceof Boolean)) {
            return String.valueOf(obj);
        } else {
            if (obj != Boolean.FALSE) {
                return "1";
            }
            return null;
        }
    }

    public void zzb(com.google.android.gms.measurement.zzc zzc) {
        zzx.zzz(zzc);
        zzx.zzb(zzc.zzAz(), (Object) "Can't deliver not submitted measurement");
        zzx.zzcE("deliver should be called on worker thread");
        com.google.android.gms.measurement.zzc zzAu = zzc.zzAu();
        zzke zzke = (zzke) zzAu.zzf(zzke.class);
        if (TextUtils.isEmpty(zzke.zziS())) {
            zzjm().zzh(zzc(zzAu), "Ignoring measurement without type");
        } else if (TextUtils.isEmpty(zzke.getClientId())) {
            zzjm().zzh(zzc(zzAu), "Ignoring measurement without client id");
        } else if (!this.zzOK.zzjz().getAppOptOut()) {
            double zziX = zzke.zziX();
            if (zzam.zza(zziX, zzke.getClientId())) {
                zzb("Sampling enabled. Hit sampled out. sampling rate", Double.valueOf(zziX));
                return;
            }
            Map<String, String> zzc2 = zzc(zzAu);
            zzc2.put("v", "1");
            zzc2.put("_v", zze.zzQm);
            zzc2.put("tid", this.zzOV);
            if (this.zzOK.zzjz().isDryRunEnabled()) {
                zzc("Dry run is enabled. GoogleAnalytics would have sent", zzH(zzc2));
                return;
            }
            HashMap hashMap = new HashMap();
            zzam.zzc(hashMap, "uid", zzke.getUserId());
            zzpq zzpq = (zzpq) zzc.zze(zzpq.class);
            if (zzpq != null) {
                zzam.zzc(hashMap, "an", zzpq.zzlg());
                zzam.zzc(hashMap, "aid", zzpq.zzwK());
                zzam.zzc(hashMap, "av", zzpq.zzli());
                zzam.zzc(hashMap, "aiid", zzpq.zzAJ());
            }
            zzc2.put("_s", String.valueOf(zziH().zza(new zzh(0, zzke.getClientId(), this.zzOV, !TextUtils.isEmpty(zzke.zziT()), 0, hashMap))));
            zziH().zza(new zzab(zzjm(), zzc2, zzc.zzAx(), true));
        }
    }

    public Uri zziA() {
        return this.zzOW;
    }
}
