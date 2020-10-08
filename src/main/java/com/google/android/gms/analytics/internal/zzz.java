package com.google.android.gms.analytics.internal;

import com.google.android.gms.analytics.internal.zzq;

public class zzz extends zzq<zzaa> {

    private static class zza implements zzq.zza<zzaa> {
        private final zzf zzQj;
        private final zzaa zzSD = new zzaa();

        public zza(zzf zzf) {
            this.zzQj = zzf;
        }

        public void zzc(String str, int i) {
            if ("ga_dispatchPeriod".equals(str)) {
                this.zzSD.zzSH = i;
            } else {
                this.zzQj.zzjm().zzd("Int xml configuration name not recognized", str);
            }
        }

        public void zzf(String str, boolean z) {
            if ("ga_dryRun".equals(str)) {
                this.zzSD.zzSI = z ? 1 : 0;
                return;
            }
            this.zzQj.zzjm().zzd("Bool xml configuration name not recognized", str);
        }

        public void zzj(String str, String str2) {
        }

        public void zzk(String str, String str2) {
            if ("ga_appName".equals(str)) {
                this.zzSD.zzSE = str2;
            } else if ("ga_appVersion".equals(str)) {
                this.zzSD.zzSF = str2;
            } else if ("ga_logLevel".equals(str)) {
                this.zzSD.zzSG = str2;
            } else {
                this.zzQj.zzjm().zzd("String xml configuration name not recognized", str);
            }
        }

        /* renamed from: zzle */
        public zzaa zzkq() {
            return this.zzSD;
        }
    }

    public zzz(zzf zzf) {
        super(zzf, new zza(zzf));
    }
}
