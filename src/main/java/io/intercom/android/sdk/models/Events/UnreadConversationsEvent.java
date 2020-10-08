package io.intercom.android.sdk.models.Events;

import io.intercom.android.sdk.models.ConversationList;

public class UnreadConversationsEvent {
    private final ConversationList response;

    public UnreadConversationsEvent(ConversationList response2) {
        this.response = response2;
    }

    public ConversationList getResponse() {
        return this.response;
    }
}
