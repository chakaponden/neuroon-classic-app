package com.google.android.gms.analytics.internal;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.common.internal.zzx;
import java.util.UUID;

public class zzai extends zzd {
    /* access modifiers changed from: private */
    public SharedPreferences zzTh;
    private long zzTi;
    private long zzTj = -1;
    private final zza zzTk = new zza("monitoring", zzjn().zzkX());

    public final class zza {
        private final String mName;
        private final long zzTl;

        private zza(String str, long j) {
            zzx.zzcM(str);
            zzx.zzac(j > 0);
            this.mName = str;
            this.zzTl = j;
        }

        private void zzlL() {
            long currentTimeMillis = zzai.this.zzjl().currentTimeMillis();
            SharedPreferences.Editor edit = zzai.this.zzTh.edit();
            edit.remove(zzlQ());
            edit.remove(zzlR());
            edit.putLong(zzlP(), currentTimeMillis);
            edit.commit();
        }

        private long zzlM() {
            long zzlO = zzlO();
            if (zzlO == 0) {
                return 0;
            }
            return Math.abs(zzlO - zzai.this.zzjl().currentTimeMillis());
        }

        private long zzlO() {
            return zzai.this.zzTh.getLong(zzlP(), 0);
        }

        private String zzlP() {
            return this.mName + ":start";
        }

        private String zzlQ() {
            return this.mName + ":count";
        }

        public void zzbq(String str) {
            if (zzlO() == 0) {
                zzlL();
            }
            if (str == null) {
                str = "";
            }
            synchronized (this) {
                long j = zzai.this.zzTh.getLong(zzlQ(), 0);
                if (j <= 0) {
                    SharedPreferences.Editor edit = zzai.this.zzTh.edit();
                    edit.putString(zzlR(), str);
                    edit.putLong(zzlQ(), 1);
                    edit.apply();
                    return;
                }
                boolean z = (UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE) < Long.MAX_VALUE / (j + 1);
                SharedPreferences.Editor edit2 = zzai.this.zzTh.edit();
                if (z) {
                    edit2.putString(zzlR(), str);
                }
                edit2.putLong(zzlQ(), j + 1);
                edit2.apply();
            }
        }

        public Pair<String, Long> zzlN() {
            long zzlM = zzlM();
            if (zzlM < this.zzTl) {
                return null;
            }
            if (zzlM > this.zzTl * 2) {
                zzlL();
                return null;
            }
            String string = zzai.this.zzTh.getString(zzlR(), (String) null);
            long j = zzai.this.zzTh.getLong(zzlQ(), 0);
            zzlL();
            if (string == null || j <= 0) {
                return null;
            }
            return new Pair<>(string, Long.valueOf(j));
        }

        /* access modifiers changed from: protected */
        public String zzlR() {
            return this.mName + ":value";
        }
    }

    protected zzai(zzf zzf) {
        super(zzf);
    }

    public void zzbp(String str) {
        zzjk();
        zzjv();
        SharedPreferences.Editor edit = this.zzTh.edit();
        if (TextUtils.isEmpty(str)) {
            edit.remove("installation_campaign");
        } else {
            edit.putString("installation_campaign", str);
        }
        if (!edit.commit()) {
            zzbg("Failed to commit campaign data");
        }
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        this.zzTh = getContext().getSharedPreferences("com.google.android.gms.analytics.prefs", 0);
    }

    public long zzlF() {
        zzjk();
        zzjv();
        if (this.zzTi == 0) {
            long j = this.zzTh.getLong("first_run", 0);
            if (j != 0) {
                this.zzTi = j;
            } else {
                long currentTimeMillis = zzjl().currentTimeMillis();
                SharedPreferences.Editor edit = this.zzTh.edit();
                edit.putLong("first_run", currentTimeMillis);
                if (!edit.commit()) {
                    zzbg("Failed to commit first run time");
                }
                this.zzTi = currentTimeMillis;
            }
        }
        return this.zzTi;
    }

    public zzaj zzlG() {
        return new zzaj(zzjl(), zzlF());
    }

    public long zzlH() {
        zzjk();
        zzjv();
        if (this.zzTj == -1) {
            this.zzTj = this.zzTh.getLong("last_dispatch", 0);
        }
        return this.zzTj;
    }

    public void zzlI() {
        zzjk();
        zzjv();
        long currentTimeMillis = zzjl().currentTimeMillis();
        SharedPreferences.Editor edit = this.zzTh.edit();
        edit.putLong("last_dispatch", currentTimeMillis);
        edit.apply();
        this.zzTj = currentTimeMillis;
    }

    public String zzlJ() {
        zzjk();
        zzjv();
        String string = this.zzTh.getString("installation_campaign", (String) null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return string;
    }

    public zza zzlK() {
        return this.zzTk;
    }
}
