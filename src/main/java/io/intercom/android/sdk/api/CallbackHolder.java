package io.intercom.android.sdk.api;

import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.models.BaseResponse;
import io.intercom.android.sdk.models.Config;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.ConversationsResponse;
import io.intercom.android.sdk.models.Events.ConversationEvent;
import io.intercom.android.sdk.models.Events.Failure.InboxFailedEvent;
import io.intercom.android.sdk.models.Events.Failure.NewConversationFailedEvent;
import io.intercom.android.sdk.models.Events.Failure.ReplyFailedEvent;
import io.intercom.android.sdk.models.Events.InboxEvent;
import io.intercom.android.sdk.models.Events.NewConversationEvent;
import io.intercom.android.sdk.models.Events.ReadEvent;
import io.intercom.android.sdk.models.Events.ReplyEvent;
import io.intercom.android.sdk.models.Events.UnreadConversationsEvent;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.ReadResponse;
import io.intercom.android.sdk.models.User;
import io.intercom.android.sdk.models.UsersResponse;
import io.intercom.retrofit.Callback;
import io.intercom.retrofit.RetrofitError;

public class CallbackHolder {
    public BaseCallback<ReadResponse.Builder> readCallback(final String conversationId) {
        return new BaseCallback<ReadResponse.Builder>() {
            public void onSuccess(ReadResponse.Builder builder) {
                CallbackHolder.this.updateBaseResponseObjects(builder.build());
                Bridge.getBus().post(new ReadEvent(conversationId));
            }
        };
    }

    public BaseCallback<UsersResponse.Builder> unreadCallback() {
        return new BaseCallback<UsersResponse.Builder>() {
            public void onSuccess(UsersResponse.Builder responseBuilder) {
                UsersResponse response = responseBuilder.build();
                CallbackHolder.this.updateBaseResponseObjects(response);
                Bridge.getBus().post(new UnreadConversationsEvent(response.getUnreadConversations()));
            }
        };
    }

    public Callback<ConversationsResponse.Builder> inboxCallback() {
        return new BaseCallback<ConversationsResponse.Builder>() {
            public void onSuccess(ConversationsResponse.Builder builder) {
                ConversationsResponse response = builder.build();
                CallbackHolder.this.updateBaseResponseObjects(response);
                Bridge.getBus().post(new InboxEvent(response.getConversationPage()));
            }

            public void onFailure(RetrofitError error) {
                Bridge.getBus().post(new InboxFailedEvent());
            }
        };
    }

    public Callback<Part.Builder> replyCallback(int position, boolean isUpload, String partId, String conversationId) {
        final int i = position;
        final String str = partId;
        final String str2 = conversationId;
        final boolean z = isUpload;
        return new BaseCallback<Part.Builder>() {
            public void onSuccess(Part.Builder builder) {
                Bridge.getBus().post(new ReplyEvent(builder.build(), i, str, str2));
            }

            public void onFailure(RetrofitError error) {
                Bridge.getBus().post(new ReplyFailedEvent(i, z, str));
            }
        };
    }

    public Callback<Conversation.Builder> conversationCallback() {
        return new BaseCallback<Conversation.Builder>() {
            public void onSuccess(Conversation.Builder builder) {
                Bridge.getBus().post(new ConversationEvent(builder.build()));
            }
        };
    }

    public Callback<Conversation.Builder> newConversationCallback(final int position, final String partId) {
        return new BaseCallback<Conversation.Builder>() {
            public void onSuccess(Conversation.Builder builder) {
                Bridge.getBus().post(new NewConversationEvent(builder.build(), partId));
            }

            public void onFailure(RetrofitError error) {
                Bridge.getBus().post(new NewConversationFailedEvent(position, partId));
            }
        };
    }

    /* access modifiers changed from: private */
    public void updateBaseResponseObjects(BaseResponse response) {
        updateIdentityStore(response.getUser());
        updateAppConfig(response.getConfig());
    }

    private void updateIdentityStore(User user) {
        Bridge.getIdentityStore().setUser(user);
    }

    private void updateAppConfig(Config config) {
        Bridge.getIdentityStore().setAppConfig(config);
    }
}
