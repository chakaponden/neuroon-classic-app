package io.intercom.android.sdk.nexus;

import java.util.ArrayList;
import java.util.List;

public class NexusConfig {
    private final int connectionTimeout;
    private final List<String> endpoints;
    private final int presenceHeartbeatInterval;

    public NexusConfig() {
        this(new Builder());
    }

    public NexusConfig(Builder builder) {
        this.endpoints = builder.endpoints == null ? new ArrayList<>() : builder.endpoints;
        this.presenceHeartbeatInterval = builder.presence_heartbeat_interval;
        this.connectionTimeout = builder.connection_timeout;
    }

    public List<String> getEndpoints() {
        return this.endpoints;
    }

    public int getPresenceHeartbeatInterval() {
        return this.presenceHeartbeatInterval;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NexusConfig that = (NexusConfig) o;
        if (this.connectionTimeout == that.connectionTimeout && this.presenceHeartbeatInterval == that.presenceHeartbeatInterval && this.endpoints.equals(that.endpoints)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((this.endpoints.hashCode() * 31) + this.presenceHeartbeatInterval) * 31) + this.connectionTimeout;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public int connection_timeout;
        /* access modifiers changed from: private */
        public List<String> endpoints;
        /* access modifiers changed from: private */
        public int presence_heartbeat_interval;

        public NexusConfig build() {
            return new NexusConfig(this);
        }

        public Builder withEndpoints(List<String> endpoints2) {
            this.endpoints = endpoints2;
            return this;
        }

        public Builder withPresenceHeartbeatInterval(int presence_heartbeat_interval2) {
            this.presence_heartbeat_interval = presence_heartbeat_interval2;
            return this;
        }

        public Builder withConnectionTimeout(int connection_timeout2) {
            this.connection_timeout = connection_timeout2;
            return this;
        }
    }
}
