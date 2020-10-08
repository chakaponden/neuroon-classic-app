package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzh extends zzak {
    private static final String ID = zzad.APP_VERSION.toString();
    private final Context mContext;

    public zzh(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        try {
            return zzdf.zzR(Integer.valueOf(this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            zzbg.e("Package name " + this.mContext.getPackageName() + " not found. " + e.getMessage());
            return zzdf.zzHF();
        }
    }
}
