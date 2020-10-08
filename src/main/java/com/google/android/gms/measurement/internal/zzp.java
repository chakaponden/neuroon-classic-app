package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.AppMeasurement;
import com.raizlabs.android.dbflow.sql.language.Condition;

public class zzp extends zzz {
    private final long zzaVj = zzCp().zzBp();
    private final char zzaWB;
    private final zza zzaWC;
    private final zza zzaWD;
    private final zza zzaWE;
    private final zza zzaWF;
    private final zza zzaWG;
    private final zza zzaWH;
    private final zza zzaWI;
    private final zza zzaWJ;
    private final zza zzaWK;
    private final String zzamn = zzCp().zzBz();

    public class zza {
        private final int mPriority;
        private final boolean zzaWN;
        private final boolean zzaWO;

        zza(int i, boolean z, boolean z2) {
            this.mPriority = i;
            this.zzaWN = z;
            this.zzaWO = z2;
        }

        public void zzd(String str, Object obj, Object obj2, Object obj3) {
            zzp.this.zza(this.mPriority, this.zzaWN, this.zzaWO, str, obj, obj2, obj3);
        }

        public void zze(String str, Object obj, Object obj2) {
            zzp.this.zza(this.mPriority, this.zzaWN, this.zzaWO, str, obj, obj2, (Object) null);
        }

        public void zzfg(String str) {
            zzp.this.zza(this.mPriority, this.zzaWN, this.zzaWO, str, (Object) null, (Object) null, (Object) null);
        }

        public void zzj(String str, Object obj) {
            zzp.this.zza(this.mPriority, this.zzaWN, this.zzaWO, str, obj, (Object) null, (Object) null);
        }
    }

    zzp(zzw zzw) {
        super(zzw);
        if (zzCp().zzks()) {
            this.zzaWB = zzCp().zzkr() ? 'P' : 'C';
        } else {
            this.zzaWB = zzCp().zzkr() ? 'p' : 'c';
        }
        this.zzaWC = new zza(6, false, false);
        this.zzaWD = new zza(6, true, false);
        this.zzaWE = new zza(6, false, true);
        this.zzaWF = new zza(5, false, false);
        this.zzaWG = new zza(5, true, false);
        this.zzaWH = new zza(5, false, true);
        this.zzaWI = new zza(4, false, false);
        this.zzaWJ = new zza(3, false, false);
        this.zzaWK = new zza(2, false, false);
    }

    static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String zzc = zzc(z, obj);
        String zzc2 = zzc(z, obj2);
        String zzc3 = zzc(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zzc)) {
            sb.append(str2);
            sb.append(zzc);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzc2)) {
            sb.append(str2);
            sb.append(zzc2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zzc3)) {
            sb.append(str2);
            sb.append(zzc3);
        }
        return sb.toString();
    }

    static String zzc(boolean z, Object obj) {
        StackTraceElement stackTraceElement;
        String className;
        if (obj == null) {
            return "";
        }
        Object valueOf = obj instanceof Integer ? Long.valueOf((long) ((Integer) obj).intValue()) : obj;
        if (valueOf instanceof Long) {
            if (!z) {
                return String.valueOf(valueOf);
            }
            if (Math.abs(((Long) valueOf).longValue()) < 100) {
                return String.valueOf(valueOf);
            }
            String str = String.valueOf(valueOf).charAt(0) == '-' ? Condition.Operation.MINUS : "";
            String valueOf2 = String.valueOf(Math.abs(((Long) valueOf).longValue()));
            return str + Math.round(Math.pow(10.0d, (double) (valueOf2.length() - 1))) + "..." + str + Math.round(Math.pow(10.0d, (double) valueOf2.length()) - 1.0d);
        } else if (valueOf instanceof Boolean) {
            return String.valueOf(valueOf);
        } else {
            if (!(valueOf instanceof Throwable)) {
                return z ? Condition.Operation.MINUS : String.valueOf(valueOf);
            }
            Throwable th = (Throwable) valueOf;
            StringBuilder sb = new StringBuilder(th.toString());
            String zzff = zzff(AppMeasurement.class.getCanonicalName());
            String zzff2 = zzff(zzw.class.getCanonicalName());
            StackTraceElement[] stackTrace = th.getStackTrace();
            int length = stackTrace.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                stackTraceElement = stackTrace[i];
                if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null) {
                    String zzff3 = zzff(className);
                    if (zzff3.equals(zzff) || zzff3.equals(zzff2)) {
                        sb.append(": ");
                        sb.append(stackTraceElement);
                    }
                }
                i++;
            }
            sb.append(": ");
            sb.append(stackTraceElement);
            return sb.toString();
        }
    }

    private static String zzff(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf != -1 ? str.substring(0, lastIndexOf) : str;
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    public zza zzCE() {
        return this.zzaWC;
    }

    public zza zzCF() {
        return this.zzaWF;
    }

    public zza zzCG() {
        return this.zzaWG;
    }

    public zza zzCH() {
        return this.zzaWH;
    }

    public zza zzCI() {
        return this.zzaWI;
    }

    public zza zzCJ() {
        return this.zzaWJ;
    }

    public zza zzCK() {
        return this.zzaWK;
    }

    public String zzCL() {
        Pair<String, Long> zzlN = zzCo().zzaXi.zzlN();
        if (zzlN == null) {
            return null;
        }
        return String.valueOf(zzlN.second) + ":" + ((String) zzlN.first);
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
    public boolean zzQ(int i) {
        return Log.isLoggable(this.zzamn, i);
    }

    /* access modifiers changed from: protected */
    public void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        if (!z && zzQ(i)) {
            zzl(i, zza(false, str, obj, obj2, obj3));
        }
        if (!z2 && i >= 5) {
            zzb(i, str, obj, obj2, obj3);
        }
    }

    public void zzb(int i, String str, Object obj, Object obj2, Object obj3) {
        zzx.zzz(str);
        zzv zzCU = this.zzaTV.zzCU();
        if (zzCU == null) {
            zzl(6, "Scheduler not set. Not logging error/warn.");
        } else if (!zzCU.isInitialized()) {
            zzl(6, "Scheduler not initialized. Not logging error/warn.");
        } else if (zzCU.zzDi()) {
            zzl(6, "Scheduler shutdown. Not logging error/warn.");
        } else {
            if (i < 0) {
                i = 0;
            }
            if (i >= "01VDIWEA?".length()) {
                i = "01VDIWEA?".length() - 1;
            }
            String str2 = "1" + "01VDIWEA?".charAt(i) + this.zzaWB + this.zzaVj + ":" + zza(true, str, obj, obj2, obj3);
            final String substring = str2.length() > 1024 ? str.substring(0, 1024) : str2;
            zzCU.zzg(new Runnable() {
                public void run() {
                    zzt zzCo = zzp.this.zzaTV.zzCo();
                    if (!zzCo.isInitialized() || zzCo.zzDi()) {
                        zzp.this.zzl(6, "Persisted config not initialized . Not logging error/warn.");
                    } else {
                        zzCo.zzaXi.zzbq(substring);
                    }
                }
            });
        }
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

    /* access modifiers changed from: protected */
    public void zzl(int i, String str) {
        Log.println(i, this.zzamn, str);
    }
}
