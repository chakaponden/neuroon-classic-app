package com.google.android.gms.analytics.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.zzg;

public class zzc {
    private final zzf zzQj;

    protected zzc(zzf zzf) {
        zzx.zzz(zzf);
        this.zzQj = zzf;
    }

    private void zza(int i, String str, Object obj, Object obj2, Object obj3) {
        zzaf zzaf = null;
        if (this.zzQj != null) {
            zzaf = this.zzQj.zzjy();
        }
        if (zzaf != null) {
            zzaf.zza(i, str, obj, obj2, obj3);
            return;
        }
        String str2 = zzy.zzRL.get();
        if (Log.isLoggable(str2, i)) {
            Log.println(i, str2, zzc(str, obj, obj2, obj3));
        }
    }

    protected static String zzc(String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String zzj = zzj(obj);
        String zzj2 = zzj(obj2);
        String zzj3 = zzj(obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zzj)) {
            sb.append(str2);
            sb.append(zzj);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzj2)) {
            sb.append(str2);
            sb.append(zzj2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzj3)) {
            sb.append(str2);
            sb.append(zzj3);
        }
        return sb.toString();
    }

    private static String zzj(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (!(obj instanceof Boolean)) {
            return obj instanceof Throwable ? ((Throwable) obj).toString() : obj.toString();
        }
        return obj == Boolean.TRUE ? "true" : "false";
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.zzQj.getContext();
    }

    public void zza(String str, Object obj) {
        zza(2, str, obj, (Object) null, (Object) null);
    }

    public void zza(String str, Object obj, Object obj2) {
        zza(2, str, obj, obj2, (Object) null);
    }

    public void zza(String str, Object obj, Object obj2, Object obj3) {
        zza(3, str, obj, obj2, obj3);
    }

    public void zzb(String str, Object obj) {
        zza(3, str, obj, (Object) null, (Object) null);
    }

    public void zzb(String str, Object obj, Object obj2) {
        zza(3, str, obj, obj2, (Object) null);
    }

    public void zzb(String str, Object obj, Object obj2, Object obj3) {
        zza(5, str, obj, obj2, obj3);
    }

    public void zzbd(String str) {
        zza(2, str, (Object) null, (Object) null, (Object) null);
    }

    public void zzbe(String str) {
        zza(3, str, (Object) null, (Object) null, (Object) null);
    }

    public void zzbf(String str) {
        zza(4, str, (Object) null, (Object) null, (Object) null);
    }

    public void zzbg(String str) {
        zza(5, str, (Object) null, (Object) null, (Object) null);
    }

    public void zzbh(String str) {
        zza(6, str, (Object) null, (Object) null, (Object) null);
    }

    public void zzc(String str, Object obj) {
        zza(4, str, obj, (Object) null, (Object) null);
    }

    public void zzc(String str, Object obj, Object obj2) {
        zza(5, str, obj, obj2, (Object) null);
    }

    public void zzd(String str, Object obj) {
        zza(5, str, obj, (Object) null, (Object) null);
    }

    public void zzd(String str, Object obj, Object obj2) {
        zza(6, str, obj, obj2, (Object) null);
    }

    public void zze(String str, Object obj) {
        zza(6, str, obj, (Object) null, (Object) null);
    }

    public boolean zzhp() {
        return Log.isLoggable(zzy.zzRL.get(), 2);
    }

    public GoogleAnalytics zziC() {
        return this.zzQj.zzjz();
    }

    /* access modifiers changed from: protected */
    public zzb zziH() {
        return this.zzQj.zziH();
    }

    /* access modifiers changed from: protected */
    public zzan zziI() {
        return this.zzQj.zziI();
    }

    public zzf zzji() {
        return this.zzQj;
    }

    /* access modifiers changed from: protected */
    public void zzjj() {
        if (zzjn().zzkr()) {
            throw new IllegalStateException("Call only supported on the client side");
        }
    }

    /* access modifiers changed from: protected */
    public void zzjk() {
        this.zzQj.zzjk();
    }

    /* access modifiers changed from: protected */
    public zzmq zzjl() {
        return this.zzQj.zzjl();
    }

    /* access modifiers changed from: protected */
    public zzaf zzjm() {
        return this.zzQj.zzjm();
    }

    /* access modifiers changed from: protected */
    public zzr zzjn() {
        return this.zzQj.zzjn();
    }

    /* access modifiers changed from: protected */
    public zzg zzjo() {
        return this.zzQj.zzjo();
    }

    /* access modifiers changed from: protected */
    public zzv zzjp() {
        return this.zzQj.zzjp();
    }

    /* access modifiers changed from: protected */
    public zzai zzjq() {
        return this.zzQj.zzjq();
    }

    /* access modifiers changed from: protected */
    public zzn zzjr() {
        return this.zzQj.zzjC();
    }

    /* access modifiers changed from: protected */
    public zza zzjs() {
        return this.zzQj.zzjB();
    }

    /* access modifiers changed from: protected */
    public zzk zzjt() {
        return this.zzQj.zzjt();
    }

    /* access modifiers changed from: protected */
    public zzu zzju() {
        return this.zzQj.zzju();
    }
}
