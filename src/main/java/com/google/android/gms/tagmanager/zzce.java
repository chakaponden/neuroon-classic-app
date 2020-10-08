package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class zzce extends zzak {
    private static final String ID = zzad.REGEX_GROUP.toString();
    private static final String zzbka = zzae.ARG0.toString();
    private static final String zzbkb = zzae.ARG1.toString();
    private static final String zzbkc = zzae.IGNORE_CASE.toString();
    private static final String zzbkd = zzae.GROUP.toString();

    public zzce() {
        super(ID, zzbka, zzbkb);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        int i;
        zzag.zza zza = map.get(zzbka);
        zzag.zza zza2 = map.get(zzbkb);
        if (zza == null || zza == zzdf.zzHF() || zza2 == null || zza2 == zzdf.zzHF()) {
            return zzdf.zzHF();
        }
        int i2 = 64;
        if (zzdf.zzk(map.get(zzbkc)).booleanValue()) {
            i2 = 66;
        }
        zzag.zza zza3 = map.get(zzbkd);
        if (zza3 != null) {
            Long zzi = zzdf.zzi(zza3);
            if (zzi == zzdf.zzHA()) {
                return zzdf.zzHF();
            }
            i = zzi.intValue();
            if (i < 0) {
                return zzdf.zzHF();
            }
        } else {
            i = 1;
        }
        try {
            String zzg = zzdf.zzg(zza);
            String str = null;
            Matcher matcher = Pattern.compile(zzdf.zzg(zza2), i2).matcher(zzg);
            if (matcher.find() && matcher.groupCount() >= i) {
                str = matcher.group(i);
            }
            return str == null ? zzdf.zzHF() : zzdf.zzR(str);
        } catch (PatternSyntaxException e) {
            return zzdf.zzHF();
        }
    }
}
