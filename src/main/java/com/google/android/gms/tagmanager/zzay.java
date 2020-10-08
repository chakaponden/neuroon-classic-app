package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class zzay extends zzak {
    private static final String ID = zzad.JOINER.toString();
    private static final String zzbiQ = zzae.ARG0.toString();
    private static final String zzbji = zzae.ITEM_SEPARATOR.toString();
    private static final String zzbjj = zzae.KEY_VALUE_SEPARATOR.toString();
    private static final String zzbjk = zzae.ESCAPE.toString();

    private enum zza {
        NONE,
        URL,
        BACKSLASH
    }

    public zzay() {
        super(ID, zzbiQ);
    }

    private String zza(String str, zza zza2, Set<Character> set) {
        switch (zza2) {
            case URL:
                try {
                    return zzdj.zzgA(str);
                } catch (UnsupportedEncodingException e) {
                    zzbg.zzb("Joiner: unsupported encoding", e);
                    return str;
                }
            case BACKSLASH:
                String replace = str.replace("\\", "\\\\");
                Iterator<Character> it = set.iterator();
                while (true) {
                    String str2 = replace;
                    if (!it.hasNext()) {
                        return str2;
                    }
                    String ch = it.next().toString();
                    replace = str2.replace(ch, "\\" + ch);
                }
            default:
                return str;
        }
    }

    private void zza(StringBuilder sb, String str, zza zza2, Set<Character> set) {
        sb.append(zza(str, zza2, set));
    }

    private void zza(Set<Character> set, String str) {
        for (int i = 0; i < str.length(); i++) {
            set.add(Character.valueOf(str.charAt(i)));
        }
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        HashSet hashSet;
        zza zza2;
        zzag.zza zza3 = map.get(zzbiQ);
        if (zza3 == null) {
            return zzdf.zzHF();
        }
        zzag.zza zza4 = map.get(zzbji);
        String zzg = zza4 != null ? zzdf.zzg(zza4) : "";
        zzag.zza zza5 = map.get(zzbjj);
        String zzg2 = zza5 != null ? zzdf.zzg(zza5) : Condition.Operation.EQUALS;
        zza zza6 = zza.NONE;
        zzag.zza zza7 = map.get(zzbjk);
        if (zza7 != null) {
            String zzg3 = zzdf.zzg(zza7);
            if ("url".equals(zzg3)) {
                zza2 = zza.URL;
                hashSet = null;
            } else if ("backslash".equals(zzg3)) {
                zza2 = zza.BACKSLASH;
                hashSet = new HashSet();
                zza(hashSet, zzg);
                zza(hashSet, zzg2);
                hashSet.remove('\\');
            } else {
                zzbg.e("Joiner: unsupported escape type: " + zzg3);
                return zzdf.zzHF();
            }
        } else {
            hashSet = null;
            zza2 = zza6;
        }
        StringBuilder sb = new StringBuilder();
        switch (zza3.type) {
            case 2:
                boolean z = true;
                zzag.zza[] zzaArr = zza3.zzjy;
                int length = zzaArr.length;
                int i = 0;
                while (i < length) {
                    zzag.zza zza8 = zzaArr[i];
                    if (!z) {
                        sb.append(zzg);
                    }
                    zza(sb, zzdf.zzg(zza8), zza2, hashSet);
                    i++;
                    z = false;
                }
                break;
            case 3:
                for (int i2 = 0; i2 < zza3.zzjz.length; i2++) {
                    if (i2 > 0) {
                        sb.append(zzg);
                    }
                    String zzg4 = zzdf.zzg(zza3.zzjz[i2]);
                    String zzg5 = zzdf.zzg(zza3.zzjA[i2]);
                    zza(sb, zzg4, zza2, hashSet);
                    sb.append(zzg2);
                    zza(sb, zzg5, zza2, hashSet);
                }
                break;
            default:
                zza(sb, zzdf.zzg(zza3), zza2, hashSet);
                break;
        }
        return zzdf.zzR(sb.toString());
    }
}
