package io.intercom.android.sdk;

import io.intercom.android.sdk.conversation.events.AdminIsTypingEvent;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.metrics.MetricType;
import io.intercom.android.sdk.models.Events.realtime.CreateConversationEvent;
import io.intercom.android.sdk.models.Events.realtime.NewCommentEvent;
import io.intercom.android.sdk.nexus.NexusClient;
import io.intercom.android.sdk.nexus.NexusConfig;
import io.intercom.android.sdk.nexus.NexusEvent;
import io.intercom.android.sdk.nexus.NexusListener;

public class NexusWrapper extends NexusClient implements NexusListener {
    public void connect(NexusConfig config, boolean shouldSendPresence) {
        if (!isConnected() && !config.getEndpoints().isEmpty()) {
            super.connect(config, shouldSendPresence);
            addEventListener(this);
        }
    }

    public void disconnect() {
        removeEventListener(this);
        super.disconnect();
    }

    public void cycle(NexusConfig config, boolean shouldSendPresence) {
        disconnect();
        connect(config, shouldSendPresence);
    }

    public void notifyEvent(NexusEvent event) {
        switch (event) {
            case AdminIsTyping:
                if (isRealTime()) {
                    IntercomLogger.INTERNAL("nexus realtime", "received " + event.name() + " event");
                    Bridge.getBus().post(new AdminIsTypingEvent(event.getAdminId(), event.getConversationId(), event.getAdminName(), event.getAdminAvatarUrl()));
                    return;
                }
                return;
            case NewComment:
                IntercomLogger.INTERNAL("nexus realtime", "received " + event.name() + " event");
                Bridge.getBus().post(new NewCommentEvent(event.getConversationId(), event.getUserId()));
                return;
            case CreateConversation:
                IntercomLogger.INTERNAL("nexus realtime", "received " + event.name() + " event");
                Bridge.getBus().post(new CreateConversationEvent());
                return;
            default:
                IntercomLogger.INTERNAL("nexus realtime", "unexpected event: " + event.name());
                return;
        }
    }

    public void onConnect() {
        Bridge.getMetricsStore().increment(MetricType.NEXUS_CONNECTION_SUCCESS);
    }

    public void onConnectFailed() {
        Bridge.getMetricsStore().increment(MetricType.NEXUS_CONNECTION_FAILURE);
    }

    private static boolean isRealTime() {
        return Bridge.getIdentityStore().getAppConfig().isRealTime();
    }
}
