package com.google.android.gms.analytics.internal;

import com.google.android.gms.analytics.internal.zzq;

public class zzak extends zzq<zzal> {

    private static class zza extends zzc implements zzq.zza<zzal> {
        private final zzal zzTn = new zzal();

        public zza(zzf zzf) {
            super(zzf);
        }

        public void zzc(String str, int i) {
            if ("ga_sessionTimeout".equals(str)) {
                this.zzTn.zzTp = i;
            } else {
                zzd("int configuration name not recognized", str);
            }
        }

        public void zzf(String str, boolean z) {
            int i = 1;
            if ("ga_autoActivityTracking".equals(str)) {
                zzal zzal = this.zzTn;
                if (!z) {
                    i = 0;
                }
                zzal.zzTq = i;
            } else if ("ga_anonymizeIp".equals(str)) {
                zzal zzal2 = this.zzTn;
                if (!z) {
                    i = 0;
                }
                zzal2.zzTr = i;
            } else if ("ga_reportUncaughtExceptions".equals(str)) {
                zzal zzal3 = this.zzTn;
                if (!z) {
                    i = 0;
                }
                zzal3.zzTs = i;
            } else {
                zzd("bool configuration name not recognized", str);
            }
        }

        public void zzj(String str, String str2) {
            this.zzTn.zzTt.put(str, str2);
        }

        public void zzk(String str, String str2) {
            if ("ga_trackingId".equals(str)) {
                this.zzTn.zzOV = str2;
            } else if ("ga_sampleFrequency".equals(str)) {
                try {
                    this.zzTn.zzTo = Double.parseDouble(str2);
                } catch (NumberFormatException e) {
                    zzc("Error parsing ga_sampleFrequency value", str2, e);
                }
            } else {
                zzd("string configuration name not recognized", str);
            }
        }

        /* renamed from: zzlS */
        public zzal zzkq() {
            return this.zzTn;
        }
    }

    public zzak(zzf zzf) {
        super(zzf, new zza(zzf));
    }
}
