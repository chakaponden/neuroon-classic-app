package com.google.android.gms.tagmanager;

import android.content.Context;
import android.provider.Settings;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzag;
import java.util.Map;

class zzbk extends zzak {
    private static final String ID = zzad.MOBILE_ADWORDS_UNIQUE_ID.toString();
    private final Context mContext;

    public zzbk(Context context) {
        super(ID, new String[0]);
        this.mContext = context;
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        String zzaY = zzaY(this.mContext);
        return zzaY == null ? zzdf.zzHF() : zzdf.zzR(zzaY);
    }

    /* access modifiers changed from: protected */
    public String zzaY(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }
}
