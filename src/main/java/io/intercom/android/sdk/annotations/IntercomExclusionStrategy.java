package io.intercom.android.sdk.annotations;

import io.intercom.com.google.gson.ExclusionStrategy;
import io.intercom.com.google.gson.FieldAttributes;

public class IntercomExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(Exclude.class) != null;
    }

    public boolean shouldSkipClass(Class<?> cls) {
        return false;
    }
}
