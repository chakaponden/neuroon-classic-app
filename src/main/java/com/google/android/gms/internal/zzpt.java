package com.google.android.gms.internal;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.measurement.zze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzpt extends zze<zzpt> {
    private ProductAction zzPn;
    private final Map<String, List<Product>> zzPo = new HashMap();
    private final List<Promotion> zzPp = new ArrayList();
    private final List<Product> zzPq = new ArrayList();

    public String toString() {
        HashMap hashMap = new HashMap();
        if (!this.zzPq.isEmpty()) {
            hashMap.put("products", this.zzPq);
        }
        if (!this.zzPp.isEmpty()) {
            hashMap.put("promotions", this.zzPp);
        }
        if (!this.zzPo.isEmpty()) {
            hashMap.put("impressions", this.zzPo);
        }
        hashMap.put("productAction", this.zzPn);
        return zzF(hashMap);
    }

    public ProductAction zzAV() {
        return this.zzPn;
    }

    public List<Product> zzAW() {
        return Collections.unmodifiableList(this.zzPq);
    }

    public Map<String, List<Product>> zzAX() {
        return this.zzPo;
    }

    public List<Promotion> zzAY() {
        return Collections.unmodifiableList(this.zzPp);
    }

    public void zza(Product product, String str) {
        if (product != null) {
            if (str == null) {
                str = "";
            }
            if (!this.zzPo.containsKey(str)) {
                this.zzPo.put(str, new ArrayList());
            }
            this.zzPo.get(str).add(product);
        }
    }

    public void zza(zzpt zzpt) {
        zzpt.zzPq.addAll(this.zzPq);
        zzpt.zzPp.addAll(this.zzPp);
        for (Map.Entry next : this.zzPo.entrySet()) {
            String str = (String) next.getKey();
            for (Product zza : (List) next.getValue()) {
                zzpt.zza(zza, str);
            }
        }
        if (this.zzPn != null) {
            zzpt.zzPn = this.zzPn;
        }
    }
}
