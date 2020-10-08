package io.intercom.android.sdk.conversation.events;

public class AdminIsTypingEvent {
    private final String adminAvatarUrl;
    private final String adminId;
    private final String adminName;
    private final String conversationId;

    public AdminIsTypingEvent(String adminId2, String conversationId2, String adminName2, String adminAvatarUrl2) {
        this.adminId = adminId2;
        this.conversationId = conversationId2;
        this.adminName = adminName2;
        this.adminAvatarUrl = adminAvatarUrl2;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public String getAdminAvatarUrl() {
        return this.adminAvatarUrl;
    }
}
