package io.intercom.android.sdk.preview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.api.PollingManager;
import io.intercom.android.sdk.lifecycles.EmptyActivityLifecycleCallbacks;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.metrics.MetricType;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Events.FirstMessageEvent;
import io.intercom.android.sdk.models.Events.ReadEvent;
import io.intercom.android.sdk.models.Events.UnreadConversationsEvent;
import io.intercom.android.sdk.models.Events.realtime.CreateConversationEvent;
import io.intercom.android.sdk.models.Events.realtime.NewCommentEvent;
import io.intercom.android.sdk.utilities.IntercomUtils;
import io.intercom.com.squareup.otto.Bus;
import io.intercom.com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TargetApi(14)
public class PreviewManager extends EmptyActivityLifecycleCallbacks {
    private final List<Conversation> conversations = new ArrayList();
    private final PreviewDisplayManager displayManager;
    private Activity lastActive;
    private int unreadCount;

    public PreviewManager(Application application, Bus bus, PollingManager pollingManager, PreviewDisplayManager displayManager2) {
        this.displayManager = displayManager2;
        bus.register(this);
        pollingManager.startPreviewPolling();
        application.registerActivityLifecycleCallbacks(this);
    }

    public void setVisibility(int visibility) {
        this.displayManager.setVisibility(visibility);
        if (visibility == 0) {
            try {
                this.displayManager.conditionallyAddPreview(this.lastActive, this.conversations, this.unreadCount);
            } catch (Exception e) {
            }
        } else {
            this.displayManager.conditionallyRemovePreview(this.lastActive);
        }
    }

    public void setPresentationMode(IntercomPreviewPosition presentationMode) {
        this.displayManager.setPreviewPosition(presentationMode);
    }

    public void reset() {
        this.conversations.clear();
        this.displayManager.reset(this.lastActive);
    }

    @Subscribe
    public void unreadConversationsReveived(UnreadConversationsEvent event) {
        IntercomLogger.INTERNAL("chathead", "received unread count update with: " + event.getResponse().getConversations() + " conversations and total unread count of " + event.getResponse().getTotalUnreadCount());
        this.conversations.clear();
        this.conversations.addAll(event.getResponse().getConversations());
        this.unreadCount = event.getResponse().getTotalUnreadCount();
        if (!this.conversations.isEmpty()) {
            Bridge.getMetricsStore().increment(MetricType.CONVERSATION_RECEIVED);
            if (this.displayManager.getVisibility() == 0) {
                Bridge.getMetricsStore().increment(MetricType.CONVERSATION_PREVIEW_SHOWN);
            }
        }
        try {
            this.displayManager.conditionallyAddPreview(this.lastActive, this.conversations, this.unreadCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void conversationMarkedAsRead(ReadEvent event) {
        Iterator<Conversation> it = this.conversations.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Conversation conversation = it.next();
            if (conversation.getId().equals(event.getConversationId())) {
                this.conversations.remove(conversation);
                this.unreadCount--;
                break;
            }
        }
        if (this.conversations.size() == 1 && this.unreadCount > this.conversations.size()) {
            getUnreadConversations();
        }
    }

    @Subscribe
    public void newComment(NewCommentEvent event) {
        realtimeUpdate();
    }

    @Subscribe
    public void newConversation(CreateConversationEvent event) {
        realtimeUpdate();
    }

    @Subscribe
    public void firstMessage(FirstMessageEvent event) {
        IntercomLogger.INFO(Bridge.getContext().getString(R.string.intercomsdk_activated_key_message));
        try {
            this.displayManager.displayFirstMessageVisuals(this.lastActive);
        } catch (Exception e) {
        }
    }

    private void realtimeUpdate() {
        if (IntercomUtils.isIntercomActivity(this.lastActive)) {
            return;
        }
        if (Bridge.getIdentityStore().getAppConfig().isRealTime()) {
            getUnreadConversations();
        } else {
            Bridge.getPoller().throttledPreviewPoll();
        }
    }

    private void getUnreadConversations() {
        Bridge.getApi().getUnreadConversations();
        Bridge.getPoller().resetPreviewPoll();
    }

    public void onActivityResumed(Activity activity) {
        this.lastActive = activity;
        try {
            this.displayManager.conditionallyAddPreview(activity, this.conversations, this.unreadCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityPaused(Activity activity) {
        try {
            this.displayManager.conditionallyRemovePreview(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.lastActive = null;
    }
}
