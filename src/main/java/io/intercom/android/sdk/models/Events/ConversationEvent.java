package io.intercom.android.sdk.models.Events;

import io.intercom.android.sdk.models.Conversation;

public class ConversationEvent {
    private final Conversation response;

    public ConversationEvent(Conversation response2) {
        this.response = response2;
    }

    public Conversation getResponse() {
        return this.response;
    }
}
