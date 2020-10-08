package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.internal.zzx;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Locale;

class zzt extends zzz {
    static final Pair<String, Long> zzaXh = new Pair<>("", 0L);
    /* access modifiers changed from: private */
    public SharedPreferences zzTh;
    public final zzc zzaXi = new zzc("health_monitor", zzCp().zzkX());
    public final zzb zzaXj = new zzb("last_upload", 0);
    public final zzb zzaXk = new zzb("last_upload_attempt", 0);
    public final zzb zzaXl = new zzb("backoff", 0);
    public final zzb zzaXm = new zzb("last_delete_stale", 0);
    public final zzb zzaXn = new zzb("midnight_offset", 0);
    private String zzaXo;
    private boolean zzaXp;
    private long zzaXq;
    /* access modifiers changed from: private */
    public final SecureRandom zzaXr = new SecureRandom();
    public final zzb zzaXs = new zzb("time_before_start", 10000);
    public final zzb zzaXt = new zzb("session_timeout", 1800000);
    public final zza zzaXu = new zza("start_new_session", true);
    public final zzb zzaXv = new zzb("last_pause_time", 0);
    public final zzb zzaXw = new zzb("time_active", 0);
    public boolean zzaXx;

    public final class zza {
        private final boolean zzaXy;
        private boolean zzaXz;
        private boolean zzagf;
        private final String zzvs;

        public zza(String str, boolean z) {
            zzx.zzcM(str);
            this.zzvs = str;
            this.zzaXy = z;
        }

        @WorkerThread
        private void zzCR() {
            if (!this.zzaXz) {
                this.zzaXz = true;
                this.zzagf = zzt.this.zzTh.getBoolean(this.zzvs, this.zzaXy);
            }
        }

        @WorkerThread
        public boolean get() {
            zzCR();
            return this.zzagf;
        }

        @WorkerThread
        public void set(boolean value) {
            SharedPreferences.Editor edit = zzt.this.zzTh.edit();
            edit.putBoolean(this.zzvs, value);
            edit.apply();
            this.zzagf = value;
        }
    }

    public final class zzb {
        private long zzaDV;
        private final long zzaXB;
        private boolean zzaXz;
        private final String zzvs;

        public zzb(String str, long j) {
            zzx.zzcM(str);
            this.zzvs = str;
            this.zzaXB = j;
        }

        @WorkerThread
        private void zzCR() {
            if (!this.zzaXz) {
                this.zzaXz = true;
                this.zzaDV = zzt.this.zzTh.getLong(this.zzvs, this.zzaXB);
            }
        }

        @WorkerThread
        public long get() {
            zzCR();
            return this.zzaDV;
        }

        @WorkerThread
        public void set(long value) {
            SharedPreferences.Editor edit = zzt.this.zzTh.edit();
            edit.putLong(this.zzvs, value);
            edit.apply();
            this.zzaDV = value;
        }
    }

    public final class zzc {
        private final long zzTl;
        final String zzaXC;
        private final String zzaXD;
        private final String zzaXE;

        private zzc(String str, long j) {
            zzx.zzcM(str);
            zzx.zzac(j > 0);
            this.zzaXC = str + ":start";
            this.zzaXD = str + ":count";
            this.zzaXE = str + ":value";
            this.zzTl = j;
        }

        @WorkerThread
        private void zzlL() {
            zzt.this.zzjk();
            long currentTimeMillis = zzt.this.zzjl().currentTimeMillis();
            SharedPreferences.Editor edit = zzt.this.zzTh.edit();
            edit.remove(this.zzaXD);
            edit.remove(this.zzaXE);
            edit.putLong(this.zzaXC, currentTimeMillis);
            edit.apply();
        }

        @WorkerThread
        private long zzlM() {
            zzt.this.zzjk();
            long zzlO = zzlO();
            if (zzlO != 0) {
                return Math.abs(zzlO - zzt.this.zzjl().currentTimeMillis());
            }
            zzlL();
            return 0;
        }

        @WorkerThread
        private long zzlO() {
            return zzt.this.zzCO().getLong(this.zzaXC, 0);
        }

        @WorkerThread
        public void zzbq(String str) {
            zzf(str, 1);
        }

        @WorkerThread
        public void zzf(String str, long j) {
            zzt.this.zzjk();
            if (zzlO() == 0) {
                zzlL();
            }
            if (str == null) {
                str = "";
            }
            long j2 = zzt.this.zzTh.getLong(this.zzaXD, 0);
            if (j2 <= 0) {
                SharedPreferences.Editor edit = zzt.this.zzTh.edit();
                edit.putString(this.zzaXE, str);
                edit.putLong(this.zzaXD, j);
                edit.apply();
                return;
            }
            boolean z = (zzt.this.zzaXr.nextLong() & Long.MAX_VALUE) < (Long.MAX_VALUE / (j2 + j)) * j;
            SharedPreferences.Editor edit2 = zzt.this.zzTh.edit();
            if (z) {
                edit2.putString(this.zzaXE, str);
            }
            edit2.putLong(this.zzaXD, j2 + j);
            edit2.apply();
        }

        @WorkerThread
        public Pair<String, Long> zzlN() {
            zzt.this.zzjk();
            long zzlM = zzlM();
            if (zzlM < this.zzTl) {
                return null;
            }
            if (zzlM > this.zzTl * 2) {
                zzlL();
                return null;
            }
            String string = zzt.this.zzCO().getString(this.zzaXE, (String) null);
            long j = zzt.this.zzCO().getLong(this.zzaXD, 0);
            zzlL();
            return (string == null || j <= 0) ? zzt.zzaXh : new Pair<>(string, Long.valueOf(j));
        }
    }

    zzt(zzw zzw) {
        super(zzw);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public SharedPreferences zzCO() {
        zzjk();
        zzjv();
        return this.zzTh;
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void setMeasurementEnabled(boolean enabled) {
        zzjk();
        zzAo().zzCK().zzj("Setting measurementEnabled", Boolean.valueOf(enabled));
        SharedPreferences.Editor edit = zzCO().edit();
        edit.putBoolean("measurement_enabled", enabled);
        edit.apply();
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public boolean zzAr() {
        zzjk();
        return zzCO().getBoolean("measurement_enabled", !com.google.android.gms.measurement.zza.zzAs());
    }

    /* access modifiers changed from: package-private */
    public String zzCM() {
        byte[] bArr = new byte[16];
        this.zzaXr.nextBytes(bArr);
        return String.format(Locale.US, "%032x", new Object[]{new BigInteger(1, bArr)});
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public long zzCN() {
        zzjv();
        zzjk();
        long j = this.zzaXn.get();
        if (j != 0) {
            return j;
        }
        long nextInt = (long) (this.zzaXr.nextInt(86400000) + 1);
        this.zzaXn.set(nextInt);
        return nextInt;
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public Boolean zzCP() {
        zzjk();
        if (!zzCO().contains("use_service")) {
            return null;
        }
        return Boolean.valueOf(zzCO().getBoolean("use_service", false));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public String zzCQ() {
        zzjk();
        String string = zzCO().getString("previous_os_version", (String) null);
        String zzCy = zzCh().zzCy();
        if (!TextUtils.isEmpty(zzCy) && !zzCy.equals(string)) {
            SharedPreferences.Editor edit = zzCO().edit();
            edit.putString("previous_os_version", zzCy);
            edit.apply();
        }
        return string;
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzar(boolean z) {
        zzjk();
        zzAo().zzCK().zzj("Setting useService", Boolean.valueOf(z));
        SharedPreferences.Editor edit = zzCO().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public Pair<String, Boolean> zzfh(String str) {
        zzjk();
        long elapsedRealtime = zzjl().elapsedRealtime();
        if (this.zzaXo != null && elapsedRealtime < this.zzaXq) {
            return new Pair<>(this.zzaXo, Boolean.valueOf(this.zzaXp));
        }
        this.zzaXq = elapsedRealtime + zzCp().zzeS(str);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            this.zzaXo = advertisingIdInfo.getId();
            this.zzaXp = advertisingIdInfo.isLimitAdTrackingEnabled();
        } catch (Throwable th) {
            zzAo().zzCJ().zzj("Unable to get advertising id", th);
            this.zzaXo = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzaXo, Boolean.valueOf(this.zzaXp));
    }

    /* access modifiers changed from: package-private */
    public String zzfi(String str) {
        String str2 = (String) zzfh(str).first;
        MessageDigest zzbv = zzaj.zzbv(CommonUtils.MD5_INSTANCE);
        if (zzbv == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, zzbv.digest(str2.getBytes()))});
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        this.zzTh = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzaXx = this.zzTh.getBoolean("has_been_opened", false);
        if (!this.zzaXx) {
            SharedPreferences.Editor edit = this.zzTh.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
    }
}
