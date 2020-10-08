package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzaj {
    private static void zza(DataLayer dataLayer, zzaf.zzd zzd) {
        for (zzag.zza zzg : zzd.zziD) {
            dataLayer.zzfX(zzdf.zzg(zzg));
        }
    }

    public static void zza(DataLayer dataLayer, zzaf.zzi zzi) {
        if (zzi.zzjs == null) {
            zzbg.zzaK("supplemental missing experimentSupplemental");
            return;
        }
        zza(dataLayer, zzi.zzjs);
        zzb(dataLayer, zzi.zzjs);
        zzc(dataLayer, zzi.zzjs);
    }

    private static void zzb(DataLayer dataLayer, zzaf.zzd zzd) {
        for (zzag.zza zzc : zzd.zziC) {
            Map<String, Object> zzc2 = zzc(zzc);
            if (zzc2 != null) {
                dataLayer.push(zzc2);
            }
        }
    }

    private static Map<String, Object> zzc(zzag.zza zza) {
        Object zzl = zzdf.zzl(zza);
        if (zzl instanceof Map) {
            return (Map) zzl;
        }
        zzbg.zzaK("value: " + zzl + " is not a map value, ignored.");
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0039, code lost:
        if (r0.longValue() > r10) goto L_0x003b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zzc(com.google.android.gms.tagmanager.DataLayer r14, com.google.android.gms.internal.zzaf.zzd r15) {
        /*
            r3 = 0
            com.google.android.gms.internal.zzaf$zzc[] r4 = r15.zziE
            int r5 = r4.length
            r2 = r3
        L_0x0005:
            if (r2 >= r5) goto L_0x00b9
            r6 = r4[r2]
            java.lang.String r0 = r6.key
            if (r0 != 0) goto L_0x0016
            java.lang.String r0 = "GaExperimentRandom: No key"
            com.google.android.gms.tagmanager.zzbg.zzaK(r0)
        L_0x0012:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0005
        L_0x0016:
            java.lang.String r0 = r6.key
            java.lang.Object r1 = r14.get(r0)
            boolean r0 = r1 instanceof java.lang.Number
            if (r0 != 0) goto L_0x0088
            r0 = 0
        L_0x0021:
            long r8 = r6.zziy
            long r10 = r6.zziz
            boolean r7 = r6.zziA
            if (r7 == 0) goto L_0x003b
            if (r0 == 0) goto L_0x003b
            long r12 = r0.longValue()
            int r7 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r7 < 0) goto L_0x003b
            long r12 = r0.longValue()
            int r0 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r0 <= 0) goto L_0x0050
        L_0x003b:
            int r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r0 > 0) goto L_0x0094
            double r0 = java.lang.Math.random()
            long r10 = r10 - r8
            double r10 = (double) r10
            double r0 = r0 * r10
            double r8 = (double) r8
            double r0 = r0 + r8
            long r0 = java.lang.Math.round(r0)
            java.lang.Long r1 = java.lang.Long.valueOf(r0)
        L_0x0050:
            java.lang.String r0 = r6.key
            r14.zzfX(r0)
            java.lang.String r0 = r6.key
            java.util.Map r1 = r14.zzn(r0, r1)
            long r8 = r6.zziB
            r10 = 0
            int r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r0 <= 0) goto L_0x0084
            java.lang.String r0 = "gtm"
            boolean r0 = r1.containsKey(r0)
            if (r0 != 0) goto L_0x009b
            java.lang.String r0 = "gtm"
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.String r8 = "lifetime"
            r7[r3] = r8
            r8 = 1
            long r10 = r6.zziB
            java.lang.Long r6 = java.lang.Long.valueOf(r10)
            r7[r8] = r6
            java.util.Map r6 = com.google.android.gms.tagmanager.DataLayer.mapOf(r7)
            r1.put(r0, r6)
        L_0x0084:
            r14.push(r1)
            goto L_0x0012
        L_0x0088:
            r0 = r1
            java.lang.Number r0 = (java.lang.Number) r0
            long r8 = r0.longValue()
            java.lang.Long r0 = java.lang.Long.valueOf(r8)
            goto L_0x0021
        L_0x0094:
            java.lang.String r0 = "GaExperimentRandom: random range invalid"
            com.google.android.gms.tagmanager.zzbg.zzaK(r0)
            goto L_0x0012
        L_0x009b:
            java.lang.String r0 = "gtm"
            java.lang.Object r0 = r1.get(r0)
            boolean r7 = r0 instanceof java.util.Map
            if (r7 == 0) goto L_0x00b3
            java.util.Map r0 = (java.util.Map) r0
            java.lang.String r7 = "lifetime"
            long r8 = r6.zziB
            java.lang.Long r6 = java.lang.Long.valueOf(r8)
            r0.put(r7, r6)
            goto L_0x0084
        L_0x00b3:
            java.lang.String r0 = "GaExperimentRandom: gtm not a map"
            com.google.android.gms.tagmanager.zzbg.zzaK(r0)
            goto L_0x0084
        L_0x00b9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzaj.zzc(com.google.android.gms.tagmanager.DataLayer, com.google.android.gms.internal.zzaf$zzd):void");
    }
}
