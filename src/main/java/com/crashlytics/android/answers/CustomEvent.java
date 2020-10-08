package com.crashlytics.android.answers;

public class CustomEvent extends AnswersEvent<CustomEvent> {
    private final String eventName;

    public CustomEvent(String eventName2) {
        if (eventName2 == null) {
            throw new NullPointerException("eventName must not be null");
        }
        this.eventName = this.validator.limitStringLength(eventName2);
    }

    /* access modifiers changed from: package-private */
    public String getCustomType() {
        return this.eventName;
    }

    public String toString() {
        return "{eventName:\"" + this.eventName + '\"' + ", customAttributes:" + this.customAttributes + "}";
    }
}
