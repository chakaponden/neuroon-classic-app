package com.google.android.gms.tagmanager;

import android.os.Build;
import android.support.v4.os.EnvironmentCompat;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzab extends zzak {
    private static final String ID = zzad.DEVICE_NAME.toString();

    public zzab() {
        super(ID, new String[0]);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (!str2.startsWith(str) && !str.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
            str2 = str + " " + str2;
        }
        return zzdf.zzR(str2);
    }
}
