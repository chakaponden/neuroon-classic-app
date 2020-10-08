package com.google.android.gms.measurement;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzc {
    private final zzf zzaUi;
    private boolean zzaUj;
    private long zzaUk;
    private long zzaUl;
    private long zzaUm;
    private long zzaUn;
    private long zzaUo;
    private boolean zzaUp;
    private final Map<Class<? extends zze>, zze> zzaUq;
    private final List<zzi> zzaUr;
    private final zzmq zzqW;

    zzc(zzc zzc) {
        this.zzaUi = zzc.zzaUi;
        this.zzqW = zzc.zzqW;
        this.zzaUk = zzc.zzaUk;
        this.zzaUl = zzc.zzaUl;
        this.zzaUm = zzc.zzaUm;
        this.zzaUn = zzc.zzaUn;
        this.zzaUo = zzc.zzaUo;
        this.zzaUr = new ArrayList(zzc.zzaUr);
        this.zzaUq = new HashMap(zzc.zzaUq.size());
        for (Map.Entry next : zzc.zzaUq.entrySet()) {
            zze zzg = zzg((Class) next.getKey());
            ((zze) next.getValue()).zza(zzg);
            this.zzaUq.put(next.getKey(), zzg);
        }
    }

    zzc(zzf zzf, zzmq zzmq) {
        zzx.zzz(zzf);
        zzx.zzz(zzmq);
        this.zzaUi = zzf;
        this.zzqW = zzmq;
        this.zzaUn = 1800000;
        this.zzaUo = 3024000000L;
        this.zzaUq = new HashMap();
        this.zzaUr = new ArrayList();
    }

    private static <T extends zze> T zzg(Class<T> cls) {
        try {
            return (zze) cls.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("dataType doesn't have default constructor", e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("dataType default constructor is not accessible", e2);
        }
    }

    /* access modifiers changed from: package-private */
    public void zzAA() {
        this.zzaUm = this.zzqW.elapsedRealtime();
        if (this.zzaUl != 0) {
            this.zzaUk = this.zzaUl;
        } else {
            this.zzaUk = this.zzqW.currentTimeMillis();
        }
        this.zzaUj = true;
    }

    /* access modifiers changed from: package-private */
    public zzf zzAB() {
        return this.zzaUi;
    }

    /* access modifiers changed from: package-private */
    public zzg zzAC() {
        return this.zzaUi.zzAC();
    }

    /* access modifiers changed from: package-private */
    public boolean zzAD() {
        return this.zzaUp;
    }

    /* access modifiers changed from: package-private */
    public void zzAE() {
        this.zzaUp = true;
    }

    public zzc zzAu() {
        return new zzc(this);
    }

    public Collection<zze> zzAv() {
        return this.zzaUq.values();
    }

    public List<zzi> zzAw() {
        return this.zzaUr;
    }

    public long zzAx() {
        return this.zzaUk;
    }

    public void zzAy() {
        zzAC().zze(this);
    }

    public boolean zzAz() {
        return this.zzaUj;
    }

    public void zzM(long j) {
        this.zzaUl = j;
    }

    public void zzb(zze zze) {
        zzx.zzz(zze);
        Class cls = zze.getClass();
        if (cls.getSuperclass() != zze.class) {
            throw new IllegalArgumentException();
        }
        zze.zza(zzf(cls));
    }

    public <T extends zze> T zze(Class<T> cls) {
        return (zze) this.zzaUq.get(cls);
    }

    public <T extends zze> T zzf(Class<T> cls) {
        T t = (zze) this.zzaUq.get(cls);
        if (t != null) {
            return t;
        }
        T zzg = zzg(cls);
        this.zzaUq.put(cls, zzg);
        return zzg;
    }
}
