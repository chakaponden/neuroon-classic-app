package com.crashlytics.android.answers;

import android.app.Activity;
import java.util.Collections;
import java.util.Map;

final class SessionEvent {
    static final String ACTIVITY_KEY = "activity";
    static final String EXCEPTION_NAME_KEY = "exceptionName";
    static final String SESSION_ID_KEY = "sessionId";
    public final Map<String, Object> customAttributes;
    public final String customType;
    public final Map<String, String> details;
    public final Map<String, Object> predefinedAttributes;
    public final String predefinedType;
    public final SessionEventMetadata sessionEventMetadata;
    private String stringRepresentation;
    public final long timestamp;
    public final Type type;

    enum Type {
        START,
        RESUME,
        PAUSE,
        STOP,
        CRASH,
        INSTALL,
        CUSTOM,
        PREDEFINED
    }

    public static Builder lifecycleEventBuilder(Type type2, Activity activity) {
        return new Builder(type2).details(Collections.singletonMap(ACTIVITY_KEY, activity.getClass().getName()));
    }

    public static Builder installEventBuilder() {
        return new Builder(Type.INSTALL);
    }

    public static Builder crashEventBuilder(String sessionId) {
        return new Builder(Type.CRASH).details(Collections.singletonMap(SESSION_ID_KEY, sessionId));
    }

    public static Builder crashEventBuilder(String sessionId, String exceptionName) {
        return crashEventBuilder(sessionId).customAttributes(Collections.singletonMap(EXCEPTION_NAME_KEY, exceptionName));
    }

    public static Builder customEventBuilder(CustomEvent event) {
        return new Builder(Type.CUSTOM).customType(event.getCustomType()).customAttributes(event.getCustomAttributes());
    }

    public static Builder predefinedEventBuilder(PredefinedEvent<?> event) {
        return new Builder(Type.PREDEFINED).predefinedType(event.getPredefinedType()).predefinedAttributes(event.getPredefinedAttributes()).customAttributes(event.getCustomAttributes());
    }

    private SessionEvent(SessionEventMetadata sessionEventMetadata2, long timestamp2, Type type2, Map<String, String> details2, String customType2, Map<String, Object> customAttributes2, String predefinedType2, Map<String, Object> predefinedAttributes2) {
        this.sessionEventMetadata = sessionEventMetadata2;
        this.timestamp = timestamp2;
        this.type = type2;
        this.details = details2;
        this.customType = customType2;
        this.customAttributes = customAttributes2;
        this.predefinedType = predefinedType2;
        this.predefinedAttributes = predefinedAttributes2;
    }

    static class Builder {
        Map<String, Object> customAttributes = null;
        String customType = null;
        Map<String, String> details = null;
        Map<String, Object> predefinedAttributes = null;
        String predefinedType = null;
        final long timestamp = System.currentTimeMillis();
        final Type type;

        public Builder(Type type2) {
            this.type = type2;
        }

        public Builder details(Map<String, String> details2) {
            this.details = details2;
            return this;
        }

        public Builder customType(String customType2) {
            this.customType = customType2;
            return this;
        }

        public Builder customAttributes(Map<String, Object> customAttributes2) {
            this.customAttributes = customAttributes2;
            return this;
        }

        public Builder predefinedType(String predefinedType2) {
            this.predefinedType = predefinedType2;
            return this;
        }

        public Builder predefinedAttributes(Map<String, Object> predefinedAttributes2) {
            this.predefinedAttributes = predefinedAttributes2;
            return this;
        }

        public SessionEvent build(SessionEventMetadata sessionEventMetadata) {
            return new SessionEvent(sessionEventMetadata, this.timestamp, this.type, this.details, this.customType, this.customAttributes, this.predefinedType, this.predefinedAttributes);
        }
    }

    public String toString() {
        if (this.stringRepresentation == null) {
            this.stringRepresentation = "[" + getClass().getSimpleName() + ": " + "timestamp=" + this.timestamp + ", type=" + this.type + ", details=" + this.details + ", customType=" + this.customType + ", customAttributes=" + this.customAttributes + ", predefinedType=" + this.predefinedType + ", predefinedAttributes=" + this.predefinedAttributes + ", metadata=[" + this.sessionEventMetadata + "]]";
        }
        return this.stringRepresentation;
    }
}
