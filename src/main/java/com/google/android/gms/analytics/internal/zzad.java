package com.google.android.gms.analytics.internal;

import com.google.android.gms.internal.zzmq;

public class zzad {
    private final long zzSP;
    private final int zzSQ;
    private double zzSR;
    private long zzSS;
    private final Object zzST;
    private final String zzSU;
    private final zzmq zzqW;

    public zzad(int i, long j, String str, zzmq zzmq) {
        this.zzST = new Object();
        this.zzSQ = i;
        this.zzSR = (double) this.zzSQ;
        this.zzSP = j;
        this.zzSU = str;
        this.zzqW = zzmq;
    }

    public zzad(String str, zzmq zzmq) {
        this(60, 2000, str, zzmq);
    }

    public boolean zzlw() {
        boolean z;
        synchronized (this.zzST) {
            long currentTimeMillis = this.zzqW.currentTimeMillis();
            if (this.zzSR < ((double) this.zzSQ)) {
                double d = ((double) (currentTimeMillis - this.zzSS)) / ((double) this.zzSP);
                if (d > 0.0d) {
                    this.zzSR = Math.min((double) this.zzSQ, d + this.zzSR);
                }
            }
            this.zzSS = currentTimeMillis;
            if (this.zzSR >= 1.0d) {
                this.zzSR -= 1.0d;
                z = true;
            } else {
                zzae.zzaK("Excessive " + this.zzSU + " detected; call ignored.");
                z = false;
            }
        }
        return z;
    }
}
