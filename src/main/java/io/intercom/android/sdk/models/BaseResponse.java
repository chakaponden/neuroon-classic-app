package io.intercom.android.sdk.models;

import io.intercom.android.sdk.models.Config;
import io.intercom.android.sdk.models.User;

public abstract class BaseResponse {
    private final Config config;
    private final User user;

    protected BaseResponse(Builder builder) {
        this.config = builder.config == null ? new Config() : builder.config.build();
        this.user = builder.user == null ? new User() : builder.user.build();
    }

    public Config getConfig() {
        return this.config;
    }

    public User getUser() {
        return this.user;
    }

    protected static abstract class Builder {
        protected Config.Builder config;
        protected User.Builder user;

        public abstract BaseResponse build();

        protected Builder() {
        }
    }
}
