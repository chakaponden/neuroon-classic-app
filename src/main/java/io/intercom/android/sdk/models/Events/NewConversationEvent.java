package io.intercom.android.sdk.models.Events;

import io.intercom.android.sdk.models.Conversation;

public class NewConversationEvent {
    private final String identifier;
    private final Conversation response;

    public NewConversationEvent(Conversation response2, String identifier2) {
        this.response = response2;
        this.identifier = identifier2;
    }

    public Conversation getResponse() {
        return this.response;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
