package io.intercom.android.sdk.identity;

import android.text.TextUtils;
import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.HashMap;
import java.util.Map;

public class Registration {
    private Map<String, Object> attributes = new HashMap();
    private String email = "";
    private boolean isValidRegistration = true;
    private String registrationId = "";
    private String userId = "";

    public static Registration create() {
        return new Registration();
    }

    public Registration withEmail(String email2) {
        if (TextUtils.isEmpty(email2)) {
            IntercomLogger.ERROR("Email cannot be null or empty");
            this.isValidRegistration = false;
        } else {
            this.email = email2;
        }
        return this;
    }

    public Registration withUserId(String userId2) {
        if (TextUtils.isEmpty(userId2)) {
            IntercomLogger.ERROR("UserId cannot be null or empty");
            this.isValidRegistration = false;
        } else {
            this.userId = userId2;
        }
        return this;
    }

    public Registration withGCMRegistrationId(String registrationId2) {
        if (TextUtils.isEmpty(registrationId2)) {
            IntercomLogger.ERROR("GCM registration id should not be null or empty");
        } else {
            this.registrationId = registrationId2;
        }
        return this;
    }

    public Registration withUserAttributes(Map<String, Object> attributes2) {
        this.isValidRegistration = false;
        if (attributes2 == null) {
            IntercomLogger.ERROR("Registration.withUserAttributes method failed: the attributes Map provided is null");
        } else if (attributes2.isEmpty()) {
            IntercomLogger.ERROR("Registration.withUserAttributes method failed: the attributes Map provided is empty");
        } else {
            this.attributes = attributes2;
            this.isValidRegistration = true;
        }
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /* access modifiers changed from: protected */
    public boolean isValidRegistration() {
        return this.isValidRegistration;
    }
}
