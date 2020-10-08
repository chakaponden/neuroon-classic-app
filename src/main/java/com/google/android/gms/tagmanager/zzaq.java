package com.google.android.gms.tagmanager;

import android.text.TextUtils;

class zzaq {
    private final long zzSL;
    private final long zzbiX;
    private final long zzbiY;
    private String zzbiZ;

    zzaq(long j, long j2, long j3) {
        this.zzbiX = j;
        this.zzSL = j2;
        this.zzbiY = j3;
    }

    /* access modifiers changed from: package-private */
    public long zzGD() {
        return this.zzbiX;
    }

    /* access modifiers changed from: package-private */
    public long zzGE() {
        return this.zzbiY;
    }

    /* access modifiers changed from: package-private */
    public String zzGF() {
        return this.zzbiZ;
    }

    /* access modifiers changed from: package-private */
    public void zzgf(String str) {
        if (str != null && !TextUtils.isEmpty(str.trim())) {
            this.zzbiZ = str;
        }
    }
}
