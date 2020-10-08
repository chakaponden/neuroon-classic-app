package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzag;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract class zzak {
    private final Set<String> zzbiU;
    private final String zzbiV;

    public zzak(String str, String... strArr) {
        this.zzbiV = str;
        this.zzbiU = new HashSet(strArr.length);
        for (String add : strArr) {
            this.zzbiU.add(add);
        }
    }

    public abstract boolean zzFW();

    public String zzGB() {
        return this.zzbiV;
    }

    public Set<String> zzGC() {
        return this.zzbiU;
    }

    public abstract zzag.zza zzP(Map<String, zzag.zza> map);

    /* access modifiers changed from: package-private */
    public boolean zze(Set<String> set) {
        return set.containsAll(this.zzbiU);
    }
}
