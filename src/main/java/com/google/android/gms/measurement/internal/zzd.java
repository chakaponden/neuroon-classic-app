package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.common.zzc;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zznf;
import com.google.android.gms.measurement.internal.zzl;
import io.fabric.sdk.android.services.common.AbstractSpiCall;

public class zzd extends zzy {
    static final String zzaVA = String.valueOf(zzc.GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000).replaceAll("(\\d+)(\\d)(\\d\\d)", "$1.$2.$3");
    private Boolean zzRy;

    zzd(zzw zzw) {
        super(zzw);
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    public int zzBA() {
        return 25;
    }

    /* access modifiers changed from: package-private */
    public int zzBB() {
        return 32;
    }

    public int zzBC() {
        return 24;
    }

    /* access modifiers changed from: package-private */
    public int zzBD() {
        return 36;
    }

    /* access modifiers changed from: package-private */
    public int zzBE() {
        return 256;
    }

    public int zzBF() {
        return 36;
    }

    public int zzBG() {
        return 2048;
    }

    /* access modifiers changed from: package-private */
    public int zzBH() {
        return 500;
    }

    public long zzBI() {
        return (long) zzl.zzaWk.get().intValue();
    }

    public long zzBJ() {
        return (long) zzl.zzaWl.get().intValue();
    }

    public long zzBK() {
        return (long) zzl.zzaWm.get().intValue();
    }

    /* access modifiers changed from: package-private */
    public int zzBL() {
        return 25;
    }

    /* access modifiers changed from: package-private */
    public int zzBM() {
        return 50;
    }

    /* access modifiers changed from: package-private */
    public long zzBN() {
        return 3600000;
    }

    /* access modifiers changed from: package-private */
    public long zzBO() {
        return 60000;
    }

    /* access modifiers changed from: package-private */
    public long zzBP() {
        return 61000;
    }

    public long zzBQ() {
        return zzl.zzaWw.get().longValue();
    }

    public long zzBR() {
        return zzl.zzaWs.get().longValue();
    }

    public long zzBS() {
        return 1000;
    }

    public int zzBT() {
        return Math.max(0, zzl.zzaWi.get().intValue());
    }

    public int zzBU() {
        return Math.max(1, zzl.zzaWj.get().intValue());
    }

    public String zzBV() {
        return zzl.zzaWo.get();
    }

    public long zzBW() {
        return zzl.zzaWd.get().longValue();
    }

    public long zzBX() {
        return Math.max(0, zzl.zzaWp.get().longValue());
    }

    public long zzBY() {
        return Math.max(0, zzl.zzaWr.get().longValue());
    }

    public long zzBZ() {
        return zzl.zzaWq.get().longValue();
    }

    public long zzBp() {
        return (long) (zzc.GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000);
    }

    /* access modifiers changed from: package-private */
    public String zzBz() {
        return zzl.zzaWa.get();
    }

    public long zzCa() {
        return Math.max(0, zzl.zzaWt.get().longValue());
    }

    public long zzCb() {
        return Math.max(0, zzl.zzaWu.get().longValue());
    }

    public int zzCc() {
        return Math.min(20, Math.max(0, zzl.zzaWv.get().intValue()));
    }

    public /* bridge */ /* synthetic */ void zzCd() {
        super.zzCd();
    }

    public /* bridge */ /* synthetic */ zzc zzCe() {
        return super.zzCe();
    }

    public /* bridge */ /* synthetic */ zzab zzCf() {
        return super.zzCf();
    }

    public /* bridge */ /* synthetic */ zzn zzCg() {
        return super.zzCg();
    }

    public /* bridge */ /* synthetic */ zzg zzCh() {
        return super.zzCh();
    }

    public /* bridge */ /* synthetic */ zzac zzCi() {
        return super.zzCi();
    }

    public /* bridge */ /* synthetic */ zze zzCj() {
        return super.zzCj();
    }

    public /* bridge */ /* synthetic */ zzaj zzCk() {
        return super.zzCk();
    }

    public /* bridge */ /* synthetic */ zzu zzCl() {
        return super.zzCl();
    }

    public /* bridge */ /* synthetic */ zzad zzCm() {
        return super.zzCm();
    }

    public /* bridge */ /* synthetic */ zzv zzCn() {
        return super.zzCn();
    }

    public /* bridge */ /* synthetic */ zzt zzCo() {
        return super.zzCo();
    }

    public /* bridge */ /* synthetic */ zzd zzCp() {
        return super.zzCp();
    }

    public String zzH(String str, String str2) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(zzl.zzaWe.get()).authority(zzl.zzaWf.get()).path("config/app/" + str).appendQueryParameter("app_instance_id", str2).appendQueryParameter("platform", AbstractSpiCall.ANDROID_CLIENT_TYPE).appendQueryParameter("gmp_version", String.valueOf(zzBp()));
        return builder.build().toString();
    }

    public long zza(String str, zzl.zza<Long> zza) {
        if (str == null) {
            return zza.get().longValue();
        }
        String zzO = zzCl().zzO(str, zza.getKey());
        if (TextUtils.isEmpty(zzO)) {
            return zza.get().longValue();
        }
        try {
            return zza.get(Long.valueOf(Long.valueOf(zzO).longValue())).longValue();
        } catch (NumberFormatException e) {
            return zza.get().longValue();
        }
    }

    public int zzb(String str, zzl.zza<Integer> zza) {
        if (str == null) {
            return zza.get().intValue();
        }
        String zzO = zzCl().zzO(str, zza.getKey());
        if (TextUtils.isEmpty(zzO)) {
            return zza.get().intValue();
        }
        try {
            return zza.get(Integer.valueOf(Integer.valueOf(zzO).intValue())).intValue();
        } catch (NumberFormatException e) {
            return zza.get().intValue();
        }
    }

    /* access modifiers changed from: package-private */
    public long zzeS(String str) {
        return zza(str, zzl.zzaWb);
    }

    /* access modifiers changed from: package-private */
    public int zzeT(String str) {
        return zzb(str, zzl.zzaWx);
    }

    public int zzeU(String str) {
        return zzb(str, zzl.zzaWg);
    }

    public int zzeV(String str) {
        return Math.max(0, zzb(str, zzl.zzaWh));
    }

    public int zzeW(String str) {
        return Math.max(0, Math.min(1000000, zzb(str, zzl.zzaWn)));
    }

    public /* bridge */ /* synthetic */ void zzjj() {
        super.zzjj();
    }

    public /* bridge */ /* synthetic */ void zzjk() {
        super.zzjk();
    }

    public /* bridge */ /* synthetic */ zzmq zzjl() {
        return super.zzjl();
    }

    /* access modifiers changed from: package-private */
    public long zzkM() {
        return zzl.zzaWy.get().longValue();
    }

    public String zzkR() {
        return "google_app_measurement.db";
    }

    public String zzkS() {
        return "google_app_measurement2.db";
    }

    public long zzkX() {
        return Math.max(0, zzl.zzaWc.get().longValue());
    }

    public boolean zzkr() {
        return com.google.android.gms.common.internal.zzd.zzakE;
    }

    public boolean zzks() {
        if (this.zzRy == null) {
            synchronized (this) {
                if (this.zzRy == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String zzi = zznf.zzi(getContext(), Process.myPid());
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzRy = Boolean.valueOf(str != null && str.equals(zzi));
                    }
                    if (this.zzRy == null) {
                        this.zzRy = Boolean.TRUE;
                        zzAo().zzCE().zzfg("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzRy.booleanValue();
    }
}
