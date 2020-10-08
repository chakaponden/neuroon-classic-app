package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class zzdg extends zzdd {
    private static final String ID = zzad.UNIVERSAL_ANALYTICS.toString();
    private static final String zzblN = zzae.ACCOUNT.toString();
    private static final String zzblO = zzae.ANALYTICS_PASS_THROUGH.toString();
    private static final String zzblP = zzae.ENABLE_ECOMMERCE.toString();
    private static final String zzblQ = zzae.ECOMMERCE_USE_DATA_LAYER.toString();
    private static final String zzblR = zzae.ECOMMERCE_MACRO_DATA.toString();
    private static final String zzblS = zzae.ANALYTICS_FIELDS.toString();
    private static final String zzblT = zzae.TRACK_TRANSACTION.toString();
    private static final String zzblU = zzae.TRANSACTION_DATALAYER_MAP.toString();
    private static final String zzblV = zzae.TRANSACTION_ITEM_DATALAYER_MAP.toString();
    private static final List<String> zzblW = Arrays.asList(new String[]{ProductAction.ACTION_DETAIL, ProductAction.ACTION_CHECKOUT, ProductAction.ACTION_CHECKOUT_OPTION, "click", ProductAction.ACTION_ADD, ProductAction.ACTION_REMOVE, ProductAction.ACTION_PURCHASE, ProductAction.ACTION_REFUND});
    private static final Pattern zzblX = Pattern.compile("dimension(\\d+)");
    private static final Pattern zzblY = Pattern.compile("metric(\\d+)");
    private static Map<String, String> zzblZ;
    private static Map<String, String> zzbma;
    private final DataLayer zzbhN;
    private final Set<String> zzbmb;
    private final zzdc zzbmc;

    public zzdg(Context context, DataLayer dataLayer) {
        this(context, dataLayer, new zzdc(context));
    }

    zzdg(Context context, DataLayer dataLayer, zzdc zzdc) {
        super(ID, new String[0]);
        this.zzbhN = dataLayer;
        this.zzbmc = zzdc;
        this.zzbmb = new HashSet();
        this.zzbmb.add("");
        this.zzbmb.add("0");
        this.zzbmb.add("false");
    }

    private Double zzV(Object obj) {
        if (obj instanceof String) {
            try {
                return Double.valueOf((String) obj);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot convert the object to Double: " + e.getMessage());
            }
        } else if (obj instanceof Integer) {
            return Double.valueOf(((Integer) obj).doubleValue());
        } else {
            if (obj instanceof Double) {
                return (Double) obj;
            }
            throw new RuntimeException("Cannot convert the object to Double: " + obj.toString());
        }
    }

    private Integer zzW(Object obj) {
        if (obj instanceof String) {
            try {
                return Integer.valueOf((String) obj);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot convert the object to Integer: " + e.getMessage());
            }
        } else if (obj instanceof Double) {
            return Integer.valueOf(((Double) obj).intValue());
        } else {
            if (obj instanceof Integer) {
                return (Integer) obj;
            }
            throw new RuntimeException("Cannot convert the object to Integer: " + obj.toString());
        }
    }

    private Promotion zzZ(Map<String, String> map) {
        Promotion promotion = new Promotion();
        String str = map.get("id");
        if (str != null) {
            promotion.setId(String.valueOf(str));
        }
        String str2 = map.get("name");
        if (str2 != null) {
            promotion.setName(String.valueOf(str2));
        }
        String str3 = map.get("creative");
        if (str3 != null) {
            promotion.setCreative(String.valueOf(str3));
        }
        String str4 = map.get("position");
        if (str4 != null) {
            promotion.setPosition(String.valueOf(str4));
        }
        return promotion;
    }

    private void zza(Tracker tracker, Map<String, zzag.zza> map) {
        String zzgy = zzgy("transactionId");
        if (zzgy == null) {
            zzbg.e("Cannot find transactionId in data layer.");
            return;
        }
        LinkedList<Map> linkedList = new LinkedList<>();
        try {
            Map<String, String> zzm = zzm(map.get(zzblS));
            zzm.put("&t", "transaction");
            for (Map.Entry next : zzab(map).entrySet()) {
                zze(zzm, (String) next.getValue(), zzgy((String) next.getKey()));
            }
            linkedList.add(zzm);
            List<Map<String, String>> zzgz = zzgz("transactionProducts");
            if (zzgz != null) {
                for (Map next2 : zzgz) {
                    if (next2.get("name") == null) {
                        zzbg.e("Unable to send transaction item hit due to missing 'name' field.");
                        return;
                    }
                    Map<String, String> zzm2 = zzm(map.get(zzblS));
                    zzm2.put("&t", "item");
                    zzm2.put("&ti", zzgy);
                    for (Map.Entry next3 : zzac(map).entrySet()) {
                        zze(zzm2, (String) next3.getValue(), (String) next2.get(next3.getKey()));
                    }
                    linkedList.add(zzm2);
                }
            }
            for (Map send : linkedList) {
                tracker.send(send);
            }
        } catch (IllegalArgumentException e) {
            zzbg.zzb("Unable to send transaction", e);
        }
    }

    private Product zzaa(Map<String, Object> map) {
        Product product = new Product();
        Object obj = map.get("id");
        if (obj != null) {
            product.setId(String.valueOf(obj));
        }
        Object obj2 = map.get("name");
        if (obj2 != null) {
            product.setName(String.valueOf(obj2));
        }
        Object obj3 = map.get("brand");
        if (obj3 != null) {
            product.setBrand(String.valueOf(obj3));
        }
        Object obj4 = map.get("category");
        if (obj4 != null) {
            product.setCategory(String.valueOf(obj4));
        }
        Object obj5 = map.get("variant");
        if (obj5 != null) {
            product.setVariant(String.valueOf(obj5));
        }
        Object obj6 = map.get("coupon");
        if (obj6 != null) {
            product.setCouponCode(String.valueOf(obj6));
        }
        Object obj7 = map.get("position");
        if (obj7 != null) {
            product.setPosition(zzW(obj7).intValue());
        }
        Object obj8 = map.get("price");
        if (obj8 != null) {
            product.setPrice(zzV(obj8).doubleValue());
        }
        Object obj9 = map.get("quantity");
        if (obj9 != null) {
            product.setQuantity(zzW(obj9).intValue());
        }
        for (String next : map.keySet()) {
            Matcher matcher = zzblX.matcher(next);
            if (matcher.matches()) {
                try {
                    product.setCustomDimension(Integer.parseInt(matcher.group(1)), String.valueOf(map.get(next)));
                } catch (NumberFormatException e) {
                    zzbg.zzaK("illegal number in custom dimension value: " + next);
                }
            } else {
                Matcher matcher2 = zzblY.matcher(next);
                if (matcher2.matches()) {
                    try {
                        product.setCustomMetric(Integer.parseInt(matcher2.group(1)), zzW(map.get(next)).intValue());
                    } catch (NumberFormatException e2) {
                        zzbg.zzaK("illegal number in custom metric value: " + next);
                    }
                }
            }
        }
        return product;
    }

    private Map<String, String> zzab(Map<String, zzag.zza> map) {
        zzag.zza zza = map.get(zzblU);
        if (zza != null) {
            return zzc(zza);
        }
        if (zzblZ == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("transactionId", "&ti");
            hashMap.put("transactionAffiliation", "&ta");
            hashMap.put("transactionTax", "&tt");
            hashMap.put("transactionShipping", "&ts");
            hashMap.put("transactionTotal", "&tr");
            hashMap.put("transactionCurrency", "&cu");
            zzblZ = hashMap;
        }
        return zzblZ;
    }

    private Map<String, String> zzac(Map<String, zzag.zza> map) {
        zzag.zza zza = map.get(zzblV);
        if (zza != null) {
            return zzc(zza);
        }
        if (zzbma == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", "&in");
            hashMap.put("sku", "&ic");
            hashMap.put("category", "&iv");
            hashMap.put("price", "&ip");
            hashMap.put("quantity", "&iq");
            hashMap.put("currency", "&cu");
            zzbma = hashMap;
        }
        return zzbma;
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x011d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zzb(com.google.android.gms.analytics.Tracker r8, java.util.Map<java.lang.String, com.google.android.gms.internal.zzag.zza> r9) {
        /*
            r7 = this;
            r1 = 0
            com.google.android.gms.analytics.HitBuilders$ScreenViewBuilder r3 = new com.google.android.gms.analytics.HitBuilders$ScreenViewBuilder
            r3.<init>()
            java.lang.String r0 = zzblS
            java.lang.Object r0 = r9.get(r0)
            com.google.android.gms.internal.zzag$zza r0 = (com.google.android.gms.internal.zzag.zza) r0
            java.util.Map r4 = r7.zzm(r0)
            r3.setAll(r4)
            java.lang.String r0 = zzblQ
            boolean r0 = r7.zzj(r9, r0)
            if (r0 == 0) goto L_0x008f
            com.google.android.gms.tagmanager.DataLayer r0 = r7.zzbhN
            java.lang.String r2 = "ecommerce"
            java.lang.Object r0 = r0.get(r2)
            boolean r2 = r0 instanceof java.util.Map
            if (r2 == 0) goto L_0x01ca
            java.util.Map r0 = (java.util.Map) r0
        L_0x002b:
            r2 = r0
        L_0x002c:
            if (r2 == 0) goto L_0x0199
            java.lang.String r0 = "&cu"
            java.lang.Object r0 = r4.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0040
            java.lang.String r0 = "currencyCode"
            java.lang.Object r0 = r2.get(r0)
            java.lang.String r0 = (java.lang.String) r0
        L_0x0040:
            if (r0 == 0) goto L_0x0047
            java.lang.String r4 = "&cu"
            r3.set(r4, r0)
        L_0x0047:
            java.lang.String r0 = "impressions"
            java.lang.Object r0 = r2.get(r0)
            boolean r4 = r0 instanceof java.util.List
            if (r4 == 0) goto L_0x00a3
            java.util.List r0 = (java.util.List) r0
            java.util.Iterator r4 = r0.iterator()
        L_0x0057:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x00a3
            java.lang.Object r0 = r4.next()
            java.util.Map r0 = (java.util.Map) r0
            com.google.android.gms.analytics.ecommerce.Product r5 = r7.zzaa(r0)     // Catch:{ RuntimeException -> 0x0073 }
            java.lang.String r6 = "list"
            java.lang.Object r0 = r0.get(r6)     // Catch:{ RuntimeException -> 0x0073 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ RuntimeException -> 0x0073 }
            r3.addImpression(r5, r0)     // Catch:{ RuntimeException -> 0x0073 }
            goto L_0x0057
        L_0x0073:
            r0 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to extract a product from DataLayer. "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r5.append(r0)
            java.lang.String r0 = r0.toString()
            com.google.android.gms.tagmanager.zzbg.e(r0)
            goto L_0x0057
        L_0x008f:
            java.lang.String r0 = zzblR
            java.lang.Object r0 = r9.get(r0)
            com.google.android.gms.internal.zzag$zza r0 = (com.google.android.gms.internal.zzag.zza) r0
            java.lang.Object r0 = com.google.android.gms.tagmanager.zzdf.zzl(r0)
            boolean r2 = r0 instanceof java.util.Map
            if (r2 == 0) goto L_0x01c7
            java.util.Map r0 = (java.util.Map) r0
            r2 = r0
            goto L_0x002c
        L_0x00a3:
            java.lang.String r0 = "promoClick"
            boolean r0 = r2.containsKey(r0)
            if (r0 == 0) goto L_0x00f2
            java.lang.String r0 = "promoClick"
            java.lang.Object r0 = r2.get(r0)
            java.util.Map r0 = (java.util.Map) r0
            java.lang.String r1 = "promotions"
            java.lang.Object r0 = r0.get(r1)
            java.util.List r0 = (java.util.List) r0
        L_0x00bb:
            r1 = 1
            if (r0 == 0) goto L_0x0180
            java.util.Iterator r4 = r0.iterator()
        L_0x00c2:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x010b
            java.lang.Object r0 = r4.next()
            java.util.Map r0 = (java.util.Map) r0
            com.google.android.gms.analytics.ecommerce.Promotion r0 = r7.zzZ(r0)     // Catch:{ RuntimeException -> 0x00d6 }
            r3.addPromotion(r0)     // Catch:{ RuntimeException -> 0x00d6 }
            goto L_0x00c2
        L_0x00d6:
            r0 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to extract a promotion from DataLayer. "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r5.append(r0)
            java.lang.String r0 = r0.toString()
            com.google.android.gms.tagmanager.zzbg.e(r0)
            goto L_0x00c2
        L_0x00f2:
            java.lang.String r0 = "promoView"
            boolean r0 = r2.containsKey(r0)
            if (r0 == 0) goto L_0x01c4
            java.lang.String r0 = "promoView"
            java.lang.Object r0 = r2.get(r0)
            java.util.Map r0 = (java.util.Map) r0
            java.lang.String r1 = "promotions"
            java.lang.Object r0 = r0.get(r1)
            java.util.List r0 = (java.util.List) r0
            goto L_0x00bb
        L_0x010b:
            java.lang.String r0 = "promoClick"
            boolean r0 = r2.containsKey(r0)
            if (r0 == 0) goto L_0x0179
            java.lang.String r0 = "&promoa"
            java.lang.String r1 = "click"
            r3.set(r0, r1)
            r0 = 0
        L_0x011b:
            if (r0 == 0) goto L_0x0199
            java.util.List<java.lang.String> r0 = zzblW
            java.util.Iterator r1 = r0.iterator()
        L_0x0123:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x0199
            java.lang.Object r0 = r1.next()
            java.lang.String r0 = (java.lang.String) r0
            boolean r4 = r2.containsKey(r0)
            if (r4 == 0) goto L_0x0123
            java.lang.Object r1 = r2.get(r0)
            java.util.Map r1 = (java.util.Map) r1
            java.lang.String r2 = "products"
            java.lang.Object r2 = r1.get(r2)
            java.util.List r2 = (java.util.List) r2
            if (r2 == 0) goto L_0x0182
            java.util.Iterator r4 = r2.iterator()
        L_0x0149:
            boolean r2 = r4.hasNext()
            if (r2 == 0) goto L_0x0182
            java.lang.Object r2 = r4.next()
            java.util.Map r2 = (java.util.Map) r2
            com.google.android.gms.analytics.ecommerce.Product r2 = r7.zzaa(r2)     // Catch:{ RuntimeException -> 0x015d }
            r3.addProduct(r2)     // Catch:{ RuntimeException -> 0x015d }
            goto L_0x0149
        L_0x015d:
            r2 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to extract a product from DataLayer. "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r2 = r2.getMessage()
            java.lang.StringBuilder r2 = r5.append(r2)
            java.lang.String r2 = r2.toString()
            com.google.android.gms.tagmanager.zzbg.e(r2)
            goto L_0x0149
        L_0x0179:
            java.lang.String r0 = "&promoa"
            java.lang.String r4 = "view"
            r3.set(r0, r4)
        L_0x0180:
            r0 = r1
            goto L_0x011b
        L_0x0182:
            java.lang.String r2 = "actionField"
            boolean r2 = r1.containsKey(r2)     // Catch:{ RuntimeException -> 0x01a8 }
            if (r2 == 0) goto L_0x01a1
            java.lang.String r2 = "actionField"
            java.lang.Object r1 = r1.get(r2)     // Catch:{ RuntimeException -> 0x01a8 }
            java.util.Map r1 = (java.util.Map) r1     // Catch:{ RuntimeException -> 0x01a8 }
            com.google.android.gms.analytics.ecommerce.ProductAction r0 = r7.zzd(r0, r1)     // Catch:{ RuntimeException -> 0x01a8 }
        L_0x0196:
            r3.setProductAction(r0)     // Catch:{ RuntimeException -> 0x01a8 }
        L_0x0199:
            java.util.Map r0 = r3.build()
            r8.send(r0)
            return
        L_0x01a1:
            com.google.android.gms.analytics.ecommerce.ProductAction r1 = new com.google.android.gms.analytics.ecommerce.ProductAction     // Catch:{ RuntimeException -> 0x01a8 }
            r1.<init>(r0)     // Catch:{ RuntimeException -> 0x01a8 }
            r0 = r1
            goto L_0x0196
        L_0x01a8:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Failed to extract a product action from DataLayer. "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            com.google.android.gms.tagmanager.zzbg.e(r0)
            goto L_0x0199
        L_0x01c4:
            r0 = r1
            goto L_0x00bb
        L_0x01c7:
            r2 = r1
            goto L_0x002c
        L_0x01ca:
            r0 = r1
            goto L_0x002b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzdg.zzb(com.google.android.gms.analytics.Tracker, java.util.Map):void");
    }

    private Map<String, String> zzc(zzag.zza zza) {
        Object zzl = zzdf.zzl(zza);
        if (!(zzl instanceof Map)) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry entry : ((Map) zzl).entrySet()) {
            linkedHashMap.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return linkedHashMap;
    }

    private ProductAction zzd(String str, Map<String, Object> map) {
        ProductAction productAction = new ProductAction(str);
        Object obj = map.get("id");
        if (obj != null) {
            productAction.setTransactionId(String.valueOf(obj));
        }
        Object obj2 = map.get("affiliation");
        if (obj2 != null) {
            productAction.setTransactionAffiliation(String.valueOf(obj2));
        }
        Object obj3 = map.get("coupon");
        if (obj3 != null) {
            productAction.setTransactionCouponCode(String.valueOf(obj3));
        }
        Object obj4 = map.get("list");
        if (obj4 != null) {
            productAction.setProductActionList(String.valueOf(obj4));
        }
        Object obj5 = map.get("option");
        if (obj5 != null) {
            productAction.setCheckoutOptions(String.valueOf(obj5));
        }
        Object obj6 = map.get("revenue");
        if (obj6 != null) {
            productAction.setTransactionRevenue(zzV(obj6).doubleValue());
        }
        Object obj7 = map.get("tax");
        if (obj7 != null) {
            productAction.setTransactionTax(zzV(obj7).doubleValue());
        }
        Object obj8 = map.get("shipping");
        if (obj8 != null) {
            productAction.setTransactionShipping(zzV(obj8).doubleValue());
        }
        Object obj9 = map.get("step");
        if (obj9 != null) {
            productAction.setCheckoutStep(zzW(obj9).intValue());
        }
        return productAction;
    }

    private void zze(Map<String, String> map, String str, String str2) {
        if (str2 != null) {
            map.put(str, str2);
        }
    }

    private String zzgy(String str) {
        Object obj = this.zzbhN.get(str);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    private List<Map<String, String>> zzgz(String str) {
        Object obj = this.zzbhN.get(str);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List)) {
            throw new IllegalArgumentException("transactionProducts should be of type List.");
        }
        for (Object obj2 : (List) obj) {
            if (!(obj2 instanceof Map)) {
                throw new IllegalArgumentException("Each element of transactionProducts should be of type Map.");
            }
        }
        return (List) obj;
    }

    private boolean zzj(Map<String, zzag.zza> map, String str) {
        zzag.zza zza = map.get(str);
        if (zza == null) {
            return false;
        }
        return zzdf.zzk(zza).booleanValue();
    }

    private Map<String, String> zzm(zzag.zza zza) {
        if (zza == null) {
            return new HashMap();
        }
        Map<String, String> zzc = zzc(zza);
        if (zzc == null) {
            return new HashMap();
        }
        String str = zzc.get("&aip");
        if (str != null && this.zzbmb.contains(str.toLowerCase())) {
            zzc.remove("&aip");
        }
        return zzc;
    }

    public /* bridge */ /* synthetic */ boolean zzFW() {
        return super.zzFW();
    }

    public /* bridge */ /* synthetic */ String zzGB() {
        return super.zzGB();
    }

    public /* bridge */ /* synthetic */ Set zzGC() {
        return super.zzGC();
    }

    public /* bridge */ /* synthetic */ zzag.zza zzP(Map map) {
        return super.zzP(map);
    }

    public void zzR(Map<String, zzag.zza> map) {
        Tracker zzgq = this.zzbmc.zzgq("_GTM_DEFAULT_TRACKER_");
        zzgq.enableAdvertisingIdCollection(zzj(map, "collect_adid"));
        if (zzj(map, zzblP)) {
            zzb(zzgq, map);
        } else if (zzj(map, zzblO)) {
            zzgq.send(zzm(map.get(zzblS)));
        } else if (zzj(map, zzblT)) {
            zza(zzgq, map);
        } else {
            zzbg.zzaK("Ignoring unknown tag.");
        }
    }
}
