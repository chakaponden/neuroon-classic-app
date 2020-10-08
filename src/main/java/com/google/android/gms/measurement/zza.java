package com.google.android.gms.measurement;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.google.android.gms.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzx;

public final class zza {
    private static volatile zza zzaUe;
    private final String zzaUa;
    private final Status zzaUb;
    private final boolean zzaUc;
    private final boolean zzaUd;

    zza(Context context) {
        boolean z = true;
        Resources resources = context.getResources();
        String resourcePackageName = resources.getResourcePackageName(R.string.common_google_play_services_unknown_issue);
        int identifier = resources.getIdentifier("google_app_measurement_enable", "integer", resourcePackageName);
        if (identifier != 0) {
            boolean z2 = resources.getInteger(identifier) != 0;
            this.zzaUd = z2 ? false : z;
            z = z2;
        } else {
            this.zzaUd = false;
        }
        this.zzaUc = z;
        int identifier2 = resources.getIdentifier("google_app_id", "string", resourcePackageName);
        if (identifier2 == 0) {
            if (this.zzaUc) {
                this.zzaUb = new Status(10, "Missing an expected resource: 'R.string.google_app_id' for initializing Google services.  Possible causes are missing google-services.json or com.google.gms.google-services gradle plugin.");
            } else {
                this.zzaUb = Status.zzagC;
            }
            this.zzaUa = null;
            return;
        }
        String string = resources.getString(identifier2);
        if (TextUtils.isEmpty(string)) {
            if (this.zzaUc) {
                this.zzaUb = new Status(10, "The resource 'R.string.google_app_id' is invalid, expected an app  identifier and found: '" + string + "'.");
            } else {
                this.zzaUb = Status.zzagC;
            }
            this.zzaUa = null;
            return;
        }
        this.zzaUa = string;
        this.zzaUb = Status.zzagC;
    }

    zza(Context context, String str, boolean z) {
        this.zzaUa = str;
        this.zzaUb = Status.zzagC;
        this.zzaUc = z;
        this.zzaUd = !z;
    }

    public static String zzAp() {
        if (zzaUe == null) {
            synchronized (zza.class) {
                if (zzaUe == null) {
                    throw new IllegalStateException("Initialize must be called before getGoogleAppId.");
                }
            }
        }
        return zzaUe.zzAq();
    }

    public static boolean zzAr() {
        if (zzaUe == null) {
            synchronized (zza.class) {
                if (zzaUe == null) {
                    throw new IllegalStateException("Initialize must be called before isMeasurementEnabled.");
                }
            }
        }
        return zzaUe.zzAt();
    }

    public static boolean zzAs() {
        if (zzaUe == null) {
            synchronized (zza.class) {
                if (zzaUe == null) {
                    throw new IllegalStateException("Initialize must be called before isMeasurementExplicitlyDisabled.");
                }
            }
        }
        return zzaUe.zzaUd;
    }

    public static Status zzaR(Context context) {
        zzx.zzb(context, (Object) "Context must not be null.");
        if (zzaUe == null) {
            synchronized (zza.class) {
                if (zzaUe == null) {
                    zzaUe = new zza(context);
                }
            }
        }
        return zzaUe.zzaUb;
    }

    public static Status zzb(Context context, String str, boolean z) {
        zzx.zzb(context, (Object) "Context must not be null.");
        zzx.zzh(str, "App ID must be nonempty.");
        synchronized (zza.class) {
            if (zzaUe != null) {
                Status zzeu = zzaUe.zzeu(str);
                return zzeu;
            }
            zzaUe = new zza(context, str, z);
            return zzaUe.zzaUb;
        }
    }

    /* access modifiers changed from: package-private */
    public String zzAq() {
        return this.zzaUa;
    }

    /* access modifiers changed from: package-private */
    public boolean zzAt() {
        return this.zzaUb.isSuccess() && this.zzaUc;
    }

    /* access modifiers changed from: package-private */
    public Status zzeu(String str) {
        return (this.zzaUa == null || this.zzaUa.equals(str)) ? Status.zzagC : new Status(10, "Initialize was called with two different Google App IDs.  Only the first app ID will be used: '" + this.zzaUa + "'.");
    }
}
