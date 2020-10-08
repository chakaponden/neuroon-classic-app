package com.google.android.gms.tagmanager;

import android.os.Build;

class zzm<K, V> {
    final zza<K, V> zzbhK = new zza<K, V>() {
        public int sizeOf(K k, V v) {
            return 1;
        }
    };

    public interface zza<K, V> {
        int sizeOf(K k, V v);
    }

    /* access modifiers changed from: package-private */
    public int zzFY() {
        return Build.VERSION.SDK_INT;
    }

    public zzl<K, V> zza(int i, zza<K, V> zza2) {
        if (i > 0) {
            return zzFY() < 12 ? new zzcw(i, zza2) : new zzba(i, zza2);
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
}
