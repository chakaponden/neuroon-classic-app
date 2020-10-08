package com.google.android.gms.tagmanager;

import android.net.Uri;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

class zzcb {
    private static zzcb zzbjQ;
    private volatile String zzbhM;
    private volatile zza zzbjR;
    private volatile String zzbjS;
    private volatile String zzbjT;

    enum zza {
        NONE,
        CONTAINER,
        CONTAINER_DEBUG
    }

    zzcb() {
        clear();
    }

    static zzcb zzGU() {
        zzcb zzcb;
        synchronized (zzcb.class) {
            if (zzbjQ == null) {
                zzbjQ = new zzcb();
            }
            zzcb = zzbjQ;
        }
        return zzcb;
    }

    private String zzgk(String str) {
        return str.split("&")[0].split(Condition.Operation.EQUALS)[1];
    }

    private String zzq(Uri uri) {
        return uri.getQuery().replace("&gtm_debug=x", "");
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        this.zzbjR = zza.NONE;
        this.zzbjS = null;
        this.zzbhM = null;
        this.zzbjT = null;
    }

    /* access modifiers changed from: package-private */
    public String getContainerId() {
        return this.zzbhM;
    }

    /* access modifiers changed from: package-private */
    public zza zzGV() {
        return this.zzbjR;
    }

    /* access modifiers changed from: package-private */
    public String zzGW() {
        return this.zzbjS;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean zzp(Uri uri) {
        boolean z = true;
        synchronized (this) {
            try {
                String decode = URLDecoder.decode(uri.toString(), HttpRequest.CHARSET_UTF8);
                if (decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_auth=\\S+&gtm_preview=\\d+(&gtm_debug=x)?$")) {
                    zzbg.v("Container preview url: " + decode);
                    if (decode.matches(".*?&gtm_debug=x$")) {
                        this.zzbjR = zza.CONTAINER_DEBUG;
                    } else {
                        this.zzbjR = zza.CONTAINER;
                    }
                    this.zzbjT = zzq(uri);
                    if (this.zzbjR == zza.CONTAINER || this.zzbjR == zza.CONTAINER_DEBUG) {
                        this.zzbjS = "/r?" + this.zzbjT;
                    }
                    this.zzbhM = zzgk(this.zzbjT);
                } else if (!decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_preview=$")) {
                    zzbg.zzaK("Invalid preview uri: " + decode);
                    z = false;
                } else if (zzgk(uri.getQuery()).equals(this.zzbhM)) {
                    zzbg.v("Exit preview mode for container: " + this.zzbhM);
                    this.zzbjR = zza.NONE;
                    this.zzbjS = null;
                } else {
                    z = false;
                }
            } catch (UnsupportedEncodingException e) {
                z = false;
            }
        }
        return z;
    }
}
