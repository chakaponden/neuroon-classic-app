package com.google.android.gms.tagmanager;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzmt;
import com.google.android.gms.internal.zzrq;
import com.google.android.gms.internal.zzrr;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.tagmanager.zzbf;
import com.google.android.gms.tagmanager.zzcb;
import com.google.android.gms.tagmanager.zzo;

public class zzp extends com.google.android.gms.common.api.internal.zzb<ContainerHolder> {
    private final Context mContext;
    private final Looper zzagr;
    private final String zzbhM;
    /* access modifiers changed from: private */
    public long zzbhR;
    private final TagManager zzbhY;
    private final zzd zzbib;
    /* access modifiers changed from: private */
    public final zzcd zzbic;
    private final int zzbid;
    private zzf zzbie;
    private zzrr zzbif;
    /* access modifiers changed from: private */
    public volatile zzo zzbig;
    /* access modifiers changed from: private */
    public volatile boolean zzbih;
    /* access modifiers changed from: private */
    public zzaf.zzj zzbii;
    private String zzbij;
    private zze zzbik;
    private zza zzbil;
    /* access modifiers changed from: private */
    public final zzmq zzqW;

    /* renamed from: com.google.android.gms.tagmanager.zzp$1  reason: invalid class name */
    class AnonymousClass1 {
    }

    interface zza {
        boolean zzb(Container container);
    }

    private class zzb implements zzbf<zzrq.zza> {
        private zzb() {
        }

        /* synthetic */ zzb(zzp zzp, AnonymousClass1 r2) {
            this();
        }

        public void zzGk() {
        }

        /* renamed from: zza */
        public void zzI(zzrq.zza zza) {
            zzaf.zzj zzj;
            if (zza.zzbme != null) {
                zzj = zza.zzbme;
            } else {
                zzaf.zzf zzf = zza.zzju;
                zzj = new zzaf.zzj();
                zzj.zzju = zzf;
                zzj.zzjt = null;
                zzj.zzjv = zzf.version;
            }
            zzp.this.zza(zzj, zza.zzbmd, true);
        }

        public void zza(zzbf.zza zza) {
            if (!zzp.this.zzbih) {
                zzp.this.zzak(0);
            }
        }
    }

    private class zzc implements zzbf<zzaf.zzj> {
        private zzc() {
        }

        /* synthetic */ zzc(zzp zzp, AnonymousClass1 r2) {
            this();
        }

        public void zzGk() {
        }

        public void zza(zzbf.zza zza) {
            synchronized (zzp.this) {
                if (!zzp.this.isReady()) {
                    if (zzp.this.zzbig != null) {
                        zzp.this.zza(zzp.this.zzbig);
                    } else {
                        zzp.this.zza(zzp.this.zzc(Status.zzagF));
                    }
                }
            }
            zzp.this.zzak(3600000);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            return;
         */
        /* renamed from: zzb */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void zzI(com.google.android.gms.internal.zzaf.zzj r6) {
            /*
                r5 = this;
                com.google.android.gms.tagmanager.zzp r1 = com.google.android.gms.tagmanager.zzp.this
                monitor-enter(r1)
                com.google.android.gms.internal.zzaf$zzf r0 = r6.zzju     // Catch:{ all -> 0x0065 }
                if (r0 != 0) goto L_0x002a
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                com.google.android.gms.internal.zzaf$zzj r0 = r0.zzbii     // Catch:{ all -> 0x0065 }
                com.google.android.gms.internal.zzaf$zzf r0 = r0.zzju     // Catch:{ all -> 0x0065 }
                if (r0 != 0) goto L_0x0020
                java.lang.String r0 = "Current resource is null; network resource is also null"
                com.google.android.gms.tagmanager.zzbg.e(r0)     // Catch:{ all -> 0x0065 }
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                r2 = 3600000(0x36ee80, double:1.7786363E-317)
                r0.zzak(r2)     // Catch:{ all -> 0x0065 }
                monitor-exit(r1)     // Catch:{ all -> 0x0065 }
            L_0x001f:
                return
            L_0x0020:
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                com.google.android.gms.internal.zzaf$zzj r0 = r0.zzbii     // Catch:{ all -> 0x0065 }
                com.google.android.gms.internal.zzaf$zzf r0 = r0.zzju     // Catch:{ all -> 0x0065 }
                r6.zzju = r0     // Catch:{ all -> 0x0065 }
            L_0x002a:
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                com.google.android.gms.tagmanager.zzp r2 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                com.google.android.gms.internal.zzmq r2 = r2.zzqW     // Catch:{ all -> 0x0065 }
                long r2 = r2.currentTimeMillis()     // Catch:{ all -> 0x0065 }
                r4 = 0
                r0.zza(r6, r2, r4)     // Catch:{ all -> 0x0065 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0065 }
                r0.<init>()     // Catch:{ all -> 0x0065 }
                java.lang.String r2 = "setting refresh time to current time: "
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x0065 }
                com.google.android.gms.tagmanager.zzp r2 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                long r2 = r2.zzbhR     // Catch:{ all -> 0x0065 }
                java.lang.StringBuilder r0 = r0.append(r2)     // Catch:{ all -> 0x0065 }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0065 }
                com.google.android.gms.tagmanager.zzbg.v(r0)     // Catch:{ all -> 0x0065 }
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                boolean r0 = r0.zzGj()     // Catch:{ all -> 0x0065 }
                if (r0 != 0) goto L_0x0063
                com.google.android.gms.tagmanager.zzp r0 = com.google.android.gms.tagmanager.zzp.this     // Catch:{ all -> 0x0065 }
                r0.zza((com.google.android.gms.internal.zzaf.zzj) r6)     // Catch:{ all -> 0x0065 }
            L_0x0063:
                monitor-exit(r1)     // Catch:{ all -> 0x0065 }
                goto L_0x001f
            L_0x0065:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0065 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzp.zzc.zzI(com.google.android.gms.internal.zzaf$zzj):void");
        }
    }

    private class zzd implements zzo.zza {
        private zzd() {
        }

        /* synthetic */ zzd(zzp zzp, AnonymousClass1 r2) {
            this();
        }

        public String zzGd() {
            return zzp.this.zzGd();
        }

        public void zzGf() {
            if (zzp.this.zzbic.zzlw()) {
                zzp.this.zzak(0);
            }
        }

        public void zzfT(String str) {
            zzp.this.zzfT(str);
        }
    }

    interface zze extends Releasable {
        void zza(zzbf<zzaf.zzj> zzbf);

        void zzf(long j, String str);

        void zzfW(String str);
    }

    interface zzf extends Releasable {
        void zzGl();

        void zza(zzbf<zzrq.zza> zzbf);

        void zzb(zzrq.zza zza);

        zzrs.zzc zzke(int i);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    zzp(Context context, TagManager tagManager, Looper looper, String str, int i, zzf zzf2, zze zze2, zzrr zzrr, zzmq zzmq, zzcd zzcd) {
        super(looper == null ? Looper.getMainLooper() : looper);
        this.mContext = context;
        this.zzbhY = tagManager;
        this.zzagr = looper == null ? Looper.getMainLooper() : looper;
        this.zzbhM = str;
        this.zzbid = i;
        this.zzbie = zzf2;
        this.zzbik = zze2;
        this.zzbif = zzrr;
        this.zzbib = new zzd(this, (AnonymousClass1) null);
        this.zzbii = new zzaf.zzj();
        this.zzqW = zzmq;
        this.zzbic = zzcd;
        if (zzGj()) {
            zzfT(zzcb.zzGU().zzGW());
        }
    }

    public zzp(Context context, TagManager tagManager, Looper looper, String str, int i, zzs zzs) {
        this(context, tagManager, looper, str, i, new zzcn(context, str), new zzcm(context, str, zzs), new zzrr(context), zzmt.zzsc(), new zzbe(30, 900000, 5000, "refreshing", zzmt.zzsc()));
        this.zzbif.zzgB(zzs.zzGm());
    }

    /* access modifiers changed from: private */
    public boolean zzGj() {
        zzcb zzGU = zzcb.zzGU();
        return (zzGU.zzGV() == zzcb.zza.CONTAINER || zzGU.zzGV() == zzcb.zza.CONTAINER_DEBUG) && this.zzbhM.equals(zzGU.getContainerId());
    }

    /* access modifiers changed from: private */
    public synchronized void zza(zzaf.zzj zzj) {
        if (this.zzbie != null) {
            zzrq.zza zza2 = new zzrq.zza();
            zza2.zzbmd = this.zzbhR;
            zza2.zzju = new zzaf.zzf();
            zza2.zzbme = zzj;
            this.zzbie.zzb(zza2);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0008, code lost:
        if (r8.zzbih != false) goto L_0x000a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void zza(com.google.android.gms.internal.zzaf.zzj r9, long r10, boolean r12) {
        /*
            r8 = this;
            r6 = 43200000(0x2932e00, double:2.1343636E-316)
            monitor-enter(r8)
            if (r12 == 0) goto L_0x000c
            boolean r0 = r8.zzbih     // Catch:{ all -> 0x006a }
            if (r0 == 0) goto L_0x000c
        L_0x000a:
            monitor-exit(r8)
            return
        L_0x000c:
            boolean r0 = r8.isReady()     // Catch:{ all -> 0x006a }
            if (r0 == 0) goto L_0x0016
            com.google.android.gms.tagmanager.zzo r0 = r8.zzbig     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x0016
        L_0x0016:
            r8.zzbii = r9     // Catch:{ all -> 0x006a }
            r8.zzbhR = r10     // Catch:{ all -> 0x006a }
            r0 = 0
            r2 = 43200000(0x2932e00, double:2.1343636E-316)
            long r4 = r8.zzbhR     // Catch:{ all -> 0x006a }
            long r4 = r4 + r6
            com.google.android.gms.internal.zzmq r6 = r8.zzqW     // Catch:{ all -> 0x006a }
            long r6 = r6.currentTimeMillis()     // Catch:{ all -> 0x006a }
            long r4 = r4 - r6
            long r2 = java.lang.Math.min(r2, r4)     // Catch:{ all -> 0x006a }
            long r0 = java.lang.Math.max(r0, r2)     // Catch:{ all -> 0x006a }
            r8.zzak(r0)     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.Container r0 = new com.google.android.gms.tagmanager.Container     // Catch:{ all -> 0x006a }
            android.content.Context r1 = r8.mContext     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.TagManager r2 = r8.zzbhY     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.DataLayer r2 = r2.getDataLayer()     // Catch:{ all -> 0x006a }
            java.lang.String r3 = r8.zzbhM     // Catch:{ all -> 0x006a }
            r4 = r10
            r6 = r9
            r0.<init>((android.content.Context) r1, (com.google.android.gms.tagmanager.DataLayer) r2, (java.lang.String) r3, (long) r4, (com.google.android.gms.internal.zzaf.zzj) r6)     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.zzo r1 = r8.zzbig     // Catch:{ all -> 0x006a }
            if (r1 != 0) goto L_0x006d
            com.google.android.gms.tagmanager.zzo r1 = new com.google.android.gms.tagmanager.zzo     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.TagManager r2 = r8.zzbhY     // Catch:{ all -> 0x006a }
            android.os.Looper r3 = r8.zzagr     // Catch:{ all -> 0x006a }
            com.google.android.gms.tagmanager.zzp$zzd r4 = r8.zzbib     // Catch:{ all -> 0x006a }
            r1.<init>(r2, r3, r0, r4)     // Catch:{ all -> 0x006a }
            r8.zzbig = r1     // Catch:{ all -> 0x006a }
        L_0x0056:
            boolean r1 = r8.isReady()     // Catch:{ all -> 0x006a }
            if (r1 != 0) goto L_0x000a
            com.google.android.gms.tagmanager.zzp$zza r1 = r8.zzbil     // Catch:{ all -> 0x006a }
            boolean r0 = r1.zzb(r0)     // Catch:{ all -> 0x006a }
            if (r0 == 0) goto L_0x000a
            com.google.android.gms.tagmanager.zzo r0 = r8.zzbig     // Catch:{ all -> 0x006a }
            r8.zza(r0)     // Catch:{ all -> 0x006a }
            goto L_0x000a
        L_0x006a:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        L_0x006d:
            com.google.android.gms.tagmanager.zzo r1 = r8.zzbig     // Catch:{ all -> 0x006a }
            r1.zza(r0)     // Catch:{ all -> 0x006a }
            goto L_0x0056
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzp.zza(com.google.android.gms.internal.zzaf$zzj, long, boolean):void");
    }

    /* access modifiers changed from: private */
    public synchronized void zzak(long j) {
        if (this.zzbik == null) {
            zzbg.zzaK("Refresh requested, but no network load scheduler.");
        } else {
            this.zzbik.zzf(j, this.zzbii.zzjv);
        }
    }

    private void zzaw(final boolean z) {
        this.zzbie.zza(new zzb(this, (AnonymousClass1) null));
        this.zzbik.zza(new zzc(this, (AnonymousClass1) null));
        zzrs.zzc zzke = this.zzbie.zzke(this.zzbid);
        if (zzke != null) {
            this.zzbig = new zzo(this.zzbhY, this.zzagr, new Container(this.mContext, this.zzbhY.getDataLayer(), this.zzbhM, 0, zzke), this.zzbib);
        }
        this.zzbil = new zza() {
            public boolean zzb(Container container) {
                return z ? container.getLastRefreshTime() + 43200000 >= zzp.this.zzqW.currentTimeMillis() : !container.isDefault();
            }
        };
        if (zzGj()) {
            this.zzbik.zzf(0, "");
        } else {
            this.zzbie.zzGl();
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized String zzGd() {
        return this.zzbij;
    }

    public void zzGg() {
        zzrs.zzc zzke = this.zzbie.zzke(this.zzbid);
        if (zzke != null) {
            zza(new zzo(this.zzbhY, this.zzagr, new Container(this.mContext, this.zzbhY.getDataLayer(), this.zzbhM, 0, zzke), new zzo.zza() {
                public String zzGd() {
                    return zzp.this.zzGd();
                }

                public void zzGf() {
                    zzbg.zzaK("Refresh ignored: container loaded as default only.");
                }

                public void zzfT(String str) {
                    zzp.this.zzfT(str);
                }
            }));
        } else {
            zzbg.e("Default was requested, but no default container was found");
            zza(zzc(new Status(10, "Default was requested, but no default container was found", (PendingIntent) null)));
        }
        this.zzbik = null;
        this.zzbie = null;
    }

    public void zzGh() {
        zzaw(false);
    }

    public void zzGi() {
        zzaw(true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzbn */
    public ContainerHolder zzc(Status status) {
        if (this.zzbig != null) {
            return this.zzbig;
        }
        if (status == Status.zzagF) {
            zzbg.e("timer expired: setting result to failure");
        }
        return new zzo(status);
    }

    /* access modifiers changed from: package-private */
    public synchronized void zzfT(String str) {
        this.zzbij = str;
        if (this.zzbik != null) {
            this.zzbik.zzfW(str);
        }
    }
}
