package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class zzan extends zzd {
    protected boolean zzPi;
    protected int zzRB;
    protected String zzSE;
    protected String zzSF;
    protected int zzSH;
    protected boolean zzTv;
    protected boolean zzTw;
    protected boolean zzTx;

    public zzan(zzf zzf) {
        super(zzf);
    }

    private static int zzby(String str) {
        String lowerCase = str.toLowerCase();
        if ("verbose".equals(lowerCase)) {
            return 0;
        }
        if ("info".equals(lowerCase)) {
            return 1;
        }
        if ("warning".equals(lowerCase)) {
            return 2;
        }
        return "error".equals(lowerCase) ? 3 : -1;
    }

    public int getLogLevel() {
        zzjv();
        return this.zzRB;
    }

    /* access modifiers changed from: package-private */
    public void zza(zzaa zzaa) {
        int zzby;
        zzbd("Loading global XML config values");
        if (zzaa.zzlf()) {
            String zzlg = zzaa.zzlg();
            this.zzSE = zzlg;
            zzb("XML config - app name", zzlg);
        }
        if (zzaa.zzlh()) {
            String zzli = zzaa.zzli();
            this.zzSF = zzli;
            zzb("XML config - app version", zzli);
        }
        if (zzaa.zzlj() && (zzby = zzby(zzaa.zzlk())) >= 0) {
            this.zzRB = zzby;
            zza("XML config - log level", Integer.valueOf(zzby));
        }
        if (zzaa.zzll()) {
            int zzlm = zzaa.zzlm();
            this.zzSH = zzlm;
            this.zzTw = true;
            zzb("XML config - dispatch period (sec)", Integer.valueOf(zzlm));
        }
        if (zzaa.zzln()) {
            boolean zzlo = zzaa.zzlo();
            this.zzPi = zzlo;
            this.zzTx = true;
            zzb("XML config - dry run", Boolean.valueOf(zzlo));
        }
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        zzmd();
    }

    public String zzlg() {
        zzjv();
        return this.zzSE;
    }

    public String zzli() {
        zzjv();
        return this.zzSF;
    }

    public boolean zzlj() {
        zzjv();
        return this.zzTv;
    }

    public boolean zzll() {
        zzjv();
        return this.zzTw;
    }

    public boolean zzln() {
        zzjv();
        return this.zzTx;
    }

    public boolean zzlo() {
        zzjv();
        return this.zzPi;
    }

    public int zzmc() {
        zzjv();
        return this.zzSH;
    }

    /* access modifiers changed from: protected */
    public void zzmd() {
        ApplicationInfo applicationInfo;
        int i;
        zzaa zzaa;
        Context context = getContext();
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 129);
        } catch (PackageManager.NameNotFoundException e) {
            zzd("PackageManager doesn't know about the app package", e);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            zzbg("Couldn't get ApplicationInfo to load global config");
            return;
        }
        Bundle bundle = applicationInfo.metaData;
        if (bundle != null && (i = bundle.getInt("com.google.android.gms.analytics.globalConfigResource")) > 0 && (zzaa = (zzaa) new zzz(zzji()).zzah(i)) != null) {
            zza(zzaa);
        }
    }
}
