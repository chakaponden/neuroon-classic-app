package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzag;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.tagmanager.zzm;
import com.google.android.gms.tagmanager.zzt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class zzcp {
    private static final zzbw<zzag.zza> zzbkq = new zzbw<>(zzdf.zzHF(), true);
    private final DataLayer zzbhN;
    private volatile String zzbkA;
    private int zzbkB;
    private final zzrs.zzc zzbkr;
    private final zzah zzbks;
    private final Map<String, zzak> zzbkt;
    private final Map<String, zzak> zzbku;
    private final Map<String, zzak> zzbkv;
    private final zzl<zzrs.zza, zzbw<zzag.zza>> zzbkw;
    private final zzl<String, zzb> zzbkx;
    private final Set<zzrs.zze> zzbky;
    private final Map<String, zzc> zzbkz;

    interface zza {
        void zza(zzrs.zze zze, Set<zzrs.zza> set, Set<zzrs.zza> set2, zzck zzck);
    }

    private static class zzb {
        private zzbw<zzag.zza> zzbkH;
        private zzag.zza zzbkI;

        public zzb(zzbw<zzag.zza> zzbw, zzag.zza zza) {
            this.zzbkH = zzbw;
            this.zzbkI = zza;
        }

        public int getSize() {
            return (this.zzbkI == null ? 0 : this.zzbkI.getCachedSize()) + this.zzbkH.getObject().getCachedSize();
        }

        public zzbw<zzag.zza> zzHg() {
            return this.zzbkH;
        }

        public zzag.zza zzHh() {
            return this.zzbkI;
        }
    }

    private static class zzc {
        private final Map<zzrs.zze, List<zzrs.zza>> zzbkJ = new HashMap();
        private final Map<zzrs.zze, List<zzrs.zza>> zzbkK = new HashMap();
        private final Map<zzrs.zze, List<String>> zzbkL = new HashMap();
        private final Map<zzrs.zze, List<String>> zzbkM = new HashMap();
        private zzrs.zza zzbkN;
        private final Set<zzrs.zze> zzbky = new HashSet();

        public Set<zzrs.zze> zzHi() {
            return this.zzbky;
        }

        public Map<zzrs.zze, List<zzrs.zza>> zzHj() {
            return this.zzbkJ;
        }

        public Map<zzrs.zze, List<String>> zzHk() {
            return this.zzbkL;
        }

        public Map<zzrs.zze, List<String>> zzHl() {
            return this.zzbkM;
        }

        public Map<zzrs.zze, List<zzrs.zza>> zzHm() {
            return this.zzbkK;
        }

        public zzrs.zza zzHn() {
            return this.zzbkN;
        }

        public void zza(zzrs.zze zze) {
            this.zzbky.add(zze);
        }

        public void zza(zzrs.zze zze, zzrs.zza zza) {
            List list = this.zzbkJ.get(zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkJ.put(zze, list);
            }
            list.add(zza);
        }

        public void zza(zzrs.zze zze, String str) {
            List list = this.zzbkL.get(zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkL.put(zze, list);
            }
            list.add(str);
        }

        public void zzb(zzrs.zza zza) {
            this.zzbkN = zza;
        }

        public void zzb(zzrs.zze zze, zzrs.zza zza) {
            List list = this.zzbkK.get(zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkK.put(zze, list);
            }
            list.add(zza);
        }

        public void zzb(zzrs.zze zze, String str) {
            List list = this.zzbkM.get(zze);
            if (list == null) {
                list = new ArrayList();
                this.zzbkM.put(zze, list);
            }
            list.add(str);
        }
    }

    public zzcp(Context context, zzrs.zzc zzc2, DataLayer dataLayer, zzt.zza zza2, zzt.zza zza3, zzah zzah) {
        if (zzc2 == null) {
            throw new NullPointerException("resource cannot be null");
        }
        this.zzbkr = zzc2;
        this.zzbky = new HashSet(zzc2.zzHL());
        this.zzbhN = dataLayer;
        this.zzbks = zzah;
        this.zzbkw = new zzm().zza(1048576, new zzm.zza<zzrs.zza, zzbw<zzag.zza>>() {
            /* renamed from: zza */
            public int sizeOf(zzrs.zza zza, zzbw<zzag.zza> zzbw) {
                return zzbw.getObject().getCachedSize();
            }
        });
        this.zzbkx = new zzm().zza(1048576, new zzm.zza<String, zzb>() {
            /* renamed from: zza */
            public int sizeOf(String str, zzb zzb) {
                return str.length() + zzb.getSize();
            }
        });
        this.zzbkt = new HashMap();
        zzb(new zzj(context));
        zzb(new zzt(zza3));
        zzb(new zzx(dataLayer));
        zzb(new zzdg(context, dataLayer));
        zzb(new zzdb(context, dataLayer));
        this.zzbku = new HashMap();
        zzc(new zzr());
        zzc(new zzae());
        zzc(new zzaf());
        zzc(new zzam());
        zzc(new zzan());
        zzc(new zzbc());
        zzc(new zzbd());
        zzc(new zzcf());
        zzc(new zzcy());
        this.zzbkv = new HashMap();
        zza((zzak) new zzb(context));
        zza((zzak) new zzc(context));
        zza((zzak) new zze(context));
        zza((zzak) new zzf(context));
        zza((zzak) new zzg(context));
        zza((zzak) new zzh(context));
        zza((zzak) new zzi(context));
        zza((zzak) new zzn());
        zza((zzak) new zzq(this.zzbkr.getVersion()));
        zza((zzak) new zzt(zza2));
        zza((zzak) new zzv(dataLayer));
        zza((zzak) new zzaa(context));
        zza((zzak) new zzab());
        zza((zzak) new zzad());
        zza((zzak) new zzai(this));
        zza((zzak) new zzao());
        zza((zzak) new zzap());
        zza((zzak) new zzaw(context));
        zza((zzak) new zzay());
        zza((zzak) new zzbb());
        zza((zzak) new zzbi());
        zza((zzak) new zzbk(context));
        zza((zzak) new zzbx());
        zza((zzak) new zzbz());
        zza((zzak) new zzcc());
        zza((zzak) new zzce());
        zza((zzak) new zzcg(context));
        zza((zzak) new zzcq());
        zza((zzak) new zzcr());
        zza((zzak) new zzda());
        zza((zzak) new zzdh());
        this.zzbkz = new HashMap();
        for (zzrs.zze next : this.zzbky) {
            if (zzah.zzGA()) {
                zza(next.zzHT(), next.zzHU(), "add macro");
                zza(next.zzHY(), next.zzHV(), "remove macro");
                zza(next.zzHR(), next.zzHW(), "add tag");
                zza(next.zzHS(), next.zzHX(), "remove tag");
            }
            for (int i = 0; i < next.zzHT().size(); i++) {
                zzrs.zza zza4 = next.zzHT().get(i);
                String str = "Unknown";
                if (zzah.zzGA() && i < next.zzHU().size()) {
                    str = next.zzHU().get(i);
                }
                zzc zzi = zzi(this.zzbkz, zza(zza4));
                zzi.zza(next);
                zzi.zza(next, zza4);
                zzi.zza(next, str);
            }
            for (int i2 = 0; i2 < next.zzHY().size(); i2++) {
                zzrs.zza zza5 = next.zzHY().get(i2);
                String str2 = "Unknown";
                if (zzah.zzGA() && i2 < next.zzHV().size()) {
                    str2 = next.zzHV().get(i2);
                }
                zzc zzi2 = zzi(this.zzbkz, zza(zza5));
                zzi2.zza(next);
                zzi2.zzb(next, zza5);
                zzi2.zzb(next, str2);
            }
        }
        for (Map.Entry next2 : this.zzbkr.zzHM().entrySet()) {
            for (zzrs.zza zza6 : (List) next2.getValue()) {
                if (!zzdf.zzk(zza6.zzHI().get(zzae.NOT_DEFAULT_MACRO.toString())).booleanValue()) {
                    zzi(this.zzbkz, (String) next2.getKey()).zzb(zza6);
                }
            }
        }
    }

    private String zzHf() {
        if (this.zzbkB <= 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(this.zzbkB));
        for (int i = 2; i < this.zzbkB; i++) {
            sb.append(' ');
        }
        sb.append(": ");
        return sb.toString();
    }

    private zzbw<zzag.zza> zza(zzag.zza zza2, Set<String> set, zzdi zzdi) {
        if (!zza2.zzjH) {
            return new zzbw<>(zza2, true);
        }
        switch (zza2.type) {
            case 2:
                zzag.zza zzo = zzrs.zzo(zza2);
                zzo.zzjy = new zzag.zza[zza2.zzjy.length];
                for (int i = 0; i < zza2.zzjy.length; i++) {
                    zzbw<zzag.zza> zza3 = zza(zza2.zzjy[i], set, zzdi.zzkh(i));
                    if (zza3 == zzbkq) {
                        return zzbkq;
                    }
                    zzo.zzjy[i] = zza3.getObject();
                }
                return new zzbw<>(zzo, false);
            case 3:
                zzag.zza zzo2 = zzrs.zzo(zza2);
                if (zza2.zzjz.length != zza2.zzjA.length) {
                    zzbg.e("Invalid serving value: " + zza2.toString());
                    return zzbkq;
                }
                zzo2.zzjz = new zzag.zza[zza2.zzjz.length];
                zzo2.zzjA = new zzag.zza[zza2.zzjz.length];
                for (int i2 = 0; i2 < zza2.zzjz.length; i2++) {
                    zzbw<zzag.zza> zza4 = zza(zza2.zzjz[i2], set, zzdi.zzki(i2));
                    zzbw<zzag.zza> zza5 = zza(zza2.zzjA[i2], set, zzdi.zzkj(i2));
                    if (zza4 == zzbkq || zza5 == zzbkq) {
                        return zzbkq;
                    }
                    zzo2.zzjz[i2] = zza4.getObject();
                    zzo2.zzjA[i2] = zza5.getObject();
                }
                return new zzbw<>(zzo2, false);
            case 4:
                if (set.contains(zza2.zzjB)) {
                    zzbg.e("Macro cycle detected.  Current macro reference: " + zza2.zzjB + "." + "  Previous macro references: " + set.toString() + ".");
                    return zzbkq;
                }
                set.add(zza2.zzjB);
                zzbw<zzag.zza> zza6 = zzdj.zza(zza(zza2.zzjB, set, zzdi.zzGO()), zza2.zzjG);
                set.remove(zza2.zzjB);
                return zza6;
            case 7:
                zzag.zza zzo3 = zzrs.zzo(zza2);
                zzo3.zzjF = new zzag.zza[zza2.zzjF.length];
                for (int i3 = 0; i3 < zza2.zzjF.length; i3++) {
                    zzbw<zzag.zza> zza7 = zza(zza2.zzjF[i3], set, zzdi.zzkk(i3));
                    if (zza7 == zzbkq) {
                        return zzbkq;
                    }
                    zzo3.zzjF[i3] = zza7.getObject();
                }
                return new zzbw<>(zzo3, false);
            default:
                zzbg.e("Unknown type: " + zza2.type);
                return zzbkq;
        }
    }

    private zzbw<zzag.zza> zza(String str, Set<String> set, zzbj zzbj) {
        zzrs.zza zza2;
        this.zzbkB++;
        zzb zzb2 = this.zzbkx.get(str);
        if (zzb2 == null || this.zzbks.zzGA()) {
            zzc zzc2 = this.zzbkz.get(str);
            if (zzc2 == null) {
                zzbg.e(zzHf() + "Invalid macro: " + str);
                this.zzbkB--;
                return zzbkq;
            }
            zzbw<Set<zzrs.zza>> zza3 = zza(str, zzc2.zzHi(), zzc2.zzHj(), zzc2.zzHk(), zzc2.zzHm(), zzc2.zzHl(), set, zzbj.zzGq());
            if (zza3.getObject().isEmpty()) {
                zza2 = zzc2.zzHn();
            } else {
                if (zza3.getObject().size() > 1) {
                    zzbg.zzaK(zzHf() + "Multiple macros active for macroName " + str);
                }
                zza2 = (zzrs.zza) zza3.getObject().iterator().next();
            }
            if (zza2 == null) {
                this.zzbkB--;
                return zzbkq;
            }
            zzbw<zzag.zza> zza4 = zza(this.zzbkv, zza2, set, zzbj.zzGG());
            zzbw<zzag.zza> zzbw = zza4 == zzbkq ? zzbkq : new zzbw<>(zza4.getObject(), zza3.zzGP() && zza4.zzGP());
            zzag.zza zzHh = zza2.zzHh();
            if (zzbw.zzGP()) {
                this.zzbkx.zzh(str, new zzb(zzbw, zzHh));
            }
            zza(zzHh, set);
            this.zzbkB--;
            return zzbw;
        }
        zza(zzb2.zzHh(), set);
        this.zzbkB--;
        return zzb2.zzHg();
    }

    private zzbw<zzag.zza> zza(Map<String, zzak> map, zzrs.zza zza2, Set<String> set, zzch zzch) {
        boolean z;
        boolean z2 = true;
        zzag.zza zza3 = zza2.zzHI().get(zzae.FUNCTION.toString());
        if (zza3 == null) {
            zzbg.e("No function id in properties");
            return zzbkq;
        }
        String str = zza3.zzjC;
        zzak zzak = map.get(str);
        if (zzak == null) {
            zzbg.e(str + " has no backing implementation.");
            return zzbkq;
        }
        zzbw<zzag.zza> zzbw = this.zzbkw.get(zza2);
        if (zzbw != null && !this.zzbks.zzGA()) {
            return zzbw;
        }
        HashMap hashMap = new HashMap();
        boolean z3 = true;
        for (Map.Entry next : zza2.zzHI().entrySet()) {
            zzbw<zzag.zza> zza4 = zza((zzag.zza) next.getValue(), set, zzch.zzgj((String) next.getKey()).zze((zzag.zza) next.getValue()));
            if (zza4 == zzbkq) {
                return zzbkq;
            }
            if (zza4.zzGP()) {
                zza2.zza((String) next.getKey(), zza4.getObject());
                z = z3;
            } else {
                z = false;
            }
            hashMap.put(next.getKey(), zza4.getObject());
            z3 = z;
        }
        if (!zzak.zze(hashMap.keySet())) {
            zzbg.e("Incorrect keys for function " + str + " required " + zzak.zzGC() + " had " + hashMap.keySet());
            return zzbkq;
        }
        if (!z3 || !zzak.zzFW()) {
            z2 = false;
        }
        zzbw<zzag.zza> zzbw2 = new zzbw<>(zzak.zzP(hashMap), z2);
        if (z2) {
            this.zzbkw.zzh(zza2, zzbw2);
        }
        zzch.zzd(zzbw2.getObject());
        return zzbw2;
    }

    private zzbw<Set<zzrs.zza>> zza(Set<zzrs.zze> set, Set<String> set2, zza zza2, zzco zzco) {
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        boolean z = true;
        for (zzrs.zze next : set) {
            zzck zzGN = zzco.zzGN();
            zzbw<Boolean> zza3 = zza(next, set2, zzGN);
            if (zza3.getObject().booleanValue()) {
                zza2.zza(next, hashSet, hashSet2, zzGN);
            }
            z = z && zza3.zzGP();
        }
        hashSet.removeAll(hashSet2);
        zzco.zzf(hashSet);
        return new zzbw<>(hashSet, z);
    }

    private static String zza(zzrs.zza zza2) {
        return zzdf.zzg(zza2.zzHI().get(zzae.INSTANCE_NAME.toString()));
    }

    private void zza(zzag.zza zza2, Set<String> set) {
        zzbw<zzag.zza> zza3;
        if (zza2 != null && (zza3 = zza(zza2, set, (zzdi) new zzbu())) != zzbkq) {
            Object zzl = zzdf.zzl(zza3.getObject());
            if (zzl instanceof Map) {
                this.zzbhN.push((Map) zzl);
            } else if (zzl instanceof List) {
                for (Object next : (List) zzl) {
                    if (next instanceof Map) {
                        this.zzbhN.push((Map) next);
                    } else {
                        zzbg.zzaK("pushAfterEvaluate: value not a Map");
                    }
                }
            } else {
                zzbg.zzaK("pushAfterEvaluate: value not a Map or List");
            }
        }
    }

    private static void zza(List<zzrs.zza> list, List<String> list2, String str) {
        if (list.size() != list2.size()) {
            zzbg.zzaJ("Invalid resource: imbalance of rule names of functions for " + str + " operation. Using default rule name instead");
        }
    }

    private static void zza(Map<String, zzak> map, zzak zzak) {
        if (map.containsKey(zzak.zzGB())) {
            throw new IllegalArgumentException("Duplicate function type name: " + zzak.zzGB());
        }
        map.put(zzak.zzGB(), zzak);
    }

    private static zzc zzi(Map<String, zzc> map, String str) {
        zzc zzc2 = map.get(str);
        if (zzc2 != null) {
            return zzc2;
        }
        zzc zzc3 = new zzc();
        map.put(str, zzc3);
        return zzc3;
    }

    public synchronized void zzF(List<zzaf.zzi> list) {
        for (zzaf.zzi next : list) {
            if (next.name == null || !next.name.startsWith("gaExperiment:")) {
                zzbg.v("Ignored supplemental: " + next);
            } else {
                zzaj.zza(this.zzbhN, next);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized String zzHe() {
        return this.zzbkA;
    }

    /* access modifiers changed from: package-private */
    public zzbw<Boolean> zza(zzrs.zza zza2, Set<String> set, zzch zzch) {
        zzbw<zzag.zza> zza3 = zza(this.zzbku, zza2, set, zzch);
        Boolean zzk = zzdf.zzk(zza3.getObject());
        zzch.zzd(zzdf.zzR(zzk));
        return new zzbw<>(zzk, zza3.zzGP());
    }

    /* access modifiers changed from: package-private */
    public zzbw<Boolean> zza(zzrs.zze zze, Set<String> set, zzck zzck) {
        boolean z;
        boolean z2 = true;
        for (zzrs.zza zza2 : zze.zzHQ()) {
            zzbw<Boolean> zza3 = zza(zza2, set, zzck.zzGH());
            if (zza3.getObject().booleanValue()) {
                zzck.zzf(zzdf.zzR(false));
                return new zzbw<>(false, zza3.zzGP());
            }
            z2 = z && zza3.zzGP();
        }
        for (zzrs.zza zza4 : zze.zzHP()) {
            zzbw<Boolean> zza5 = zza(zza4, set, zzck.zzGI());
            if (!zza5.getObject().booleanValue()) {
                zzck.zzf(zzdf.zzR(false));
                return new zzbw<>(false, zza5.zzGP());
            }
            z = z && zza5.zzGP();
        }
        zzck.zzf(zzdf.zzR(true));
        return new zzbw<>(true, z);
    }

    /* access modifiers changed from: package-private */
    public zzbw<Set<zzrs.zza>> zza(String str, Set<zzrs.zze> set, Map<zzrs.zze, List<zzrs.zza>> map, Map<zzrs.zze, List<String>> map2, Map<zzrs.zze, List<zzrs.zza>> map3, Map<zzrs.zze, List<String>> map4, Set<String> set2, zzco zzco) {
        final Map<zzrs.zze, List<zzrs.zza>> map5 = map;
        final Map<zzrs.zze, List<String>> map6 = map2;
        final Map<zzrs.zze, List<zzrs.zza>> map7 = map3;
        final Map<zzrs.zze, List<String>> map8 = map4;
        return zza(set, set2, (zza) new zza() {
            public void zza(zzrs.zze zze, Set<zzrs.zza> set, Set<zzrs.zza> set2, zzck zzck) {
                List list = (List) map5.get(zze);
                List list2 = (List) map6.get(zze);
                if (list != null) {
                    set.addAll(list);
                    zzck.zzGJ().zzc(list, list2);
                }
                List list3 = (List) map7.get(zze);
                List list4 = (List) map8.get(zze);
                if (list3 != null) {
                    set2.addAll(list3);
                    zzck.zzGK().zzc(list3, list4);
                }
            }
        }, zzco);
    }

    /* access modifiers changed from: package-private */
    public zzbw<Set<zzrs.zza>> zza(Set<zzrs.zze> set, zzco zzco) {
        return zza(set, (Set<String>) new HashSet(), (zza) new zza() {
            public void zza(zzrs.zze zze, Set<zzrs.zza> set, Set<zzrs.zza> set2, zzck zzck) {
                set.addAll(zze.zzHR());
                set2.addAll(zze.zzHS());
                zzck.zzGL().zzc(zze.zzHR(), zze.zzHW());
                zzck.zzGM().zzc(zze.zzHS(), zze.zzHX());
            }
        }, zzco);
    }

    /* access modifiers changed from: package-private */
    public void zza(zzak zzak) {
        zza(this.zzbkv, zzak);
    }

    /* access modifiers changed from: package-private */
    public void zzb(zzak zzak) {
        zza(this.zzbkt, zzak);
    }

    /* access modifiers changed from: package-private */
    public void zzc(zzak zzak) {
        zza(this.zzbku, zzak);
    }

    public synchronized void zzfR(String str) {
        zzgo(str);
        zzag zzge = this.zzbks.zzge(str);
        zzu zzGy = zzge.zzGy();
        for (zzrs.zza zza2 : zza(this.zzbky, zzGy.zzGq()).getObject()) {
            zza(this.zzbkt, zza2, (Set<String>) new HashSet(), zzGy.zzGp());
        }
        zzge.zzGz();
        zzgo((String) null);
    }

    public zzbw<zzag.zza> zzgn(String str) {
        this.zzbkB = 0;
        zzag zzgd = this.zzbks.zzgd(str);
        zzbw<zzag.zza> zza2 = zza(str, (Set<String>) new HashSet(), zzgd.zzGx());
        zzgd.zzGz();
        return zza2;
    }

    /* access modifiers changed from: package-private */
    public synchronized void zzgo(String str) {
        this.zzbkA = str;
    }
}
