package com.google.android.gms.internal;

import com.google.android.gms.internal.zzaf;
import java.io.IOException;

public interface zzrq {

    public static final class zza extends zzso<zza> {
        public long zzbmd;
        public zzaf.zzj zzbme;
        public zzaf.zzf zzju;

        public zza() {
            zzHG();
        }

        public static zza zzy(byte[] bArr) throws zzst {
            return (zza) zzsu.mergeFrom(new zza(), bArr);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zza = (zza) o;
            if (this.zzbmd != zza.zzbmd) {
                return false;
            }
            if (this.zzju == null) {
                if (zza.zzju != null) {
                    return false;
                }
            } else if (!this.zzju.equals(zza.zzju)) {
                return false;
            }
            if (this.zzbme == null) {
                if (zza.zzbme != null) {
                    return false;
                }
            } else if (!this.zzbme.equals(zza.zzbme)) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zza.zzbuj == null || zza.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zza.zzbuj);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzbme == null ? 0 : this.zzbme.hashCode()) + (((this.zzju == null ? 0 : this.zzju.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzbmd ^ (this.zzbmd >>> 32)))) * 31)) * 31)) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i = this.zzbuj.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            output.zzb(1, this.zzbmd);
            if (this.zzju != null) {
                output.zza(2, (zzsu) this.zzju);
            }
            if (this.zzbme != null) {
                output.zza(3, (zzsu) this.zzbme);
            }
            super.writeTo(output);
        }

        public zza zzHG() {
            this.zzbmd = 0;
            this.zzju = null;
            this.zzbme = null;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzJ */
        public zza mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        this.zzbmd = zzsm.zzJa();
                        continue;
                    case 18:
                        if (this.zzju == null) {
                            this.zzju = new zzaf.zzf();
                        }
                        zzsm.zza(this.zzju);
                        continue;
                    case 26:
                        if (this.zzbme == null) {
                            this.zzbme = new zzaf.zzj();
                        }
                        zzsm.zza(this.zzbme);
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
            int zzz = super.zzz() + zzsn.zzd(1, this.zzbmd);
            if (this.zzju != null) {
                zzz += zzsn.zzc(2, (zzsu) this.zzju);
            }
            return this.zzbme != null ? zzz + zzsn.zzc(3, (zzsu) this.zzbme) : zzz;
        }
    }
}
