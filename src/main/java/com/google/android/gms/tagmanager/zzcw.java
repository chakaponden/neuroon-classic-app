package com.google.android.gms.tagmanager;

import com.google.android.gms.tagmanager.zzm;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class zzcw<K, V> implements zzl<K, V> {
    private final Map<K, V> zzbld = new HashMap();
    private final int zzble;
    private final zzm.zza<K, V> zzblf;
    private int zzblg;

    zzcw(int i, zzm.zza<K, V> zza) {
        this.zzble = i;
        this.zzblf = zza;
    }

    public synchronized V get(K key) {
        return this.zzbld.get(key);
    }

    public synchronized void zzh(K k, V v) {
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        this.zzblg += this.zzblf.sizeOf(k, v);
        if (this.zzblg > this.zzble) {
            Iterator<Map.Entry<K, V>> it = this.zzbld.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                this.zzblg -= this.zzblf.sizeOf(next.getKey(), next.getValue());
                it.remove();
                if (this.zzblg <= this.zzble) {
                    break;
                }
            }
        }
        this.zzbld.put(k, v);
    }
}
