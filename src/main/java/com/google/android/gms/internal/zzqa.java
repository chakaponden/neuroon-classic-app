package com.google.android.gms.internal;

import com.google.android.gms.internal.zzpz;
import java.io.IOException;

public interface zzqa {

    public static final class zza extends zzsu {
        private static volatile zza[] zzaZR;
        public String name;
        public Boolean zzaZS;

        public zza() {
            zzDL();
        }

        public static zza[] zzDK() {
            if (zzaZR == null) {
                synchronized (zzss.zzbut) {
                    if (zzaZR == null) {
                        zzaZR = new zza[0];
                    }
                }
            }
            return zzaZR;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zza = (zza) o;
            if (this.name == null) {
                if (zza.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zza.name)) {
                return false;
            }
            return this.zzaZS == null ? zza.zzaZS == null : this.zzaZS.equals(zza.zzaZS);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31;
            if (this.zzaZS != null) {
                i = this.zzaZS.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.name != null) {
                output.zzn(1, this.name);
            }
            if (this.zzaZS != null) {
                output.zze(2, this.zzaZS.booleanValue());
            }
            super.writeTo(output);
        }

        public zza zzDL() {
            this.name = null;
            this.zzaZS = null;
            this.zzbuu = -1;
            return this;
        }

        /* access modifiers changed from: protected */
        public int zzz() {
            int zzz = super.zzz();
            if (this.name != null) {
                zzz += zzsn.zzo(1, this.name);
            }
            return this.zzaZS != null ? zzz + zzsn.zzf(2, this.zzaZS.booleanValue()) : zzz;
        }

        /* renamed from: zzz */
        public zza mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        this.name = zzsm.readString();
                        continue;
                    case 16:
                        this.zzaZS = Boolean.valueOf(zzsm.zzJc());
                        continue;
                    default:
                        if (!zzsx.zzb(zzsm, zzIX)) {
                            break;
                        } else {
                            continue;
                        }
                }
            }
            return this;
        }
    }

    public static final class zzb extends zzsu {
        public String zzaVt;
        public Long zzaZT;
        public Integer zzaZU;
        public zzc[] zzaZV;
        public zza[] zzaZW;
        public zzpz.zza[] zzaZX;

        public zzb() {
            zzDM();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb zzb = (zzb) o;
            if (this.zzaZT == null) {
                if (zzb.zzaZT != null) {
                    return false;
                }
            } else if (!this.zzaZT.equals(zzb.zzaZT)) {
                return false;
            }
            if (this.zzaVt == null) {
                if (zzb.zzaVt != null) {
                    return false;
                }
            } else if (!this.zzaVt.equals(zzb.zzaVt)) {
                return false;
            }
            if (this.zzaZU == null) {
                if (zzb.zzaZU != null) {
                    return false;
                }
            } else if (!this.zzaZU.equals(zzb.zzaZU)) {
                return false;
            }
            if (!zzss.equals((Object[]) this.zzaZV, (Object[]) zzb.zzaZV)) {
                return false;
            }
            if (!zzss.equals((Object[]) this.zzaZW, (Object[]) zzb.zzaZW)) {
                return false;
            }
            return zzss.equals((Object[]) this.zzaZX, (Object[]) zzb.zzaZX);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzaVt == null ? 0 : this.zzaVt.hashCode()) + (((this.zzaZT == null ? 0 : this.zzaZT.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
            if (this.zzaZU != null) {
                i = this.zzaZU.hashCode();
            }
            return ((((((hashCode + i) * 31) + zzss.hashCode((Object[]) this.zzaZV)) * 31) + zzss.hashCode((Object[]) this.zzaZW)) * 31) + zzss.hashCode((Object[]) this.zzaZX);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zzaZT != null) {
                output.zzb(1, this.zzaZT.longValue());
            }
            if (this.zzaVt != null) {
                output.zzn(2, this.zzaVt);
            }
            if (this.zzaZU != null) {
                output.zzA(3, this.zzaZU.intValue());
            }
            if (this.zzaZV != null && this.zzaZV.length > 0) {
                for (zzc zzc : this.zzaZV) {
                    if (zzc != null) {
                        output.zza(4, (zzsu) zzc);
                    }
                }
            }
            if (this.zzaZW != null && this.zzaZW.length > 0) {
                for (zza zza : this.zzaZW) {
                    if (zza != null) {
                        output.zza(5, (zzsu) zza);
                    }
                }
            }
            if (this.zzaZX != null && this.zzaZX.length > 0) {
                for (zzpz.zza zza2 : this.zzaZX) {
                    if (zza2 != null) {
                        output.zza(6, (zzsu) zza2);
                    }
                }
            }
            super.writeTo(output);
        }

        /* renamed from: zzA */
        public zzb mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        this.zzaZT = Long.valueOf(zzsm.zzJa());
                        continue;
                    case 18:
                        this.zzaVt = zzsm.readString();
                        continue;
                    case 24:
                        this.zzaZU = Integer.valueOf(zzsm.zzJb());
                        continue;
                    case 34:
                        int zzc = zzsx.zzc(zzsm, 34);
                        int length = this.zzaZV == null ? 0 : this.zzaZV.length;
                        zzc[] zzcArr = new zzc[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzaZV, 0, zzcArr, 0, length);
                        }
                        while (length < zzcArr.length - 1) {
                            zzcArr[length] = new zzc();
                            zzsm.zza(zzcArr[length]);
                            zzsm.zzIX();
                            length++;
                        }
                        zzcArr[length] = new zzc();
                        zzsm.zza(zzcArr[length]);
                        this.zzaZV = zzcArr;
                        continue;
                    case 42:
                        int zzc2 = zzsx.zzc(zzsm, 42);
                        int length2 = this.zzaZW == null ? 0 : this.zzaZW.length;
                        zza[] zzaArr = new zza[(zzc2 + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zzaZW, 0, zzaArr, 0, length2);
                        }
                        while (length2 < zzaArr.length - 1) {
                            zzaArr[length2] = new zza();
                            zzsm.zza(zzaArr[length2]);
                            zzsm.zzIX();
                            length2++;
                        }
                        zzaArr[length2] = new zza();
                        zzsm.zza(zzaArr[length2]);
                        this.zzaZW = zzaArr;
                        continue;
                    case 50:
                        int zzc3 = zzsx.zzc(zzsm, 50);
                        int length3 = this.zzaZX == null ? 0 : this.zzaZX.length;
                        zzpz.zza[] zzaArr2 = new zzpz.zza[(zzc3 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zzaZX, 0, zzaArr2, 0, length3);
                        }
                        while (length3 < zzaArr2.length - 1) {
                            zzaArr2[length3] = new zzpz.zza();
                            zzsm.zza(zzaArr2[length3]);
                            zzsm.zzIX();
                            length3++;
                        }
                        zzaArr2[length3] = new zzpz.zza();
                        zzsm.zza(zzaArr2[length3]);
                        this.zzaZX = zzaArr2;
                        continue;
                    default:
                        if (!zzsx.zzb(zzsm, zzIX)) {
                            break;
                        } else {
                            continue;
                        }
                }
            }
            return this;
        }

        public zzb zzDM() {
            this.zzaZT = null;
            this.zzaVt = null;
            this.zzaZU = null;
            this.zzaZV = zzc.zzDN();
            this.zzaZW = zza.zzDK();
            this.zzaZX = zzpz.zza.zzDA();
            this.zzbuu = -1;
            return this;
        }

        /* access modifiers changed from: protected */
        public int zzz() {
            int zzz = super.zzz();
            if (this.zzaZT != null) {
                zzz += zzsn.zzd(1, this.zzaZT.longValue());
            }
            if (this.zzaVt != null) {
                zzz += zzsn.zzo(2, this.zzaVt);
            }
            if (this.zzaZU != null) {
                zzz += zzsn.zzC(3, this.zzaZU.intValue());
            }
            if (this.zzaZV != null && this.zzaZV.length > 0) {
                int i = zzz;
                for (zzc zzc : this.zzaZV) {
                    if (zzc != null) {
                        i += zzsn.zzc(4, (zzsu) zzc);
                    }
                }
                zzz = i;
            }
            if (this.zzaZW != null && this.zzaZW.length > 0) {
                int i2 = zzz;
                for (zza zza : this.zzaZW) {
                    if (zza != null) {
                        i2 += zzsn.zzc(5, (zzsu) zza);
                    }
                }
                zzz = i2;
            }
            if (this.zzaZX != null && this.zzaZX.length > 0) {
                for (zzpz.zza zza2 : this.zzaZX) {
                    if (zza2 != null) {
                        zzz += zzsn.zzc(6, (zzsu) zza2);
                    }
                }
            }
            return zzz;
        }
    }

    public static final class zzc extends zzsu {
        private static volatile zzc[] zzaZY;
        public String key;
        public String value;

        public zzc() {
            zzDO();
        }

        public static zzc[] zzDN() {
            if (zzaZY == null) {
                synchronized (zzss.zzbut) {
                    if (zzaZY == null) {
                        zzaZY = new zzc[0];
                    }
                }
            }
            return zzaZY;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzc)) {
                return false;
            }
            zzc zzc = (zzc) o;
            if (this.key == null) {
                if (zzc.key != null) {
                    return false;
                }
            } else if (!this.key.equals(zzc.key)) {
                return false;
            }
            return this.value == null ? zzc.value == null : this.value.equals(zzc.value);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31;
            if (this.value != null) {
                i = this.value.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.key != null) {
                output.zzn(1, this.key);
            }
            if (this.value != null) {
                output.zzn(2, this.value);
            }
            super.writeTo(output);
        }

        /* renamed from: zzB */
        public zzc mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        this.key = zzsm.readString();
                        continue;
                    case 18:
                        this.value = zzsm.readString();
                        continue;
                    default:
                        if (!zzsx.zzb(zzsm, zzIX)) {
                            break;
                        } else {
                            continue;
                        }
                }
            }
            return this;
        }

        public zzc zzDO() {
            this.key = null;
            this.value = null;
            this.zzbuu = -1;
            return this;
        }

        /* access modifiers changed from: protected */
        public int zzz() {
            int zzz = super.zzz();
            if (this.key != null) {
                zzz += zzsn.zzo(1, this.key);
            }
            return this.value != null ? zzz + zzsn.zzo(2, this.value) : zzz;
        }
    }
}
