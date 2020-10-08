package io.intercom.android.sdk.models.Events;

public class ReadEvent {
    private final String conversationId;

    public ReadEvent(String conversationId2) {
        this.conversationId = conversationId2;
    }

    public String getConversationId() {
        return this.conversationId;
    }
}
