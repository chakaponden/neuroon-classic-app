package io.intercom.android.sdk.models.Events;

import io.intercom.android.sdk.models.ConversationList;

public class InboxEvent {
    private final ConversationList response;

    public InboxEvent(ConversationList response2) {
        this.response = response2;
    }

    public ConversationList getResponse() {
        return this.response;
    }
}
