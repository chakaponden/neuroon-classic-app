package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import java.io.IOException;

public class zza {
    private static zza zzbhA;
    private static Object zzbhz = new Object();
    private volatile boolean mClosed;
    /* access modifiers changed from: private */
    public final Context mContext;
    private final Thread zzLM;
    private volatile AdvertisingIdClient.Info zzPW;
    private volatile long zzbht;
    private volatile long zzbhu;
    private volatile long zzbhv;
    private volatile long zzbhw;
    private final Object zzbhx;
    private C0026zza zzbhy;
    private final zzmq zzqW;

    /* renamed from: com.google.android.gms.tagmanager.zza$zza  reason: collision with other inner class name */
    public interface C0026zza {
        AdvertisingIdClient.Info zzFV();
    }

    private zza(Context context) {
        this(context, (C0026zza) null, zzmt.zzsc());
    }

    public zza(Context context, C0026zza zza, zzmq zzmq) {
        this.zzbht = 900000;
        this.zzbhu = 30000;
        this.mClosed = false;
        this.zzbhx = new Object();
        this.zzbhy = new C0026zza() {
            public AdvertisingIdClient.Info zzFV() {
                try {
                    return AdvertisingIdClient.getAdvertisingIdInfo(zza.this.mContext);
                } catch (IllegalStateException e) {
                    zzbg.zzd("IllegalStateException getting Advertising Id Info", e);
                    return null;
                } catch (GooglePlayServicesRepairableException e2) {
                    zzbg.zzd("GooglePlayServicesRepairableException getting Advertising Id Info", e2);
                    return null;
                } catch (IOException e3) {
                    zzbg.zzd("IOException getting Ad Id Info", e3);
                    return null;
                } catch (GooglePlayServicesNotAvailableException e4) {
                    zzbg.zzd("GooglePlayServicesNotAvailableException getting Advertising Id Info", e4);
                    return null;
                } catch (Exception e5) {
                    zzbg.zzd("Unknown exception. Could not get the Advertising Id Info.", e5);
                    return null;
                }
            }
        };
        this.zzqW = zzmq;
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        if (zza != null) {
            this.zzbhy = zza;
        }
        this.zzbhv = this.zzqW.currentTimeMillis();
        this.zzLM = new Thread(new Runnable() {
            public void run() {
                zza.this.zzFU();
            }
        });
    }

    private void zzFR() {
        synchronized (this) {
            try {
                zzFS();
                wait(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void zzFS() {
        if (this.zzqW.currentTimeMillis() - this.zzbhv > this.zzbhu) {
            synchronized (this.zzbhx) {
                this.zzbhx.notify();
            }
            this.zzbhv = this.zzqW.currentTimeMillis();
        }
    }

    private void zzFT() {
        if (this.zzqW.currentTimeMillis() - this.zzbhw > 3600000) {
            this.zzPW = null;
        }
    }

    /* access modifiers changed from: private */
    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:225)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public void zzFU() {
        /*
            r4 = this;
            r0 = 10
            android.os.Process.setThreadPriority(r0)
        L_0x0005:
            boolean r0 = r4.mClosed
            if (r0 != 0) goto L_0x003e
            com.google.android.gms.tagmanager.zza$zza r0 = r4.zzbhy
            com.google.android.gms.ads.identifier.AdvertisingIdClient$Info r0 = r0.zzFV()
            if (r0 == 0) goto L_0x0020
            r4.zzPW = r0
            com.google.android.gms.internal.zzmq r0 = r4.zzqW
            long r0 = r0.currentTimeMillis()
            r4.zzbhw = r0
            java.lang.String r0 = "Obtained fresh AdvertisingId info from GmsCore."
            com.google.android.gms.tagmanager.zzbg.zzaJ(r0)
        L_0x0020:
            monitor-enter(r4)
            r4.notifyAll()     // Catch:{ all -> 0x003b }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            java.lang.Object r1 = r4.zzbhx     // Catch:{ InterruptedException -> 0x0034 }
            monitor-enter(r1)     // Catch:{ InterruptedException -> 0x0034 }
            java.lang.Object r0 = r4.zzbhx     // Catch:{ all -> 0x0031 }
            long r2 = r4.zzbht     // Catch:{ all -> 0x0031 }
            r0.wait(r2)     // Catch:{ all -> 0x0031 }
            monitor-exit(r1)     // Catch:{ all -> 0x0031 }
            goto L_0x0005
        L_0x0031:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0031 }
            throw r0     // Catch:{ InterruptedException -> 0x0034 }
        L_0x0034:
            r0 = move-exception
            java.lang.String r0 = "sleep interrupted in AdvertiserDataPoller thread; continuing"
            com.google.android.gms.tagmanager.zzbg.zzaJ(r0)
            goto L_0x0005
        L_0x003b:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r0
        L_0x003e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zza.zzFU():void");
    }

    public static zza zzaW(Context context) {
        if (zzbhA == null) {
            synchronized (zzbhz) {
                if (zzbhA == null) {
                    zzbhA = new zza(context);
                    zzbhA.start();
                }
            }
        }
        return zzbhA;
    }

    public boolean isLimitAdTrackingEnabled() {
        if (this.zzPW == null) {
            zzFR();
        } else {
            zzFS();
        }
        zzFT();
        if (this.zzPW == null) {
            return true;
        }
        return this.zzPW.isLimitAdTrackingEnabled();
    }

    public void start() {
        this.zzLM.start();
    }

    public String zzFQ() {
        if (this.zzPW == null) {
            zzFR();
        } else {
            zzFS();
        }
        zzFT();
        if (this.zzPW == null) {
            return null;
        }
        return this.zzPW.getId();
    }
}
