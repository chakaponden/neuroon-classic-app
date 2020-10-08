package com.google.android.gms.measurement.internal;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpz;
import com.google.android.gms.internal.zzqb;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class zzc extends zzz {
    zzc(zzw zzw) {
        super(zzw);
    }

    private Boolean zza(zzpz.zzb zzb, zzqb.zzb zzb2, long j) {
        if (zzb.zzaZz != null) {
            Boolean zzac = new zzs(zzb.zzaZz).zzac(j);
            if (zzac == null) {
                return null;
            }
            if (!zzac.booleanValue()) {
                return false;
            }
        }
        HashSet hashSet = new HashSet();
        for (zzpz.zzc zzc : zzb.zzaZx) {
            if (TextUtils.isEmpty(zzc.zzaZE)) {
                zzAo().zzCF().zzj("null or empty param name in filter. event", zzb2.name);
                return null;
            }
            hashSet.add(zzc.zzaZE);
        }
        ArrayMap arrayMap = new ArrayMap();
        for (zzqb.zzc zzc2 : zzb2.zzbae) {
            if (hashSet.contains(zzc2.name)) {
                if (zzc2.zzbai != null) {
                    arrayMap.put(zzc2.name, zzc2.zzbai);
                } else if (zzc2.zzaZo != null) {
                    arrayMap.put(zzc2.name, zzc2.zzaZo);
                } else if (zzc2.zzamJ != null) {
                    arrayMap.put(zzc2.name, zzc2.zzamJ);
                } else {
                    zzAo().zzCF().zze("Unknown value for param. event, param", zzb2.name, zzc2.name);
                    return null;
                }
            }
        }
        for (zzpz.zzc zzc3 : zzb.zzaZx) {
            String str = zzc3.zzaZE;
            if (TextUtils.isEmpty(str)) {
                zzAo().zzCF().zzj("Event has empty param name. event", zzb2.name);
                return null;
            }
            Object obj = arrayMap.get(str);
            if (obj instanceof Long) {
                if (zzc3.zzaZC == null) {
                    zzAo().zzCF().zze("No number filter for long param. event, param", zzb2.name, str);
                    return null;
                }
                Boolean zzac2 = new zzs(zzc3.zzaZC).zzac(((Long) obj).longValue());
                if (zzac2 == null) {
                    return null;
                }
                if (!zzac2.booleanValue()) {
                    return false;
                }
            } else if (obj instanceof Float) {
                if (zzc3.zzaZC == null) {
                    zzAo().zzCF().zze("No number filter for float param. event, param", zzb2.name, str);
                    return null;
                }
                Boolean zzi = new zzs(zzc3.zzaZC).zzi(((Float) obj).floatValue());
                if (zzi == null) {
                    return null;
                }
                if (!zzi.booleanValue()) {
                    return false;
                }
            } else if (obj instanceof String) {
                if (zzc3.zzaZB == null) {
                    zzAo().zzCF().zze("No string filter for String param. event, param", zzb2.name, str);
                    return null;
                }
                Boolean zzfp = new zzae(zzc3.zzaZB).zzfp((String) obj);
                if (zzfp == null) {
                    return null;
                }
                if (!zzfp.booleanValue()) {
                    return false;
                }
            } else if (obj == null) {
                zzAo().zzCK().zze("Missing param for filter. event, param", zzb2.name, str);
                return false;
            } else {
                zzAo().zzCF().zze("Unknown param type. event, param", zzb2.name, str);
                return null;
            }
        }
        return true;
    }

    private Boolean zza(zzpz.zze zze, zzqb.zzg zzg) {
        zzpz.zzc zzc = zze.zzaZM;
        if (zzc == null) {
            zzAo().zzCF().zzj("Missing property filter. property", zzg.name);
            return null;
        } else if (zzg.zzbai != null) {
            if (zzc.zzaZC != null) {
                return new zzs(zzc.zzaZC).zzac(zzg.zzbai.longValue());
            }
            zzAo().zzCF().zzj("No number filter for long property. property", zzg.name);
            return null;
        } else if (zzg.zzaZo != null) {
            if (zzc.zzaZC != null) {
                return new zzs(zzc.zzaZC).zzi(zzg.zzaZo.floatValue());
            }
            zzAo().zzCF().zzj("No number filter for float property. property", zzg.name);
            return null;
        } else if (zzg.zzamJ == null) {
            zzAo().zzCF().zzj("User property has no value, property", zzg.name);
            return null;
        } else if (zzc.zzaZB != null) {
            return new zzae(zzc.zzaZB).zzfp(zzg.zzamJ);
        } else {
            if (zzc.zzaZC == null) {
                zzAo().zzCF().zzj("No string or number filter defined. property", zzg.name);
                return null;
            }
            zzs zzs = new zzs(zzc.zzaZC);
            if (!zzc.zzaZC.zzaZG.booleanValue()) {
                if (zzeQ(zzg.zzamJ)) {
                    try {
                        return zzs.zzac(Long.parseLong(zzg.zzamJ));
                    } catch (NumberFormatException e) {
                        zzAo().zzCF().zze("User property value exceeded Long value range. property, value", zzg.name, zzg.zzamJ);
                        return null;
                    }
                } else {
                    zzAo().zzCF().zze("Invalid user property value for Long number filter. property, value", zzg.name, zzg.zzamJ);
                    return null;
                }
            } else if (zzeR(zzg.zzamJ)) {
                try {
                    float parseFloat = Float.parseFloat(zzg.zzamJ);
                    if (!Float.isInfinite(parseFloat)) {
                        return zzs.zzi(parseFloat);
                    }
                    zzAo().zzCF().zze("User property value exceeded Float value range. property, value", zzg.name, zzg.zzamJ);
                    return null;
                } catch (NumberFormatException e2) {
                    zzAo().zzCF().zze("User property value exceeded Float value range. property, value", zzg.name, zzg.zzamJ);
                    return null;
                }
            } else {
                zzAo().zzCF().zze("Invalid user property value for Float number filter. property, value", zzg.name, zzg.zzamJ);
                return null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zza(String str, zzpz.zza[] zzaArr) {
        zzCj().zzb(str, zzaArr);
    }

    /* access modifiers changed from: package-private */
    public zzqb.zza[] zza(String str, zzqb.zzb[] zzbArr, zzqb.zzg[] zzgArr) {
        Map map;
        zzqb.zza zza;
        zzi zzCB;
        Map map2;
        zzqb.zza zza2;
        zzx.zzcM(str);
        HashSet hashSet = new HashSet();
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        if (zzbArr != null) {
            ArrayMap arrayMap4 = new ArrayMap();
            int length = zzbArr.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= length) {
                    break;
                }
                zzqb.zzb zzb = zzbArr[i2];
                zzi zzI = zzCj().zzI(str, zzb.name);
                if (zzI == null) {
                    zzAo().zzCF().zzj("Event aggregate wasn't created during raw event logging. event", zzb.name);
                    zzCB = new zzi(str, zzb.name, 1, 1, zzb.zzbaf.longValue());
                } else {
                    zzCB = zzI.zzCB();
                }
                zzCj().zza(zzCB);
                long j = zzCB.zzaVP;
                Map map3 = (Map) arrayMap4.get(zzb.name);
                if (map3 == null) {
                    Map zzL = zzCj().zzL(str, zzb.name);
                    if (zzL == null) {
                        zzL = new ArrayMap();
                    }
                    arrayMap4.put(zzb.name, zzL);
                    map2 = zzL;
                } else {
                    map2 = map3;
                }
                zzAo().zzCK().zze("Found audiences. event, audience count", zzb.name, Integer.valueOf(map2.size()));
                for (Integer intValue : map2.keySet()) {
                    int intValue2 = intValue.intValue();
                    if (hashSet.contains(Integer.valueOf(intValue2))) {
                        zzAo().zzCK().zzj("Skipping failed audience ID", Integer.valueOf(intValue2));
                    } else {
                        zzqb.zza zza3 = (zzqb.zza) arrayMap.get(Integer.valueOf(intValue2));
                        if (zza3 == null) {
                            zzqb.zza zza4 = new zzqb.zza();
                            arrayMap.put(Integer.valueOf(intValue2), zza4);
                            zza4.zzbac = false;
                            zza2 = zza4;
                        } else {
                            zza2 = zza3;
                        }
                        List<zzpz.zzb> list = (List) map2.get(Integer.valueOf(intValue2));
                        BitSet bitSet = (BitSet) arrayMap2.get(Integer.valueOf(intValue2));
                        BitSet bitSet2 = (BitSet) arrayMap3.get(Integer.valueOf(intValue2));
                        if (bitSet == null) {
                            bitSet = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue2), bitSet);
                            bitSet2 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue2), bitSet2);
                        }
                        if (zza2.zzbab == null && !zza2.zzbac.booleanValue()) {
                            zzqb.zzf zzC = zzCj().zzC(str, intValue2);
                            if (zzC == null) {
                                zza2.zzbac = true;
                            } else {
                                zza2.zzbab = zzC;
                                for (int i3 = 0; i3 < zzC.zzbaH.length * 64; i3++) {
                                    if (zzaj.zza(zzC.zzbaH, i3)) {
                                        zzAo().zzCK().zze("Event filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue2), Integer.valueOf(i3));
                                        bitSet.set(i3);
                                        bitSet2.set(i3);
                                    }
                                }
                            }
                        }
                        for (zzpz.zzb zzb2 : list) {
                            if (zzAo().zzQ(2)) {
                                zzAo().zzCK().zzd("Evaluating filter. audience, filter, event", Integer.valueOf(intValue2), zzb2.zzaZv, zzb2.zzaZw);
                                zzAo().zzCK().zzj("Filter definition", zzb2);
                            }
                            if (zzb2.zzaZv.intValue() > 256) {
                                zzAo().zzCF().zzj("Invalid event filter ID > 256. id", zzb2.zzaZv);
                            } else if (!bitSet2.get(zzb2.zzaZv.intValue())) {
                                Boolean zza5 = zza(zzb2, zzb, j);
                                zzAo().zzCK().zzj("Event filter result", zza5);
                                if (zza5 == null) {
                                    hashSet.add(Integer.valueOf(intValue2));
                                } else {
                                    bitSet2.set(zzb2.zzaZv.intValue());
                                    if (zza5.booleanValue()) {
                                        bitSet.set(zzb2.zzaZv.intValue());
                                    }
                                }
                            }
                        }
                    }
                }
                i = i2 + 1;
            }
        }
        if (zzgArr != null) {
            ArrayMap arrayMap5 = new ArrayMap();
            for (zzqb.zzg zzg : zzgArr) {
                Map map4 = (Map) arrayMap5.get(zzg.name);
                if (map4 == null) {
                    Map zzM = zzCj().zzM(str, zzg.name);
                    if (zzM == null) {
                        zzM = new ArrayMap();
                    }
                    arrayMap5.put(zzg.name, zzM);
                    map = zzM;
                } else {
                    map = map4;
                }
                zzAo().zzCK().zze("Found audiences. property, audience count", zzg.name, Integer.valueOf(map.size()));
                for (Integer intValue3 : map.keySet()) {
                    int intValue4 = intValue3.intValue();
                    if (!hashSet.contains(Integer.valueOf(intValue4))) {
                        zzqb.zza zza6 = (zzqb.zza) arrayMap.get(Integer.valueOf(intValue4));
                        if (zza6 == null) {
                            zzqb.zza zza7 = new zzqb.zza();
                            arrayMap.put(Integer.valueOf(intValue4), zza7);
                            zza7.zzbac = false;
                            zza = zza7;
                        } else {
                            zza = zza6;
                        }
                        List list2 = (List) map.get(Integer.valueOf(intValue4));
                        BitSet bitSet3 = (BitSet) arrayMap2.get(Integer.valueOf(intValue4));
                        BitSet bitSet4 = (BitSet) arrayMap3.get(Integer.valueOf(intValue4));
                        if (bitSet3 == null) {
                            bitSet3 = new BitSet();
                            arrayMap2.put(Integer.valueOf(intValue4), bitSet3);
                            bitSet4 = new BitSet();
                            arrayMap3.put(Integer.valueOf(intValue4), bitSet4);
                        }
                        if (zza.zzbab == null && !zza.zzbac.booleanValue()) {
                            zzqb.zzf zzC2 = zzCj().zzC(str, intValue4);
                            if (zzC2 == null) {
                                zza.zzbac = 1;
                            } else {
                                zza.zzbab = zzC2;
                                for (int i4 = 0; i4 < zzC2.zzbaH.length * 64; i4++) {
                                    if (zzaj.zza(zzC2.zzbaH, i4)) {
                                        bitSet3.set(i4);
                                        bitSet4.set(i4);
                                    }
                                }
                            }
                        }
                        Iterator it = list2.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            zzpz.zze zze = (zzpz.zze) it.next();
                            if (zzAo().zzQ(2)) {
                                zzAo().zzCK().zzd("Evaluating filter. audience, filter, property", Integer.valueOf(intValue4), zze.zzaZv, zze.zzaZL);
                                zzAo().zzCK().zzj("Filter definition", zze);
                            }
                            if (zze.zzaZv == null || zze.zzaZv.intValue() > 256) {
                                zzAo().zzCF().zzj("Invalid property filter ID. id", String.valueOf(zze.zzaZv));
                                hashSet.add(Integer.valueOf(intValue4));
                            } else if (bitSet4.get(zze.zzaZv.intValue())) {
                                zzAo().zzCK().zze("Property filter already evaluated true. audience ID, filter ID", Integer.valueOf(intValue4), zze.zzaZv);
                            } else {
                                Boolean zza8 = zza(zze, zzg);
                                zzAo().zzCK().zzj("Property filter result", zza8);
                                if (zza8 == null) {
                                    hashSet.add(Integer.valueOf(intValue4));
                                } else {
                                    bitSet4.set(zze.zzaZv.intValue());
                                    if (zza8.booleanValue()) {
                                        bitSet3.set(zze.zzaZv.intValue());
                                    }
                                }
                            }
                        }
                    } else {
                        zzAo().zzCK().zzj("Skipping failed audience ID", Integer.valueOf(intValue4));
                    }
                }
            }
        }
        zzqb.zza[] zzaArr = new zzqb.zza[arrayMap2.size()];
        int i5 = 0;
        for (Integer intValue5 : arrayMap2.keySet()) {
            int intValue6 = intValue5.intValue();
            if (!hashSet.contains(Integer.valueOf(intValue6))) {
                zzqb.zza zza9 = (zzqb.zza) arrayMap.get(Integer.valueOf(intValue6));
                if (zza9 == null) {
                    zza9 = new zzqb.zza();
                }
                zzqb.zza zza10 = zza9;
                zzaArr[i5] = zza10;
                zza10.zzaZr = Integer.valueOf(intValue6);
                zza10.zzbaa = new zzqb.zzf();
                zza10.zzbaa.zzbaH = zzaj.zza((BitSet) arrayMap2.get(Integer.valueOf(intValue6)));
                zza10.zzbaa.zzbaG = zzaj.zza((BitSet) arrayMap3.get(Integer.valueOf(intValue6)));
                zzCj().zza(str, intValue6, zza10.zzbaa);
                i5++;
            }
        }
        return (zzqb.zza[]) Arrays.copyOf(zzaArr, i5);
    }

    /* access modifiers changed from: package-private */
    public boolean zzeQ(String str) {
        return Pattern.matches("[+-]?[0-9]+", str);
    }

    /* access modifiers changed from: package-private */
    public boolean zzeR(String str) {
        return Pattern.matches("[+-]?(([0-9]+\\.?)|([0-9]*\\.[0-9]+))", str);
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }
}
