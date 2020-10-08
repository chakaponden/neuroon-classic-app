package io.intercom.android.sdk.models;

import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.nexus.NexusConfig;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private final boolean audioEnabled;
    private final String baseColor;
    private final boolean inboundMessages;
    private final boolean isFirstRequest;
    private final String messageResponse;
    private final int noRealtimeThrottle;
    private final int pollingInterval;
    private final int rateLimitCount;
    private final int rateLimitPeriod;
    private final boolean realTime;
    private final NexusConfig realTimeConfig;
    private final boolean showPoweredBy;
    private final int userUpdateCacheMaxAge;
    private final List<Block> welcomeMessage;

    public Config() {
        this(new Builder());
    }

    public Config(Builder builder) {
        this.welcomeMessage = new ArrayList();
        if (builder.welcome_message != null) {
            for (Block.Builder blockBuilder : builder.welcome_message) {
                this.welcomeMessage.add(blockBuilder.withAlign("center").build());
            }
        }
        this.isFirstRequest = builder.is_first_request;
        this.inboundMessages = builder.inbound_messages;
        this.showPoweredBy = builder.show_powered_by;
        this.baseColor = builder.base_color == null ? String.format("#%06X", new Object[]{Integer.valueOf(16777215 & Bridge.getContext().getResources().getColor(R.color.intercomsdk_main_blue))}) : builder.base_color;
        this.pollingInterval = builder.polling_interval;
        this.noRealtimeThrottle = builder.no_real_time_throttle;
        this.userUpdateCacheMaxAge = builder.user_update_dup_cache_max_age;
        this.realTime = builder.real_time;
        this.realTimeConfig = builder.real_time_config == null ? new NexusConfig() : builder.real_time_config.build();
        this.messageResponse = builder.auto_response == null ? "" : builder.auto_response;
        this.audioEnabled = builder.audio_enabled;
        this.rateLimitPeriod = builder.local_rate_limit_period;
        this.rateLimitCount = builder.local_rate_limit;
    }

    public String getBaseColor() {
        return this.baseColor;
    }

    public boolean isFirstRequest() {
        return this.isFirstRequest;
    }

    public boolean isInboundMessages() {
        return this.inboundMessages;
    }

    public boolean isShowPoweredBy() {
        return this.showPoweredBy;
    }

    public List<Block> getWelcomeMessage() {
        return this.welcomeMessage;
    }

    public int getPollingInterval() {
        return this.pollingInterval;
    }

    public int getNoRealtimeThrottle() {
        return this.noRealtimeThrottle;
    }

    public int getUserUpdateCacheMaxAge() {
        return this.userUpdateCacheMaxAge;
    }

    public boolean isRealTime() {
        return this.realTime;
    }

    public NexusConfig getRealTimeConfig() {
        return this.realTimeConfig;
    }

    public String getMessageResponse() {
        return this.messageResponse;
    }

    public boolean isAudioEnabled() {
        return this.audioEnabled;
    }

    public int getRateLimitPeriod() {
        return this.rateLimitPeriod;
    }

    public int getRateLimitCount() {
        return this.rateLimitCount;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean audio_enabled;
        /* access modifiers changed from: private */
        public String auto_response;
        /* access modifiers changed from: private */
        public String base_color;
        /* access modifiers changed from: private */
        public boolean inbound_messages;
        /* access modifiers changed from: private */
        public boolean is_first_request;
        /* access modifiers changed from: private */
        public int local_rate_limit;
        /* access modifiers changed from: private */
        public int local_rate_limit_period;
        /* access modifiers changed from: private */
        public int no_real_time_throttle;
        /* access modifiers changed from: private */
        public int polling_interval;
        /* access modifiers changed from: private */
        public boolean real_time;
        /* access modifiers changed from: private */
        public NexusConfig.Builder real_time_config;
        /* access modifiers changed from: private */
        public boolean show_powered_by;
        /* access modifiers changed from: private */
        public int user_update_dup_cache_max_age;
        /* access modifiers changed from: private */
        public List<Block.Builder> welcome_message;

        public Config build() {
            return new Config(this);
        }
    }
}
