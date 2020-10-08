package io.intercom.android.sdk.api;

import android.os.Handler;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.lifecycles.Lifecycles;
import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.concurrent.TimeUnit;

public class PollingManager {
    /* access modifiers changed from: private */
    public final Handler conversationHandler = new Handler();
    /* access modifiers changed from: private */
    public Runnable conversationPoller;
    /* access modifiers changed from: private */
    public boolean currentlyThrottlingConversationPoll;
    /* access modifiers changed from: private */
    public boolean currentlyThrottlingInboxPoll;
    /* access modifiers changed from: private */
    public boolean currentlyThrottlingPreviewPoll;
    /* access modifiers changed from: private */
    public final Handler handler = new Handler();
    /* access modifiers changed from: private */
    public final Runnable inboxPoller = new Runnable() {
        public void run() {
            IntercomLogger.INTERNAL("polling", "inbox poll");
            Bridge.getApi().getInbox();
            PollingManager.this.poll(PollingManager.this.handler, PollingManager.this.inboxPoller);
        }
    };
    /* access modifiers changed from: private */
    public final Runnable previewPoller = new Runnable() {
        public void run() {
            IntercomLogger.INTERNAL("polling", "chathead poll");
            Bridge.getApi().getUnreadConversations();
            PollingManager.this.poll(PollingManager.this.handler, PollingManager.this.previewPoller);
        }
    };

    public void startConversationPolling(final String conversationId) {
        IntercomLogger.INTERNAL("polling", "starting conversation polling");
        this.conversationPoller = new Runnable() {
            public void run() {
                IntercomLogger.INTERNAL("polling", "conversation poll");
                Bridge.getApi().getConversation(conversationId);
                PollingManager.this.poll(PollingManager.this.conversationHandler, PollingManager.this.conversationPoller);
            }
        };
        poll(this.conversationHandler, this.conversationPoller);
    }

    public void startPreviewPolling() {
        IntercomLogger.INTERNAL("polling", "starting chathead polling");
        this.handler.removeCallbacksAndMessages((Object) null);
        poll(this.handler, this.previewPoller);
    }

    public void startInboxPolling() {
        IntercomLogger.INTERNAL("polling", "starting inbox polling");
        this.handler.removeCallbacksAndMessages((Object) null);
        poll(this.handler, this.inboxPoller);
    }

    public void throttledInboxPoll() {
        if (!this.currentlyThrottlingInboxPoll) {
            IntercomLogger.INTERNAL("throttled polling", "inbox poll scheduled");
            this.currentlyThrottlingInboxPoll = true;
            throttledPoll(this.handler, new Runnable() {
                public void run() {
                    IntercomLogger.INTERNAL("throttled polling", "performing inbox poll");
                    Bridge.getApi().getInbox();
                    PollingManager.this.resetInboxPoll();
                    boolean unused = PollingManager.this.currentlyThrottlingInboxPoll = false;
                }
            });
        }
    }

    public void throttledConversationPoll(final String conversationId) {
        if (!this.currentlyThrottlingConversationPoll) {
            IntercomLogger.INTERNAL("throttled polling", "conversation poll scheduled");
            this.currentlyThrottlingConversationPoll = true;
            throttledPoll(this.conversationHandler, new Runnable() {
                public void run() {
                    IntercomLogger.INTERNAL("throttled polling", "performing conversation poll");
                    Bridge.getApi().getConversation(conversationId);
                    if (PollingManager.this.conversationPoller != null) {
                        PollingManager.this.resetConversationPoll();
                    }
                    boolean unused = PollingManager.this.currentlyThrottlingConversationPoll = false;
                }
            });
        }
    }

    public void throttledPreviewPoll() {
        if (!this.currentlyThrottlingPreviewPoll) {
            IntercomLogger.INTERNAL("throttled polling", "preview poll scheduled");
            this.currentlyThrottlingPreviewPoll = true;
            throttledPoll(this.handler, new Runnable() {
                public void run() {
                    IntercomLogger.INTERNAL("throttled polling", "performing preview poll");
                    Bridge.getApi().getUnreadConversations();
                    PollingManager.this.resetPreviewPoll();
                    boolean unused = PollingManager.this.currentlyThrottlingPreviewPoll = false;
                }
            });
        }
    }

    public void endPolling() {
        IntercomLogger.INTERNAL("polling", "ending polling");
        this.handler.removeCallbacksAndMessages((Object) null);
        this.conversationHandler.removeCallbacksAndMessages((Object) null);
        this.currentlyThrottlingConversationPoll = false;
        this.currentlyThrottlingInboxPoll = false;
        this.currentlyThrottlingPreviewPoll = false;
    }

    public void endConversationPolling() {
        IntercomLogger.INTERNAL("polling", "ending conversation polling");
        this.conversationHandler.removeCallbacksAndMessages((Object) null);
        this.currentlyThrottlingConversationPoll = false;
    }

    public void resetConversationPoll() {
        IntercomLogger.INTERNAL("polling", "resetting conversation poller");
        this.conversationHandler.removeCallbacksAndMessages((Object) null);
        poll(this.conversationHandler, this.conversationPoller);
    }

    public void resetInboxPoll() {
        IntercomLogger.INTERNAL("polling", "resetting inbox poller");
        this.handler.removeCallbacks(this.inboxPoller);
        poll(this.handler, this.inboxPoller);
    }

    public void resetPreviewPoll() {
        IntercomLogger.INTERNAL("polling", "resetting preview poller");
        this.handler.removeCallbacks(this.previewPoller);
        poll(this.handler, this.previewPoller);
    }

    /* access modifiers changed from: private */
    public void poll(Handler handler2, Runnable poller) {
        if (!Lifecycles.isAppBackgrounded()) {
            int pollingInterval = Bridge.getIdentityStore().getAppConfig().getPollingInterval();
            if (pollingInterval > 0) {
                handler2.postDelayed(poller, TimeUnit.SECONDS.toMillis((long) pollingInterval));
                return;
            }
            return;
        }
        IntercomLogger.INTERNAL("polling", "ending polling because app is backgrounded");
    }

    private void throttledPoll(Handler handler2, Runnable poller) {
        if (!Lifecycles.isAppBackgrounded()) {
            int pollingInterval = Bridge.getIdentityStore().getAppConfig().getNoRealtimeThrottle();
            if (pollingInterval > 0) {
                handler2.postDelayed(poller, TimeUnit.SECONDS.toMillis((long) pollingInterval));
                return;
            }
            return;
        }
        IntercomLogger.INTERNAL("throttled polling", "ignoring polling because app is backgrounded");
    }
}
