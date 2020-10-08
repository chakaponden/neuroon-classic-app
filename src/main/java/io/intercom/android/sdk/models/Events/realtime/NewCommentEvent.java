package io.intercom.android.sdk.models.Events.realtime;

public class NewCommentEvent {
    private final String conversationId;
    private final String userId;

    public NewCommentEvent(String conversationId2, String userId2) {
        this.conversationId = conversationId2;
        this.userId = userId2;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public String getUserId() {
        return this.userId;
    }
}
