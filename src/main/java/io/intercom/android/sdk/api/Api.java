package io.intercom.android.sdk.api;

import android.content.Context;
import android.text.TextUtils;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.annotations.IntercomExclusionStrategy;
import io.intercom.android.sdk.api.RetryInterceptor;
import io.intercom.android.sdk.blocks.Image;
import io.intercom.android.sdk.commons.utilities.DeviceUtils;
import io.intercom.android.sdk.conversation.UploadProgressListener;
import io.intercom.android.sdk.identity.IdentityStore;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.identity.UserIdentity;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Events.Failure.UploadFailedEvent;
import io.intercom.android.sdk.models.Events.UploadEvent;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Upload;
import io.intercom.android.sdk.models.UsersResponse;
import io.intercom.android.sdk.transforms.RoundedCornersTransform;
import io.intercom.android.sdk.user.DeviceData;
import io.intercom.android.sdk.utilities.ImageUtils;
import io.intercom.com.google.gson.GsonBuilder;
import io.intercom.com.squareup.okhttp.Cache;
import io.intercom.com.squareup.okhttp.CertificatePinner;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.MultipartBuilder;
import io.intercom.com.squareup.okhttp.OkHttpClient;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.Transformation;
import io.intercom.retrofit.Callback;
import io.intercom.retrofit.RestAdapter;
import io.intercom.retrofit.RetrofitError;
import io.intercom.retrofit.client.Client;
import io.intercom.retrofit.client.OkClient;
import io.intercom.retrofit.client.Response;
import io.intercom.retrofit.converter.GsonConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class Api {
    private static final String CACHE_NAME = "Intercom_SDK/HttpCache";
    private static final int CACHE_SIZE = 10485760;
    private static final String DATA = "data";
    private static final String DEVICE_DATA = "device_data";
    private static final String DEVICE_TOKEN = "device_token";
    private static final String ENDPOINT = "/oreo";
    private static final String HMAC = "hmac";
    public static final int MAX_DNS_SEGMENT_SIZE = 63;
    private static final String NEW_SESSION = "new_session";
    private static final String PARTIAL_HOSTNAME = ".mobile-sdk-api.intercom.io";
    private static final String PROTOCOL = "https://";
    private static final String UPLOAD = "upload";
    private static final String USER = "user";
    private static final String USER_ATTRIBUTES = "user_attributes";
    private final String appId;
    /* access modifiers changed from: private */
    public final CallbackHolder callbacks = new CallbackHolder();
    private IntercomApiInterface intercomApiInterface;
    private boolean synchronous;

    public Api(Context context, String appId2, boolean synchronous2) {
        this.appId = appId2;
        this.synchronous = synchronous2;
        this.intercomApiInterface = createIntercomApiInterface(context);
    }

    public void configureRequestSynchronicity(Context context, boolean hasIntercomId) {
        if (hasIntercomId == this.synchronous) {
            this.synchronous = !hasIntercomId;
            this.intercomApiInterface = createIntercomApiInterface(context);
        }
    }

    /* access modifiers changed from: protected */
    public IntercomApiInterface createIntercomApiInterface(Context context) {
        return (IntercomApiInterface) configureRestAdapter(context, this.synchronous).create(IntercomApiInterface.class);
    }

    private RestAdapter configureRestAdapter(Context context, boolean synchronousRequests) {
        File cacheDirectory = new File(context.getCacheDir().getAbsolutePath(), CACHE_NAME);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(2, TimeUnit.MINUTES);
        client.setReadTimeout(2, TimeUnit.MINUTES);
        client.setWriteTimeout(2, TimeUnit.MINUTES);
        client.interceptors().add(new IdentityInterceptor());
        client.interceptors().add(new RetryInterceptor(new RetryInterceptor.Sleeper()));
        client.networkInterceptors().add(new HeaderInterceptor(context));
        String hostname = getFullHostname(DeviceUtils.getAppVersion(context), this.appId);
        client.setCache(new Cache(cacheDirectory, 10485760));
        client.setCertificatePinner(new CertificatePinner.Builder().add(hostname, "sha1/BiCgk94N+oILO/VULX+wYS6gWKU=").build());
        RestAdapter.Builder a = new RestAdapter.Builder().setEndpoint(convertHostnameToUrl(hostname)).setClient((Client) new OkClient(client)).setConverter(new GsonConverter(new GsonBuilder().setExclusionStrategies(new IntercomExclusionStrategy()).create()));
        if (synchronousRequests) {
            Executor executorService = Executors.newSingleThreadScheduledExecutor();
            a.setExecutors(executorService, executorService);
        }
        return a.build();
    }

    public void registerUnidentifiedUser() {
        retriableUpdateUser(generateUpdateUserParams(DeviceData.generateDeviceData(Bridge.getContext()), true), Bridge.getIdentityStore().getUserIdentityFingerprint());
    }

    public void registerIdentifiedUser(Registration registration) {
        Map<String, Object> deviceData = DeviceData.generateDeviceData(Bridge.getContext());
        if (!registration.getRegistrationId().isEmpty()) {
            deviceData.put(DEVICE_TOKEN, registration.getRegistrationId());
        }
        Map<String, Object> params = generateUpdateUserParams(deviceData, true);
        params.put(USER_ATTRIBUTES, registration.getAttributes());
        retriableUpdateUser(params, Bridge.getIdentityStore().getUserIdentityFingerprint());
    }

    public void ping() {
        IdentityStore identityStore = Bridge.getIdentityStore();
        if (identityStore.userIdentityExists() && identityStore.appIdentityExists()) {
            retriableUpdateUser(generateUpdateUserParams(DeviceData.generateDeviceData(Bridge.getContext()), true), identityStore.getUserIdentityFingerprint());
        }
    }

    public void setGCMPushKey(String gcmRegistrationId) {
        Map<String, Object> deviceData = DeviceData.generateDeviceData(Bridge.getContext());
        deviceData.put(DEVICE_TOKEN, gcmRegistrationId);
        retriableUpdateUser(generateUpdateUserParams(deviceData, false), Bridge.getIdentityStore().getUserIdentityFingerprint());
    }

    public void removeGCMDeviceToken(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", Bridge.getIdentityStore().getUserMap());
        params.put(DEVICE_TOKEN, token);
        this.intercomApiInterface.deleteDeviceToken(params, new Callback<UsersResponse.Builder>() {
            public void success(UsersResponse.Builder builder, Response response) {
            }

            public void failure(RetrofitError error) {
            }
        });
    }

    public void updateUser(Map<String, ?> attributes) {
        Map<String, Object> params = generateUpdateUserParams(DeviceData.generateDeviceData(Bridge.getContext()), false);
        params.put(USER_ATTRIBUTES, attributes);
        retriableUpdateUser(params, Bridge.getIdentityStore().getUserIdentityFingerprint());
    }

    /* access modifiers changed from: protected */
    public void retriableUpdateUser(final Map<String, Object> params, final String fingerprint) {
        this.intercomApiInterface.updateUser(params, new BaseCallback<UsersResponse.Builder>() {
            public void onSuccess(UsersResponse.Builder builder) {
                if (fingerprint.equals(Bridge.getIdentityStore().getUserIdentityFingerprint())) {
                    IntercomLogger.INFO("Successfully registered or updated user");
                    Api.this.callbacks.unreadCallback().onSuccess(builder);
                }
            }

            public void onFailure(RetrofitError error) {
                Map user = (Map) params.get("user");
                if (Api.isUserNotFound(error, user)) {
                    user.remove(UserIdentity.INTERCOM_ID);
                    params.put("user", user);
                    Api.this.retriableUpdateUser(params, fingerprint);
                }
            }

            /* access modifiers changed from: package-private */
            public void logFailure(String reason, RetrofitError error) {
                super.logFailure("Failed to register or update user", error);
            }
        });
    }

    public void logEvent(String name, Map<String, ?> metaData) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> eventParams = new HashMap<>();
        params.put("user", Bridge.getIdentityStore().getUserMap());
        eventParams.put("event_name", name);
        if (!metaData.isEmpty()) {
            eventParams.put("metadata", metaData);
        }
        params.put("event", eventParams);
        addSecureHash(params);
        this.intercomApiInterface.logEvent(params, this.callbacks.unreadCallback());
    }

    public void markConversationAsRead(String conversationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", Bridge.getIdentityStore().getAppId());
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.markAsRead(conversationId, params, this.callbacks.readCallback(conversationId));
    }

    public void sendLWRResponse(String conversationId, String replyType, String replyOption) {
        Map<String, Object> params = new HashMap<>();
        params.put("reply_type", replyType);
        params.put("reply_option", replyOption);
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.replyToConversation(conversationId, params, new Callback<Part.Builder>() {
            public void success(Part.Builder builder, Response response) {
                IntercomLogger.INTERNAL("API Success", "Successfully sent LWR");
            }

            public void failure(RetrofitError error) {
                IntercomLogger.INTERNAL("API Failure", "Sending LWR failed");
            }
        });
    }

    public void getInbox() {
        Map<String, Object> params = Bridge.getIdentityStore().getUserMap();
        params.put("per_page", "20");
        addSecureHash(params);
        this.intercomApiInterface.getConversations(params, this.callbacks.inboxCallback());
    }

    public void getInboxBefore(long before) {
        Map<String, Object> params = Bridge.getIdentityStore().getUserMap();
        params.put("before", String.valueOf(before));
        params.put("per_page", "20");
        addSecureHash(params);
        this.intercomApiInterface.getConversations(params, this.callbacks.inboxCallback());
    }

    public void getUnreadConversations() {
        Map<String, Object> params = Bridge.getIdentityStore().getUserMap();
        params.put("per_page", "20");
        addSecureHash(params);
        this.intercomApiInterface.getUnreadConversations(params, this.callbacks.unreadCallback());
    }

    public void getConversation(String conversationId) {
        Map<String, Object> params = Bridge.getIdentityStore().getUserMap();
        addSecureHash(params);
        this.intercomApiInterface.getConversation(conversationId, params, this.callbacks.conversationCallback());
    }

    public void textReply(String conversationId, String messageText, int tempPartPosition, String tempPartId) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", Bridge.getIdentityStore().getAppId());
        params.put(UserIdentity.TYPE, "user");
        params.put("message_type", "comment");
        params.put("body", messageText);
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.replyToConversation(conversationId, params, this.callbacks.replyCallback(tempPartPosition, false, tempPartId, conversationId));
    }

    public void attachmentReply(String conversationId, int uploadId, int tempPartPosition, String tempPartId) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", Bridge.getIdentityStore().getAppId());
        params.put(UserIdentity.TYPE, "user");
        params.put("message_type", "comment");
        params.put("upload_ids", new int[]{uploadId});
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.replyToConversation(conversationId, params, this.callbacks.replyCallback(tempPartPosition, true, tempPartId, conversationId));
    }

    public void startNewConversation(String messageText, int position, String tempPartId) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", Bridge.getIdentityStore().getAppId());
        params.put("body", messageText);
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.startNewConversation(params, this.callbacks.newConversationCallback(position, tempPartId));
    }

    public void startNewAttachmentConversation(int uploadId, int position, String tempPartId) {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", Bridge.getIdentityStore().getAppId());
        params.put("upload_ids", new int[]{uploadId});
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        this.intercomApiInterface.startNewConversation(params, this.callbacks.newConversationCallback(position, tempPartId));
    }

    public void uploadFile(File file, String originalFileName, long size, String mimeType, int width, int height, int tempPartPosition, String tempPartId, UploadProgressListener listener, Context context) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> uploadMap = new HashMap<>();
        uploadMap.put("original_filename", originalFileName);
        uploadMap.put("size_in_bytes", Long.valueOf(size));
        uploadMap.put("content_type", mimeType);
        uploadMap.put(SettingsJsonConstants.ICON_WIDTH_KEY, Integer.valueOf(width));
        uploadMap.put(SettingsJsonConstants.ICON_HEIGHT_KEY, Integer.valueOf(height));
        params.put(UPLOAD, uploadMap);
        params.put("user", Bridge.getIdentityStore().getUserMap());
        addSecureHash(params);
        final String str = mimeType;
        final Context context2 = context;
        final File file2 = file;
        final int i = width;
        final int i2 = height;
        final String str2 = originalFileName;
        final UploadProgressListener uploadProgressListener = listener;
        final int i3 = tempPartPosition;
        final String str3 = tempPartId;
        this.intercomApiInterface.uploadFile(params, new BaseCallback<Upload.Builder>() {
            public void onSuccess(Upload.Builder builder) {
                final Upload uploadResponse = builder.build();
                if (str.contains(Image.MIME_TYPE)) {
                    int maxWidth = context2.getResources().getDimensionPixelSize(R.dimen.intercomsdk_max_image_width);
                    Picasso.with(context2).load(file2).stableKey(uploadResponse.getPublicUrl()).resize(maxWidth, ImageUtils.getAspectHeight(maxWidth, ImageUtils.getAspectRatio(i, i2))).transform((Transformation) new RoundedCornersTransform(context2.getResources().getDimensionPixelSize(R.dimen.intercomsdk_image_rounded_corners))).onlyScaleDown().fetch();
                }
                new OkHttpClient().newCall(new Request.Builder().url(uploadResponse.getUploadDestination()).post(new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("key", uploadResponse.getKey()).addFormDataPart("acl", uploadResponse.getAcl()).addFormDataPart(HttpRequest.HEADER_CONTENT_TYPE, uploadResponse.getContentType()).addFormDataPart("AWSAccessKeyId", uploadResponse.getAwsAccessKey()).addFormDataPart("policy", uploadResponse.getPolicy()).addFormDataPart("signature", uploadResponse.getSignature()).addFormDataPart("success_action_status", uploadResponse.getSuccessActionStatus()).addFormDataPart("file", str2, new ProgressRequestBody(MediaType.parse(str), file2, uploadProgressListener)).build()).build()).enqueue(new io.intercom.com.squareup.okhttp.Callback() {
                    public void onFailure(Request request, IOException e) {
                        IntercomLogger.ERROR("Upload failed: request body " + request.body());
                        Bridge.getBus().post(new UploadFailedEvent(i3, str3));
                    }

                    public void onResponse(io.intercom.com.squareup.okhttp.Response response) {
                        IntercomLogger.INTERNAL("API Success", "Successfully uploaded");
                        if (response.isSuccessful()) {
                            Bridge.getBus().post(new UploadEvent(uploadResponse.getId(), i3, str3));
                        } else {
                            IntercomLogger.ERROR("Upload failed: request body " + response.body());
                            Bridge.getBus().post(new UploadFailedEvent(i3, str3));
                        }
                        try {
                            response.body().close();
                        } catch (IOException e) {
                        }
                    }
                });
                IntercomLogger.INTERNAL("API Success", "Successfully uploaded");
            }

            public void onFailure(RetrofitError error) {
                Bridge.getBus().post(new UploadFailedEvent(i3, str3));
            }

            /* access modifiers changed from: package-private */
            public void logFailure(String reason, RetrofitError error) {
                super.logFailure("Upload failed", error);
            }
        });
    }

    public void getVideo(String url, io.intercom.com.squareup.okhttp.Callback callback) {
        new OkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(callback);
    }

    public void hitTrackingUrl(String url) {
        new OkHttpClient().newCall(new Request.Builder().url(url).build()).enqueue(new io.intercom.com.squareup.okhttp.Callback() {
            public void onFailure(Request request, IOException e) {
                IntercomLogger.INTERNAL("Tracking Url", "Failed tracking url request");
            }

            public void onResponse(io.intercom.com.squareup.okhttp.Response response) {
                try {
                    response.body().close();
                } catch (IOException e) {
                }
            }
        });
    }

    static String convertHostnameToUrl(String hostname) {
        return PROTOCOL + hostname + ENDPOINT;
    }

    static String getFullHostname(String version, String appId2) {
        return createUniqueIdentifier(version, appId2) + PARTIAL_HOSTNAME;
    }

    static String createUniqueIdentifier(String version, String appId2) {
        String result = appId2 + "-android" + TextUtils.join(Condition.Operation.MINUS, version.replaceAll("[^\\d]", "").split(""));
        if (result.length() > 63) {
            return result.substring(0, 63);
        }
        return result;
    }

    protected static int getRetryTimer(int count) {
        return (int) (Math.pow(2.0d, (double) count) * 1000.0d);
    }

    protected static boolean isUserNotFound(RetrofitError error, Map userParams) {
        if (!(error.getResponse() == null || error.getResponse().getStatus() != 404 || userParams == null || userParams.get(UserIdentity.INTERCOM_ID) == null || userParams.size() <= 1 || error.getResponse().getBody() == null)) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(error.getResponse().getBody().in()));
                StringBuilder out = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    out.append(line);
                }
                reader.close();
                if (new JSONObject(out.toString()).getJSONArray("errors").getJSONObject(0).getString("code").equals("not_found")) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    private void addSecureHash(Map<String, Object> params) {
        IdentityStore identityStore = Bridge.getIdentityStore();
        String secureData = identityStore.getData();
        String secureHmac = identityStore.getHmac();
        if (!TextUtils.isEmpty(secureData) && !TextUtils.isEmpty(secureHmac)) {
            params.put("data", secureData);
            params.put(HMAC, secureHmac);
        }
    }

    private Map<String, Object> generateUpdateUserParams(Map<String, Object> deviceData, boolean newSession) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", Bridge.getIdentityStore().getUserMap());
        params.put(DEVICE_DATA, deviceData);
        params.put(NEW_SESSION, Boolean.valueOf(newSession));
        params.put("count_metrics", Bridge.getMetricsStore().getMap());
        addSecureHash(params);
        return params;
    }

    public boolean isSynchronous() {
        return this.synchronous;
    }
}
