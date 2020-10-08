package io.intercom.android.sdk.conversation.events;

public class AdminTypingEndedEvent {
    private final String adminId;
    private final String conversationId;
    private final String partId;

    public AdminTypingEndedEvent(String adminId2, String conversationId2, String partId2) {
        this.adminId = adminId2;
        this.conversationId = conversationId2;
        this.partId = partId2;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public String getPartId() {
        return this.partId;
    }
}
