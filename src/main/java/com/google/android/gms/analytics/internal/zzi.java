package com.google.android.gms.analytics.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.stats.zzb;
import java.util.Collections;

public class zzi extends zzd {
    /* access modifiers changed from: private */
    public final zza zzQH = new zza();
    private zzac zzQI;
    private final zzt zzQJ;
    private zzaj zzQK;

    protected class zza implements ServiceConnection {
        private volatile zzac zzQM;
        private volatile boolean zzQN;

        protected zza() {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
            r4.zzQL.zzbh("Service connect failed to get IAnalyticsService");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x005a, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            notifyAll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x005e, code lost:
            throw r0;
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [B:3:0x0008, B:9:0x0015] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onServiceConnected(android.content.ComponentName r5, android.os.IBinder r6) {
            /*
                r4 = this;
                java.lang.String r0 = "AnalyticsServiceConnection.onServiceConnected"
                com.google.android.gms.common.internal.zzx.zzcD(r0)
                monitor-enter(r4)
                if (r6 != 0) goto L_0x0014
                com.google.android.gms.analytics.internal.zzi r0 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ all -> 0x005a }
                java.lang.String r1 = "Service connected with null binder"
                r0.zzbh(r1)     // Catch:{ all -> 0x005a }
                r4.notifyAll()     // Catch:{ all -> 0x0046 }
                monitor-exit(r4)     // Catch:{ all -> 0x0046 }
            L_0x0013:
                return
            L_0x0014:
                r0 = 0
                java.lang.String r1 = r6.getInterfaceDescriptor()     // Catch:{ RemoteException -> 0x0051 }
                java.lang.String r2 = "com.google.android.gms.analytics.internal.IAnalyticsService"
                boolean r2 = r2.equals(r1)     // Catch:{ RemoteException -> 0x0051 }
                if (r2 == 0) goto L_0x0049
                com.google.android.gms.analytics.internal.zzac r0 = com.google.android.gms.analytics.internal.zzac.zza.zzaf(r6)     // Catch:{ RemoteException -> 0x0051 }
                com.google.android.gms.analytics.internal.zzi r1 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ RemoteException -> 0x0051 }
                java.lang.String r2 = "Bound to IAnalyticsService interface"
                r1.zzbd(r2)     // Catch:{ RemoteException -> 0x0051 }
            L_0x002c:
                if (r0 != 0) goto L_0x005f
                com.google.android.gms.common.stats.zzb r0 = com.google.android.gms.common.stats.zzb.zzrP()     // Catch:{ IllegalArgumentException -> 0x007c }
                com.google.android.gms.analytics.internal.zzi r1 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ IllegalArgumentException -> 0x007c }
                android.content.Context r1 = r1.getContext()     // Catch:{ IllegalArgumentException -> 0x007c }
                com.google.android.gms.analytics.internal.zzi r2 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ IllegalArgumentException -> 0x007c }
                com.google.android.gms.analytics.internal.zzi$zza r2 = r2.zzQH     // Catch:{ IllegalArgumentException -> 0x007c }
                r0.zza(r1, r2)     // Catch:{ IllegalArgumentException -> 0x007c }
            L_0x0041:
                r4.notifyAll()     // Catch:{ all -> 0x0046 }
                monitor-exit(r4)     // Catch:{ all -> 0x0046 }
                goto L_0x0013
            L_0x0046:
                r0 = move-exception
                monitor-exit(r4)     // Catch:{ all -> 0x0046 }
                throw r0
            L_0x0049:
                com.google.android.gms.analytics.internal.zzi r2 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ RemoteException -> 0x0051 }
                java.lang.String r3 = "Got binder with a wrong descriptor"
                r2.zze(r3, r1)     // Catch:{ RemoteException -> 0x0051 }
                goto L_0x002c
            L_0x0051:
                r1 = move-exception
                com.google.android.gms.analytics.internal.zzi r1 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ all -> 0x005a }
                java.lang.String r2 = "Service connect failed to get IAnalyticsService"
                r1.zzbh(r2)     // Catch:{ all -> 0x005a }
                goto L_0x002c
            L_0x005a:
                r0 = move-exception
                r4.notifyAll()     // Catch:{ all -> 0x0046 }
                throw r0     // Catch:{ all -> 0x0046 }
            L_0x005f:
                boolean r1 = r4.zzQN     // Catch:{ all -> 0x005a }
                if (r1 != 0) goto L_0x0079
                com.google.android.gms.analytics.internal.zzi r1 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ all -> 0x005a }
                java.lang.String r2 = "onServiceConnected received after the timeout limit"
                r1.zzbg(r2)     // Catch:{ all -> 0x005a }
                com.google.android.gms.analytics.internal.zzi r1 = com.google.android.gms.analytics.internal.zzi.this     // Catch:{ all -> 0x005a }
                com.google.android.gms.measurement.zzg r1 = r1.zzjo()     // Catch:{ all -> 0x005a }
                com.google.android.gms.analytics.internal.zzi$zza$1 r2 = new com.google.android.gms.analytics.internal.zzi$zza$1     // Catch:{ all -> 0x005a }
                r2.<init>(r0)     // Catch:{ all -> 0x005a }
                r1.zzf(r2)     // Catch:{ all -> 0x005a }
                goto L_0x0041
            L_0x0079:
                r4.zzQM = r0     // Catch:{ all -> 0x005a }
                goto L_0x0041
            L_0x007c:
                r0 = move-exception
                goto L_0x0041
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzi.zza.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        public void onServiceDisconnected(final ComponentName name) {
            zzx.zzcD("AnalyticsServiceConnection.onServiceDisconnected");
            zzi.this.zzjo().zzf(new Runnable() {
                public void run() {
                    zzi.this.onServiceDisconnected(name);
                }
            });
        }

        public zzac zzjK() {
            zzac zzac = null;
            zzi.this.zzjk();
            Intent intent = new Intent("com.google.android.gms.analytics.service.START");
            intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.analytics.service.AnalyticsService"));
            Context context = zzi.this.getContext();
            intent.putExtra("app_package_name", context.getPackageName());
            zzb zzrP = zzb.zzrP();
            synchronized (this) {
                this.zzQM = null;
                this.zzQN = true;
                boolean zza = zzrP.zza(context, intent, (ServiceConnection) zzi.this.zzQH, 129);
                zzi.this.zza("Bind to service requested", Boolean.valueOf(zza));
                if (!zza) {
                    this.zzQN = false;
                } else {
                    try {
                        wait(zzi.this.zzjn().zzkN());
                    } catch (InterruptedException e) {
                        zzi.this.zzbg("Wait for service connect was interrupted");
                    }
                    this.zzQN = false;
                    zzac = this.zzQM;
                    this.zzQM = null;
                    if (zzac == null) {
                        zzi.this.zzbh("Successfully bound to service but never got onServiceConnected callback");
                    }
                }
            }
            return zzac;
        }
    }

    protected zzi(zzf zzf) {
        super(zzf);
        this.zzQK = new zzaj(zzf.zzjl());
        this.zzQJ = new zzt(zzf) {
            public void run() {
                zzi.this.zzjJ();
            }
        };
    }

    private void onDisconnect() {
        zziH().zzjf();
    }

    /* access modifiers changed from: private */
    public void onServiceDisconnected(ComponentName name) {
        zzjk();
        if (this.zzQI != null) {
            this.zzQI = null;
            zza("Disconnected from device AnalyticsService", name);
            onDisconnect();
        }
    }

    /* access modifiers changed from: private */
    public void zza(zzac zzac) {
        zzjk();
        this.zzQI = zzac;
        zzjI();
        zziH().onServiceConnected();
    }

    private void zzjI() {
        this.zzQK.start();
        this.zzQJ.zzt(zzjn().zzkM());
    }

    /* access modifiers changed from: private */
    public void zzjJ() {
        zzjk();
        if (isConnected()) {
            zzbd("Inactivity, disconnecting from device AnalyticsService");
            disconnect();
        }
    }

    public boolean connect() {
        zzjk();
        zzjv();
        if (this.zzQI != null) {
            return true;
        }
        zzac zzjK = this.zzQH.zzjK();
        if (zzjK == null) {
            return false;
        }
        this.zzQI = zzjK;
        zzjI();
        return true;
    }

    public void disconnect() {
        zzjk();
        zzjv();
        try {
            zzb.zzrP().zza(getContext(), this.zzQH);
        } catch (IllegalArgumentException | IllegalStateException e) {
        }
        if (this.zzQI != null) {
            this.zzQI = null;
            onDisconnect();
        }
    }

    public boolean isConnected() {
        zzjk();
        zzjv();
        return this.zzQI != null;
    }

    public boolean zzb(zzab zzab) {
        zzx.zzz(zzab);
        zzjk();
        zzjv();
        zzac zzac = this.zzQI;
        if (zzac == null) {
            return false;
        }
        try {
            zzac.zza(zzab.zzn(), zzab.zzlr(), zzab.zzlt() ? zzjn().zzkF() : zzjn().zzkG(), Collections.emptyList());
            zzjI();
            return true;
        } catch (RemoteException e) {
            zzbd("Failed to send hits to AnalyticsService");
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }

    public boolean zzjH() {
        zzjk();
        zzjv();
        zzac zzac = this.zzQI;
        if (zzac == null) {
            return false;
        }
        try {
            zzac.zzjc();
            zzjI();
            return true;
        } catch (RemoteException e) {
            zzbd("Failed to clear hits from AnalyticsService");
            return false;
        }
    }
}
