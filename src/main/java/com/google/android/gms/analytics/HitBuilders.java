package com.google.android.gms.analytics;

import android.text.TextUtils;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;
import com.google.android.gms.analytics.internal.zzae;
import com.google.android.gms.analytics.internal.zzam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HitBuilders {

    @Deprecated
    public static class AppViewBuilder extends HitBuilder<AppViewBuilder> {
        public AppViewBuilder() {
            set("&t", "screenview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class EventBuilder extends HitBuilder<EventBuilder> {
        public EventBuilder() {
            set("&t", "event");
        }

        public EventBuilder(String category, String action) {
            this();
            setCategory(category);
            setAction(action);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public EventBuilder setAction(String action) {
            set("&ea", action);
            return this;
        }

        public EventBuilder setCategory(String category) {
            set("&ec", category);
            return this;
        }

        public EventBuilder setLabel(String label) {
            set("&el", label);
            return this;
        }

        public EventBuilder setValue(long value) {
            set("&ev", Long.toString(value));
            return this;
        }
    }

    public static class ExceptionBuilder extends HitBuilder<ExceptionBuilder> {
        public ExceptionBuilder() {
            set("&t", "exception");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public ExceptionBuilder setDescription(String description) {
            set("&exd", description);
            return this;
        }

        public ExceptionBuilder setFatal(boolean fatal) {
            set("&exf", zzam.zzK(fatal));
            return this;
        }
    }

    protected static class HitBuilder<T extends HitBuilder> {
        private Map<String, String> zzPm = new HashMap();
        ProductAction zzPn;
        Map<String, List<Product>> zzPo = new HashMap();
        List<Promotion> zzPp = new ArrayList();
        List<Product> zzPq = new ArrayList();

        protected HitBuilder() {
        }

        public T addImpression(Product product, String impressionList) {
            if (product == null) {
                zzae.zzaK("product should be non-null");
            } else {
                if (impressionList == null) {
                    impressionList = "";
                }
                if (!this.zzPo.containsKey(impressionList)) {
                    this.zzPo.put(impressionList, new ArrayList());
                }
                this.zzPo.get(impressionList).add(product);
            }
            return this;
        }

        public T addProduct(Product product) {
            if (product == null) {
                zzae.zzaK("product should be non-null");
            } else {
                this.zzPq.add(product);
            }
            return this;
        }

        public T addPromotion(Promotion promotion) {
            if (promotion == null) {
                zzae.zzaK("promotion should be non-null");
            } else {
                this.zzPp.add(promotion);
            }
            return this;
        }

        public Map<String, String> build() {
            HashMap hashMap = new HashMap(this.zzPm);
            if (this.zzPn != null) {
                hashMap.putAll(this.zzPn.build());
            }
            int i = 1;
            for (Promotion zzba : this.zzPp) {
                hashMap.putAll(zzba.zzba(zzc.zzZ(i)));
                i++;
            }
            int i2 = 1;
            for (Product zzba2 : this.zzPq) {
                hashMap.putAll(zzba2.zzba(zzc.zzX(i2)));
                i2++;
            }
            int i3 = 1;
            for (Map.Entry next : this.zzPo.entrySet()) {
                String zzac = zzc.zzac(i3);
                int i4 = 1;
                for (Product zzba3 : (List) next.getValue()) {
                    hashMap.putAll(zzba3.zzba(zzac + zzc.zzab(i4)));
                    i4++;
                }
                if (!TextUtils.isEmpty((CharSequence) next.getKey())) {
                    hashMap.put(zzac + "nm", next.getKey());
                }
                i3++;
            }
            return hashMap;
        }

        /* access modifiers changed from: protected */
        public String get(String paramName) {
            return this.zzPm.get(paramName);
        }

        public final T set(String paramName, String paramValue) {
            if (paramName != null) {
                this.zzPm.put(paramName, paramValue);
            } else {
                zzae.zzaK(" HitBuilder.set() called with a null paramName.");
            }
            return this;
        }

        public final T setAll(Map<String, String> params) {
            if (params != null) {
                this.zzPm.putAll(new HashMap(params));
            }
            return this;
        }

        public T setCampaignParamsFromUrl(String utmParams) {
            String zzbu = zzam.zzbu(utmParams);
            if (!TextUtils.isEmpty(zzbu)) {
                Map<String, String> zzbs = zzam.zzbs(zzbu);
                set("&cc", zzbs.get("utm_content"));
                set("&cm", zzbs.get("utm_medium"));
                set("&cn", zzbs.get("utm_campaign"));
                set("&cs", zzbs.get("utm_source"));
                set("&ck", zzbs.get("utm_term"));
                set("&ci", zzbs.get("utm_id"));
                set("&anid", zzbs.get("anid"));
                set("&gclid", zzbs.get("gclid"));
                set("&dclid", zzbs.get("dclid"));
                set("&aclid", zzbs.get("aclid"));
                set("&gmob_t", zzbs.get("gmob_t"));
            }
            return this;
        }

        public T setCustomDimension(int index, String dimension) {
            set(zzc.zzT(index), dimension);
            return this;
        }

        public T setCustomMetric(int index, float metric) {
            set(zzc.zzV(index), Float.toString(metric));
            return this;
        }

        /* access modifiers changed from: protected */
        public T setHitType(String hitType) {
            set("&t", hitType);
            return this;
        }

        public T setNewSession() {
            set("&sc", "start");
            return this;
        }

        public T setNonInteraction(boolean nonInteraction) {
            set("&ni", zzam.zzK(nonInteraction));
            return this;
        }

        public T setProductAction(ProductAction action) {
            this.zzPn = action;
            return this;
        }

        public T setPromotionAction(String action) {
            this.zzPm.put("&promoa", action);
            return this;
        }
    }

    @Deprecated
    public static class ItemBuilder extends HitBuilder<ItemBuilder> {
        public ItemBuilder() {
            set("&t", "item");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public ItemBuilder setCategory(String category) {
            set("&iv", category);
            return this;
        }

        public ItemBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public ItemBuilder setName(String name) {
            set("&in", name);
            return this;
        }

        public ItemBuilder setPrice(double price) {
            set("&ip", Double.toString(price));
            return this;
        }

        public ItemBuilder setQuantity(long quantity) {
            set("&iq", Long.toString(quantity));
            return this;
        }

        public ItemBuilder setSku(String sku) {
            set("&ic", sku);
            return this;
        }

        public ItemBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }

    public static class ScreenViewBuilder extends HitBuilder<ScreenViewBuilder> {
        public ScreenViewBuilder() {
            set("&t", "screenview");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }
    }

    public static class SocialBuilder extends HitBuilder<SocialBuilder> {
        public SocialBuilder() {
            set("&t", "social");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public SocialBuilder setAction(String action) {
            set("&sa", action);
            return this;
        }

        public SocialBuilder setNetwork(String network) {
            set("&sn", network);
            return this;
        }

        public SocialBuilder setTarget(String target) {
            set("&st", target);
            return this;
        }
    }

    public static class TimingBuilder extends HitBuilder<TimingBuilder> {
        public TimingBuilder() {
            set("&t", "timing");
        }

        public TimingBuilder(String category, String variable, long value) {
            this();
            setVariable(variable);
            setValue(value);
            setCategory(category);
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public TimingBuilder setCategory(String category) {
            set("&utc", category);
            return this;
        }

        public TimingBuilder setLabel(String label) {
            set("&utl", label);
            return this;
        }

        public TimingBuilder setValue(long value) {
            set("&utt", Long.toString(value));
            return this;
        }

        public TimingBuilder setVariable(String variable) {
            set("&utv", variable);
            return this;
        }
    }

    @Deprecated
    public static class TransactionBuilder extends HitBuilder<TransactionBuilder> {
        public TransactionBuilder() {
            set("&t", "transaction");
        }

        public /* bridge */ /* synthetic */ Map build() {
            return super.build();
        }

        public TransactionBuilder setAffiliation(String affiliation) {
            set("&ta", affiliation);
            return this;
        }

        public TransactionBuilder setCurrencyCode(String currencyCode) {
            set("&cu", currencyCode);
            return this;
        }

        public TransactionBuilder setRevenue(double revenue) {
            set("&tr", Double.toString(revenue));
            return this;
        }

        public TransactionBuilder setShipping(double shipping) {
            set("&ts", Double.toString(shipping));
            return this;
        }

        public TransactionBuilder setTax(double tax) {
            set("&tt", Double.toString(tax));
            return this;
        }

        public TransactionBuilder setTransactionId(String transactionid) {
            set("&ti", transactionid);
            return this;
        }
    }
}
