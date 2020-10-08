package com.crashlytics.android.answers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

class AnswersAttributes {
    final Map<String, Object> attributes = new ConcurrentHashMap();
    final AnswersEventValidator validator;

    public AnswersAttributes(AnswersEventValidator validator2) {
        this.validator = validator2;
    }

    /* access modifiers changed from: package-private */
    public void put(String key, String value) {
        if (!this.validator.isNull(key, "key") && !this.validator.isNull(value, "value")) {
            putAttribute(this.validator.limitStringLength(key), this.validator.limitStringLength(value));
        }
    }

    /* access modifiers changed from: package-private */
    public void put(String key, Number value) {
        if (!this.validator.isNull(key, "key") && !this.validator.isNull(value, "value")) {
            putAttribute(this.validator.limitStringLength(key), value);
        }
    }

    /* access modifiers changed from: package-private */
    public void putAttribute(String key, Object value) {
        if (!this.validator.isFullMap(this.attributes, key)) {
            this.attributes.put(key, value);
        }
    }

    public String toString() {
        return new JSONObject(this.attributes).toString();
    }
}
