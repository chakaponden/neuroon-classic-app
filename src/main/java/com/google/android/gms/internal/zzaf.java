package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import com.google.android.gms.internal.zzag;
import java.io.IOException;

public interface zzaf {

    public static final class zza extends zzso<zza> {
        public int level;
        public int zziq;
        public int zzir;

        public zza() {
            zzB();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zza = (zza) o;
            if (this.level != zza.level || this.zziq != zza.zziq || this.zzir != zza.zzir) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zza.zzbuj == null || zza.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zza.zzbuj);
        }

        public int hashCode() {
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((((((((getClass().getName().hashCode() + 527) * 31) + this.level) * 31) + this.zziq) * 31) + this.zzir) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.level != 1) {
                output.zzA(1, this.level);
            }
            if (this.zziq != 0) {
                output.zzA(2, this.zziq);
            }
            if (this.zzir != 0) {
                output.zzA(3, this.zzir);
            }
            super.writeTo(output);
        }

        public zza zzB() {
            this.level = 1;
            this.zziq = 0;
            this.zzir = 0;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zza */
        public zza mergeFrom(zzsm zzsm) throws IOException {
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
                                this.level = zzJb;
                                break;
                            default:
                                continue;
                        }
                    case 16:
                        this.zziq = zzsm.zzJb();
                        continue;
                    case 24:
                        this.zzir = zzsm.zzJb();
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
            int zzz = super.zzz();
            if (this.level != 1) {
                zzz += zzsn.zzC(1, this.level);
            }
            if (this.zziq != 0) {
                zzz += zzsn.zzC(2, this.zziq);
            }
            return this.zzir != 0 ? zzz + zzsn.zzC(3, this.zzir) : zzz;
        }
    }

    public static final class zzb extends zzso<zzb> {
        private static volatile zzb[] zzis;
        public int name;
        public int[] zzit;
        public int zziu;
        public boolean zziv;
        public boolean zziw;

        public zzb() {
            zzD();
        }

        public static zzb[] zzC() {
            if (zzis == null) {
                synchronized (zzss.zzbut) {
                    if (zzis == null) {
                        zzis = new zzb[0];
                    }
                }
            }
            return zzis;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb zzb = (zzb) o;
            if (!zzss.equals(this.zzit, zzb.zzit) || this.zziu != zzb.zziu || this.name != zzb.name || this.zziv != zzb.zziv || this.zziw != zzb.zziw) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzb.zzbuj == null || zzb.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzb.zzbuj);
        }

        public int hashCode() {
            int i = 1231;
            int hashCode = ((this.zziv ? 1231 : 1237) + ((((((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode(this.zzit)) * 31) + this.zziu) * 31) + this.name) * 31)) * 31;
            if (!this.zziw) {
                i = 1237;
            }
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((hashCode + i) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zziw) {
                output.zze(1, this.zziw);
            }
            output.zzA(2, this.zziu);
            if (this.zzit != null && this.zzit.length > 0) {
                for (int zzA : this.zzit) {
                    output.zzA(3, zzA);
                }
            }
            if (this.name != 0) {
                output.zzA(4, this.name);
            }
            if (this.zziv) {
                output.zze(6, this.zziv);
            }
            super.writeTo(output);
        }

        public zzb zzD() {
            this.zzit = zzsx.zzbuw;
            this.zziu = 0;
            this.name = 0;
            this.zziv = false;
            this.zziw = false;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzb */
        public zzb mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        this.zziw = zzsm.zzJc();
                        continue;
                    case 16:
                        this.zziu = zzsm.zzJb();
                        continue;
                    case 24:
                        int zzc = zzsx.zzc(zzsm, 24);
                        int length = this.zzit == null ? 0 : this.zzit.length;
                        int[] iArr = new int[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzit, 0, iArr, 0, length);
                        }
                        while (length < iArr.length - 1) {
                            iArr[length] = zzsm.zzJb();
                            zzsm.zzIX();
                            length++;
                        }
                        iArr[length] = zzsm.zzJb();
                        this.zzit = iArr;
                        continue;
                    case 26:
                        int zzmq = zzsm.zzmq(zzsm.zzJf());
                        int position = zzsm.getPosition();
                        int i = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i++;
                        }
                        zzsm.zzms(position);
                        int length2 = this.zzit == null ? 0 : this.zzit.length;
                        int[] iArr2 = new int[(i + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zzit, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length) {
                            iArr2[length2] = zzsm.zzJb();
                            length2++;
                        }
                        this.zzit = iArr2;
                        zzsm.zzmr(zzmq);
                        continue;
                    case 32:
                        this.name = zzsm.zzJb();
                        continue;
                    case 48:
                        this.zziv = zzsm.zzJc();
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
            int i;
            int i2 = 0;
            int zzz = super.zzz();
            if (this.zziw) {
                zzz += zzsn.zzf(1, this.zziw);
            }
            int zzC = zzsn.zzC(2, this.zziu) + zzz;
            if (this.zzit == null || this.zzit.length <= 0) {
                i = zzC;
            } else {
                for (int zzmx : this.zzit) {
                    i2 += zzsn.zzmx(zzmx);
                }
                i = zzC + i2 + (this.zzit.length * 1);
            }
            if (this.name != 0) {
                i += zzsn.zzC(4, this.name);
            }
            return this.zziv ? i + zzsn.zzf(6, this.zziv) : i;
        }
    }

    public static final class zzc extends zzso<zzc> {
        private static volatile zzc[] zzix;
        public String key;
        public boolean zziA;
        public long zziB;
        public long zziy;
        public long zziz;

        public zzc() {
            zzF();
        }

        public static zzc[] zzE() {
            if (zzix == null) {
                synchronized (zzss.zzbut) {
                    if (zzix == null) {
                        zzix = new zzc[0];
                    }
                }
            }
            return zzix;
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
            if (this.zziy != zzc.zziy || this.zziz != zzc.zziz || this.zziA != zzc.zziA || this.zziB != zzc.zziB) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzc.zzbuj == null || zzc.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzc.zzbuj);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((this.zziA ? 1231 : 1237) + (((((((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + ((int) (this.zziy ^ (this.zziy >>> 32)))) * 31) + ((int) (this.zziz ^ (this.zziz >>> 32)))) * 31)) * 31) + ((int) (this.zziB ^ (this.zziB >>> 32)))) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i = this.zzbuj.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (!this.key.equals("")) {
                output.zzn(1, this.key);
            }
            if (this.zziy != 0) {
                output.zzb(2, this.zziy);
            }
            if (this.zziz != 2147483647L) {
                output.zzb(3, this.zziz);
            }
            if (this.zziA) {
                output.zze(4, this.zziA);
            }
            if (this.zziB != 0) {
                output.zzb(5, this.zziB);
            }
            super.writeTo(output);
        }

        public zzc zzF() {
            this.key = "";
            this.zziy = 0;
            this.zziz = 2147483647L;
            this.zziA = false;
            this.zziB = 0;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzc */
        public zzc mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        this.key = zzsm.readString();
                        continue;
                    case 16:
                        this.zziy = zzsm.zzJa();
                        continue;
                    case 24:
                        this.zziz = zzsm.zzJa();
                        continue;
                    case 32:
                        this.zziA = zzsm.zzJc();
                        continue;
                    case 40:
                        this.zziB = zzsm.zzJa();
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
            int zzz = super.zzz();
            if (!this.key.equals("")) {
                zzz += zzsn.zzo(1, this.key);
            }
            if (this.zziy != 0) {
                zzz += zzsn.zzd(2, this.zziy);
            }
            if (this.zziz != 2147483647L) {
                zzz += zzsn.zzd(3, this.zziz);
            }
            if (this.zziA) {
                zzz += zzsn.zzf(4, this.zziA);
            }
            return this.zziB != 0 ? zzz + zzsn.zzd(5, this.zziB) : zzz;
        }
    }

    public static final class zzd extends zzso<zzd> {
        public zzag.zza[] zziC;
        public zzag.zza[] zziD;
        public zzc[] zziE;

        public zzd() {
            zzG();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            zzd zzd = (zzd) o;
            if (!zzss.equals((Object[]) this.zziC, (Object[]) zzd.zziC) || !zzss.equals((Object[]) this.zziD, (Object[]) zzd.zziD) || !zzss.equals((Object[]) this.zziE, (Object[]) zzd.zziE)) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzd.zzbuj == null || zzd.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzd.zzbuj);
        }

        public int hashCode() {
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((((((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode((Object[]) this.zziC)) * 31) + zzss.hashCode((Object[]) this.zziD)) * 31) + zzss.hashCode((Object[]) this.zziE)) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zziC != null && this.zziC.length > 0) {
                for (zzag.zza zza : this.zziC) {
                    if (zza != null) {
                        output.zza(1, (zzsu) zza);
                    }
                }
            }
            if (this.zziD != null && this.zziD.length > 0) {
                for (zzag.zza zza2 : this.zziD) {
                    if (zza2 != null) {
                        output.zza(2, (zzsu) zza2);
                    }
                }
            }
            if (this.zziE != null && this.zziE.length > 0) {
                for (zzc zzc : this.zziE) {
                    if (zzc != null) {
                        output.zza(3, (zzsu) zzc);
                    }
                }
            }
            super.writeTo(output);
        }

        public zzd zzG() {
            this.zziC = zzag.zza.zzQ();
            this.zziD = zzag.zza.zzQ();
            this.zziE = zzc.zzE();
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzd */
        public zzd mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        int zzc = zzsx.zzc(zzsm, 10);
                        int length = this.zziC == null ? 0 : this.zziC.length;
                        zzag.zza[] zzaArr = new zzag.zza[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zziC, 0, zzaArr, 0, length);
                        }
                        while (length < zzaArr.length - 1) {
                            zzaArr[length] = new zzag.zza();
                            zzsm.zza(zzaArr[length]);
                            zzsm.zzIX();
                            length++;
                        }
                        zzaArr[length] = new zzag.zza();
                        zzsm.zza(zzaArr[length]);
                        this.zziC = zzaArr;
                        continue;
                    case 18:
                        int zzc2 = zzsx.zzc(zzsm, 18);
                        int length2 = this.zziD == null ? 0 : this.zziD.length;
                        zzag.zza[] zzaArr2 = new zzag.zza[(zzc2 + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zziD, 0, zzaArr2, 0, length2);
                        }
                        while (length2 < zzaArr2.length - 1) {
                            zzaArr2[length2] = new zzag.zza();
                            zzsm.zza(zzaArr2[length2]);
                            zzsm.zzIX();
                            length2++;
                        }
                        zzaArr2[length2] = new zzag.zza();
                        zzsm.zza(zzaArr2[length2]);
                        this.zziD = zzaArr2;
                        continue;
                    case 26:
                        int zzc3 = zzsx.zzc(zzsm, 26);
                        int length3 = this.zziE == null ? 0 : this.zziE.length;
                        zzc[] zzcArr = new zzc[(zzc3 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zziE, 0, zzcArr, 0, length3);
                        }
                        while (length3 < zzcArr.length - 1) {
                            zzcArr[length3] = new zzc();
                            zzsm.zza(zzcArr[length3]);
                            zzsm.zzIX();
                            length3++;
                        }
                        zzcArr[length3] = new zzc();
                        zzsm.zza(zzcArr[length3]);
                        this.zziE = zzcArr;
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
            int zzz = super.zzz();
            if (this.zziC != null && this.zziC.length > 0) {
                int i = zzz;
                for (zzag.zza zza : this.zziC) {
                    if (zza != null) {
                        i += zzsn.zzc(1, (zzsu) zza);
                    }
                }
                zzz = i;
            }
            if (this.zziD != null && this.zziD.length > 0) {
                int i2 = zzz;
                for (zzag.zza zza2 : this.zziD) {
                    if (zza2 != null) {
                        i2 += zzsn.zzc(2, (zzsu) zza2);
                    }
                }
                zzz = i2;
            }
            if (this.zziE != null && this.zziE.length > 0) {
                for (zzc zzc : this.zziE) {
                    if (zzc != null) {
                        zzz += zzsn.zzc(3, (zzsu) zzc);
                    }
                }
            }
            return zzz;
        }
    }

    public static final class zze extends zzso<zze> {
        private static volatile zze[] zziF;
        public int key;
        public int value;

        public zze() {
            zzI();
        }

        public static zze[] zzH() {
            if (zziF == null) {
                synchronized (zzss.zzbut) {
                    if (zziF == null) {
                        zziF = new zze[0];
                    }
                }
            }
            return zziF;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            zze zze = (zze) o;
            if (this.key != zze.key || this.value != zze.value) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zze.zzbuj == null || zze.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zze.zzbuj);
        }

        public int hashCode() {
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + this.key) * 31) + this.value) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            output.zzA(1, this.key);
            output.zzA(2, this.value);
            super.writeTo(output);
        }

        public zze zzI() {
            this.key = 0;
            this.value = 0;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zze */
        public zze mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        this.key = zzsm.zzJb();
                        continue;
                    case 16:
                        this.value = zzsm.zzJb();
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
            return super.zzz() + zzsn.zzC(1, this.key) + zzsn.zzC(2, this.value);
        }
    }

    public static final class zzf extends zzso<zzf> {
        public String version;
        public String[] zziG;
        public String[] zziH;
        public zzag.zza[] zziI;
        public zze[] zziJ;
        public zzb[] zziK;
        public zzb[] zziL;
        public zzb[] zziM;
        public zzg[] zziN;
        public String zziO;
        public String zziP;
        public String zziQ;
        public zza zziR;
        public float zziS;
        public boolean zziT;
        public String[] zziU;
        public int zziV;

        public zzf() {
            zzJ();
        }

        public static zzf zzc(byte[] bArr) throws zzst {
            return (zzf) zzsu.mergeFrom(new zzf(), bArr);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzf)) {
                return false;
            }
            zzf zzf = (zzf) o;
            if (!zzss.equals((Object[]) this.zziG, (Object[]) zzf.zziG) || !zzss.equals((Object[]) this.zziH, (Object[]) zzf.zziH) || !zzss.equals((Object[]) this.zziI, (Object[]) zzf.zziI) || !zzss.equals((Object[]) this.zziJ, (Object[]) zzf.zziJ) || !zzss.equals((Object[]) this.zziK, (Object[]) zzf.zziK) || !zzss.equals((Object[]) this.zziL, (Object[]) zzf.zziL) || !zzss.equals((Object[]) this.zziM, (Object[]) zzf.zziM) || !zzss.equals((Object[]) this.zziN, (Object[]) zzf.zziN)) {
                return false;
            }
            if (this.zziO == null) {
                if (zzf.zziO != null) {
                    return false;
                }
            } else if (!this.zziO.equals(zzf.zziO)) {
                return false;
            }
            if (this.zziP == null) {
                if (zzf.zziP != null) {
                    return false;
                }
            } else if (!this.zziP.equals(zzf.zziP)) {
                return false;
            }
            if (this.zziQ == null) {
                if (zzf.zziQ != null) {
                    return false;
                }
            } else if (!this.zziQ.equals(zzf.zziQ)) {
                return false;
            }
            if (this.version == null) {
                if (zzf.version != null) {
                    return false;
                }
            } else if (!this.version.equals(zzf.version)) {
                return false;
            }
            if (this.zziR == null) {
                if (zzf.zziR != null) {
                    return false;
                }
            } else if (!this.zziR.equals(zzf.zziR)) {
                return false;
            }
            if (Float.floatToIntBits(this.zziS) != Float.floatToIntBits(zzf.zziS) || this.zziT != zzf.zziT || !zzss.equals((Object[]) this.zziU, (Object[]) zzf.zziU) || this.zziV != zzf.zziV) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzf.zzbuj == null || zzf.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzf.zzbuj);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((((this.zziT ? 1231 : 1237) + (((((this.zziR == null ? 0 : this.zziR.hashCode()) + (((this.version == null ? 0 : this.version.hashCode()) + (((this.zziQ == null ? 0 : this.zziQ.hashCode()) + (((this.zziP == null ? 0 : this.zziP.hashCode()) + (((this.zziO == null ? 0 : this.zziO.hashCode()) + ((((((((((((((((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode((Object[]) this.zziG)) * 31) + zzss.hashCode((Object[]) this.zziH)) * 31) + zzss.hashCode((Object[]) this.zziI)) * 31) + zzss.hashCode((Object[]) this.zziJ)) * 31) + zzss.hashCode((Object[]) this.zziK)) * 31) + zzss.hashCode((Object[]) this.zziL)) * 31) + zzss.hashCode((Object[]) this.zziM)) * 31) + zzss.hashCode((Object[]) this.zziN)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + Float.floatToIntBits(this.zziS)) * 31)) * 31) + zzss.hashCode((Object[]) this.zziU)) * 31) + this.zziV) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i = this.zzbuj.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zziH != null && this.zziH.length > 0) {
                for (String str : this.zziH) {
                    if (str != null) {
                        output.zzn(1, str);
                    }
                }
            }
            if (this.zziI != null && this.zziI.length > 0) {
                for (zzag.zza zza : this.zziI) {
                    if (zza != null) {
                        output.zza(2, (zzsu) zza);
                    }
                }
            }
            if (this.zziJ != null && this.zziJ.length > 0) {
                for (zze zze : this.zziJ) {
                    if (zze != null) {
                        output.zza(3, (zzsu) zze);
                    }
                }
            }
            if (this.zziK != null && this.zziK.length > 0) {
                for (zzb zzb : this.zziK) {
                    if (zzb != null) {
                        output.zza(4, (zzsu) zzb);
                    }
                }
            }
            if (this.zziL != null && this.zziL.length > 0) {
                for (zzb zzb2 : this.zziL) {
                    if (zzb2 != null) {
                        output.zza(5, (zzsu) zzb2);
                    }
                }
            }
            if (this.zziM != null && this.zziM.length > 0) {
                for (zzb zzb3 : this.zziM) {
                    if (zzb3 != null) {
                        output.zza(6, (zzsu) zzb3);
                    }
                }
            }
            if (this.zziN != null && this.zziN.length > 0) {
                for (zzg zzg : this.zziN) {
                    if (zzg != null) {
                        output.zza(7, (zzsu) zzg);
                    }
                }
            }
            if (!this.zziO.equals("")) {
                output.zzn(9, this.zziO);
            }
            if (!this.zziP.equals("")) {
                output.zzn(10, this.zziP);
            }
            if (!this.zziQ.equals("0")) {
                output.zzn(12, this.zziQ);
            }
            if (!this.version.equals("")) {
                output.zzn(13, this.version);
            }
            if (this.zziR != null) {
                output.zza(14, (zzsu) this.zziR);
            }
            if (Float.floatToIntBits(this.zziS) != Float.floatToIntBits(0.0f)) {
                output.zzb(15, this.zziS);
            }
            if (this.zziU != null && this.zziU.length > 0) {
                for (String str2 : this.zziU) {
                    if (str2 != null) {
                        output.zzn(16, str2);
                    }
                }
            }
            if (this.zziV != 0) {
                output.zzA(17, this.zziV);
            }
            if (this.zziT) {
                output.zze(18, this.zziT);
            }
            if (this.zziG != null && this.zziG.length > 0) {
                for (String str3 : this.zziG) {
                    if (str3 != null) {
                        output.zzn(19, str3);
                    }
                }
            }
            super.writeTo(output);
        }

        public zzf zzJ() {
            this.zziG = zzsx.zzbuB;
            this.zziH = zzsx.zzbuB;
            this.zziI = zzag.zza.zzQ();
            this.zziJ = zze.zzH();
            this.zziK = zzb.zzC();
            this.zziL = zzb.zzC();
            this.zziM = zzb.zzC();
            this.zziN = zzg.zzK();
            this.zziO = "";
            this.zziP = "";
            this.zziQ = "0";
            this.version = "";
            this.zziR = null;
            this.zziS = 0.0f;
            this.zziT = false;
            this.zziU = zzsx.zzbuB;
            this.zziV = 0;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzf */
        public zzf mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        int zzc = zzsx.zzc(zzsm, 10);
                        int length = this.zziH == null ? 0 : this.zziH.length;
                        String[] strArr = new String[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zziH, 0, strArr, 0, length);
                        }
                        while (length < strArr.length - 1) {
                            strArr[length] = zzsm.readString();
                            zzsm.zzIX();
                            length++;
                        }
                        strArr[length] = zzsm.readString();
                        this.zziH = strArr;
                        continue;
                    case 18:
                        int zzc2 = zzsx.zzc(zzsm, 18);
                        int length2 = this.zziI == null ? 0 : this.zziI.length;
                        zzag.zza[] zzaArr = new zzag.zza[(zzc2 + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zziI, 0, zzaArr, 0, length2);
                        }
                        while (length2 < zzaArr.length - 1) {
                            zzaArr[length2] = new zzag.zza();
                            zzsm.zza(zzaArr[length2]);
                            zzsm.zzIX();
                            length2++;
                        }
                        zzaArr[length2] = new zzag.zza();
                        zzsm.zza(zzaArr[length2]);
                        this.zziI = zzaArr;
                        continue;
                    case 26:
                        int zzc3 = zzsx.zzc(zzsm, 26);
                        int length3 = this.zziJ == null ? 0 : this.zziJ.length;
                        zze[] zzeArr = new zze[(zzc3 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zziJ, 0, zzeArr, 0, length3);
                        }
                        while (length3 < zzeArr.length - 1) {
                            zzeArr[length3] = new zze();
                            zzsm.zza(zzeArr[length3]);
                            zzsm.zzIX();
                            length3++;
                        }
                        zzeArr[length3] = new zze();
                        zzsm.zza(zzeArr[length3]);
                        this.zziJ = zzeArr;
                        continue;
                    case 34:
                        int zzc4 = zzsx.zzc(zzsm, 34);
                        int length4 = this.zziK == null ? 0 : this.zziK.length;
                        zzb[] zzbArr = new zzb[(zzc4 + length4)];
                        if (length4 != 0) {
                            System.arraycopy(this.zziK, 0, zzbArr, 0, length4);
                        }
                        while (length4 < zzbArr.length - 1) {
                            zzbArr[length4] = new zzb();
                            zzsm.zza(zzbArr[length4]);
                            zzsm.zzIX();
                            length4++;
                        }
                        zzbArr[length4] = new zzb();
                        zzsm.zza(zzbArr[length4]);
                        this.zziK = zzbArr;
                        continue;
                    case 42:
                        int zzc5 = zzsx.zzc(zzsm, 42);
                        int length5 = this.zziL == null ? 0 : this.zziL.length;
                        zzb[] zzbArr2 = new zzb[(zzc5 + length5)];
                        if (length5 != 0) {
                            System.arraycopy(this.zziL, 0, zzbArr2, 0, length5);
                        }
                        while (length5 < zzbArr2.length - 1) {
                            zzbArr2[length5] = new zzb();
                            zzsm.zza(zzbArr2[length5]);
                            zzsm.zzIX();
                            length5++;
                        }
                        zzbArr2[length5] = new zzb();
                        zzsm.zza(zzbArr2[length5]);
                        this.zziL = zzbArr2;
                        continue;
                    case 50:
                        int zzc6 = zzsx.zzc(zzsm, 50);
                        int length6 = this.zziM == null ? 0 : this.zziM.length;
                        zzb[] zzbArr3 = new zzb[(zzc6 + length6)];
                        if (length6 != 0) {
                            System.arraycopy(this.zziM, 0, zzbArr3, 0, length6);
                        }
                        while (length6 < zzbArr3.length - 1) {
                            zzbArr3[length6] = new zzb();
                            zzsm.zza(zzbArr3[length6]);
                            zzsm.zzIX();
                            length6++;
                        }
                        zzbArr3[length6] = new zzb();
                        zzsm.zza(zzbArr3[length6]);
                        this.zziM = zzbArr3;
                        continue;
                    case 58:
                        int zzc7 = zzsx.zzc(zzsm, 58);
                        int length7 = this.zziN == null ? 0 : this.zziN.length;
                        zzg[] zzgArr = new zzg[(zzc7 + length7)];
                        if (length7 != 0) {
                            System.arraycopy(this.zziN, 0, zzgArr, 0, length7);
                        }
                        while (length7 < zzgArr.length - 1) {
                            zzgArr[length7] = new zzg();
                            zzsm.zza(zzgArr[length7]);
                            zzsm.zzIX();
                            length7++;
                        }
                        zzgArr[length7] = new zzg();
                        zzsm.zza(zzgArr[length7]);
                        this.zziN = zzgArr;
                        continue;
                    case 74:
                        this.zziO = zzsm.readString();
                        continue;
                    case 82:
                        this.zziP = zzsm.readString();
                        continue;
                    case 98:
                        this.zziQ = zzsm.readString();
                        continue;
                    case 106:
                        this.version = zzsm.readString();
                        continue;
                    case 114:
                        if (this.zziR == null) {
                            this.zziR = new zza();
                        }
                        zzsm.zza(this.zziR);
                        continue;
                    case 125:
                        this.zziS = zzsm.readFloat();
                        continue;
                    case TransportMediator.KEYCODE_MEDIA_RECORD:
                        int zzc8 = zzsx.zzc(zzsm, TransportMediator.KEYCODE_MEDIA_RECORD);
                        int length8 = this.zziU == null ? 0 : this.zziU.length;
                        String[] strArr2 = new String[(zzc8 + length8)];
                        if (length8 != 0) {
                            System.arraycopy(this.zziU, 0, strArr2, 0, length8);
                        }
                        while (length8 < strArr2.length - 1) {
                            strArr2[length8] = zzsm.readString();
                            zzsm.zzIX();
                            length8++;
                        }
                        strArr2[length8] = zzsm.readString();
                        this.zziU = strArr2;
                        continue;
                    case 136:
                        this.zziV = zzsm.zzJb();
                        continue;
                    case 144:
                        this.zziT = zzsm.zzJc();
                        continue;
                    case 154:
                        int zzc9 = zzsx.zzc(zzsm, 154);
                        int length9 = this.zziG == null ? 0 : this.zziG.length;
                        String[] strArr3 = new String[(zzc9 + length9)];
                        if (length9 != 0) {
                            System.arraycopy(this.zziG, 0, strArr3, 0, length9);
                        }
                        while (length9 < strArr3.length - 1) {
                            strArr3[length9] = zzsm.readString();
                            zzsm.zzIX();
                            length9++;
                        }
                        strArr3[length9] = zzsm.readString();
                        this.zziG = strArr3;
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
            int i;
            int zzz = super.zzz();
            if (this.zziH == null || this.zziH.length <= 0) {
                i = zzz;
            } else {
                int i2 = 0;
                int i3 = 0;
                for (String str : this.zziH) {
                    if (str != null) {
                        i3++;
                        i2 += zzsn.zzgO(str);
                    }
                }
                i = zzz + i2 + (i3 * 1);
            }
            if (this.zziI != null && this.zziI.length > 0) {
                int i4 = i;
                for (zzag.zza zza : this.zziI) {
                    if (zza != null) {
                        i4 += zzsn.zzc(2, (zzsu) zza);
                    }
                }
                i = i4;
            }
            if (this.zziJ != null && this.zziJ.length > 0) {
                int i5 = i;
                for (zze zze : this.zziJ) {
                    if (zze != null) {
                        i5 += zzsn.zzc(3, (zzsu) zze);
                    }
                }
                i = i5;
            }
            if (this.zziK != null && this.zziK.length > 0) {
                int i6 = i;
                for (zzb zzb : this.zziK) {
                    if (zzb != null) {
                        i6 += zzsn.zzc(4, (zzsu) zzb);
                    }
                }
                i = i6;
            }
            if (this.zziL != null && this.zziL.length > 0) {
                int i7 = i;
                for (zzb zzb2 : this.zziL) {
                    if (zzb2 != null) {
                        i7 += zzsn.zzc(5, (zzsu) zzb2);
                    }
                }
                i = i7;
            }
            if (this.zziM != null && this.zziM.length > 0) {
                int i8 = i;
                for (zzb zzb3 : this.zziM) {
                    if (zzb3 != null) {
                        i8 += zzsn.zzc(6, (zzsu) zzb3);
                    }
                }
                i = i8;
            }
            if (this.zziN != null && this.zziN.length > 0) {
                int i9 = i;
                for (zzg zzg : this.zziN) {
                    if (zzg != null) {
                        i9 += zzsn.zzc(7, (zzsu) zzg);
                    }
                }
                i = i9;
            }
            if (!this.zziO.equals("")) {
                i += zzsn.zzo(9, this.zziO);
            }
            if (!this.zziP.equals("")) {
                i += zzsn.zzo(10, this.zziP);
            }
            if (!this.zziQ.equals("0")) {
                i += zzsn.zzo(12, this.zziQ);
            }
            if (!this.version.equals("")) {
                i += zzsn.zzo(13, this.version);
            }
            if (this.zziR != null) {
                i += zzsn.zzc(14, (zzsu) this.zziR);
            }
            if (Float.floatToIntBits(this.zziS) != Float.floatToIntBits(0.0f)) {
                i += zzsn.zzc(15, this.zziS);
            }
            if (this.zziU != null && this.zziU.length > 0) {
                int i10 = 0;
                int i11 = 0;
                for (String str2 : this.zziU) {
                    if (str2 != null) {
                        i11++;
                        i10 += zzsn.zzgO(str2);
                    }
                }
                i = i + i10 + (i11 * 2);
            }
            if (this.zziV != 0) {
                i += zzsn.zzC(17, this.zziV);
            }
            if (this.zziT) {
                i += zzsn.zzf(18, this.zziT);
            }
            if (this.zziG == null || this.zziG.length <= 0) {
                return i;
            }
            int i12 = 0;
            int i13 = 0;
            for (String str3 : this.zziG) {
                if (str3 != null) {
                    i13++;
                    i12 += zzsn.zzgO(str3);
                }
            }
            return i + i12 + (i13 * 2);
        }
    }

    public static final class zzg extends zzso<zzg> {
        private static volatile zzg[] zziW;
        public int[] zziX;
        public int[] zziY;
        public int[] zziZ;
        public int[] zzja;
        public int[] zzjb;
        public int[] zzjc;
        public int[] zzjd;
        public int[] zzje;
        public int[] zzjf;
        public int[] zzjg;

        public zzg() {
            zzL();
        }

        public static zzg[] zzK() {
            if (zziW == null) {
                synchronized (zzss.zzbut) {
                    if (zziW == null) {
                        zziW = new zzg[0];
                    }
                }
            }
            return zziW;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzg)) {
                return false;
            }
            zzg zzg = (zzg) o;
            if (!zzss.equals(this.zziX, zzg.zziX) || !zzss.equals(this.zziY, zzg.zziY) || !zzss.equals(this.zziZ, zzg.zziZ) || !zzss.equals(this.zzja, zzg.zzja) || !zzss.equals(this.zzjb, zzg.zzjb) || !zzss.equals(this.zzjc, zzg.zzjc) || !zzss.equals(this.zzjd, zzg.zzjd) || !zzss.equals(this.zzje, zzg.zzje) || !zzss.equals(this.zzjf, zzg.zzjf) || !zzss.equals(this.zzjg, zzg.zzjg)) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzg.zzbuj == null || zzg.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzg.zzbuj);
        }

        public int hashCode() {
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((((((((((((((((((((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode(this.zziX)) * 31) + zzss.hashCode(this.zziY)) * 31) + zzss.hashCode(this.zziZ)) * 31) + zzss.hashCode(this.zzja)) * 31) + zzss.hashCode(this.zzjb)) * 31) + zzss.hashCode(this.zzjc)) * 31) + zzss.hashCode(this.zzjd)) * 31) + zzss.hashCode(this.zzje)) * 31) + zzss.hashCode(this.zzjf)) * 31) + zzss.hashCode(this.zzjg)) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zziX != null && this.zziX.length > 0) {
                for (int zzA : this.zziX) {
                    output.zzA(1, zzA);
                }
            }
            if (this.zziY != null && this.zziY.length > 0) {
                for (int zzA2 : this.zziY) {
                    output.zzA(2, zzA2);
                }
            }
            if (this.zziZ != null && this.zziZ.length > 0) {
                for (int zzA3 : this.zziZ) {
                    output.zzA(3, zzA3);
                }
            }
            if (this.zzja != null && this.zzja.length > 0) {
                for (int zzA4 : this.zzja) {
                    output.zzA(4, zzA4);
                }
            }
            if (this.zzjb != null && this.zzjb.length > 0) {
                for (int zzA5 : this.zzjb) {
                    output.zzA(5, zzA5);
                }
            }
            if (this.zzjc != null && this.zzjc.length > 0) {
                for (int zzA6 : this.zzjc) {
                    output.zzA(6, zzA6);
                }
            }
            if (this.zzjd != null && this.zzjd.length > 0) {
                for (int zzA7 : this.zzjd) {
                    output.zzA(7, zzA7);
                }
            }
            if (this.zzje != null && this.zzje.length > 0) {
                for (int zzA8 : this.zzje) {
                    output.zzA(8, zzA8);
                }
            }
            if (this.zzjf != null && this.zzjf.length > 0) {
                for (int zzA9 : this.zzjf) {
                    output.zzA(9, zzA9);
                }
            }
            if (this.zzjg != null && this.zzjg.length > 0) {
                for (int zzA10 : this.zzjg) {
                    output.zzA(10, zzA10);
                }
            }
            super.writeTo(output);
        }

        public zzg zzL() {
            this.zziX = zzsx.zzbuw;
            this.zziY = zzsx.zzbuw;
            this.zziZ = zzsx.zzbuw;
            this.zzja = zzsx.zzbuw;
            this.zzjb = zzsx.zzbuw;
            this.zzjc = zzsx.zzbuw;
            this.zzjd = zzsx.zzbuw;
            this.zzje = zzsx.zzbuw;
            this.zzjf = zzsx.zzbuw;
            this.zzjg = zzsx.zzbuw;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzg */
        public zzg mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        int zzc = zzsx.zzc(zzsm, 8);
                        int length = this.zziX == null ? 0 : this.zziX.length;
                        int[] iArr = new int[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zziX, 0, iArr, 0, length);
                        }
                        while (length < iArr.length - 1) {
                            iArr[length] = zzsm.zzJb();
                            zzsm.zzIX();
                            length++;
                        }
                        iArr[length] = zzsm.zzJb();
                        this.zziX = iArr;
                        continue;
                    case 10:
                        int zzmq = zzsm.zzmq(zzsm.zzJf());
                        int position = zzsm.getPosition();
                        int i = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i++;
                        }
                        zzsm.zzms(position);
                        int length2 = this.zziX == null ? 0 : this.zziX.length;
                        int[] iArr2 = new int[(i + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zziX, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length) {
                            iArr2[length2] = zzsm.zzJb();
                            length2++;
                        }
                        this.zziX = iArr2;
                        zzsm.zzmr(zzmq);
                        continue;
                    case 16:
                        int zzc2 = zzsx.zzc(zzsm, 16);
                        int length3 = this.zziY == null ? 0 : this.zziY.length;
                        int[] iArr3 = new int[(zzc2 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zziY, 0, iArr3, 0, length3);
                        }
                        while (length3 < iArr3.length - 1) {
                            iArr3[length3] = zzsm.zzJb();
                            zzsm.zzIX();
                            length3++;
                        }
                        iArr3[length3] = zzsm.zzJb();
                        this.zziY = iArr3;
                        continue;
                    case 18:
                        int zzmq2 = zzsm.zzmq(zzsm.zzJf());
                        int position2 = zzsm.getPosition();
                        int i2 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i2++;
                        }
                        zzsm.zzms(position2);
                        int length4 = this.zziY == null ? 0 : this.zziY.length;
                        int[] iArr4 = new int[(i2 + length4)];
                        if (length4 != 0) {
                            System.arraycopy(this.zziY, 0, iArr4, 0, length4);
                        }
                        while (length4 < iArr4.length) {
                            iArr4[length4] = zzsm.zzJb();
                            length4++;
                        }
                        this.zziY = iArr4;
                        zzsm.zzmr(zzmq2);
                        continue;
                    case 24:
                        int zzc3 = zzsx.zzc(zzsm, 24);
                        int length5 = this.zziZ == null ? 0 : this.zziZ.length;
                        int[] iArr5 = new int[(zzc3 + length5)];
                        if (length5 != 0) {
                            System.arraycopy(this.zziZ, 0, iArr5, 0, length5);
                        }
                        while (length5 < iArr5.length - 1) {
                            iArr5[length5] = zzsm.zzJb();
                            zzsm.zzIX();
                            length5++;
                        }
                        iArr5[length5] = zzsm.zzJb();
                        this.zziZ = iArr5;
                        continue;
                    case 26:
                        int zzmq3 = zzsm.zzmq(zzsm.zzJf());
                        int position3 = zzsm.getPosition();
                        int i3 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i3++;
                        }
                        zzsm.zzms(position3);
                        int length6 = this.zziZ == null ? 0 : this.zziZ.length;
                        int[] iArr6 = new int[(i3 + length6)];
                        if (length6 != 0) {
                            System.arraycopy(this.zziZ, 0, iArr6, 0, length6);
                        }
                        while (length6 < iArr6.length) {
                            iArr6[length6] = zzsm.zzJb();
                            length6++;
                        }
                        this.zziZ = iArr6;
                        zzsm.zzmr(zzmq3);
                        continue;
                    case 32:
                        int zzc4 = zzsx.zzc(zzsm, 32);
                        int length7 = this.zzja == null ? 0 : this.zzja.length;
                        int[] iArr7 = new int[(zzc4 + length7)];
                        if (length7 != 0) {
                            System.arraycopy(this.zzja, 0, iArr7, 0, length7);
                        }
                        while (length7 < iArr7.length - 1) {
                            iArr7[length7] = zzsm.zzJb();
                            zzsm.zzIX();
                            length7++;
                        }
                        iArr7[length7] = zzsm.zzJb();
                        this.zzja = iArr7;
                        continue;
                    case 34:
                        int zzmq4 = zzsm.zzmq(zzsm.zzJf());
                        int position4 = zzsm.getPosition();
                        int i4 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i4++;
                        }
                        zzsm.zzms(position4);
                        int length8 = this.zzja == null ? 0 : this.zzja.length;
                        int[] iArr8 = new int[(i4 + length8)];
                        if (length8 != 0) {
                            System.arraycopy(this.zzja, 0, iArr8, 0, length8);
                        }
                        while (length8 < iArr8.length) {
                            iArr8[length8] = zzsm.zzJb();
                            length8++;
                        }
                        this.zzja = iArr8;
                        zzsm.zzmr(zzmq4);
                        continue;
                    case 40:
                        int zzc5 = zzsx.zzc(zzsm, 40);
                        int length9 = this.zzjb == null ? 0 : this.zzjb.length;
                        int[] iArr9 = new int[(zzc5 + length9)];
                        if (length9 != 0) {
                            System.arraycopy(this.zzjb, 0, iArr9, 0, length9);
                        }
                        while (length9 < iArr9.length - 1) {
                            iArr9[length9] = zzsm.zzJb();
                            zzsm.zzIX();
                            length9++;
                        }
                        iArr9[length9] = zzsm.zzJb();
                        this.zzjb = iArr9;
                        continue;
                    case 42:
                        int zzmq5 = zzsm.zzmq(zzsm.zzJf());
                        int position5 = zzsm.getPosition();
                        int i5 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i5++;
                        }
                        zzsm.zzms(position5);
                        int length10 = this.zzjb == null ? 0 : this.zzjb.length;
                        int[] iArr10 = new int[(i5 + length10)];
                        if (length10 != 0) {
                            System.arraycopy(this.zzjb, 0, iArr10, 0, length10);
                        }
                        while (length10 < iArr10.length) {
                            iArr10[length10] = zzsm.zzJb();
                            length10++;
                        }
                        this.zzjb = iArr10;
                        zzsm.zzmr(zzmq5);
                        continue;
                    case 48:
                        int zzc6 = zzsx.zzc(zzsm, 48);
                        int length11 = this.zzjc == null ? 0 : this.zzjc.length;
                        int[] iArr11 = new int[(zzc6 + length11)];
                        if (length11 != 0) {
                            System.arraycopy(this.zzjc, 0, iArr11, 0, length11);
                        }
                        while (length11 < iArr11.length - 1) {
                            iArr11[length11] = zzsm.zzJb();
                            zzsm.zzIX();
                            length11++;
                        }
                        iArr11[length11] = zzsm.zzJb();
                        this.zzjc = iArr11;
                        continue;
                    case 50:
                        int zzmq6 = zzsm.zzmq(zzsm.zzJf());
                        int position6 = zzsm.getPosition();
                        int i6 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i6++;
                        }
                        zzsm.zzms(position6);
                        int length12 = this.zzjc == null ? 0 : this.zzjc.length;
                        int[] iArr12 = new int[(i6 + length12)];
                        if (length12 != 0) {
                            System.arraycopy(this.zzjc, 0, iArr12, 0, length12);
                        }
                        while (length12 < iArr12.length) {
                            iArr12[length12] = zzsm.zzJb();
                            length12++;
                        }
                        this.zzjc = iArr12;
                        zzsm.zzmr(zzmq6);
                        continue;
                    case 56:
                        int zzc7 = zzsx.zzc(zzsm, 56);
                        int length13 = this.zzjd == null ? 0 : this.zzjd.length;
                        int[] iArr13 = new int[(zzc7 + length13)];
                        if (length13 != 0) {
                            System.arraycopy(this.zzjd, 0, iArr13, 0, length13);
                        }
                        while (length13 < iArr13.length - 1) {
                            iArr13[length13] = zzsm.zzJb();
                            zzsm.zzIX();
                            length13++;
                        }
                        iArr13[length13] = zzsm.zzJb();
                        this.zzjd = iArr13;
                        continue;
                    case 58:
                        int zzmq7 = zzsm.zzmq(zzsm.zzJf());
                        int position7 = zzsm.getPosition();
                        int i7 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i7++;
                        }
                        zzsm.zzms(position7);
                        int length14 = this.zzjd == null ? 0 : this.zzjd.length;
                        int[] iArr14 = new int[(i7 + length14)];
                        if (length14 != 0) {
                            System.arraycopy(this.zzjd, 0, iArr14, 0, length14);
                        }
                        while (length14 < iArr14.length) {
                            iArr14[length14] = zzsm.zzJb();
                            length14++;
                        }
                        this.zzjd = iArr14;
                        zzsm.zzmr(zzmq7);
                        continue;
                    case 64:
                        int zzc8 = zzsx.zzc(zzsm, 64);
                        int length15 = this.zzje == null ? 0 : this.zzje.length;
                        int[] iArr15 = new int[(zzc8 + length15)];
                        if (length15 != 0) {
                            System.arraycopy(this.zzje, 0, iArr15, 0, length15);
                        }
                        while (length15 < iArr15.length - 1) {
                            iArr15[length15] = zzsm.zzJb();
                            zzsm.zzIX();
                            length15++;
                        }
                        iArr15[length15] = zzsm.zzJb();
                        this.zzje = iArr15;
                        continue;
                    case 66:
                        int zzmq8 = zzsm.zzmq(zzsm.zzJf());
                        int position8 = zzsm.getPosition();
                        int i8 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i8++;
                        }
                        zzsm.zzms(position8);
                        int length16 = this.zzje == null ? 0 : this.zzje.length;
                        int[] iArr16 = new int[(i8 + length16)];
                        if (length16 != 0) {
                            System.arraycopy(this.zzje, 0, iArr16, 0, length16);
                        }
                        while (length16 < iArr16.length) {
                            iArr16[length16] = zzsm.zzJb();
                            length16++;
                        }
                        this.zzje = iArr16;
                        zzsm.zzmr(zzmq8);
                        continue;
                    case 72:
                        int zzc9 = zzsx.zzc(zzsm, 72);
                        int length17 = this.zzjf == null ? 0 : this.zzjf.length;
                        int[] iArr17 = new int[(zzc9 + length17)];
                        if (length17 != 0) {
                            System.arraycopy(this.zzjf, 0, iArr17, 0, length17);
                        }
                        while (length17 < iArr17.length - 1) {
                            iArr17[length17] = zzsm.zzJb();
                            zzsm.zzIX();
                            length17++;
                        }
                        iArr17[length17] = zzsm.zzJb();
                        this.zzjf = iArr17;
                        continue;
                    case 74:
                        int zzmq9 = zzsm.zzmq(zzsm.zzJf());
                        int position9 = zzsm.getPosition();
                        int i9 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i9++;
                        }
                        zzsm.zzms(position9);
                        int length18 = this.zzjf == null ? 0 : this.zzjf.length;
                        int[] iArr18 = new int[(i9 + length18)];
                        if (length18 != 0) {
                            System.arraycopy(this.zzjf, 0, iArr18, 0, length18);
                        }
                        while (length18 < iArr18.length) {
                            iArr18[length18] = zzsm.zzJb();
                            length18++;
                        }
                        this.zzjf = iArr18;
                        zzsm.zzmr(zzmq9);
                        continue;
                    case 80:
                        int zzc10 = zzsx.zzc(zzsm, 80);
                        int length19 = this.zzjg == null ? 0 : this.zzjg.length;
                        int[] iArr19 = new int[(zzc10 + length19)];
                        if (length19 != 0) {
                            System.arraycopy(this.zzjg, 0, iArr19, 0, length19);
                        }
                        while (length19 < iArr19.length - 1) {
                            iArr19[length19] = zzsm.zzJb();
                            zzsm.zzIX();
                            length19++;
                        }
                        iArr19[length19] = zzsm.zzJb();
                        this.zzjg = iArr19;
                        continue;
                    case 82:
                        int zzmq10 = zzsm.zzmq(zzsm.zzJf());
                        int position10 = zzsm.getPosition();
                        int i10 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i10++;
                        }
                        zzsm.zzms(position10);
                        int length20 = this.zzjg == null ? 0 : this.zzjg.length;
                        int[] iArr20 = new int[(i10 + length20)];
                        if (length20 != 0) {
                            System.arraycopy(this.zzjg, 0, iArr20, 0, length20);
                        }
                        while (length20 < iArr20.length) {
                            iArr20[length20] = zzsm.zzJb();
                            length20++;
                        }
                        this.zzjg = iArr20;
                        zzsm.zzmr(zzmq10);
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
            int i;
            int zzz = super.zzz();
            if (this.zziX == null || this.zziX.length <= 0) {
                i = zzz;
            } else {
                int i2 = 0;
                for (int zzmx : this.zziX) {
                    i2 += zzsn.zzmx(zzmx);
                }
                i = zzz + i2 + (this.zziX.length * 1);
            }
            if (this.zziY != null && this.zziY.length > 0) {
                int i3 = 0;
                for (int zzmx2 : this.zziY) {
                    i3 += zzsn.zzmx(zzmx2);
                }
                i = i + i3 + (this.zziY.length * 1);
            }
            if (this.zziZ != null && this.zziZ.length > 0) {
                int i4 = 0;
                for (int zzmx3 : this.zziZ) {
                    i4 += zzsn.zzmx(zzmx3);
                }
                i = i + i4 + (this.zziZ.length * 1);
            }
            if (this.zzja != null && this.zzja.length > 0) {
                int i5 = 0;
                for (int zzmx4 : this.zzja) {
                    i5 += zzsn.zzmx(zzmx4);
                }
                i = i + i5 + (this.zzja.length * 1);
            }
            if (this.zzjb != null && this.zzjb.length > 0) {
                int i6 = 0;
                for (int zzmx5 : this.zzjb) {
                    i6 += zzsn.zzmx(zzmx5);
                }
                i = i + i6 + (this.zzjb.length * 1);
            }
            if (this.zzjc != null && this.zzjc.length > 0) {
                int i7 = 0;
                for (int zzmx6 : this.zzjc) {
                    i7 += zzsn.zzmx(zzmx6);
                }
                i = i + i7 + (this.zzjc.length * 1);
            }
            if (this.zzjd != null && this.zzjd.length > 0) {
                int i8 = 0;
                for (int zzmx7 : this.zzjd) {
                    i8 += zzsn.zzmx(zzmx7);
                }
                i = i + i8 + (this.zzjd.length * 1);
            }
            if (this.zzje != null && this.zzje.length > 0) {
                int i9 = 0;
                for (int zzmx8 : this.zzje) {
                    i9 += zzsn.zzmx(zzmx8);
                }
                i = i + i9 + (this.zzje.length * 1);
            }
            if (this.zzjf != null && this.zzjf.length > 0) {
                int i10 = 0;
                for (int zzmx9 : this.zzjf) {
                    i10 += zzsn.zzmx(zzmx9);
                }
                i = i + i10 + (this.zzjf.length * 1);
            }
            if (this.zzjg == null || this.zzjg.length <= 0) {
                return i;
            }
            int i11 = 0;
            for (int zzmx10 : this.zzjg) {
                i11 += zzsn.zzmx(zzmx10);
            }
            return i + i11 + (this.zzjg.length * 1);
        }
    }

    public static final class zzh extends zzso<zzh> {
        public static final zzsp<zzag.zza, zzh> zzjh = zzsp.zza(11, zzh.class, 810);
        private static final zzh[] zzji = new zzh[0];
        public int[] zzjj;
        public int[] zzjk;
        public int[] zzjl;
        public int zzjm;
        public int[] zzjn;
        public int zzjo;
        public int zzjp;

        public zzh() {
            zzM();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzh)) {
                return false;
            }
            zzh zzh = (zzh) o;
            if (!zzss.equals(this.zzjj, zzh.zzjj) || !zzss.equals(this.zzjk, zzh.zzjk) || !zzss.equals(this.zzjl, zzh.zzjl) || this.zzjm != zzh.zzjm || !zzss.equals(this.zzjn, zzh.zzjn) || this.zzjo != zzh.zzjo || this.zzjp != zzh.zzjp) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzh.zzbuj == null || zzh.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzh.zzbuj);
        }

        public int hashCode() {
            return ((this.zzbuj == null || this.zzbuj.isEmpty()) ? 0 : this.zzbuj.hashCode()) + ((((((((((((((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode(this.zzjj)) * 31) + zzss.hashCode(this.zzjk)) * 31) + zzss.hashCode(this.zzjl)) * 31) + this.zzjm) * 31) + zzss.hashCode(this.zzjn)) * 31) + this.zzjo) * 31) + this.zzjp) * 31);
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zzjj != null && this.zzjj.length > 0) {
                for (int zzA : this.zzjj) {
                    output.zzA(1, zzA);
                }
            }
            if (this.zzjk != null && this.zzjk.length > 0) {
                for (int zzA2 : this.zzjk) {
                    output.zzA(2, zzA2);
                }
            }
            if (this.zzjl != null && this.zzjl.length > 0) {
                for (int zzA3 : this.zzjl) {
                    output.zzA(3, zzA3);
                }
            }
            if (this.zzjm != 0) {
                output.zzA(4, this.zzjm);
            }
            if (this.zzjn != null && this.zzjn.length > 0) {
                for (int zzA4 : this.zzjn) {
                    output.zzA(5, zzA4);
                }
            }
            if (this.zzjo != 0) {
                output.zzA(6, this.zzjo);
            }
            if (this.zzjp != 0) {
                output.zzA(7, this.zzjp);
            }
            super.writeTo(output);
        }

        public zzh zzM() {
            this.zzjj = zzsx.zzbuw;
            this.zzjk = zzsx.zzbuw;
            this.zzjl = zzsx.zzbuw;
            this.zzjm = 0;
            this.zzjn = zzsx.zzbuw;
            this.zzjo = 0;
            this.zzjp = 0;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzh */
        public zzh mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 8:
                        int zzc = zzsx.zzc(zzsm, 8);
                        int length = this.zzjj == null ? 0 : this.zzjj.length;
                        int[] iArr = new int[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzjj, 0, iArr, 0, length);
                        }
                        while (length < iArr.length - 1) {
                            iArr[length] = zzsm.zzJb();
                            zzsm.zzIX();
                            length++;
                        }
                        iArr[length] = zzsm.zzJb();
                        this.zzjj = iArr;
                        continue;
                    case 10:
                        int zzmq = zzsm.zzmq(zzsm.zzJf());
                        int position = zzsm.getPosition();
                        int i = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i++;
                        }
                        zzsm.zzms(position);
                        int length2 = this.zzjj == null ? 0 : this.zzjj.length;
                        int[] iArr2 = new int[(i + length2)];
                        if (length2 != 0) {
                            System.arraycopy(this.zzjj, 0, iArr2, 0, length2);
                        }
                        while (length2 < iArr2.length) {
                            iArr2[length2] = zzsm.zzJb();
                            length2++;
                        }
                        this.zzjj = iArr2;
                        zzsm.zzmr(zzmq);
                        continue;
                    case 16:
                        int zzc2 = zzsx.zzc(zzsm, 16);
                        int length3 = this.zzjk == null ? 0 : this.zzjk.length;
                        int[] iArr3 = new int[(zzc2 + length3)];
                        if (length3 != 0) {
                            System.arraycopy(this.zzjk, 0, iArr3, 0, length3);
                        }
                        while (length3 < iArr3.length - 1) {
                            iArr3[length3] = zzsm.zzJb();
                            zzsm.zzIX();
                            length3++;
                        }
                        iArr3[length3] = zzsm.zzJb();
                        this.zzjk = iArr3;
                        continue;
                    case 18:
                        int zzmq2 = zzsm.zzmq(zzsm.zzJf());
                        int position2 = zzsm.getPosition();
                        int i2 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i2++;
                        }
                        zzsm.zzms(position2);
                        int length4 = this.zzjk == null ? 0 : this.zzjk.length;
                        int[] iArr4 = new int[(i2 + length4)];
                        if (length4 != 0) {
                            System.arraycopy(this.zzjk, 0, iArr4, 0, length4);
                        }
                        while (length4 < iArr4.length) {
                            iArr4[length4] = zzsm.zzJb();
                            length4++;
                        }
                        this.zzjk = iArr4;
                        zzsm.zzmr(zzmq2);
                        continue;
                    case 24:
                        int zzc3 = zzsx.zzc(zzsm, 24);
                        int length5 = this.zzjl == null ? 0 : this.zzjl.length;
                        int[] iArr5 = new int[(zzc3 + length5)];
                        if (length5 != 0) {
                            System.arraycopy(this.zzjl, 0, iArr5, 0, length5);
                        }
                        while (length5 < iArr5.length - 1) {
                            iArr5[length5] = zzsm.zzJb();
                            zzsm.zzIX();
                            length5++;
                        }
                        iArr5[length5] = zzsm.zzJb();
                        this.zzjl = iArr5;
                        continue;
                    case 26:
                        int zzmq3 = zzsm.zzmq(zzsm.zzJf());
                        int position3 = zzsm.getPosition();
                        int i3 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i3++;
                        }
                        zzsm.zzms(position3);
                        int length6 = this.zzjl == null ? 0 : this.zzjl.length;
                        int[] iArr6 = new int[(i3 + length6)];
                        if (length6 != 0) {
                            System.arraycopy(this.zzjl, 0, iArr6, 0, length6);
                        }
                        while (length6 < iArr6.length) {
                            iArr6[length6] = zzsm.zzJb();
                            length6++;
                        }
                        this.zzjl = iArr6;
                        zzsm.zzmr(zzmq3);
                        continue;
                    case 32:
                        this.zzjm = zzsm.zzJb();
                        continue;
                    case 40:
                        int zzc4 = zzsx.zzc(zzsm, 40);
                        int length7 = this.zzjn == null ? 0 : this.zzjn.length;
                        int[] iArr7 = new int[(zzc4 + length7)];
                        if (length7 != 0) {
                            System.arraycopy(this.zzjn, 0, iArr7, 0, length7);
                        }
                        while (length7 < iArr7.length - 1) {
                            iArr7[length7] = zzsm.zzJb();
                            zzsm.zzIX();
                            length7++;
                        }
                        iArr7[length7] = zzsm.zzJb();
                        this.zzjn = iArr7;
                        continue;
                    case 42:
                        int zzmq4 = zzsm.zzmq(zzsm.zzJf());
                        int position4 = zzsm.getPosition();
                        int i4 = 0;
                        while (zzsm.zzJk() > 0) {
                            zzsm.zzJb();
                            i4++;
                        }
                        zzsm.zzms(position4);
                        int length8 = this.zzjn == null ? 0 : this.zzjn.length;
                        int[] iArr8 = new int[(i4 + length8)];
                        if (length8 != 0) {
                            System.arraycopy(this.zzjn, 0, iArr8, 0, length8);
                        }
                        while (length8 < iArr8.length) {
                            iArr8[length8] = zzsm.zzJb();
                            length8++;
                        }
                        this.zzjn = iArr8;
                        zzsm.zzmr(zzmq4);
                        continue;
                    case 48:
                        this.zzjo = zzsm.zzJb();
                        continue;
                    case 56:
                        this.zzjp = zzsm.zzJb();
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
            int i;
            int zzz = super.zzz();
            if (this.zzjj == null || this.zzjj.length <= 0) {
                i = zzz;
            } else {
                int i2 = 0;
                for (int zzmx : this.zzjj) {
                    i2 += zzsn.zzmx(zzmx);
                }
                i = zzz + i2 + (this.zzjj.length * 1);
            }
            if (this.zzjk != null && this.zzjk.length > 0) {
                int i3 = 0;
                for (int zzmx2 : this.zzjk) {
                    i3 += zzsn.zzmx(zzmx2);
                }
                i = i + i3 + (this.zzjk.length * 1);
            }
            if (this.zzjl != null && this.zzjl.length > 0) {
                int i4 = 0;
                for (int zzmx3 : this.zzjl) {
                    i4 += zzsn.zzmx(zzmx3);
                }
                i = i + i4 + (this.zzjl.length * 1);
            }
            if (this.zzjm != 0) {
                i += zzsn.zzC(4, this.zzjm);
            }
            if (this.zzjn != null && this.zzjn.length > 0) {
                int i5 = 0;
                for (int zzmx4 : this.zzjn) {
                    i5 += zzsn.zzmx(zzmx4);
                }
                i = i + i5 + (this.zzjn.length * 1);
            }
            if (this.zzjo != 0) {
                i += zzsn.zzC(6, this.zzjo);
            }
            return this.zzjp != 0 ? i + zzsn.zzC(7, this.zzjp) : i;
        }
    }

    public static final class zzi extends zzso<zzi> {
        private static volatile zzi[] zzjq;
        public String name;
        public zzag.zza zzjr;
        public zzd zzjs;

        public zzi() {
            zzO();
        }

        public static zzi[] zzN() {
            if (zzjq == null) {
                synchronized (zzss.zzbut) {
                    if (zzjq == null) {
                        zzjq = new zzi[0];
                    }
                }
            }
            return zzjq;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzi)) {
                return false;
            }
            zzi zzi = (zzi) o;
            if (this.name == null) {
                if (zzi.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzi.name)) {
                return false;
            }
            if (this.zzjr == null) {
                if (zzi.zzjr != null) {
                    return false;
                }
            } else if (!this.zzjr.equals(zzi.zzjr)) {
                return false;
            }
            if (this.zzjs == null) {
                if (zzi.zzjs != null) {
                    return false;
                }
            } else if (!this.zzjs.equals(zzi.zzjs)) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzi.zzbuj == null || zzi.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzi.zzbuj);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzjs == null ? 0 : this.zzjs.hashCode()) + (((this.zzjr == null ? 0 : this.zzjr.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i = this.zzbuj.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (!this.name.equals("")) {
                output.zzn(1, this.name);
            }
            if (this.zzjr != null) {
                output.zza(2, (zzsu) this.zzjr);
            }
            if (this.zzjs != null) {
                output.zza(3, (zzsu) this.zzjs);
            }
            super.writeTo(output);
        }

        public zzi zzO() {
            this.name = "";
            this.zzjr = null;
            this.zzjs = null;
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzi */
        public zzi mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        this.name = zzsm.readString();
                        continue;
                    case 18:
                        if (this.zzjr == null) {
                            this.zzjr = new zzag.zza();
                        }
                        zzsm.zza(this.zzjr);
                        continue;
                    case 26:
                        if (this.zzjs == null) {
                            this.zzjs = new zzd();
                        }
                        zzsm.zza(this.zzjs);
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
            int zzz = super.zzz();
            if (!this.name.equals("")) {
                zzz += zzsn.zzo(1, this.name);
            }
            if (this.zzjr != null) {
                zzz += zzsn.zzc(2, (zzsu) this.zzjr);
            }
            return this.zzjs != null ? zzz + zzsn.zzc(3, (zzsu) this.zzjs) : zzz;
        }
    }

    public static final class zzj extends zzso<zzj> {
        public zzi[] zzjt;
        public zzf zzju;
        public String zzjv;

        public zzj() {
            zzP();
        }

        public static zzj zzd(byte[] bArr) throws zzst {
            return (zzj) zzsu.mergeFrom(new zzj(), bArr);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzj)) {
                return false;
            }
            zzj zzj = (zzj) o;
            if (!zzss.equals((Object[]) this.zzjt, (Object[]) zzj.zzjt)) {
                return false;
            }
            if (this.zzju == null) {
                if (zzj.zzju != null) {
                    return false;
                }
            } else if (!this.zzju.equals(zzj.zzju)) {
                return false;
            }
            if (this.zzjv == null) {
                if (zzj.zzjv != null) {
                    return false;
                }
            } else if (!this.zzjv.equals(zzj.zzjv)) {
                return false;
            }
            if (this.zzbuj == null || this.zzbuj.isEmpty()) {
                return zzj.zzbuj == null || zzj.zzbuj.isEmpty();
            }
            return this.zzbuj.equals(zzj.zzbuj);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzjv == null ? 0 : this.zzjv.hashCode()) + (((this.zzju == null ? 0 : this.zzju.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + zzss.hashCode((Object[]) this.zzjt)) * 31)) * 31)) * 31;
            if (this.zzbuj != null && !this.zzbuj.isEmpty()) {
                i = this.zzbuj.hashCode();
            }
            return hashCode + i;
        }

        public void writeTo(zzsn output) throws IOException {
            if (this.zzjt != null && this.zzjt.length > 0) {
                for (zzi zzi : this.zzjt) {
                    if (zzi != null) {
                        output.zza(1, (zzsu) zzi);
                    }
                }
            }
            if (this.zzju != null) {
                output.zza(2, (zzsu) this.zzju);
            }
            if (!this.zzjv.equals("")) {
                output.zzn(3, this.zzjv);
            }
            super.writeTo(output);
        }

        public zzj zzP() {
            this.zzjt = zzi.zzN();
            this.zzju = null;
            this.zzjv = "";
            this.zzbuj = null;
            this.zzbuu = -1;
            return this;
        }

        /* renamed from: zzj */
        public zzj mergeFrom(zzsm zzsm) throws IOException {
            while (true) {
                int zzIX = zzsm.zzIX();
                switch (zzIX) {
                    case 0:
                        break;
                    case 10:
                        int zzc = zzsx.zzc(zzsm, 10);
                        int length = this.zzjt == null ? 0 : this.zzjt.length;
                        zzi[] zziArr = new zzi[(zzc + length)];
                        if (length != 0) {
                            System.arraycopy(this.zzjt, 0, zziArr, 0, length);
                        }
                        while (length < zziArr.length - 1) {
                            zziArr[length] = new zzi();
                            zzsm.zza(zziArr[length]);
                            zzsm.zzIX();
                            length++;
                        }
                        zziArr[length] = new zzi();
                        zzsm.zza(zziArr[length]);
                        this.zzjt = zziArr;
                        continue;
                    case 18:
                        if (this.zzju == null) {
                            this.zzju = new zzf();
                        }
                        zzsm.zza(this.zzju);
                        continue;
                    case 26:
                        this.zzjv = zzsm.readString();
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
            int zzz = super.zzz();
            if (this.zzjt != null && this.zzjt.length > 0) {
                for (zzi zzi : this.zzjt) {
                    if (zzi != null) {
                        zzz += zzsn.zzc(1, (zzsu) zzi);
                    }
                }
            }
            if (this.zzju != null) {
                zzz += zzsn.zzc(2, (zzsu) this.zzju);
            }
            return !this.zzjv.equals("") ? zzz + zzsn.zzo(3, this.zzjv) : zzz;
        }
    }
}
