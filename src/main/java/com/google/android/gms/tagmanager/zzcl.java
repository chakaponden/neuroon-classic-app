package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzrv;
import com.google.android.gms.internal.zzrw;
import com.google.android.gms.tagmanager.zzbf;
import com.google.android.gms.tagmanager.zzcb;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

class zzcl implements Runnable {
    private final Context mContext;
    private final String zzbhM;
    private volatile String zzbij;
    private final zzrw zzbke;
    private final String zzbkf;
    private zzbf<zzaf.zzj> zzbkg;
    private volatile zzs zzbkh;
    private volatile String zzbki;

    zzcl(Context context, String str, zzrw zzrw, zzs zzs) {
        this.mContext = context;
        this.zzbke = zzrw;
        this.zzbhM = str;
        this.zzbkh = zzs;
        this.zzbkf = "/r?id=" + str;
        this.zzbij = this.zzbkf;
        this.zzbki = null;
    }

    public zzcl(Context context, String str, zzs zzs) {
        this(context, str, new zzrw(), zzs);
    }

    private boolean zzGX() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        zzbg.v("...no network connectivity");
        return false;
    }

    private void zzGY() {
        if (!zzGX()) {
            this.zzbkg.zza(zzbf.zza.NOT_AVAILABLE);
            return;
        }
        zzbg.v("Start loading resource from network ...");
        String zzGZ = zzGZ();
        zzrv zzIa = this.zzbke.zzIa();
        try {
            InputStream zzgI = zzIa.zzgI(zzGZ);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zzrs.zzb(zzgI, byteArrayOutputStream);
                zzaf.zzj zzd = zzaf.zzj.zzd(byteArrayOutputStream.toByteArray());
                zzbg.v("Successfully loaded supplemented resource: " + zzd);
                if (zzd.zzju == null && zzd.zzjt.length == 0) {
                    zzbg.v("No change for container: " + this.zzbhM);
                }
                this.zzbkg.zzI(zzd);
                zzIa.close();
                zzbg.v("Load resource from network finished.");
            } catch (IOException e) {
                zzbg.zzd("Error when parsing downloaded resources from url: " + zzGZ + " " + e.getMessage(), e);
                this.zzbkg.zza(zzbf.zza.SERVER_ERROR);
            }
        } catch (FileNotFoundException e2) {
            zzbg.zzaK("No data is retrieved from the given url: " + zzGZ + ". Make sure container_id: " + this.zzbhM + " is correct.");
            this.zzbkg.zza(zzbf.zza.SERVER_ERROR);
        } catch (IOException e3) {
            zzbg.zzd("Error when loading resources from url: " + zzGZ + " " + e3.getMessage(), e3);
            this.zzbkg.zza(zzbf.zza.IO_ERROR);
        } finally {
            zzIa.close();
        }
    }

    public void run() {
        if (this.zzbkg == null) {
            throw new IllegalStateException("callback must be set before execute");
        }
        this.zzbkg.zzGk();
        zzGY();
    }

    /* access modifiers changed from: package-private */
    public String zzGZ() {
        String str = this.zzbkh.zzGm() + this.zzbij + "&v=a65833898";
        if (this.zzbki != null && !this.zzbki.trim().equals("")) {
            str = str + "&pv=" + this.zzbki;
        }
        return zzcb.zzGU().zzGV().equals(zzcb.zza.CONTAINER_DEBUG) ? str + "&gtm_debug=x" : str;
    }

    /* access modifiers changed from: package-private */
    public void zza(zzbf<zzaf.zzj> zzbf) {
        this.zzbkg = zzbf;
    }

    /* access modifiers changed from: package-private */
    public void zzfW(String str) {
        if (str == null) {
            this.zzbij = this.zzbkf;
            return;
        }
        zzbg.zzaI("Setting CTFE URL path: " + str);
        this.zzbij = str;
    }

    /* access modifiers changed from: package-private */
    public void zzgl(String str) {
        zzbg.zzaI("Setting previous container version: " + str);
        this.zzbki = str;
    }
}
