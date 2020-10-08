package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzqa;
import com.google.android.gms.internal.zzsm;
import com.google.android.gms.internal.zzsn;
import java.io.IOException;
import java.util.Map;

public class zzu extends zzz {
    private final Map<String, Map<String, String>> zzaXF = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzaXG = new ArrayMap();
    private final Map<String, zzqa.zzb> zzaXH = new ArrayMap();

    zzu(zzw zzw) {
        super(zzw);
    }

    private Map<String, String> zza(zzqa.zzb zzb) {
        ArrayMap arrayMap = new ArrayMap();
        if (!(zzb == null || zzb.zzaZV == null)) {
            for (zzqa.zzc zzc : zzb.zzaZV) {
                if (zzc != null) {
                    arrayMap.put(zzc.key, zzc.value);
                }
            }
        }
        return arrayMap;
    }

    private Map<String, Boolean> zzb(zzqa.zzb zzb) {
        ArrayMap arrayMap = new ArrayMap();
        if (!(zzb == null || zzb.zzaZW == null)) {
            for (zzqa.zza zza : zzb.zzaZW) {
                if (zza != null) {
                    arrayMap.put(zza.name, zza.zzaZS);
                }
            }
        }
        return arrayMap;
    }

    @WorkerThread
    private zzqa.zzb zzf(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzqa.zzb();
        }
        zzsm zzD = zzsm.zzD(bArr);
        zzqa.zzb zzb = new zzqa.zzb();
        try {
            zzb.mergeFrom(zzD);
            zzAo().zzCK().zze("Parsed config. version, gmp_app_id", zzb.zzaZT, zzb.zzaVt);
            return zzb;
        } catch (IOException e) {
            zzAo().zzCF().zze("Unable to merge remote config", str, e);
            return null;
        }
    }

    @WorkerThread
    private void zzfj(String str) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        if (!this.zzaXH.containsKey(str)) {
            byte[] zzfa = zzCj().zzfa(str);
            if (zzfa == null) {
                this.zzaXF.put(str, (Object) null);
                this.zzaXG.put(str, (Object) null);
                this.zzaXH.put(str, (Object) null);
                return;
            }
            zzqa.zzb zzf = zzf(str, zzfa);
            this.zzaXF.put(str, zza(zzf));
            this.zzaXG.put(str, zzb(zzf));
            this.zzaXH.put(str, zzf);
        }
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
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

    /* access modifiers changed from: package-private */
    @WorkerThread
    public String zzO(String str, String str2) {
        zzjk();
        zzfj(str);
        Map map = this.zzaXF.get(str);
        if (map != null) {
            return (String) map.get(str2);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public boolean zzP(String str, String str2) {
        zzjk();
        zzfj(str);
        Map map = this.zzaXG.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public boolean zze(String str, byte[] bArr) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzqa.zzb zzf = zzf(str, bArr);
        if (zzf == null) {
            return false;
        }
        this.zzaXG.put(str, zzb(zzf));
        this.zzaXH.put(str, zzf);
        this.zzaXF.put(str, zza(zzf));
        zzCe().zza(str, zzf.zzaZX);
        try {
            zzf.zzaZX = null;
            byte[] bArr2 = new byte[zzf.getSerializedSize()];
            zzf.writeTo(zzsn.zzE(bArr2));
            bArr = bArr2;
        } catch (IOException e) {
            zzAo().zzCF().zzj("Unable to serialize reduced-size config.  Storing full config instead.", e);
        }
        zzCj().zzd(str, bArr);
        return true;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public zzqa.zzb zzfk(String str) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzfj(str);
        return this.zzaXH.get(str);
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
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
}
