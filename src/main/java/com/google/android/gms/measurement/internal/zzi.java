package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.zzx;

class zzi {
    final String mName;
    final String zzaUa;
    final long zzaVP;
    final long zzaVQ;
    final long zzaVR;

    zzi(String str, String str2, long j, long j2, long j3) {
        boolean z = true;
        zzx.zzcM(str);
        zzx.zzcM(str2);
        zzx.zzac(j >= 0);
        zzx.zzac(j2 < 0 ? false : z);
        this.zzaUa = str;
        this.mName = str2;
        this.zzaVP = j;
        this.zzaVQ = j2;
        this.zzaVR = j3;
    }

    /* access modifiers changed from: package-private */
    public zzi zzCB() {
        return new zzi(this.zzaUa, this.mName, this.zzaVP + 1, this.zzaVQ + 1, this.zzaVR);
    }

    /* access modifiers changed from: package-private */
    public zzi zzab(long j) {
        return new zzi(this.zzaUa, this.mName, this.zzaVP, this.zzaVQ, j);
    }
}
