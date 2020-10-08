package com.google.android.gms.analytics.internal;

import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zzab {
    private final List<Command> zzSJ;
    private final long zzSK;
    private final long zzSL;
    private final int zzSM;
    private final boolean zzSN;
    private final String zzSO;
    private final Map<String, String> zzxA;

    public zzab(zzc zzc, Map<String, String> map, long j, boolean z) {
        this(zzc, map, j, z, 0, 0, (List<Command>) null);
    }

    public zzab(zzc zzc, Map<String, String> map, long j, boolean z, long j2, int i) {
        this(zzc, map, j, z, j2, i, (List<Command>) null);
    }

    public zzab(zzc zzc, Map<String, String> map, long j, boolean z, long j2, int i, List<Command> list) {
        String zza;
        String zza2;
        zzx.zzz(zzc);
        zzx.zzz(map);
        this.zzSL = j;
        this.zzSN = z;
        this.zzSK = j2;
        this.zzSM = i;
        this.zzSJ = list != null ? list : Collections.EMPTY_LIST;
        this.zzSO = zzp(list);
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            if (zzk(next.getKey()) && (zza2 = zza(zzc, next.getKey())) != null) {
                hashMap.put(zza2, zzb(zzc, next.getValue()));
            }
        }
        for (Map.Entry next2 : map.entrySet()) {
            if (!zzk(next2.getKey()) && (zza = zza(zzc, next2.getKey())) != null) {
                hashMap.put(zza, zzb(zzc, next2.getValue()));
            }
        }
        if (!TextUtils.isEmpty(this.zzSO)) {
            zzam.zzc(hashMap, "_v", this.zzSO);
            if (this.zzSO.equals("ma4.0.0") || this.zzSO.equals("ma4.0.1")) {
                hashMap.remove("adid");
            }
        }
        this.zzxA = Collections.unmodifiableMap(hashMap);
    }

    public static zzab zza(zzc zzc, zzab zzab, Map<String, String> map) {
        return new zzab(zzc, map, zzab.zzlr(), zzab.zzlt(), zzab.zzlq(), zzab.zzlp(), zzab.zzls());
    }

    private static String zza(zzc zzc, Object obj) {
        if (obj == null) {
            return null;
        }
        String obj2 = obj.toString();
        if (obj2.startsWith("&")) {
            obj2 = obj2.substring(1);
        }
        int length = obj2.length();
        if (length > 256) {
            obj2 = obj2.substring(0, 256);
            zzc.zzc("Hit param name is too long and will be trimmed", Integer.valueOf(length), obj2);
        }
        if (TextUtils.isEmpty(obj2)) {
            return null;
        }
        return obj2;
    }

    private static String zzb(zzc zzc, Object obj) {
        String obj2 = obj == null ? "" : obj.toString();
        int length = obj2.length();
        if (length <= 8192) {
            return obj2;
        }
        String substring = obj2.substring(0, 8192);
        zzc.zzc("Hit param value is too long and will be trimmed", Integer.valueOf(length), substring);
        return substring;
    }

    private static boolean zzk(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.toString().startsWith("&");
    }

    private String zzm(String str, String str2) {
        zzx.zzcM(str);
        zzx.zzb(!str.startsWith("&"), (Object) "Short param name required");
        String str3 = this.zzxA.get(str);
        return str3 != null ? str3 : str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:14:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String zzp(java.util.List<com.google.android.gms.analytics.internal.Command> r5) {
        /*
            r1 = 0
            if (r5 == 0) goto L_0x002c
            java.util.Iterator r2 = r5.iterator()
        L_0x0007:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x002c
            java.lang.Object r0 = r2.next()
            com.google.android.gms.analytics.internal.Command r0 = (com.google.android.gms.analytics.internal.Command) r0
            java.lang.String r3 = "appendVersion"
            java.lang.String r4 = r0.getId()
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x0007
            java.lang.String r0 = r0.getValue()
        L_0x0023:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x002a
        L_0x0029:
            return r1
        L_0x002a:
            r1 = r0
            goto L_0x0029
        L_0x002c:
            r0 = r1
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzab.zzp(java.util.List):java.lang.String");
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ht=").append(this.zzSL);
        if (this.zzSK != 0) {
            stringBuffer.append(", dbId=").append(this.zzSK);
        }
        if (((long) this.zzSM) != 0) {
            stringBuffer.append(", appUID=").append(this.zzSM);
        }
        ArrayList<String> arrayList = new ArrayList<>(this.zzxA.keySet());
        Collections.sort(arrayList);
        for (String str : arrayList) {
            stringBuffer.append(", ");
            stringBuffer.append(str);
            stringBuffer.append(Condition.Operation.EQUALS);
            stringBuffer.append(this.zzxA.get(str));
        }
        return stringBuffer.toString();
    }

    public int zzlp() {
        return this.zzSM;
    }

    public long zzlq() {
        return this.zzSK;
    }

    public long zzlr() {
        return this.zzSL;
    }

    public List<Command> zzls() {
        return this.zzSJ;
    }

    public boolean zzlt() {
        return this.zzSN;
    }

    public long zzlu() {
        return zzam.zzbt(zzm("_s", "0"));
    }

    public String zzlv() {
        return zzm("_m", "");
    }

    public Map<String, String> zzn() {
        return this.zzxA;
    }
}
