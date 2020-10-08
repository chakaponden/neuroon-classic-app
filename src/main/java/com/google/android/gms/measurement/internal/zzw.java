package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzqa;
import com.google.android.gms.internal.zzqb;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.AppMeasurementReceiver;
import com.google.android.gms.measurement.AppMeasurementService;
import com.google.android.gms.measurement.internal.zze;
import com.google.android.gms.measurement.internal.zzq;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class zzw {
    private static zzaa zzaXV;
    private static volatile zzw zzaXW;
    private final Context mContext;
    private final boolean zzQk;
    private final zzd zzaXX;
    private final zzt zzaXY;
    private final zzp zzaXZ;
    private final zzv zzaYa;
    private final zzad zzaYb;
    private final zzu zzaYc;
    private final AppMeasurement zzaYd;
    private final zzaj zzaYe;
    private final zze zzaYf;
    private final zzq zzaYg;
    private final zzac zzaYh;
    private final zzg zzaYi;
    private final zzab zzaYj;
    private final zzn zzaYk;
    private final zzr zzaYl;
    private final zzag zzaYm;
    private final zzc zzaYn;
    private Boolean zzaYo;
    private List<Long> zzaYp;
    private int zzaYq;
    private int zzaYr;
    private final zzmq zzqW;

    private class zza implements zze.zzb {
        zzqb.zze zzaYt;
        List<Long> zzaYu;
        long zzaYv;
        List<zzqb.zzb> zzpH;

        private zza() {
        }

        private long zza(zzqb.zzb zzb) {
            return ((zzb.zzbaf.longValue() / 1000) / 60) / 60;
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.zzpH == null || this.zzpH.isEmpty();
        }

        public boolean zza(long j, zzqb.zzb zzb) {
            zzx.zzz(zzb);
            if (this.zzpH == null) {
                this.zzpH = new ArrayList();
            }
            if (this.zzaYu == null) {
                this.zzaYu = new ArrayList();
            }
            if (this.zzpH.size() > 0 && zza(this.zzpH.get(0)) != zza(zzb)) {
                return false;
            }
            long serializedSize = this.zzaYv + ((long) zzb.getSerializedSize());
            if (serializedSize >= ((long) zzw.this.zzCp().zzBT())) {
                return false;
            }
            this.zzaYv = serializedSize;
            this.zzpH.add(zzb);
            this.zzaYu.add(Long.valueOf(j));
            return this.zzpH.size() < zzw.this.zzCp().zzBU();
        }

        public void zzc(zzqb.zze zze) {
            zzx.zzz(zze);
            this.zzaYt = zze;
        }
    }

    zzw(zzaa zzaa) {
        zzx.zzz(zzaa);
        this.mContext = zzaa.mContext;
        this.zzqW = zzaa.zzl(this);
        this.zzaXX = zzaa.zza(this);
        zzt zzb = zzaa.zzb(this);
        zzb.zza();
        this.zzaXY = zzb;
        zzp zzc = zzaa.zzc(this);
        zzc.zza();
        this.zzaXZ = zzc;
        zzAo().zzCI().zzj("App measurement is starting up, version", Long.valueOf(zzCp().zzBp()));
        zzAo().zzCI().zzfg("To enable debug logging run: adb shell setprop log.tag.GMPM VERBOSE");
        zzAo().zzCJ().zzfg("Debug logging enabled");
        this.zzaYe = zzaa.zzi(this);
        zzg zzn = zzaa.zzn(this);
        zzn.zza();
        this.zzaYi = zzn;
        zzn zzo = zzaa.zzo(this);
        zzo.zza();
        this.zzaYk = zzo;
        zze zzj = zzaa.zzj(this);
        zzj.zza();
        this.zzaYf = zzj;
        zzc zzr = zzaa.zzr(this);
        zzr.zza();
        this.zzaYn = zzr;
        zzq zzk = zzaa.zzk(this);
        zzk.zza();
        this.zzaYg = zzk;
        zzac zzm = zzaa.zzm(this);
        zzm.zza();
        this.zzaYh = zzm;
        zzab zzh = zzaa.zzh(this);
        zzh.zza();
        this.zzaYj = zzh;
        zzag zzq = zzaa.zzq(this);
        zzq.zza();
        this.zzaYm = zzq;
        this.zzaYl = zzaa.zzp(this);
        this.zzaYd = zzaa.zzg(this);
        zzad zze = zzaa.zze(this);
        zze.zza();
        this.zzaYb = zze;
        zzu zzf = zzaa.zzf(this);
        zzf.zza();
        this.zzaYc = zzf;
        zzv zzd = zzaa.zzd(this);
        zzd.zza();
        this.zzaYa = zzd;
        if (this.zzaYq != this.zzaYr) {
            zzAo().zzCE().zze("Not all components initialized", Integer.valueOf(this.zzaYq), Integer.valueOf(this.zzaYr));
        }
        this.zzQk = true;
        if (!this.zzaXX.zzkr() && !zzCZ()) {
            if (!(this.mContext.getApplicationContext() instanceof Application)) {
                zzAo().zzCF().zzfg("Application context is not an Application");
            } else if (Build.VERSION.SDK_INT >= 14) {
                zzCf().zzDk();
            } else {
                zzAo().zzCJ().zzfg("Not tracking deep linking pre-ICS");
            }
        }
        this.zzaYa.zzg(new Runnable() {
            public void run() {
                zzw.this.start();
            }
        });
    }

    private void zzA(List<Long> list) {
        zzx.zzac(!list.isEmpty());
        if (this.zzaYp != null) {
            zzAo().zzCE().zzfg("Set uploading progress before finishing the previous upload");
        } else {
            this.zzaYp = new ArrayList(list);
        }
    }

    @WorkerThread
    private boolean zzDb() {
        zzjk();
        return this.zzaYp != null;
    }

    private boolean zzDd() {
        zzjk();
        zzjv();
        return zzCj().zzCv() || !TextUtils.isEmpty(zzCj().zzCq());
    }

    @WorkerThread
    private void zzDe() {
        zzjk();
        zzjv();
        if (!zzCS() || !zzDd()) {
            zzCX().unregister();
            zzCY().cancel();
            return;
        }
        long zzDf = zzDf();
        if (zzDf == 0) {
            zzCX().unregister();
            zzCY().cancel();
        } else if (!zzCW().zzlB()) {
            zzCX().zzly();
            zzCY().cancel();
        } else {
            long j = zzCo().zzaXl.get();
            long zzBX = zzCp().zzBX();
            if (!zzCk().zzc(j, zzBX)) {
                zzDf = Math.max(zzDf, j + zzBX);
            }
            zzCX().unregister();
            long currentTimeMillis = zzDf - zzjl().currentTimeMillis();
            if (currentTimeMillis <= 0) {
                zzCY().zzt(1);
                return;
            }
            zzAo().zzCK().zzj("Upload scheduled in approximately ms", Long.valueOf(currentTimeMillis));
            zzCY().zzt(currentTimeMillis);
        }
    }

    private long zzDf() {
        long currentTimeMillis = zzjl().currentTimeMillis();
        long zzCa = zzCp().zzCa();
        long zzBY = zzCp().zzBY();
        long j = zzCo().zzaXj.get();
        long j2 = zzCo().zzaXk.get();
        long max = Math.max(zzCj().zzCt(), zzCj().zzCu());
        if (max == 0) {
            return 0;
        }
        long abs = currentTimeMillis - Math.abs(max - currentTimeMillis);
        long abs2 = currentTimeMillis - Math.abs(j2 - currentTimeMillis);
        long max2 = Math.max(currentTimeMillis - Math.abs(j - currentTimeMillis), abs2);
        long j3 = zzCa + abs;
        if (!zzCk().zzc(max2, zzBY)) {
            j3 = max2 + zzBY;
        }
        if (abs2 == 0 || abs2 < abs) {
            return j3;
        }
        for (int i = 0; i < zzCp().zzCc(); i++) {
            j3 += ((long) (1 << i)) * zzCp().zzCb();
            if (j3 > abs2) {
                return j3;
            }
        }
        return 0;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    @WorkerThread
    public void zza(int i, Throwable th, byte[] bArr) {
        boolean z = false;
        zzjk();
        zzjv();
        if (bArr == null) {
            bArr = new byte[0];
        }
        List<Long> list = this.zzaYp;
        this.zzaYp = null;
        if ((i == 200 || i == 204) && th == null) {
            zzCo().zzaXj.set(zzjl().currentTimeMillis());
            zzCo().zzaXk.set(0);
            zzDe();
            zzAo().zzCK().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
            zzCj().beginTransaction();
            try {
                for (Long longValue : list) {
                    zzCj().zzZ(longValue.longValue());
                }
                zzCj().setTransactionSuccessful();
                zzCj().endTransaction();
                if (!zzCW().zzlB() || !zzDd()) {
                    zzDe();
                } else {
                    zzDc();
                }
            } catch (Throwable th2) {
                zzCj().endTransaction();
                throw th2;
            }
        } else {
            zzAo().zzCK().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzCo().zzaXk.set(zzjl().currentTimeMillis());
            if (i == 503 || i == 429) {
                z = true;
            }
            if (z) {
                zzCo().zzaXl.set(zzjl().currentTimeMillis());
            }
            zzDe();
        }
    }

    private void zza(zzy zzy) {
        if (zzy == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private void zza(zzz zzz) {
        if (zzz == null) {
            throw new IllegalStateException("Component not created");
        } else if (!zzz.isInitialized()) {
            throw new IllegalStateException("Component not initialized");
        }
    }

    private zzqb.zza[] zza(String str, zzqb.zzg[] zzgArr, zzqb.zzb[] zzbArr) {
        zzx.zzcM(str);
        return zzCe().zza(str, zzbArr, zzgArr);
    }

    public static zzw zzaT(Context context) {
        zzx.zzz(context);
        zzx.zzz(context.getApplicationContext());
        if (zzaXW == null) {
            synchronized (zzw.class) {
                if (zzaXW == null) {
                    zzaXW = (zzaXV != null ? zzaXV : new zzaa(context)).zzDj();
                }
            }
        }
        return zzaXW;
    }

    private void zzb(Bundle bundle, int i) {
        if (bundle.getLong("_err") == 0) {
            bundle.putLong("_err", (long) i);
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void zzb(String str, int i, Throwable th, byte[] bArr) {
        boolean z = false;
        zzjk();
        zzjv();
        zzx.zzcM(str);
        if (bArr == null) {
            bArr = new byte[0];
        }
        zzCj().beginTransaction();
        zza zzeY = zzCj().zzeY(str);
        if (((i == 200 || i == 204 || i == 304) && th == null) || i == 404) {
            if (i == 404 || i == 304) {
                if (zzCl().zzfk(str) == null && !zzCl().zze(str, (byte[]) null)) {
                    zzCj().endTransaction();
                    return;
                }
            } else if (!zzCl().zze(str, bArr)) {
                zzCj().endTransaction();
                return;
            }
            try {
                zzeY.zzT(zzjl().currentTimeMillis());
                zzCj().zza(zzeY);
                if (i == 404) {
                    zzAo().zzCF().zzfg("Config not found. Using empty config");
                } else {
                    zzAo().zzCK().zze("Successfully fetched config. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                }
                if (!zzCW().zzlB() || !zzDd()) {
                    zzDe();
                } else {
                    zzDc();
                }
            } catch (Throwable th2) {
                zzCj().endTransaction();
                throw th2;
            }
        } else {
            zzeY.zzU(zzjl().currentTimeMillis());
            zzCj().zza(zzeY);
            zzAo().zzCK().zze("Fetching config failed. code, error", Integer.valueOf(i), th);
            zzCo().zzaXk.set(zzjl().currentTimeMillis());
            if (i == 503 || i == 429) {
                z = true;
            }
            if (z) {
                zzCo().zzaXl.set(zzjl().currentTimeMillis());
            }
            zzDe();
        }
        zzCj().setTransactionSuccessful();
        zzCj().endTransaction();
    }

    @WorkerThread
    private void zze(AppMetadata appMetadata) {
        boolean z = true;
        zzjk();
        zzjv();
        zzx.zzz(appMetadata);
        zzx.zzcM(appMetadata.packageName);
        zza zzeY = zzCj().zzeY(appMetadata.packageName);
        String zzfi = zzCo().zzfi(appMetadata.packageName);
        boolean z2 = false;
        if (zzeY == null) {
            zza zza2 = new zza(this, appMetadata.packageName);
            zza2.zzeM(zzCo().zzCM());
            zza2.zzeO(zzfi);
            zzeY = zza2;
            z2 = true;
        } else if (!zzfi.equals(zzeY.zzBl())) {
            zzeY.zzeO(zzfi);
            zzeY.zzeM(zzCo().zzCM());
            z2 = true;
        }
        if (!TextUtils.isEmpty(appMetadata.zzaVt) && !appMetadata.zzaVt.equals(zzeY.zzBk())) {
            zzeY.zzeN(appMetadata.zzaVt);
            z2 = true;
        }
        if (!(appMetadata.zzaVv == 0 || appMetadata.zzaVv == zzeY.zzBp())) {
            zzeY.zzQ(appMetadata.zzaVv);
            z2 = true;
        }
        if (!TextUtils.isEmpty(appMetadata.zzaMV) && !appMetadata.zzaMV.equals(zzeY.zzli())) {
            zzeY.setAppVersion(appMetadata.zzaMV);
            z2 = true;
        }
        if (!TextUtils.isEmpty(appMetadata.zzaVu) && !appMetadata.zzaVu.equals(zzeY.zzBo())) {
            zzeY.zzeP(appMetadata.zzaVu);
            z2 = true;
        }
        if (appMetadata.zzaVw != zzeY.zzBq()) {
            zzeY.zzR(appMetadata.zzaVw);
            z2 = true;
        }
        if (appMetadata.zzaVy != zzeY.zzAr()) {
            zzeY.setMeasurementEnabled(appMetadata.zzaVy);
        } else {
            z = z2;
        }
        if (z) {
            zzCj().zza(zzeY);
        }
    }

    private boolean zzg(String str, long j) {
        int i;
        zzCj().beginTransaction();
        try {
            zza zza2 = new zza();
            zzCj().zza(str, j, (zze.zzb) zza2);
            if (!zza2.isEmpty()) {
                zzqb.zze zze = zza2.zzaYt;
                zze.zzbam = new zzqb.zzb[zza2.zzpH.size()];
                int i2 = 0;
                int i3 = 0;
                while (i3 < zza2.zzpH.size()) {
                    if (zzCl().zzP(zza2.zzaYt.appId, zza2.zzpH.get(i3).name)) {
                        zzAo().zzCK().zzj("Dropping blacklisted raw event", zza2.zzpH.get(i3).name);
                        i = i2;
                    } else {
                        zze.zzbam[i2] = zza2.zzpH.get(i3);
                        i = i2 + 1;
                    }
                    i3++;
                    i2 = i;
                }
                if (i2 < zza2.zzpH.size()) {
                    zze.zzbam = (zzqb.zzb[]) Arrays.copyOf(zze.zzbam, i2);
                }
                zze.zzbaF = zza(zza2.zzaYt.appId, zza2.zzaYt.zzban, zze.zzbam);
                zze.zzbap = zze.zzbam[0].zzbaf;
                zze.zzbaq = zze.zzbam[0].zzbaf;
                for (int i4 = 1; i4 < zze.zzbam.length; i4++) {
                    zzqb.zzb zzb = zze.zzbam[i4];
                    if (zzb.zzbaf.longValue() < zze.zzbap.longValue()) {
                        zze.zzbap = zzb.zzbaf;
                    }
                    if (zzb.zzbaf.longValue() > zze.zzbaq.longValue()) {
                        zze.zzbaq = zzb.zzbaf;
                    }
                }
                String str2 = zza2.zzaYt.appId;
                zza zzeY = zzCj().zzeY(str2);
                if (zzeY == null) {
                    zzAo().zzCE().zzfg("Bundling raw events w/o app info");
                } else {
                    long zzBn = zzeY.zzBn();
                    zze.zzbas = zzBn != 0 ? Long.valueOf(zzBn) : null;
                    long zzBm = zzeY.zzBm();
                    if (zzBm != 0) {
                        zzBn = zzBm;
                    }
                    zze.zzbar = zzBn != 0 ? Long.valueOf(zzBn) : null;
                    zzeY.zzBu();
                    zze.zzbaD = Integer.valueOf((int) zzeY.zzBr());
                    zzeY.zzO(zze.zzbap.longValue());
                    zzeY.zzP(zze.zzbaq.longValue());
                    zzCj().zza(zzeY);
                }
                zze.zzaVx = zzAo().zzCL();
                zzCj().zza(zze);
                zzCj().zzz(zza2.zzaYu);
                zzCj().zzfc(str2);
                zzCj().setTransactionSuccessful();
                return true;
            }
            zzCj().setTransactionSuccessful();
            zzCj().endTransaction();
            return false;
        } finally {
            zzCj().endTransaction();
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void start() {
        zzjk();
        if (!zzCZ() || (this.zzaYa.isInitialized() && !this.zzaYa.zzDi())) {
            zzCj().zzCr();
            if (!zzCS()) {
                if (zzCo().zzAr()) {
                    if (!zzCk().zzbk("android.permission.INTERNET")) {
                        zzAo().zzCE().zzfg("App is missing INTERNET permission");
                    }
                    if (!zzCk().zzbk("android.permission.ACCESS_NETWORK_STATE")) {
                        zzAo().zzCE().zzfg("App is missing ACCESS_NETWORK_STATE permission");
                    }
                    if (!AppMeasurementReceiver.zzY(getContext())) {
                        zzAo().zzCE().zzfg("AppMeasurementReceiver not registered/enabled");
                    }
                    if (!AppMeasurementService.zzZ(getContext())) {
                        zzAo().zzCE().zzfg("AppMeasurementService not registered/enabled");
                    }
                    zzAo().zzCE().zzfg("Uploading is not possible. App measurement disabled");
                }
            } else if (!zzCp().zzkr() && !zzCZ() && !TextUtils.isEmpty(zzCg().zzBk())) {
                zzCf().zzDl();
            }
            zzDe();
            return;
        }
        zzAo().zzCE().zzfg("Scheduler shutting down before Scion.start() called");
    }

    public zzp zzAo() {
        zza((zzz) this.zzaXZ);
        return this.zzaXZ;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public boolean zzCS() {
        boolean z = true;
        zzjv();
        zzjk();
        if (this.zzaYo == null) {
            this.zzaYo = Boolean.valueOf(zzCk().zzbk("android.permission.INTERNET") && zzCk().zzbk("android.permission.ACCESS_NETWORK_STATE") && AppMeasurementReceiver.zzY(getContext()) && AppMeasurementService.zzZ(getContext()));
            if (this.zzaYo.booleanValue() && !zzCp().zzkr()) {
                if (TextUtils.isEmpty(zzCg().zzBk())) {
                    z = false;
                }
                this.zzaYo = Boolean.valueOf(z);
            }
        }
        return this.zzaYo.booleanValue();
    }

    public zzp zzCT() {
        if (this.zzaXZ == null || !this.zzaXZ.isInitialized()) {
            return null;
        }
        return this.zzaXZ;
    }

    /* access modifiers changed from: package-private */
    public zzv zzCU() {
        return this.zzaYa;
    }

    public AppMeasurement zzCV() {
        return this.zzaYd;
    }

    public zzq zzCW() {
        zza((zzz) this.zzaYg);
        return this.zzaYg;
    }

    public zzr zzCX() {
        if (this.zzaYl != null) {
            return this.zzaYl;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    public zzag zzCY() {
        zza((zzz) this.zzaYm);
        return this.zzaYm;
    }

    /* access modifiers changed from: protected */
    public boolean zzCZ() {
        return false;
    }

    public zzc zzCe() {
        zza((zzz) this.zzaYn);
        return this.zzaYn;
    }

    public zzab zzCf() {
        zza((zzz) this.zzaYj);
        return this.zzaYj;
    }

    public zzn zzCg() {
        zza((zzz) this.zzaYk);
        return this.zzaYk;
    }

    public zzg zzCh() {
        zza((zzz) this.zzaYi);
        return this.zzaYi;
    }

    public zzac zzCi() {
        zza((zzz) this.zzaYh);
        return this.zzaYh;
    }

    public zze zzCj() {
        zza((zzz) this.zzaYf);
        return this.zzaYf;
    }

    public zzaj zzCk() {
        zza((zzy) this.zzaYe);
        return this.zzaYe;
    }

    public zzu zzCl() {
        zza((zzz) this.zzaYc);
        return this.zzaYc;
    }

    public zzad zzCm() {
        zza((zzz) this.zzaYb);
        return this.zzaYb;
    }

    public zzv zzCn() {
        zza((zzz) this.zzaYa);
        return this.zzaYa;
    }

    public zzt zzCo() {
        zza((zzy) this.zzaXY);
        return this.zzaXY;
    }

    public zzd zzCp() {
        return this.zzaXX;
    }

    /* access modifiers changed from: package-private */
    public long zzDa() {
        return ((((zzjl().currentTimeMillis() + zzCo().zzCN()) / 1000) / 60) / 60) / 24;
    }

    @WorkerThread
    public void zzDc() {
        zza zzeY;
        String str;
        List<Pair<zzqb.zze, Long>> list;
        ArrayMap arrayMap = null;
        zzjk();
        zzjv();
        if (!zzCp().zzkr()) {
            Boolean zzCP = zzCo().zzCP();
            if (zzCP == null) {
                zzAo().zzCF().zzfg("Upload data called on the client side before use of service was decided");
                return;
            } else if (zzCP.booleanValue()) {
                zzAo().zzCE().zzfg("Upload called in the client side when service should be used");
                return;
            }
        }
        if (zzDb()) {
            zzAo().zzCF().zzfg("Uploading requested multiple times");
        } else if (!zzCW().zzlB()) {
            zzAo().zzCF().zzfg("Network not connected, ignoring upload request");
            zzDe();
        } else {
            long currentTimeMillis = zzjl().currentTimeMillis();
            zzad(currentTimeMillis - zzCp().zzBW());
            long j = zzCo().zzaXj.get();
            if (j != 0) {
                zzAo().zzCJ().zzj("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
            }
            String zzCq = zzCj().zzCq();
            if (!TextUtils.isEmpty(zzCq)) {
                List<Pair<zzqb.zze, Long>> zzn = zzCj().zzn(zzCq, zzCp().zzeU(zzCq), zzCp().zzeV(zzCq));
                if (!zzn.isEmpty()) {
                    Iterator<Pair<zzqb.zze, Long>> it = zzn.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            str = null;
                            break;
                        }
                        zzqb.zze zze = (zzqb.zze) it.next().first;
                        if (!TextUtils.isEmpty(zze.zzbaz)) {
                            str = zze.zzbaz;
                            break;
                        }
                    }
                    if (str != null) {
                        int i = 0;
                        while (true) {
                            if (i >= zzn.size()) {
                                break;
                            }
                            zzqb.zze zze2 = (zzqb.zze) zzn.get(i).first;
                            if (!TextUtils.isEmpty(zze2.zzbaz) && !zze2.zzbaz.equals(str)) {
                                list = zzn.subList(0, i);
                                break;
                            }
                            i++;
                        }
                    }
                    list = zzn;
                    zzqb.zzd zzd = new zzqb.zzd();
                    zzd.zzbaj = new zzqb.zze[list.size()];
                    ArrayList arrayList = new ArrayList(list.size());
                    for (int i2 = 0; i2 < zzd.zzbaj.length; i2++) {
                        zzd.zzbaj[i2] = (zzqb.zze) list.get(i2).first;
                        arrayList.add(list.get(i2).second);
                        zzd.zzbaj[i2].zzbay = Long.valueOf(zzCp().zzBp());
                        zzd.zzbaj[i2].zzbao = Long.valueOf(currentTimeMillis);
                        zzd.zzbaj[i2].zzbaE = Boolean.valueOf(zzCp().zzkr());
                    }
                    String zzb = zzAo().zzQ(2) ? zzaj.zzb(zzd) : null;
                    byte[] zza2 = zzCk().zza(zzd);
                    String zzBV = zzCp().zzBV();
                    try {
                        URL url = new URL(zzBV);
                        zzA(arrayList);
                        zzCo().zzaXk.set(currentTimeMillis);
                        String str2 = Condition.Operation.EMPTY_PARAM;
                        if (zzd.zzbaj.length > 0) {
                            str2 = zzd.zzbaj[0].appId;
                        }
                        zzAo().zzCK().zzd("Uploading data. app, uncompressed size, data", str2, Integer.valueOf(zza2.length), zzb);
                        zzCW().zza(zzCq, url, zza2, (Map<String, String>) null, new zzq.zza() {
                            public void zza(String str, int i, Throwable th, byte[] bArr) {
                                zzw.this.zza(i, th, bArr);
                            }
                        });
                    } catch (MalformedURLException e) {
                        zzAo().zzCE().zzj("Failed to parse upload URL. Not uploading", zzBV);
                    }
                }
            } else {
                String zzaa = zzCj().zzaa(currentTimeMillis - zzCp().zzBW());
                if (!TextUtils.isEmpty(zzaa) && (zzeY = zzCj().zzeY(zzaa)) != null) {
                    String zzH = zzCp().zzH(zzeY.zzBk(), zzeY.zzBj());
                    try {
                        URL url2 = new URL(zzH);
                        zzAo().zzCK().zzj("Fetching remote configuration", zzeY.zzwK());
                        zzqa.zzb zzfk = zzCl().zzfk(zzeY.zzwK());
                        if (!(zzfk == null || zzfk.zzaZT == null)) {
                            arrayMap = new ArrayMap();
                            arrayMap.put("Config-Version", String.valueOf(zzfk.zzaZT));
                        }
                        zzCW().zza(zzaa, url2, arrayMap, new zzq.zza() {
                            public void zza(String str, int i, Throwable th, byte[] bArr) {
                                zzw.this.zzb(str, i, th, bArr);
                            }
                        });
                    } catch (MalformedURLException e2) {
                        zzAo().zzCE().zzj("Failed to parse config URL. Not fetching", zzH);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zzDg() {
        this.zzaYr++;
    }

    /* access modifiers changed from: package-private */
    public void zzE(String str, int i) {
    }

    public void zzJ(boolean z) {
        zzDe();
    }

    /* access modifiers changed from: package-private */
    public void zza(EventParcel eventParcel, String str) {
        zza zzeY = zzCj().zzeY(str);
        if (zzeY == null || TextUtils.isEmpty(zzeY.zzli())) {
            zzAo().zzCJ().zzj("No app data available; dropping event", str);
            return;
        }
        try {
            String str2 = getContext().getPackageManager().getPackageInfo(str, 0).versionName;
            if (zzeY.zzli() != null && !zzeY.zzli().equals(str2)) {
                zzAo().zzCF().zzj("App version does not match; dropping event", str);
                return;
            }
        } catch (PackageManager.NameNotFoundException e) {
            zzAo().zzCF().zzj("Could not find package", str);
        }
        zzb(eventParcel, new AppMetadata(str, zzeY.zzBk(), zzeY.zzli(), zzeY.zzBo(), zzeY.zzBp(), zzeY.zzBq(), (String) null, zzeY.zzAr(), false));
    }

    /* access modifiers changed from: package-private */
    public void zza(zzh zzh, AppMetadata appMetadata) {
        zzjk();
        zzjv();
        zzx.zzz(zzh);
        zzx.zzz(appMetadata);
        zzx.zzcM(zzh.zzaUa);
        zzx.zzac(zzh.zzaUa.equals(appMetadata.packageName));
        zzqb.zze zze = new zzqb.zze();
        zze.zzbal = 1;
        zze.zzbat = AbstractSpiCall.ANDROID_CLIENT_TYPE;
        zze.appId = appMetadata.packageName;
        zze.zzaVu = appMetadata.zzaVu;
        zze.zzaMV = appMetadata.zzaMV;
        zze.zzbax = Long.valueOf(appMetadata.zzaVv);
        zze.zzaVt = appMetadata.zzaVt;
        zze.zzbaC = appMetadata.zzaVw == 0 ? null : Long.valueOf(appMetadata.zzaVw);
        Pair<String, Boolean> zzfh = zzCo().zzfh(appMetadata.packageName);
        if (!(zzfh == null || zzfh.first == null || zzfh.second == null)) {
            zze.zzbaz = (String) zzfh.first;
            zze.zzbaA = (Boolean) zzfh.second;
        }
        zze.zzbau = zzCh().zzht();
        zze.osVersion = zzCh().zzCy();
        zze.zzbaw = Integer.valueOf((int) zzCh().zzCz());
        zze.zzbav = zzCh().zzCA();
        zze.zzbay = null;
        zze.zzbao = null;
        zze.zzbap = null;
        zze.zzbaq = null;
        zza zzeY = zzCj().zzeY(appMetadata.packageName);
        if (zzeY == null) {
            zzeY = new zza(this, appMetadata.packageName);
            zzeY.zzeM(zzCo().zzCM());
            zzeY.zzeN(appMetadata.zzaVt);
            zzeY.zzeO(zzCo().zzfi(appMetadata.packageName));
            zzeY.zzS(0);
            zzeY.zzO(0);
            zzeY.zzP(0);
            zzeY.setAppVersion(appMetadata.zzaMV);
            zzeY.zzeP(appMetadata.zzaVu);
            zzeY.zzQ(appMetadata.zzaVv);
            zzeY.zzR(appMetadata.zzaVw);
            zzeY.setMeasurementEnabled(appMetadata.zzaVy);
            zzCj().zza(zzeY);
        }
        zze.zzbaB = zzeY.zzBj();
        List<zzai> zzeX = zzCj().zzeX(appMetadata.packageName);
        zze.zzban = new zzqb.zzg[zzeX.size()];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < zzeX.size()) {
                zzqb.zzg zzg = new zzqb.zzg();
                zze.zzban[i2] = zzg;
                zzg.name = zzeX.get(i2).mName;
                zzg.zzbaJ = Long.valueOf(zzeX.get(i2).zzaZp);
                zzCk().zza(zzg, zzeX.get(i2).zzNc);
                i = i2 + 1;
            } else {
                try {
                    zzCj().zza(zzh, zzCj().zzb(zze));
                    return;
                } catch (IOException e) {
                    zzAo().zzCE().zzj("Data loss. Failed to insert raw event metadata", e);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean zzad(long j) {
        return zzg((String) null, j);
    }

    /* access modifiers changed from: package-private */
    public void zzb(EventParcel eventParcel, AppMetadata appMetadata) {
        zzi zzab;
        zzai zzai;
        long nanoTime = System.nanoTime();
        zzjk();
        zzjv();
        String str = appMetadata.packageName;
        zzx.zzcM(str);
        if (!TextUtils.isEmpty(appMetadata.zzaVt)) {
            if (!appMetadata.zzaVy) {
                zze(appMetadata);
            } else if (zzCl().zzP(str, eventParcel.name)) {
                zzAo().zzCK().zzj("Dropping blacklisted event", eventParcel.name);
            } else {
                if (zzAo().zzQ(2)) {
                    zzAo().zzCK().zzj("Logging event", eventParcel);
                }
                zzCj().beginTransaction();
                try {
                    Bundle zzCC = eventParcel.zzaVV.zzCC();
                    zze(appMetadata);
                    if ("_iap".equals(eventParcel.name) || "ecommerce_purchase".equals(eventParcel.name)) {
                        String string = zzCC.getString("currency");
                        long j = zzCC.getLong("value");
                        if (!TextUtils.isEmpty(string) && j > 0) {
                            String upperCase = string.toUpperCase(Locale.US);
                            if (upperCase.matches("[A-Z]{3}")) {
                                String str2 = "_ltv_" + upperCase;
                                zzai zzK = zzCj().zzK(str, str2);
                                if (zzK == null || !(zzK.zzNc instanceof Long)) {
                                    zzCj().zzA(str, zzCp().zzeT(str) - 1);
                                    zzai = new zzai(str, str2, zzjl().currentTimeMillis(), Long.valueOf(j));
                                } else {
                                    zzai = new zzai(str, str2, zzjl().currentTimeMillis(), Long.valueOf(j + ((Long) zzK.zzNc).longValue()));
                                }
                                zzCj().zza(zzai);
                            }
                        }
                    }
                    boolean zzfq = zzaj.zzfq(eventParcel.name);
                    boolean zzI = zzaj.zzI(zzCC);
                    zze.zza zza2 = zzCj().zza(zzDa(), str, zzfq, zzfq && zzI);
                    long zzBI = zza2.zzaVF - zzCp().zzBI();
                    if (zzBI > 0) {
                        if (zzBI % 1000 == 1) {
                            zzAo().zzCF().zzj("Data loss. Too many events logged. count", Long.valueOf(zza2.zzaVF));
                        }
                        zzCj().setTransactionSuccessful();
                        return;
                    }
                    if (zzfq) {
                        long zzBJ = zza2.zzaVE - zzCp().zzBJ();
                        if (zzBJ > 0) {
                            zzE(str, 2);
                            if (zzBJ % 1000 == 1) {
                                zzAo().zzCF().zzj("Data loss. Too many public events logged. count", Long.valueOf(zza2.zzaVE));
                            }
                            zzCj().setTransactionSuccessful();
                            zzCj().endTransaction();
                            return;
                        }
                    }
                    if (zzfq && zzI) {
                        if (zza2.zzaVG - zzCp().zzBK() > 0) {
                            zzCC.remove("_c");
                            zzb(zzCC, 4);
                        }
                    }
                    long zzeZ = zzCj().zzeZ(str);
                    if (zzeZ > 0) {
                        zzAo().zzCF().zzj("Data lost. Too many events stored on disk, deleted", Long.valueOf(zzeZ));
                    }
                    zzh zzh = new zzh(this, eventParcel.zzaVW, str, eventParcel.name, eventParcel.zzaVX, 0, zzCC);
                    zzi zzI2 = zzCj().zzI(str, zzh.mName);
                    if (zzI2 != null) {
                        zzh = zzh.zza(this, zzI2.zzaVR);
                        zzab = zzI2.zzab(zzh.zzaez);
                    } else if (zzCj().zzfd(str) >= ((long) zzCp().zzBH())) {
                        zzAo().zzCF().zze("Too many event names used, ignoring event. name, supported count", zzh.mName, Integer.valueOf(zzCp().zzBH()));
                        zzE(str, 1);
                        zzCj().endTransaction();
                        return;
                    } else {
                        zzab = new zzi(str, zzh.mName, 0, 0, zzh.zzaez);
                    }
                    zzCj().zza(zzab);
                    zza(zzh, appMetadata);
                    zzCj().setTransactionSuccessful();
                    if (zzAo().zzQ(2)) {
                        zzAo().zzCK().zzj("Event recorded", zzh);
                    }
                    zzCj().endTransaction();
                    zzDe();
                    zzAo().zzCK().zzj("Background event processing time, ms", Long.valueOf(((System.nanoTime() - nanoTime) + 500000) / 1000000));
                } finally {
                    zzCj().endTransaction();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzb(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) {
        zzjk();
        zzjv();
        if (!TextUtils.isEmpty(appMetadata.zzaVt)) {
            if (!appMetadata.zzaVy) {
                zze(appMetadata);
                return;
            }
            zzCk().zzfs(userAttributeParcel.name);
            Object zzm = zzCk().zzm(userAttributeParcel.name, userAttributeParcel.getValue());
            if (zzm != null) {
                zzai zzai = new zzai(appMetadata.packageName, userAttributeParcel.name, userAttributeParcel.zzaZm, zzm);
                zzAo().zzCJ().zze("Setting user property", zzai.mName, zzm);
                zzCj().beginTransaction();
                try {
                    zze(appMetadata);
                    boolean zza2 = zzCj().zza(zzai);
                    zzCj().setTransactionSuccessful();
                    if (zza2) {
                        zzAo().zzCJ().zze("User property set", zzai.mName, zzai.zzNc);
                    } else {
                        zzAo().zzCH().zze("Ignoring user property. Value too long", zzai.mName, zzai.zzNc);
                    }
                } finally {
                    zzCj().endTransaction();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zzb(zzz zzz) {
        this.zzaYq++;
    }

    /* access modifiers changed from: package-private */
    public void zzc(AppMetadata appMetadata) {
        zzjk();
        zzjv();
        zzx.zzcM(appMetadata.packageName);
        zze(appMetadata);
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzc(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) {
        zzjk();
        zzjv();
        if (!TextUtils.isEmpty(appMetadata.zzaVt)) {
            if (!appMetadata.zzaVy) {
                zze(appMetadata);
                return;
            }
            zzAo().zzCJ().zzj("Removing user property", userAttributeParcel.name);
            zzCj().beginTransaction();
            try {
                zze(appMetadata);
                zzCj().zzJ(appMetadata.packageName, userAttributeParcel.name);
                zzCj().setTransactionSuccessful();
                zzAo().zzCJ().zzj("User property removed", userAttributeParcel.name);
            } finally {
                zzCj().endTransaction();
            }
        }
    }

    @WorkerThread
    public void zzd(AppMetadata appMetadata) {
        zzjk();
        zzjv();
        zzx.zzz(appMetadata);
        zzx.zzcM(appMetadata.packageName);
        if (!TextUtils.isEmpty(appMetadata.zzaVt)) {
            if (!appMetadata.zzaVy) {
                zze(appMetadata);
                return;
            }
            long currentTimeMillis = zzjl().currentTimeMillis();
            zzCj().beginTransaction();
            try {
                zza zzeY = zzCj().zzeY(appMetadata.packageName);
                if (!(zzeY == null || zzeY.zzli() == null || zzeY.zzli().equals(appMetadata.zzaMV))) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_pv", zzeY.zzli());
                    zzb(new EventParcel("_au", new EventParams(bundle), "auto", currentTimeMillis), appMetadata);
                }
                zze(appMetadata);
                if (zzCj().zzI(appMetadata.packageName, "_f") == null) {
                    zzb(new UserAttributeParcel("_fot", currentTimeMillis, Long.valueOf(((currentTimeMillis / 3600000) + 1) * 3600000), "auto"), appMetadata);
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("_c", 1);
                    zzb(new EventParcel("_f", new EventParams(bundle2), "auto", currentTimeMillis), appMetadata);
                } else if (appMetadata.zzaVz) {
                    zzb(new EventParcel("_cd", new EventParams(new Bundle()), "auto", currentTimeMillis), appMetadata);
                }
                zzCj().setTransactionSuccessful();
            } finally {
                zzCj().endTransaction();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void zzjj() {
        if (zzCp().zzkr()) {
            throw new IllegalStateException("Unexpected call on package side");
        }
    }

    @WorkerThread
    public void zzjk() {
        zzCn().zzjk();
    }

    public zzmq zzjl() {
        return this.zzqW;
    }

    /* access modifiers changed from: package-private */
    public void zzjv() {
        if (!this.zzQk) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }
}
