package io.intercom.android.sdk.identity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import io.intercom.android.sdk.models.User;
import io.intercom.android.sdk.utilities.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserIdentity {
    public static final String ANONYMOUS_ID = "anonymous_id";
    public static final String EMAIL = "email";
    public static final String INTERCOM_ID = "intercom_id";
    private static final String PREFS_ANONYMOUS_ID = "INTERCOM_SDK_ANONYMOUS_ID";
    private static final String PREFS_EMAIL_ID = "INTERCOM_SDK_EMAIL_ID";
    private static final String PREFS_INTERCOM_ID = "INTERCOM_SDK_INTERCOM_ID";
    private static final String PREFS_USER_ID = "INTERCOM_SDK_USER_ID";
    public static final String TYPE = "type";
    private static final String USER = "user";
    public static final String USER_ID = "user_id";
    private String anonymousId;
    private String data;
    private String email;
    private String fingerprint = "";
    private String hmac;
    private String intercomId;
    private final SharedPreferences prefs;
    private String userId;

    public UserIdentity(Context context) {
        this.prefs = context.getSharedPreferences(Constants.INTERCOM_USER_PREFS, 0);
        this.anonymousId = this.prefs.getString("intercomsdk-session-INTERCOM_SDK_ANONYMOUS_ID", "");
        this.intercomId = this.prefs.getString("intercomsdk-session-INTERCOM_SDK_INTERCOM_ID", "");
        this.userId = this.prefs.getString("intercomsdk-session-INTERCOM_SDK_USER_ID", "");
        this.email = this.prefs.getString("intercomsdk-session-INTERCOM_SDK_EMAIL_ID", "");
        this.data = this.prefs.getString("intercomsdk-session-SecureMode_Data", "");
        this.hmac = this.prefs.getString("intercomsdk-session-SecureMode_HMAC", "");
        if (identityExists()) {
            this.fingerprint = generateFingerprint();
        }
    }

    /* access modifiers changed from: protected */
    public boolean register(String userId2, String email2, String intercomId2) {
        boolean shouldRegisterUser = !isIdentifiedUser();
        if (shouldRegisterUser) {
            SharedPreferences.Editor editor = this.prefs.edit();
            this.intercomId = "";
            editor.putString("intercomsdk-session-INTERCOM_SDK_INTERCOM_ID", intercomId2);
            if (!userId2.isEmpty()) {
                this.userId = userId2;
                editor.putString("intercomsdk-session-INTERCOM_SDK_USER_ID", userId2);
            }
            if (!email2.isEmpty()) {
                this.email = email2;
                editor.putString("intercomsdk-session-INTERCOM_SDK_EMAIL_ID", email2);
            }
            if (!intercomId2.isEmpty()) {
                this.intercomId = intercomId2;
                editor.putString("intercomsdk-session-INTERCOM_SDK_INTERCOM_ID", intercomId2);
            }
            editor.apply();
            if (this.fingerprint.isEmpty()) {
                this.fingerprint = generateFingerprint();
            }
        }
        return shouldRegisterUser;
    }

    /* access modifiers changed from: protected */
    public boolean registerUnidentified() {
        if (isEmpty()) {
            SharedPreferences.Editor editor = this.prefs.edit();
            this.anonymousId = UUID.randomUUID().toString();
            editor.putString("intercomsdk-session-INTERCOM_SDK_ANONYMOUS_ID", this.anonymousId);
            editor.apply();
            if (this.fingerprint.isEmpty()) {
                this.fingerprint = generateFingerprint();
            }
        }
        return !isEmpty();
    }

    /* access modifiers changed from: protected */
    public void update(User user) {
        SharedPreferences.Editor editor = this.prefs.edit();
        this.userId = user.getUserId();
        editor.putString("intercomsdk-session-INTERCOM_SDK_USER_ID", this.userId);
        this.email = user.getEmail();
        editor.putString("intercomsdk-session-INTERCOM_SDK_EMAIL_ID", this.email);
        if (!user.getIntercomId().isEmpty()) {
            this.intercomId = user.getIntercomId();
            editor.putString("intercomsdk-session-INTERCOM_SDK_INTERCOM_ID", this.intercomId);
        }
        this.anonymousId = user.getAnonymousId();
        editor.putString("intercomsdk-session-INTERCOM_SDK_ANONYMOUS_ID", this.anonymousId);
        editor.apply();
    }

    /* access modifiers changed from: protected */
    public void setSecureMode(String hmac2, String data2) {
        this.data = data2;
        this.hmac = hmac2;
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString("intercomsdk-session-SecureMode_Data", data2);
        editor.putString("intercomsdk-session-SecureMode_HMAC", hmac2);
        editor.apply();
    }

    /* access modifiers changed from: protected */
    public String getData() {
        return this.data;
    }

    /* access modifiers changed from: protected */
    public String getHmac() {
        return this.hmac;
    }

    /* access modifiers changed from: protected */
    public String getIntercomId() {
        return this.intercomId;
    }

    /* access modifiers changed from: protected */
    public String getFingerprint() {
        return this.fingerprint;
    }

    /* access modifiers changed from: protected */
    public boolean isEmpty() {
        return this.anonymousId.isEmpty() && this.email.isEmpty() && this.userId.isEmpty() && this.intercomId.isEmpty();
    }

    /* access modifiers changed from: protected */
    public boolean identityExists() {
        return !this.email.isEmpty() || !this.userId.isEmpty() || !this.intercomId.isEmpty() || !this.anonymousId.isEmpty();
    }

    /* access modifiers changed from: protected */
    public boolean isIdentifiedUser() {
        return identityExists() && !isAnonymous();
    }

    /* access modifiers changed from: protected */
    public boolean isAnonymous() {
        return !this.anonymousId.isEmpty() && this.email.isEmpty() && this.userId.isEmpty();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"CommitPrefEdits"})
    public void clear() {
        this.prefs.edit().clear().commit();
        this.anonymousId = "";
        this.intercomId = "";
        this.userId = "";
        this.email = "";
        this.data = "";
        this.hmac = "";
        this.fingerprint = "";
    }

    /* access modifiers changed from: protected */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (!this.anonymousId.isEmpty()) {
            map.put(ANONYMOUS_ID, this.anonymousId);
        } else if (!this.intercomId.isEmpty()) {
            map.put(INTERCOM_ID, this.intercomId);
        }
        if (!this.userId.isEmpty()) {
            map.put(USER_ID, this.userId);
        }
        if (!this.email.isEmpty()) {
            map.put("email", this.email);
        }
        if (!"user".isEmpty()) {
            map.put(TYPE, "user");
        }
        return map;
    }

    private String generateFingerprint() {
        return UUID.randomUUID().toString();
    }
}
