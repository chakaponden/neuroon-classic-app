package com.google.android.gms.internal;

import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzag;
import com.google.android.gms.tagmanager.zzbg;
import com.google.android.gms.tagmanager.zzdf;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class zzrs {

    public static class zza {
        private final zzag.zza zzbkI;
        private final Map<String, zzag.zza> zzbmi;

        private zza(Map<String, zzag.zza> map, zzag.zza zza) {
            this.zzbmi = map;
            this.zzbkI = zza;
        }

        public static zzb zzHH() {
            return new zzb();
        }

        public String toString() {
            return "Properties: " + zzHI() + " pushAfterEvaluate: " + this.zzbkI;
        }

        public Map<String, zzag.zza> zzHI() {
            return Collections.unmodifiableMap(this.zzbmi);
        }

        public zzag.zza zzHh() {
            return this.zzbkI;
        }

        public void zza(String str, zzag.zza zza) {
            this.zzbmi.put(str, zza);
        }
    }

    public static class zzb {
        private zzag.zza zzbkI;
        private final Map<String, zzag.zza> zzbmi;

        private zzb() {
            this.zzbmi = new HashMap();
        }

        public zza zzHJ() {
            return new zza(this.zzbmi, this.zzbkI);
        }

        public zzb zzb(String str, zzag.zza zza) {
            this.zzbmi.put(str, zza);
            return this;
        }

        public zzb zzq(zzag.zza zza) {
            this.zzbkI = zza;
            return this;
        }
    }

    public static class zzc {
        private final String zzadc;
        private final List<zze> zzbmj;
        private final Map<String, List<zza>> zzbmk;
        private final int zzbml;

        private zzc(List<zze> list, Map<String, List<zza>> map, String str, int i) {
            this.zzbmj = Collections.unmodifiableList(list);
            this.zzbmk = Collections.unmodifiableMap(map);
            this.zzadc = str;
            this.zzbml = i;
        }

        public static zzd zzHK() {
            return new zzd();
        }

        public String getVersion() {
            return this.zzadc;
        }

        public String toString() {
            return "Rules: " + zzHL() + "  Macros: " + this.zzbmk;
        }

        public List<zze> zzHL() {
            return this.zzbmj;
        }

        public Map<String, List<zza>> zzHM() {
            return this.zzbmk;
        }
    }

    public static class zzd {
        private String zzadc;
        private final List<zze> zzbmj;
        private final Map<String, List<zza>> zzbmk;
        private int zzbml;

        private zzd() {
            this.zzbmj = new ArrayList();
            this.zzbmk = new HashMap();
            this.zzadc = "";
            this.zzbml = 0;
        }

        public zzc zzHN() {
            return new zzc(this.zzbmj, this.zzbmk, this.zzadc, this.zzbml);
        }

        public zzd zzb(zze zze) {
            this.zzbmj.add(zze);
            return this;
        }

        public zzd zzc(zza zza) {
            String zzg = zzdf.zzg(zza.zzHI().get(zzae.INSTANCE_NAME.toString()));
            List list = this.zzbmk.get(zzg);
            if (list == null) {
                list = new ArrayList();
                this.zzbmk.put(zzg, list);
            }
            list.add(zza);
            return this;
        }

        public zzd zzgD(String str) {
            this.zzadc = str;
            return this;
        }

        public zzd zzko(int i) {
            this.zzbml = i;
            return this;
        }
    }

    public static class zze {
        private final List<zza> zzbmm;
        private final List<zza> zzbmn;
        private final List<zza> zzbmo;
        private final List<zza> zzbmp;
        private final List<zza> zzbmq;
        private final List<zza> zzbmr;
        private final List<String> zzbms;
        private final List<String> zzbmt;
        private final List<String> zzbmu;
        private final List<String> zzbmv;

        private zze(List<zza> list, List<zza> list2, List<zza> list3, List<zza> list4, List<zza> list5, List<zza> list6, List<String> list7, List<String> list8, List<String> list9, List<String> list10) {
            this.zzbmm = Collections.unmodifiableList(list);
            this.zzbmn = Collections.unmodifiableList(list2);
            this.zzbmo = Collections.unmodifiableList(list3);
            this.zzbmp = Collections.unmodifiableList(list4);
            this.zzbmq = Collections.unmodifiableList(list5);
            this.zzbmr = Collections.unmodifiableList(list6);
            this.zzbms = Collections.unmodifiableList(list7);
            this.zzbmt = Collections.unmodifiableList(list8);
            this.zzbmu = Collections.unmodifiableList(list9);
            this.zzbmv = Collections.unmodifiableList(list10);
        }

        public static zzf zzHO() {
            return new zzf();
        }

        public String toString() {
            return "Positive predicates: " + zzHP() + "  Negative predicates: " + zzHQ() + "  Add tags: " + zzHR() + "  Remove tags: " + zzHS() + "  Add macros: " + zzHT() + "  Remove macros: " + zzHY();
        }

        public List<zza> zzHP() {
            return this.zzbmm;
        }

        public List<zza> zzHQ() {
            return this.zzbmn;
        }

        public List<zza> zzHR() {
            return this.zzbmo;
        }

        public List<zza> zzHS() {
            return this.zzbmp;
        }

        public List<zza> zzHT() {
            return this.zzbmq;
        }

        public List<String> zzHU() {
            return this.zzbms;
        }

        public List<String> zzHV() {
            return this.zzbmt;
        }

        public List<String> zzHW() {
            return this.zzbmu;
        }

        public List<String> zzHX() {
            return this.zzbmv;
        }

        public List<zza> zzHY() {
            return this.zzbmr;
        }
    }

    public static class zzf {
        private final List<zza> zzbmm;
        private final List<zza> zzbmn;
        private final List<zza> zzbmo;
        private final List<zza> zzbmp;
        private final List<zza> zzbmq;
        private final List<zza> zzbmr;
        private final List<String> zzbms;
        private final List<String> zzbmt;
        private final List<String> zzbmu;
        private final List<String> zzbmv;

        private zzf() {
            this.zzbmm = new ArrayList();
            this.zzbmn = new ArrayList();
            this.zzbmo = new ArrayList();
            this.zzbmp = new ArrayList();
            this.zzbmq = new ArrayList();
            this.zzbmr = new ArrayList();
            this.zzbms = new ArrayList();
            this.zzbmt = new ArrayList();
            this.zzbmu = new ArrayList();
            this.zzbmv = new ArrayList();
        }

        public zze zzHZ() {
            return new zze(this.zzbmm, this.zzbmn, this.zzbmo, this.zzbmp, this.zzbmq, this.zzbmr, this.zzbms, this.zzbmt, this.zzbmu, this.zzbmv);
        }

        public zzf zzd(zza zza) {
            this.zzbmm.add(zza);
            return this;
        }

        public zzf zze(zza zza) {
            this.zzbmn.add(zza);
            return this;
        }

        public zzf zzf(zza zza) {
            this.zzbmo.add(zza);
            return this;
        }

        public zzf zzg(zza zza) {
            this.zzbmp.add(zza);
            return this;
        }

        public zzf zzgE(String str) {
            this.zzbmu.add(str);
            return this;
        }

        public zzf zzgF(String str) {
            this.zzbmv.add(str);
            return this;
        }

        public zzf zzgG(String str) {
            this.zzbms.add(str);
            return this;
        }

        public zzf zzgH(String str) {
            this.zzbmt.add(str);
            return this;
        }

        public zzf zzh(zza zza) {
            this.zzbmq.add(zza);
            return this;
        }

        public zzf zzi(zza zza) {
            this.zzbmr.add(zza);
            return this;
        }
    }

    public static class zzg extends Exception {
        public zzg(String str) {
            super(str);
        }
    }

    private static zzag.zza zza(int i, zzaf.zzf zzf2, zzag.zza[] zzaArr, Set<Integer> set) throws zzg {
        int i2 = 0;
        if (set.contains(Integer.valueOf(i))) {
            zzgC("Value cycle detected.  Current value reference: " + i + "." + "  Previous value references: " + set + ".");
        }
        zzag.zza zza2 = (zzag.zza) zza(zzf2.zziI, i, "values");
        if (zzaArr[i] != null) {
            return zzaArr[i];
        }
        zzag.zza zza3 = null;
        set.add(Integer.valueOf(i));
        switch (zza2.type) {
            case 1:
            case 5:
            case 6:
            case 8:
                zza3 = zza2;
                break;
            case 2:
                zzaf.zzh zzp = zzp(zza2);
                zza3 = zzo(zza2);
                zza3.zzjy = new zzag.zza[zzp.zzjj.length];
                int[] iArr = zzp.zzjj;
                int length = iArr.length;
                int i3 = 0;
                while (i2 < length) {
                    zza3.zzjy[i3] = zza(iArr[i2], zzf2, zzaArr, set);
                    i2++;
                    i3++;
                }
                break;
            case 3:
                zza3 = zzo(zza2);
                zzaf.zzh zzp2 = zzp(zza2);
                if (zzp2.zzjk.length != zzp2.zzjl.length) {
                    zzgC("Uneven map keys (" + zzp2.zzjk.length + ") and map values (" + zzp2.zzjl.length + ")");
                }
                zza3.zzjz = new zzag.zza[zzp2.zzjk.length];
                zza3.zzjA = new zzag.zza[zzp2.zzjk.length];
                int[] iArr2 = zzp2.zzjk;
                int length2 = iArr2.length;
                int i4 = 0;
                int i5 = 0;
                while (i4 < length2) {
                    zza3.zzjz[i5] = zza(iArr2[i4], zzf2, zzaArr, set);
                    i4++;
                    i5++;
                }
                int[] iArr3 = zzp2.zzjl;
                int length3 = iArr3.length;
                int i6 = 0;
                while (i2 < length3) {
                    zza3.zzjA[i6] = zza(iArr3[i2], zzf2, zzaArr, set);
                    i2++;
                    i6++;
                }
                break;
            case 4:
                zza3 = zzo(zza2);
                zza3.zzjB = zzdf.zzg(zza(zzp(zza2).zzjo, zzf2, zzaArr, set));
                break;
            case 7:
                zza3 = zzo(zza2);
                zzaf.zzh zzp3 = zzp(zza2);
                zza3.zzjF = new zzag.zza[zzp3.zzjn.length];
                int[] iArr4 = zzp3.zzjn;
                int length4 = iArr4.length;
                int i7 = 0;
                while (i2 < length4) {
                    zza3.zzjF[i7] = zza(iArr4[i2], zzf2, zzaArr, set);
                    i2++;
                    i7++;
                }
                break;
        }
        if (zza3 == null) {
            zzgC("Invalid value: " + zza2);
        }
        zzaArr[i] = zza3;
        set.remove(Integer.valueOf(i));
        return zza3;
    }

    private static zza zza(zzaf.zzb zzb2, zzaf.zzf zzf2, zzag.zza[] zzaArr, int i) throws zzg {
        zzb zzHH = zza.zzHH();
        for (int valueOf : zzb2.zzit) {
            zzaf.zze zze2 = (zzaf.zze) zza(zzf2.zziJ, Integer.valueOf(valueOf).intValue(), "properties");
            String str = (String) zza(zzf2.zziH, zze2.key, "keys");
            zzag.zza zza2 = (zzag.zza) zza(zzaArr, zze2.value, "values");
            if (zzae.PUSH_AFTER_EVALUATE.toString().equals(str)) {
                zzHH.zzq(zza2);
            } else {
                zzHH.zzb(str, zza2);
            }
        }
        return zzHH.zzHJ();
    }

    private static zze zza(zzaf.zzg zzg2, List<zza> list, List<zza> list2, List<zza> list3, zzaf.zzf zzf2) {
        zzf zzHO = zze.zzHO();
        for (int valueOf : zzg2.zziX) {
            zzHO.zzd(list3.get(Integer.valueOf(valueOf).intValue()));
        }
        for (int valueOf2 : zzg2.zziY) {
            zzHO.zze(list3.get(Integer.valueOf(valueOf2).intValue()));
        }
        for (int valueOf3 : zzg2.zziZ) {
            zzHO.zzf(list.get(Integer.valueOf(valueOf3).intValue()));
        }
        for (int valueOf4 : zzg2.zzjb) {
            zzHO.zzgE(zzf2.zziI[Integer.valueOf(valueOf4).intValue()].zzjx);
        }
        for (int valueOf5 : zzg2.zzja) {
            zzHO.zzg(list.get(Integer.valueOf(valueOf5).intValue()));
        }
        for (int valueOf6 : zzg2.zzjc) {
            zzHO.zzgF(zzf2.zziI[Integer.valueOf(valueOf6).intValue()].zzjx);
        }
        for (int valueOf7 : zzg2.zzjd) {
            zzHO.zzh(list2.get(Integer.valueOf(valueOf7).intValue()));
        }
        for (int valueOf8 : zzg2.zzjf) {
            zzHO.zzgG(zzf2.zziI[Integer.valueOf(valueOf8).intValue()].zzjx);
        }
        for (int valueOf9 : zzg2.zzje) {
            zzHO.zzi(list2.get(Integer.valueOf(valueOf9).intValue()));
        }
        for (int valueOf10 : zzg2.zzjg) {
            zzHO.zzgH(zzf2.zziI[Integer.valueOf(valueOf10).intValue()].zzjx);
        }
        return zzHO.zzHZ();
    }

    private static <T> T zza(T[] tArr, int i, String str) throws zzg {
        if (i < 0 || i >= tArr.length) {
            zzgC("Index out of bounds detected: " + i + " in " + str);
        }
        return tArr[i];
    }

    public static zzc zzb(zzaf.zzf zzf2) throws zzg {
        zzag.zza[] zzaArr = new zzag.zza[zzf2.zziI.length];
        for (int i = 0; i < zzf2.zziI.length; i++) {
            zza(i, zzf2, zzaArr, (Set<Integer>) new HashSet(0));
        }
        zzd zzHK = zzc.zzHK();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < zzf2.zziL.length; i2++) {
            arrayList.add(zza(zzf2.zziL[i2], zzf2, zzaArr, i2));
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < zzf2.zziM.length; i3++) {
            arrayList2.add(zza(zzf2.zziM[i3], zzf2, zzaArr, i3));
        }
        ArrayList arrayList3 = new ArrayList();
        for (int i4 = 0; i4 < zzf2.zziK.length; i4++) {
            zza zza2 = zza(zzf2.zziK[i4], zzf2, zzaArr, i4);
            zzHK.zzc(zza2);
            arrayList3.add(zza2);
        }
        for (zzaf.zzg zza3 : zzf2.zziN) {
            zzHK.zzb(zza(zza3, arrayList, arrayList3, arrayList2, zzf2));
        }
        zzHK.zzgD(zzf2.version);
        zzHK.zzko(zzf2.zziV);
        return zzHK.zzHN();
    }

    public static void zzb(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static void zzgC(String str) throws zzg {
        zzbg.e(str);
        throw new zzg(str);
    }

    public static zzag.zza zzo(zzag.zza zza2) {
        zzag.zza zza3 = new zzag.zza();
        zza3.type = zza2.type;
        zza3.zzjG = (int[]) zza2.zzjG.clone();
        if (zza2.zzjH) {
            zza3.zzjH = zza2.zzjH;
        }
        return zza3;
    }

    private static zzaf.zzh zzp(zzag.zza zza2) throws zzg {
        if (((zzaf.zzh) zza2.zza(zzaf.zzh.zzjh)) == null) {
            zzgC("Expected a ServingValue and didn't get one. Value is: " + zza2);
        }
        return (zzaf.zzh) zza2.zza(zzaf.zzh.zzjh);
    }
}
