package io.intercom.android.sdk.api;

import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.ConversationsResponse;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.ReadResponse;
import io.intercom.android.sdk.models.Upload;
import io.intercom.android.sdk.models.UsersResponse;
import io.intercom.retrofit.Callback;
import io.intercom.retrofit.http.Body;
import io.intercom.retrofit.http.GET;
import io.intercom.retrofit.http.POST;
import io.intercom.retrofit.http.PUT;
import io.intercom.retrofit.http.Path;
import io.intercom.retrofit.http.QueryMap;
import java.util.Map;

public interface IntercomApiInterface {
    @PUT("/users/device_tokens")
    void deleteDeviceToken(@Body Map<String, Object> map, Callback<UsersResponse.Builder> callback);

    @GET("/conversations/{conversationId}")
    void getConversation(@Path("conversationId") String str, @QueryMap Map<String, Object> map, Callback<Conversation.Builder> callback);

    @GET("/conversations")
    void getConversations(@QueryMap Map<String, Object> map, Callback<ConversationsResponse.Builder> callback);

    @GET("/conversations/unread")
    void getUnreadConversations(@QueryMap Map<String, Object> map, Callback<UsersResponse.Builder> callback);

    @POST("/events")
    void logEvent(@Body Map<String, Object> map, Callback<UsersResponse.Builder> callback);

    @POST("/conversations/{conversationId}/read")
    void markAsRead(@Path("conversationId") String str, @Body Map<String, Object> map, Callback<ReadResponse.Builder> callback);

    @POST("/conversations/{conversationId}/reply")
    void replyToConversation(@Path("conversationId") String str, @Body Map<String, Object> map, Callback<Part.Builder> callback);

    @POST("/conversations")
    void startNewConversation(@Body Map<String, Object> map, Callback<Conversation.Builder> callback);

    @POST("/users")
    void updateUser(@Body Map<String, Object> map, Callback<UsersResponse.Builder> callback);

    @POST("/uploads")
    void uploadFile(@Body Map<String, Object> map, Callback<Upload.Builder> callback);
}
