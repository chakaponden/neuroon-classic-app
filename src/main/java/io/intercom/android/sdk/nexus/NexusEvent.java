package io.intercom.android.sdk.nexus;

import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public enum NexusEvent {
    NewComment {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            long j = -1;
            NexusEvent.super.init(jsonObject);
            if (jsonObject.has(NexusEvent.NX_TO_USER)) {
                String unused = this.userId = jsonObject.optString(NexusEvent.NX_TO_USER);
            } else {
                String unused2 = this.userId = jsonObject.optString(NexusEvent.NX_FROM_USER);
            }
            JSONObject eventData = jsonObject.optJSONObject(NexusEvent.EVENT_DATA);
            if (eventData != null) {
                j = eventData.optLong(NexusEvent.ADMIN_TIMESTAMP, -1);
            }
            long unused3 = this.adminTimestamp = j;
        }
    },
    NewMessage {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_TO_USER);
        }
    },
    CreateConversation {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_TO_USER);
        }
    },
    ConversationSeen {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_FROM_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_FROM_USER);
        }
    },
    UserIsTyping {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_FROM_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_FROM_USER);
        }
    },
    AdminIsTyping {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            JSONObject eventData = event.optJSONObject(NexusEvent.EVENT_DATA);
            eventData.put(NexusEvent.ADMIN_NAME, getAdminName());
            eventData.put(NexusEvent.ADMIN_ID, getAdminId());
            eventData.put(NexusEvent.ADMIN_AVATAR, getAdminAvatarUrl());
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_TO_USER);
            JSONObject eventData = jsonObject.optJSONObject(NexusEvent.EVENT_DATA);
            if (eventData != null) {
                String unused2 = this.adminId = eventData.optString(NexusEvent.ADMIN_ID);
                String unused3 = this.adminName = eventData.optString(NexusEvent.ADMIN_NAME);
                String unused4 = this.adminAvatarUrl = eventData.optString(NexusEvent.ADMIN_AVATAR);
            }
        }
    },
    AdminIsTypingANote {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            JSONObject eventData = event.optJSONObject(NexusEvent.EVENT_DATA);
            eventData.put(NexusEvent.ADMIN_NAME, getAdminName());
            eventData.put(NexusEvent.ADMIN_ID, getAdminId());
            eventData.put(NexusEvent.ADMIN_AVATAR, getAdminAvatarUrl());
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.userId = jsonObject.optString(NexusEvent.NX_TO_USER);
            JSONObject eventData = jsonObject.optJSONObject(NexusEvent.EVENT_DATA);
            if (eventData != null) {
                String unused2 = this.adminId = eventData.optString(NexusEvent.ADMIN_ID);
                String unused3 = this.adminName = eventData.optString(NexusEvent.ADMIN_NAME);
                String unused4 = this.adminAvatarUrl = eventData.optString(NexusEvent.ADMIN_AVATAR);
            }
        }
    },
    NewNote {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.optJSONObject(NexusEvent.EVENT_DATA).put(NexusEvent.ADMIN_ID, getAdminId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.adminId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString(NexusEvent.ADMIN_ID);
        }
    },
    ConversationAssigned {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            JSONObject eventData = event.optJSONObject(NexusEvent.EVENT_DATA);
            eventData.put(NexusEvent.ADMIN_ID, getAdminId());
            eventData.put("assigneeId", getAssigneeId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.adminId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString(NexusEvent.ADMIN_ID);
            String unused2 = this.assigneeId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString("assigneeId");
        }
    },
    ConversationClosed {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.optJSONObject(NexusEvent.EVENT_DATA).put(NexusEvent.ADMIN_ID, getAdminId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.adminId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString(NexusEvent.ADMIN_ID);
        }
    },
    ConversationReopened {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.optJSONObject(NexusEvent.EVENT_DATA).put(NexusEvent.ADMIN_ID, getAdminId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.adminId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString(NexusEvent.ADMIN_ID);
        }
    },
    UserPresence {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = new JSONObject();
            JSONObject eventData = new JSONObject();
            event.put(NexusEvent.EVENT_GUID, UUID.randomUUID().toString());
            event.put(NexusEvent.EVENT_NAME, "nx." + name());
            event.put(NexusEvent.EVENT_DATA, eventData);
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            String unused = this.guid = jsonObject.optString(NexusEvent.EVENT_GUID);
        }
    },
    UserContentSeenByAdmin {
        /* access modifiers changed from: protected */
        public JSONObject toJsonObject() throws JSONException {
            JSONObject event = NexusEvent.super.toJsonObject();
            event.put(NexusEvent.NX_TO_USER, getUserId());
            return event;
        }

        /* access modifiers changed from: protected */
        public void init(JSONObject jsonObject) {
            NexusEvent.super.init(jsonObject);
            String unused = this.adminId = jsonObject.optJSONObject(NexusEvent.EVENT_DATA).optString(NexusEvent.ADMIN_ID);
        }
    },
    UNKNOWN {
        /* access modifiers changed from: protected */
        public String toJsonFormattedString() {
            return "";
        }
    };
    
    private static final String ADMIN_AVATAR = "adminAvatar";
    private static final String ADMIN_ID = "adminId";
    private static final String ADMIN_NAME = "adminName";
    private static final String ADMIN_TIMESTAMP = "adminTimestamp";
    private static final String CONVERSATION_ID = "conversationId";
    private static final String EVENT_DATA = "eventData";
    private static final String EVENT_GUID = "eventGuid";
    private static final String EVENT_NAME = "eventName";
    private static final String NX_FROM_USER = "nx.FromUser";
    private static final String NX_TO_USER = "nx.ToUser";
    /* access modifiers changed from: private */
    public String adminAvatarUrl;
    /* access modifiers changed from: private */
    public String adminId;
    /* access modifiers changed from: private */
    public String adminName;
    /* access modifiers changed from: private */
    public long adminTimestamp;
    /* access modifiers changed from: private */
    public String assigneeId;
    private String conversationId;
    /* access modifiers changed from: private */
    public String guid;
    /* access modifiers changed from: private */
    public String userId;

    /* access modifiers changed from: protected */
    public String toJsonFormattedString() {
        try {
            return toJsonObject().toString();
        } catch (JSONException e) {
            return "";
        }
    }

    /* access modifiers changed from: protected */
    public JSONObject toJsonObject() throws JSONException {
        JSONObject event = new JSONObject();
        JSONObject eventData = new JSONObject();
        eventData.put(CONVERSATION_ID, getConversationId());
        event.put(EVENT_GUID, UUID.randomUUID().toString());
        event.put(EVENT_NAME, name());
        event.put(EVENT_DATA, eventData);
        return event;
    }

    /* access modifiers changed from: protected */
    public void init(JSONObject jsonObject) {
        this.guid = jsonObject.optString(EVENT_GUID);
        try {
            this.conversationId = jsonObject.getJSONObject(EVENT_DATA).optString(CONVERSATION_ID);
        } catch (JSONException e) {
            this.conversationId = jsonObject.optString(EVENT_DATA);
        }
    }

    public static NexusEvent parse(JSONObject response) {
        String name = response.optString(EVENT_NAME);
        try {
            if (name.startsWith("nx.")) {
                name = name.substring(3);
            }
            NexusEvent event = valueOf(name);
            event.init(response);
            return event;
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public static NexusEvent getAdminIsTypingEvent(String conversationId2, String adminId2, String adminName2, String adminAvatarUrl2, String userId2) {
        NexusEvent event = AdminIsTyping;
        event.adminId = adminId2;
        event.conversationId = conversationId2;
        event.adminName = adminName2;
        event.adminAvatarUrl = adminAvatarUrl2;
        event.userId = userId2;
        return event;
    }

    public static NexusEvent getAdminIsTypingNoteEvent(String conversationId2, String adminId2, String adminName2, String adminAvatarUrl2, String userId2) {
        NexusEvent event = AdminIsTypingANote;
        event.adminId = adminId2;
        event.conversationId = conversationId2;
        event.adminName = adminName2;
        event.adminAvatarUrl = adminAvatarUrl2;
        event.userId = userId2;
        return event;
    }

    public static NexusEvent getUserIsTypingEvent(String conversationId2, String userId2) {
        NexusEvent event = UserIsTyping;
        event.userId = userId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getConversationSeenEvent(String conversationId2, String userId2) {
        NexusEvent event = ConversationSeen;
        event.userId = userId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getConversationSeenAdminEvent(String conversationId2, String userId2) {
        NexusEvent event = UserContentSeenByAdmin;
        event.userId = userId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getNewCommentEvent(String conversationId2, String userId2) {
        NexusEvent event = NewComment;
        event.userId = userId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getNewNoteEvent(String conversationId2, String adminId2) {
        NexusEvent event = NewNote;
        event.userId = adminId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getCreateConversationEvent(String conversationId2, String userId2) {
        NexusEvent event = CreateConversation;
        event.userId = userId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getConversationAssignedEvent(String conversationId2, String adminId2, String assigneeId2) {
        NexusEvent event = ConversationAssigned;
        event.assigneeId = assigneeId2;
        event.adminId = adminId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getConversationClosedEvent(String conversationId2, String adminId2) {
        NexusEvent event = ConversationClosed;
        event.adminId = adminId2;
        event.conversationId = conversationId2;
        return event;
    }

    public static NexusEvent getConversationReopenedEvent(String conversationId2, String adminId2) {
        NexusEvent event = ConversationReopened;
        event.adminId = adminId2;
        event.conversationId = conversationId2;
        return event;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getAdminName() {
        return this.adminName;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    public String getGuid() {
        return this.guid == null ? UUID.randomUUID().toString() : this.guid;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }

    public String getAdminAvatarUrl() {
        return this.adminAvatarUrl;
    }

    public long getAdminTimestamp() {
        return this.adminTimestamp;
    }
}
