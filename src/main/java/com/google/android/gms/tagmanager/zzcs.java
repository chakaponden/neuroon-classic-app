package com.google.android.gms.tagmanager;

class zzcs implements zzcd {
    private final long zzSP;
    private final int zzSQ;
    private double zzSR;
    private final Object zzST;
    private long zzbkO;

    public zzcs() {
        this(60, 2000);
    }

    public zzcs(int i, long j) {
        this.zzST = new Object();
        this.zzSQ = i;
        this.zzSR = (double) this.zzSQ;
        this.zzSP = j;
    }

    public boolean zzlw() {
        boolean z;
        synchronized (this.zzST) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.zzSR < ((double) this.zzSQ)) {
                double d = ((double) (currentTimeMillis - this.zzbkO)) / ((double) this.zzSP);
                if (d > 0.0d) {
                    this.zzSR = Math.min((double) this.zzSQ, d + this.zzSR);
                }
            }
            this.zzbkO = currentTimeMillis;
            if (this.zzSR >= 1.0d) {
                this.zzSR -= 1.0d;
                z = true;
            } else {
                zzbg.zzaK("No more tokens available.");
                z = false;
            }
        }
        return z;
    }
}
