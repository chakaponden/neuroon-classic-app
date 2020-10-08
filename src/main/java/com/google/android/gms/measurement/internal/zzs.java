package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpz;

class zzs {
    final boolean zzaWY;
    final int zzaWZ;
    long zzaXa;
    float zzaXb;
    long zzaXc;
    float zzaXd;
    long zzaXe;
    float zzaXf;
    final boolean zzaXg;

    public zzs(zzpz.zzd zzd) {
        boolean z;
        boolean z2 = true;
        zzx.zzz(zzd);
        if (zzd.zzaZF == null || zzd.zzaZF.intValue() == 0) {
            z = false;
        } else {
            if (zzd.zzaZF.intValue() != 4) {
                if (zzd.zzaZH == null) {
                    z = false;
                }
            } else if (zzd.zzaZI == null || zzd.zzaZJ == null) {
                z = false;
            }
            z = true;
        }
        if (z) {
            this.zzaWZ = zzd.zzaZF.intValue();
            this.zzaWY = (zzd.zzaZG == null || !zzd.zzaZG.booleanValue()) ? false : z2;
            if (zzd.zzaZF.intValue() == 4) {
                if (this.zzaWY) {
                    this.zzaXd = Float.parseFloat(zzd.zzaZI);
                    this.zzaXf = Float.parseFloat(zzd.zzaZJ);
                } else {
                    this.zzaXc = Long.parseLong(zzd.zzaZI);
                    this.zzaXe = Long.parseLong(zzd.zzaZJ);
                }
            } else if (this.zzaWY) {
                this.zzaXb = Float.parseFloat(zzd.zzaZH);
            } else {
                this.zzaXa = Long.parseLong(zzd.zzaZH);
            }
        } else {
            this.zzaWZ = 0;
            this.zzaWY = false;
        }
        this.zzaXg = z;
    }

    public Boolean zzac(long j) {
        boolean z = true;
        if (!this.zzaXg) {
            return null;
        }
        if (this.zzaWY) {
            return null;
        }
        switch (this.zzaWZ) {
            case 1:
                if (j >= this.zzaXa) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 2:
                if (j <= this.zzaXa) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 3:
                if (j != this.zzaXa) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 4:
                if (j < this.zzaXc || j > this.zzaXe) {
                    z = false;
                }
                return Boolean.valueOf(z);
            default:
                return null;
        }
    }

    public Boolean zzi(float f) {
        boolean z = true;
        boolean z2 = false;
        if (!this.zzaXg) {
            return null;
        }
        if (!this.zzaWY) {
            return null;
        }
        switch (this.zzaWZ) {
            case 1:
                if (f >= this.zzaXb) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 2:
                if (f <= this.zzaXb) {
                    z = false;
                }
                return Boolean.valueOf(z);
            case 3:
                if (f == this.zzaXb || Math.abs(f - this.zzaXb) < 2.0f * Math.max(Math.ulp(f), Math.ulp(this.zzaXb))) {
                    z2 = true;
                }
                return Boolean.valueOf(z2);
            case 4:
                if (f < this.zzaXd || f > this.zzaXf) {
                    z = false;
                }
                return Boolean.valueOf(z);
            default:
                return null;
        }
    }
}
