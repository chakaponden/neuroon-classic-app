package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class zzdf {
    private static final Object zzblE = null;
    private static Long zzblF = new Long(0);
    private static Double zzblG = new Double(0.0d);
    private static zzde zzblH = zzde.zzam(0);
    private static String zzblI = new String("");
    private static Boolean zzblJ = new Boolean(false);
    private static List<Object> zzblK = new ArrayList(0);
    private static Map<Object, Object> zzblL = new HashMap();
    private static zzag.zza zzblM = zzR(zzblI);

    private static double getDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        zzbg.e("getDouble received non-Number");
        return 0.0d;
    }

    public static Long zzHA() {
        return zzblF;
    }

    public static Double zzHB() {
        return zzblG;
    }

    public static Boolean zzHC() {
        return zzblJ;
    }

    public static zzde zzHD() {
        return zzblH;
    }

    public static String zzHE() {
        return zzblI;
    }

    public static zzag.zza zzHF() {
        return zzblM;
    }

    public static Object zzHz() {
        return zzblE;
    }

    public static String zzM(Object obj) {
        return obj == null ? zzblI : obj.toString();
    }

    public static zzde zzN(Object obj) {
        return obj instanceof zzde ? (zzde) obj : zzT(obj) ? zzde.zzam(zzU(obj)) : zzS(obj) ? zzde.zza(Double.valueOf(getDouble(obj))) : zzgu(zzM(obj));
    }

    public static Long zzO(Object obj) {
        return zzT(obj) ? Long.valueOf(zzU(obj)) : zzgv(zzM(obj));
    }

    public static Double zzP(Object obj) {
        return zzS(obj) ? Double.valueOf(getDouble(obj)) : zzgw(zzM(obj));
    }

    public static Boolean zzQ(Object obj) {
        return obj instanceof Boolean ? (Boolean) obj : zzgx(zzM(obj));
    }

    public static zzag.zza zzR(Object obj) {
        boolean z = false;
        zzag.zza zza = new zzag.zza();
        if (obj instanceof zzag.zza) {
            return (zzag.zza) obj;
        }
        if (obj instanceof String) {
            zza.type = 1;
            zza.zzjx = (String) obj;
        } else if (obj instanceof List) {
            zza.type = 2;
            List<Object> list = (List) obj;
            ArrayList arrayList = new ArrayList(list.size());
            boolean z2 = false;
            for (Object zzR : list) {
                zzag.zza zzR2 = zzR(zzR);
                if (zzR2 == zzblM) {
                    return zzblM;
                }
                boolean z3 = z2 || zzR2.zzjH;
                arrayList.add(zzR2);
                z2 = z3;
            }
            zza.zzjy = (zzag.zza[]) arrayList.toArray(new zzag.zza[0]);
            z = z2;
        } else if (obj instanceof Map) {
            zza.type = 3;
            Set<Map.Entry> entrySet = ((Map) obj).entrySet();
            ArrayList arrayList2 = new ArrayList(entrySet.size());
            ArrayList arrayList3 = new ArrayList(entrySet.size());
            boolean z4 = false;
            for (Map.Entry entry : entrySet) {
                zzag.zza zzR3 = zzR(entry.getKey());
                zzag.zza zzR4 = zzR(entry.getValue());
                if (zzR3 == zzblM || zzR4 == zzblM) {
                    return zzblM;
                }
                boolean z5 = z4 || zzR3.zzjH || zzR4.zzjH;
                arrayList2.add(zzR3);
                arrayList3.add(zzR4);
                z4 = z5;
            }
            zza.zzjz = (zzag.zza[]) arrayList2.toArray(new zzag.zza[0]);
            zza.zzjA = (zzag.zza[]) arrayList3.toArray(new zzag.zza[0]);
            z = z4;
        } else if (zzS(obj)) {
            zza.type = 1;
            zza.zzjx = obj.toString();
        } else if (zzT(obj)) {
            zza.type = 6;
            zza.zzjD = zzU(obj);
        } else if (obj instanceof Boolean) {
            zza.type = 8;
            zza.zzjE = ((Boolean) obj).booleanValue();
        } else {
            zzbg.e("Converting to Value from unknown object type: " + (obj == null ? "null" : obj.getClass().toString()));
            return zzblM;
        }
        zza.zzjH = z;
        return zza;
    }

    private static boolean zzS(Object obj) {
        return (obj instanceof Double) || (obj instanceof Float) || ((obj instanceof zzde) && ((zzde) obj).zzHu());
    }

    private static boolean zzT(Object obj) {
        return (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || ((obj instanceof zzde) && ((zzde) obj).zzHv());
    }

    private static long zzU(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        zzbg.e("getInt64 received non-Number");
        return 0;
    }

    public static String zzg(zzag.zza zza) {
        return zzM(zzl(zza));
    }

    public static zzag.zza zzgt(String str) {
        zzag.zza zza = new zzag.zza();
        zza.type = 5;
        zza.zzjC = str;
        return zza;
    }

    private static zzde zzgu(String str) {
        try {
            return zzde.zzgs(str);
        } catch (NumberFormatException e) {
            zzbg.e("Failed to convert '" + str + "' to a number.");
            return zzblH;
        }
    }

    private static Long zzgv(String str) {
        zzde zzgu = zzgu(str);
        return zzgu == zzblH ? zzblF : Long.valueOf(zzgu.longValue());
    }

    private static Double zzgw(String str) {
        zzde zzgu = zzgu(str);
        return zzgu == zzblH ? zzblG : Double.valueOf(zzgu.doubleValue());
    }

    private static Boolean zzgx(String str) {
        return "true".equalsIgnoreCase(str) ? Boolean.TRUE : "false".equalsIgnoreCase(str) ? Boolean.FALSE : zzblJ;
    }

    public static zzde zzh(zzag.zza zza) {
        return zzN(zzl(zza));
    }

    public static Long zzi(zzag.zza zza) {
        return zzO(zzl(zza));
    }

    public static Double zzj(zzag.zza zza) {
        return zzP(zzl(zza));
    }

    public static Boolean zzk(zzag.zza zza) {
        return zzQ(zzl(zza));
    }

    public static Object zzl(zzag.zza zza) {
        int i = 0;
        if (zza == null) {
            return zzblE;
        }
        switch (zza.type) {
            case 1:
                return zza.zzjx;
            case 2:
                ArrayList arrayList = new ArrayList(zza.zzjy.length);
                zzag.zza[] zzaArr = zza.zzjy;
                int length = zzaArr.length;
                while (i < length) {
                    Object zzl = zzl(zzaArr[i]);
                    if (zzl == zzblE) {
                        return zzblE;
                    }
                    arrayList.add(zzl);
                    i++;
                }
                return arrayList;
            case 3:
                if (zza.zzjz.length != zza.zzjA.length) {
                    zzbg.e("Converting an invalid value to object: " + zza.toString());
                    return zzblE;
                }
                HashMap hashMap = new HashMap(zza.zzjA.length);
                while (i < zza.zzjz.length) {
                    Object zzl2 = zzl(zza.zzjz[i]);
                    Object zzl3 = zzl(zza.zzjA[i]);
                    if (zzl2 == zzblE || zzl3 == zzblE) {
                        return zzblE;
                    }
                    hashMap.put(zzl2, zzl3);
                    i++;
                }
                return hashMap;
            case 4:
                zzbg.e("Trying to convert a macro reference to object");
                return zzblE;
            case 5:
                zzbg.e("Trying to convert a function id to object");
                return zzblE;
            case 6:
                return Long.valueOf(zza.zzjD);
            case 7:
                StringBuffer stringBuffer = new StringBuffer();
                zzag.zza[] zzaArr2 = zza.zzjF;
                int length2 = zzaArr2.length;
                while (i < length2) {
                    String zzg = zzg(zzaArr2[i]);
                    if (zzg == zzblI) {
                        return zzblE;
                    }
                    stringBuffer.append(zzg);
                    i++;
                }
                return stringBuffer.toString();
            case 8:
                return Boolean.valueOf(zza.zzjE);
            default:
                zzbg.e("Failed to convert a value of type: " + zza.type);
                return zzblE;
        }
    }
}
