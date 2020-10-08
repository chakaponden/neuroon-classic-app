package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.zze;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.zza;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;

public class zzn extends zzz {
    private static final X500Principal zzaWz = new X500Principal("CN=Android Debug,O=Android,C=US");
    private String zzSE;
    private String zzSF;
    private String zzaUa;
    private String zzaVd;
    private String zzaVi;
    private long zzaWA;

    zzn(zzw zzw) {
        super(zzw);
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    /* access modifiers changed from: package-private */
    public String zzBk() {
        zzjv();
        return this.zzaVd;
    }

    /* access modifiers changed from: package-private */
    public String zzBo() {
        zzjv();
        return this.zzaVi;
    }

    /* access modifiers changed from: package-private */
    public long zzBp() {
        return zzCp().zzBp();
    }

    /* access modifiers changed from: package-private */
    public long zzBq() {
        zzjv();
        return this.zzaWA;
    }

    /* access modifiers changed from: package-private */
    public boolean zzCD() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(zzaWz);
            }
        } catch (CertificateException e) {
            zzAo().zzCE().zzj("Error obtaining certificate", e);
        } catch (PackageManager.NameNotFoundException e2) {
            zzAo().zzCE().zzj("Package name not found", e2);
        }
        return true;
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

    /* access modifiers changed from: protected */
    public void zzba(Status status) {
        if (status == null) {
            zzAo().zzCE().zzfg("GoogleService failed to initialize (no status)");
        } else {
            zzAo().zzCE().zze("GoogleService failed to initialize, status", Integer.valueOf(status.getStatusCode()), status.getStatusMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public AppMetadata zzfe(String str) {
        return new AppMetadata(zzwK(), zzBk(), zzli(), zzBo(), zzBp(), zzBq(), str, zzCo().zzAr(), !zzCo().zzaXx);
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        boolean z;
        String str = "Unknown";
        String str2 = "Unknown";
        PackageManager packageManager = getContext().getPackageManager();
        String packageName = getContext().getPackageName();
        String installerPackageName = packageManager.getInstallerPackageName(packageName);
        if (installerPackageName == null) {
            installerPackageName = "manual_install";
        } else if (zze.GOOGLE_PLAY_STORE_PACKAGE.equals(installerPackageName)) {
            installerPackageName = "";
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                if (!TextUtils.isEmpty(applicationLabel)) {
                    str2 = applicationLabel.toString();
                }
                str = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            zzAo().zzCE().zzj("Error retrieving package info: appName", str2);
        }
        this.zzaUa = packageName;
        this.zzaVi = installerPackageName;
        this.zzSF = str;
        this.zzSE = str2;
        MessageDigest zzbv = zzaj.zzbv(CommonUtils.MD5_INSTANCE);
        if (zzbv == null) {
            zzAo().zzCE().zzfg("Could not get MD5 instance");
            this.zzaWA = -1;
        } else {
            this.zzaWA = 0;
            try {
                if (!zzCD()) {
                    PackageInfo packageInfo2 = packageManager.getPackageInfo(getContext().getPackageName(), 64);
                    if (packageInfo2.signatures != null && packageInfo2.signatures.length > 0) {
                        this.zzaWA = zzaj.zzq(zzbv.digest(packageInfo2.signatures[0].toByteArray()));
                    }
                }
            } catch (PackageManager.NameNotFoundException e2) {
                zzAo().zzCE().zzj("Package name not found", e2);
            }
        }
        Status zzb = zzCp().zzkr() ? zza.zzb(getContext(), Condition.Operation.MINUS, true) : zza.zzaR(getContext());
        boolean z2 = zzb != null && zzb.isSuccess();
        if (!z2) {
            zzba(zzb);
        }
        if (z2) {
            z = zza.zzAr();
            if (z) {
                zzAo().zzCK().zzfg("AppMeasurement enabled");
            } else {
                zzAo().zzCI().zzfg("AppMeasurement disabled with google_app_measurement_enable=0");
            }
        } else {
            z = false;
        }
        this.zzaVd = "";
        if (!zzCp().zzkr()) {
            try {
                String zzAp = zza.zzAp();
                if (TextUtils.isEmpty(zzAp)) {
                    zzAp = "";
                }
                this.zzaVd = zzAp;
                if (z) {
                    zzAo().zzCK().zze("App package, google app id", this.zzaUa, this.zzaVd);
                }
            } catch (IllegalStateException e3) {
                zzAo().zzCE().zzj("getGoogleAppId or isMeasurementEnabled failed with exception", e3);
            }
        }
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
    public String zzli() {
        zzjv();
        return this.zzSF;
    }

    /* access modifiers changed from: package-private */
    public String zzwK() {
        zzjv();
        return this.zzaUa;
    }
}
