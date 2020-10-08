package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class zzbb extends zzak {
    private static final String ID = zzad.LANGUAGE.toString();

    public zzbb() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return false;
    }

    public /* bridge */ /* synthetic */ String zzGB() {
        return super.zzGB();
    }

    public /* bridge */ /* synthetic */ Set zzGC() {
        return super.zzGC();
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return zzdf.zzHF();
        }
        String language = locale.getLanguage();
        return language == null ? zzdf.zzHF() : zzdf.zzR(language.toLowerCase());
    }
}
