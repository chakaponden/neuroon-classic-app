package io.intercom.android.sdk.identity;

import android.content.Context;
import io.intercom.android.sdk.models.Config;
import io.intercom.android.sdk.models.User;
import java.util.Map;

public class IdentityStore {
    private final AppIdentity appIdentity;
    private final UserIdentity userIdentity;

    public IdentityStore(Context context) {
        this.userIdentity = new UserIdentity(context);
        this.appIdentity = new AppIdentity(context);
    }

    public boolean registerIdentifiedUser(Registration registration) {
        return registration.isValidRegistration() && this.userIdentity.register(registration.getUserId(), registration.getEmail(), "");
    }

    public boolean registerUnidentifiedUser() {
        return this.userIdentity.registerUnidentified();
    }

    public void setAppIdentity(String apiKey, String appId) {
        this.appIdentity.update(apiKey, appId);
    }

    public String getData() {
        return this.userIdentity.getData();
    }

    public String getHmac() {
        return this.userIdentity.getHmac();
    }

    public String getIntercomId() {
        return this.userIdentity.getIntercomId();
    }

    public void setUser(User user) {
        this.userIdentity.update(user);
    }

    public AppConfig getAppConfig() {
        return this.appIdentity.getAppConfig();
    }

    public void setAppConfig(Config config) {
        this.appIdentity.updateAppConfig(config);
    }

    public void resetUserIdentity() {
        this.userIdentity.clear();
    }

    public void setSecureMode(String secureHash, String secureData) {
        this.userIdentity.setSecureMode(secureHash, secureData);
    }

    public boolean isIdentifiedUser() {
        return this.userIdentity.isIdentifiedUser();
    }

    public boolean isAnonymousUser() {
        return this.userIdentity.isAnonymous();
    }

    public boolean userIdentityExists() {
        return this.userIdentity.identityExists();
    }

    public boolean appIdentityExists() {
        return this.appIdentity.appIdentityExists();
    }

    public boolean hasIntercomid() {
        return !this.userIdentity.getIntercomId().isEmpty();
    }

    public Map<String, Object> getUserMap() {
        return this.userIdentity.toMap();
    }

    public String getAppId() {
        return this.appIdentity.getAppId();
    }

    public String getApiKey() {
        return this.appIdentity.getApiKey();
    }

    public UserIdentity getUserIdentity() {
        return this.userIdentity;
    }

    public String getUserIdentityFingerprint() {
        return this.userIdentity.getFingerprint();
    }
}
