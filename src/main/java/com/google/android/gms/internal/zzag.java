package com.google.android.gms.internal;

import java.io.IOException;

public interface zzag {

    public static final class zza extends zzso<zza> {
        private static volatile zza[] zzjw;
        public int type;
        public zza[] zzjA;
        public String zzjB;
        public String zzjC;
        public long zzjD;
        public boolean zzjE;
        public zza[] zzjF;
        public int[] zzjG;
        public boolean zzjH;
        public String zzjx;
        public zza[] zzjy;
        public zza[] zzjz;

        public zza() {
            zzR();
        }

        public static zza[] zzQ() {
            if (zzjw == null) {
                synchronized (zzss.zzbut) {
                    if (zzjw == null) {
                        zzjw = new zza[0];
                    }
                }
            }
            return zzjw;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zza = (zza) o;
            if (this.type != zza.type) {
                return false;
            }
            if (this.zzjx == null) {
                if (zza.zzjx != null) {
                    return false;
                }
            } else if (!this.zzjx.equals(zza.zzjx)) {
                return false;
            }
            if (!zzss.equals((Object[]) this.zzjy, (Object[]) zza.zzjy) || !zzss.equals((Object[]) this.zzjz, (Object[]) zza.zzjz) || !zzss.equals((Object[]) this.zzjA, (Object[]) zza.zzjA)) {
                return false;
            }
            if (this.zzjB == null) {
                if (zza.zzjB != null) {
                    return false;
                }
            } else if (!this.zzjB.equals(zza.zzjB)) {
                return false;
            }
            if (this.zzjC == null) {
                if (zza.zzjC != null) {
                    return false;
                }
            } else if (!this.zzjC.equals(zza.zzjC)) {
                return false;
            }
            if (this.zzjD != zza.zzjD || this.zzjE != zza.zzjE || !zzss.equals((Object[]) this.zzjF, (Object[]) zza.zzjF) || !zzss.equals(this.zzjG, zza.zzjG) || this.zzjH != zza.zzjH) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zza.zzbuj == null || zza.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zza.zzbuj);
        }

        public int hashCode() {
            int i = 1231;
            int i2 = 0;
            int hashCode = ((((((this.zzjE ? 1231 : 1237) + (((((this.zzjC == null ? 0 : this.zzjC.hashCode()) + (((this.zzjB == null ? 0 : this.zzjB.hashCode()) + (((((((((this.zzjx == null ? 0 : this.zzjx.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.type) * 31)) * 31) + zzss.hashCode((Object[]) this.zzjy)) * 31) + zzss.hashCode((Object[]) this.zzjz)) * 31) + zzss.hashCode((Object[]) this.zzjA)) * 31)) * 31)) * 31) + ((int) (this.zzjD ^ (this.zzjD >>> 32)))) * 31)) * 31) + zzss.hashCode((Object[]) this.zzjF)) * 31) + zzss.hashCode(this.zzjG)) * 31;
            if (!this.zzjH) {
                i = 1237;
            }
            int i3 = (hashCode + i) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i2 = this.zzbuj.hashCode();
            }
            return i3 + i2;
        }

        public void writeTo(zzsn output) throws IOException {
            output.zzA(1, this.type);
            if (!this.zzjx.equals("")) {
                output.zzn(2, this.zzjx);
            }
            if (this.zzjy != null && this.zzjy.length > 0) {
                for (zza zza : this.zzjy) {
                    if (zza != null) {
                        output.zza(3, (zzsu) zza);
                    }
                }
            }
            if (this.zzjz != null && this.zzjz.length > 0) {
                for (zza zza2 : this.zzjz) {
                    if (zza2 != null) {
                        output.zza(4, (zzsu) zza2);
                    }
                }
            }
            if (this.zzjA != null && this.zzjA.length > 0) {
                for (zza zza3 : this.zzjA) {
                    if (zza3 != null) {
                        output.zza(5, (zzsu) zza3);
                    }
                }
            }
            if (!this.zzjB.equals("")) {
                output.zzn(6, this.zzjB);
            }
            if (!this.zzjC.equals("")) {
                output.zzn(7, this.zzjC);
            }
            if (this.zzjD != 0) {
                output.zzb(8, this.zzjD);
            }
            if (this.zzjH) {
                output.zze(9, this.zzjH);
            }
            if (this.zzjG != null && this.zzjG.length > 0) {
                for (int zzA : this.zzjG) {
                    output.zzA(10, zzA);
                }
            }
            if (this.zzjF != null && this.zzjF.length > 0) {
                for (zza zza4 : this.zzjF) {
                    if (zza4 != null) {
                        output.zza(11, (zzsu) zza4);
                    }
                }
            }
            if (this.zzjE) {
                output.zze(12, this.zzjE);
            }
            super.writeTo(output);
        }

        public zza zzR() {
            this.type = 1;
            this.zzjx = "";
            this.zzjy = zzQ();
            this.zzjz = zzQ();
            this.zzjA = zzQ();
            this.zzjB = "";
            this.zzjC = "";
            this.zzjD = 0;
            this.zzjE = false;
            this.zzjF = zzQ();
            this.zzjG = zzsx.zzbuw;
            this.zzjH = false;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzk */
        public zza mergeFrom(zzsm zzsm) throws IOException {
            int i;
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        int zzJb = zzsm.zzJb();
                        switch (zzJb) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                this.type = zzJb;
                                break;
                            default:
                                continue;
                        }
                    case 18:
                        this.zzjx = zzsm.readString();
                        continue;
                    case 26:
                        int zzc = zzsx.zzc(zzsm, 26);
                        int length = this.zzjy == null ? 0 : this.zzjy.length;
                        zza[] zzaArr = new zza[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzjy, 0, zzaArr, 0, length);
                        }
                        while (length < zzaArr.length - 1) {
                            zzaArr[length] = new zza();
                            zzsm.zza(zzaArr[length]);
                            zzsm.zzIX();
                            length++;
                        }
                        zzaArr[length] = new zza();
                        zzsm.zza(zzaArr[length]);
                        this.zzjy = zzaArr;
                        continue;
                    case 34:
                        int zzc2 = zzsx.zzc(zzsm, 34);
                        int length2 = this.zzjz == null ? 0 : this.zzjz.length;
                        zza[] zzaArr2 = new zza[(zzc2 + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zzjz, 0, zzaArr2, 0, length2);
                        }
                        while (length2 < zzaArr2.length - 1) {
                            zzaArr2[length2] = new zza();
                            zzsm.zza(zzaArr2[length2]);
                            zzsm.zzIX();
                            length2++;
                        }
                        zzaArr2[length2] = new zza();
                        zzsm.zza(zzaArr2[length2]);
                        this.zzjz = zzaArr2;
                        continue;
                    case 42:
                        int zzc3 = zzsx.zzc(zzsm, 42);
                        int length3 = this.zzjA == null ? 0 : this.zzjA.length;
                        zza[] zzaArr3 = new zza[(zzc3 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zzjA, 0, zzaArr3, 0, length3);
                        }
                        while (length3 < zzaArr3.length - 1) {
                            zzaArr3[length3] = new zza();
                            zzsm.zza(zzaArr3[length3]);
                            zzsm.zzIX();
                            length3++;
                        }
                        zzaArr3[length3] = new zza();
                        zzsm.zza(zzaArr3[length3]);
                        this.zzjA = zzaArr3;
                        continue;
                    case 50:
                        this.zzjB = zzsm.readString();
                        continue;
                    case 58:
                        this.zzjC = zzsm.readString();
                        continue;
                    case 64:
                        this.zzjD = zzsm.zzJa();
                        continue;
                    case 72:
                        this.zzjH = zzsm.zzJc();
                        continue;
                    case 80:
                        int zzc4 = zzsx.zzc(zzsm, 80);
                        int[] iArr = new int[zzc4];
                        int i2 = 0;
                        int i3 = 0;
                        while (i2 < zzc4) {
                            if (i2 != 0) {
                                zzsm.zzIX();
                            }
                            int zzJb2 = zzsm.zzJb();
                            switch (zzJb2) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                    i = i3 + 1;
                                    iArr[i3] = zzJb2;
                                    break;
                                default:
                                    i = i3;
                                    break;
                            }
                            i2++;
                            i3 = i;
                        }
                        if (i3 != 0) {
                            int length4 = this.zzjG == null ? 0 : this.zzjG.length;
                            if (length4 != 0 || i3 != iArr.length) {
                                int[] iArr2 = new int[(length4 + i3)];
                                if (length4 != 0) {
                                    System.arraycopy(this.zzjG, 0, iArr2, 0, length4);
                                }
                                System.arraycopy(iArr, 0, iArr2, length4, i3);
                                this.zzjG = iArr2;
                                break;
                            } else {
                                this.zzjG = iArr;
                                break;
                            }
                        } else {
                            continue;
                        }
                    case 82:
                        int zzmq = zzsm.zzmq(zzsm.zzJf());
                        int position = zzsm.getPosition();
                        int i4 = 0;
                        while (zzsm.zzJk() > 0) {
                            switch (zzsm.zzJb()) {
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 9:
                                case 10:
                                case 11:
                                case 12:
                                case 13:
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                    i4++;
                                    break;
                            }
                        }
                        if (i4 != 0) {
                            zzsm.zzms(position);
                            int length5 = this.zzjG == null ? 0 : this.zzjG.length;
                            int[] iArr3 = new int[(i4 + length5)];
                            if (length5 != 0) {
                                System.arraycopy(this.zzjG, 0, iArr3, 0, length5);
                            }
                            while (zzsm.zzJk() > 0) {
                                int zzJb3 = zzsm.zzJb();
                                switch (zzJb3) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                    case 10:
                                    case 11:
                                    case 12:
                                    case 13:
                                    case 14:
                                    case 15:
                                    case 16:
                                    case 17:
                                        iArr3[length5] = zzJb3;
                                        length5++;
                                        break;
                                }
                            }
                            this.zzjG = iArr3;
                        }
                        zzsm.zzmr(zzmq);
                        continue;
                    case 90:
                        int zzc5 = zzsx.zzc(zzsm, 90);
                        int length6 = this.zzjF == null ? 0 : this.zzjF.length;
                        zza[] zzaArr4 = new zza[(zzc5 + length6)];
                        if (length6 != 0) {
                            System.arraycopy(this.zzjF, 0, zzaArr4, 0, length6);
                        }
                        while (length6 < zzaArr4.length - 1) {
                            zzaArr4[length6] = new zza();
                            zzsm.zza(zzaArr4[length6]);
                            zzsm.zzIX();
                            length6++;
                        }
                        zzaArr4[length6] = new zza();
                        zzsm.zza(zzaArr4[length6]);
                        this.zzjF = zzaArr4;
                        continue;
                    case 96:
                        this.zzjE = zzsm.zzJc();
                        continue;
                    default:
                        if (!zza(zzsm, zzIX)) {
                            break;
                        } else {
                            continue;
                        }
                }
            }
            return this;
        }

        /* access modifiers changed from: protected */
        public int zzz() {
            int zzz = super.zzz() + zzsn.zzC(1, this.type);
            if (!this.zzjx.equals("")) {
                zzz += zzsn.zzo(2, this.zzjx);
            }
            if (this.zzjy != null && this.zzjy.length > 0) {
                int i = zzz;
                for (zza zza : this.zzjy) {
                    if (zza != null) {
                        i += zzsn.zzc(3, (zzsu) zza);
                    }
                }
                zzz = i;
            }
            if (this.zzjz != null && this.zzjz.length > 0) {
                int i2 = zzz;
                for (zza zza2 : this.zzjz) {
                    if (zza2 != null) {
                        i2 += zzsn.zzc(4, (zzsu) zza2);
                    }
                }
                zzz = i2;
            }
            if (this.zzjA != null && this.zzjA.length > 0) {
                int i3 = zzz;
                for (zza zza3 : this.zzjA) {
                    if (zza3 != null) {
                        i3 += zzsn.zzc(5, (zzsu) zza3);
                    }
                }
                zzz = i3;
            }
            if (!this.zzjB.equals("")) {
                zzz += zzsn.zzo(6, this.zzjB);
            }
            if (!this.zzjC.equals("")) {
                zzz += zzsn.zzo(7, this.zzjC);
            }
            if (this.zzjD != 0) {
                zzz += zzsn.zzd(8, this.zzjD);
            }
            if (this.zzjH) {
                zzz += zzsn.zzf(9, this.zzjH);
            }
            if (this.zzjG != null && this.zzjG.length > 0) {
                int i4 = 0;
                for (int zzmx : this.zzjG) {
                    i4 += zzsn.zzmx(zzmx);
                }
                zzz = zzz + i4 + (this.zzjG.length * 1);
            }
            if (this.zzjF != null && this.zzjF.length > 0) {
                for (zza zza4 : this.zzjF) {
                    if (zza4 != null) {
                        zzz += zzsn.zzc(11, (zzsu) zza4);
                    }
                }
            }
            return this.zzjE ? zzz + zzsn.zzf(12, this.zzjE) : zzz;
        }
    }
}
