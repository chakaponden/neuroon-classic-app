package io.intercom.android.sdk;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import io.intercom.android.sdk.activities.MainActivity;
import io.intercom.android.sdk.api.DeDuper;
import io.intercom.android.sdk.api.RateLimiter;
import io.intercom.android.sdk.identity.Migrations;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.logger.LogMessages;
import io.intercom.android.sdk.metrics.MetricType;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Events.CloseIAMEvent;
import io.intercom.android.sdk.preview.IntercomPreviewPosition;
import io.intercom.android.sdk.user.DeviceData;
import io.intercom.android.sdk.utilities.AttributeSanitiser;
import io.intercom.android.sdk.utilities.Constants;
import io.intercom.android.sdk.utilities.IntercomUtils;
import io.intercom.android.sdk.utilities.ValidatorUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(14)
public class RealIntercom extends Intercom {
    private boolean initialised = false;
    private final RateLimiter rateLimiter;
    private final DeDuper superDeDuper;

    protected RealIntercom(Application application, String apiKey, String appId) {
        if (!ValidatorUtil.isValidConstructorParams(application, apiKey, appId)) {
            this.superDeDuper = null;
            this.rateLimiter = null;
            return;
        }
        IntercomUtils.changeComponentState(1, application.getApplicationContext());
        Bridge.initWithAppCredentials(application, apiKey, appId);
        Migrations.runAll(application.getApplicationContext());
        this.superDeDuper = Bridge.getDeDuper();
        this.rateLimiter = new RateLimiter(Bridge.getIdentityStore().getAppConfig());
        this.initialised = true;
    }

    public void registerUnidentifiedUser() {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (Bridge.getIdentityStore().registerUnidentifiedUser()) {
            Bridge.getMetricsStore().increment(MetricType.REGISTER_UNIDENTIFIED);
            Bridge.getApi().registerUnidentifiedUser();
        } else {
            IntercomLogger.INFO("Failed to register user. We already have a registered user. If you are attempting to register a new user, call reset before this. If you are attempting to register an identified user call: registerIdentifiedUser(Registration)");
        }
    }

    public void registerIdentifiedUser(Registration userRegistration) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (userRegistration == null) {
            IntercomLogger.ERROR("The registration object you passed to is null. An example successful call is registerIdentifiedUser(new Registration().withEmail(email));");
        } else if (Bridge.getIdentityStore().registerIdentifiedUser(userRegistration)) {
            Bridge.getMetricsStore().increment(MetricType.REGISTER_IDENTIFIED);
            Bridge.getApi().registerIdentifiedUser(userRegistration);
        } else {
            IntercomLogger.INFO("Failed to register user. We already have a registered user. If you are attempting to register a new user, call reset before this. If you are attempting to change the user call update instead.");
        }
    }

    public void setSecureMode(String secureHash, String secureData) {
        if (!this.initialised) {
            return;
        }
        if (TextUtils.isEmpty(secureHash)) {
            IntercomLogger.WARNING("The hmac you sent us for secure mode was either null or empty, we will not be able to authenticate your requests without a valid hmac.");
        } else if (TextUtils.isEmpty(secureData)) {
            IntercomLogger.WARNING("The data you sent us for secure mode was either null or empty, we will not be able to authenticate your requests without valid data.");
        } else {
            Bridge.getIdentityStore().setSecureMode(secureHash, secureData);
        }
    }

    public void updateUser(Map<String, ?> attributes) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (attributes == null) {
            IntercomLogger.ERROR("updateUser method failed: the attributes Map provided is null");
        } else if (attributes.isEmpty()) {
            IntercomLogger.ERROR("updateUser method failed: the attributes Map provided is empty");
        } else if (attributes.getClass().isAnonymousClass()) {
            IntercomLogger.ERROR("updateUser method failed: the attributes Map provided is an anonymous subclass");
        } else if (this.rateLimiter.isLimited()) {
            IntercomLogger.ERROR(LogMessages.RATE_LIMIT);
            Bridge.getMetricsStore().increment(MetricType.LOCAL_RATE_LIMITED);
        } else {
            this.rateLimiter.increment();
            if (Bridge.getIdentityStore().isAnonymousUser()) {
                AttributeSanitiser.anonymousSanitisation(attributes);
            }
            if (this.superDeDuper.shouldUpdateUser(attributes)) {
                this.superDeDuper.setAttributes(attributes);
                Bridge.getMetricsStore().increment(MetricType.UPDATE_USER);
                Bridge.getApi().updateUser(attributes);
                IntercomLogger.INTERNAL("dupe", "updated user");
                return;
            }
            Bridge.getMetricsStore().increment(MetricType.UPDATE_USER_DUP);
            IntercomLogger.INTERNAL("dupe", "dropped dupe");
        }
    }

    public void logEvent(String name) {
        logEventWithValidation(name, new HashMap());
    }

    public void logEvent(String name, Map<String, ?> metadata) {
        if (metadata == null) {
            IntercomLogger.INFO("The metadata provided is null, logging event with no metadata");
            metadata = new HashMap<>();
        } else if (!metadata.isEmpty()) {
            IntercomLogger.INFO("The metadata provided is empty, logging event with no metadata");
        }
        logEventWithValidation(name, metadata);
    }

    private void logEventWithValidation(String name, Map<String, ?> metadata) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (TextUtils.isEmpty(name)) {
            IntercomLogger.ERROR("The event name is null or empty. We can't log an event with this string.");
        } else if (this.rateLimiter.isLimited()) {
            IntercomLogger.ERROR(LogMessages.RATE_LIMIT);
            Bridge.getMetricsStore().increment(MetricType.LOCAL_RATE_LIMITED);
        } else {
            this.rateLimiter.increment();
            Bridge.getApi().logEvent(name, metadata);
        }
    }

    public void displayMessageComposer() {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (Bridge.getIdentityStore().getAppConfig().isInboundMessages()) {
            openIAM(false, (TaskStackBuilder) null, "");
        } else {
            IntercomLogger.ERROR("It appears your app is not on a plan that allows message composing As a fallback we are calling displayConversationsList()");
            displayConversationsList();
        }
    }

    public void displayConversationsList() {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else {
            openIAM(true, (TaskStackBuilder) null, "");
        }
    }

    private void openIAM(boolean asConversationsList, TaskStackBuilder customStack, String conversationId) {
        if (this.initialised) {
            Context context = Bridge.getContext();
            Intent intent = new Intent(context, MainActivity.class);
            if (asConversationsList) {
                intent.putExtra(MainActivity.SHOW_INBOX, true);
                Bridge.getMetricsStore().increment(MetricType.OPENED_CONVERSATION_LIST);
            } else {
                intent.putExtra(MainActivity.OPEN_CONVERSATION, new Conversation.Builder().withId(conversationId).build());
                Bridge.getMetricsStore().increment(MetricType.OPENED_COMPOSER);
            }
            intent.addFlags(268435456);
            if (customStack == null || Build.VERSION.SDK_INT < 16) {
                context.startActivity(intent);
                return;
            }
            customStack.addNextIntent(intent);
            context.startActivities(customStack.getIntents());
        }
    }

    public void setPreviewPosition(IntercomPreviewPosition previewPosition) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
            return;
        }
        if (previewPosition == null) {
            IntercomLogger.WARNING("The previewPosition provided is null so we are defaulting to bottom left. An example correct call is: setPreviewPosition(IntercomPreviewPosition.BOTTOM_LEFT);");
            previewPosition = IntercomPreviewPosition.BOTTOM_LEFT;
        }
        Bridge.getPreviewManager().setPresentationMode(previewPosition);
    }

    public void setVisibility(int visibility) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (visibility == 0 || visibility == 8) {
            Bridge.getPreviewManager().setVisibility(visibility);
            if (visibility == 8) {
                Bridge.getBus().post(new CloseIAMEvent());
            }
        } else {
            IntercomLogger.ERROR("Visibility can only be Intercom.VISIBLE or Intercom.GONE");
        }
    }

    @Deprecated
    public void setMessagesHidden(boolean visibility) {
        setVisibility(visibility ? 0 : 8);
    }

    public void setupGCM(String regId, int appLogo) {
        if (!isInitialised()) {
            logIntercomNotInitialised();
            return;
        }
        Context context = Bridge.getContext();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.checkPermission("com.google.android.c2dm.permission.RECEIVE", context.getPackageName()) != 0 || packageManager.checkPermission("android.permission.WAKE_LOCK", context.getPackageName()) != 0 || packageManager.checkPermission("android.permission.VIBRATE", context.getPackageName()) != 0) {
            IntercomLogger.ERROR("To enable GCM notifications the following permissions are required. <uses-permission android:name=\"android.permission.WAKE_LOCK\" />\n<uses-permission android:name=\"com.google.android.c2dm.permission.RECEIVE\" />\n<uses-permission android:name=\"android.permission.VIBRATE\"/>\nIn addition to the required permissions the following optional permissions improve push reliability<uses-permission android:name=\"android.permission.READ_PHONE_STATE\"/>\n<uses-permission android:name=\"android.permission.ACCESS_WIFI_STATE\"/>");
        } else if (!Gcm.gcmModuleInstalled()) {
            IntercomLogger.ERROR("intercom-sdk-gcm module not found. Please add it to your build.gradle");
        } else if (!Gcm.isGcmEnabled(context)) {
            IntercomLogger.ERROR("GCM components are disabled on gingerbread devices");
        } else if (TextUtils.isEmpty(regId)) {
            IntercomLogger.ERROR("The GCM registrationId  passed in is null or empty, we require a valid GCM registrationId to enable push");
        } else {
            SharedPreferences.Editor editor = Bridge.getContext().getSharedPreferences(Constants.INTERCOM_PREFS, 0).edit();
            editor.putInt(Constants.PREFS_INTERCOMSDK_PUSH_LOGO, appLogo);
            editor.apply();
            if (!DeviceData.hasCachedPushToken(Bridge.getContext(), regId)) {
                Bridge.getApi().setGCMPushKey(regId);
                DeviceData.cachePushToken(Bridge.getContext(), regId);
            }
        }
    }

    public void openGCMMessage(Intent intent) {
        openGCMMessage(intent, (TaskStackBuilder) null);
    }

    public void openGCMMessage(Intent intent, TaskStackBuilder customStack) {
        if ((intent == null || (intent.getFlags() & 1048576) == 0) ? false : true) {
            return;
        }
        if (!isInitialised()) {
            logIntercomNotInitialised();
        } else if (intent == null || intent.getData() == null) {
            IntercomLogger.INFO("The Uri passed into the method openGCMMessage was null.");
        } else {
            Context context = Bridge.getContext();
            String pushOnlyId = intent.getStringExtra(Gcm.PUSH_ONLY_ID);
            if (pushOnlyId != null) {
                Bridge.getApi().markConversationAsRead(pushOnlyId);
            }
            if (intent.getDataString().isEmpty() || pushOnlyId == null) {
                openIntercomChatPush(intent, customStack);
            } else {
                openPushOnlyMessage(intent, context);
            }
        }
    }

    private void openPushOnlyMessage(Intent intent, Context context) {
        Intent notificationIntent = new Intent("android.intent.action.VIEW", Uri.parse(intent.getDataString()));
        notificationIntent.addFlags(268435456);
        if (notificationIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(notificationIntent);
            Bridge.getMetricsStore().increment(MetricType.OPENED_PUSH_MESSAGE);
            return;
        }
        IntercomLogger.INFO("This device has no application that can handlethe Uri passed in");
    }

    private void openIntercomChatPush(Intent intent, TaskStackBuilder customStack) {
        List<String> pathParts = intent.getData().getPathSegments();
        if (pathParts.size() <= 1 || !pathParts.get(0).equals(Intercom.GCM_RECEIVER)) {
            IntercomLogger.INFO("The Uri passed into the method openGCMMessage was not an Intercom GCM message");
            return;
        }
        Bridge.getMetricsStore().increment(MetricType.OPENED_PUSH_MESSAGE);
        if (pathParts.get(1).equals(Gcm.MULTIPLE_NOTIFICATIONS)) {
            openIAM(true, customStack, "");
        } else {
            openIAM(false, customStack, pathParts.get(1).substring(pathParts.get(1).lastIndexOf(61) + 1));
        }
        Gcm.clearNotifications(Bridge.getContext());
    }

    @Deprecated
    public boolean openGCMMessage(Uri data) {
        boolean wasSdkPush = false;
        if (!isInitialised()) {
            logIntercomNotInitialised();
            return false;
        }
        if (data != null) {
            List<String> pathParts = data.getPathSegments();
            if (pathParts.size() <= 1 || !pathParts.get(0).equals(Intercom.GCM_RECEIVER)) {
                IntercomLogger.INFO("The Uri passed into the method openGCMMessage was not an Intercom GCM message");
            } else {
                wasSdkPush = true;
                if (pathParts.get(1).equals(Gcm.MULTIPLE_NOTIFICATIONS)) {
                    openIAM(true, (TaskStackBuilder) null, "");
                } else {
                    openIAM(false, (TaskStackBuilder) null, pathParts.get(1).substring(pathParts.get(1).lastIndexOf(61) + 1));
                }
                Gcm.clearNotifications(Bridge.getContext());
            }
        } else {
            IntercomLogger.INFO("The Uri passed into the method openGCMMessage was null.");
        }
        if (wasSdkPush) {
            Bridge.getMetricsStore().increment(MetricType.OPENED_PUSH_MESSAGE);
        }
        return wasSdkPush;
    }

    public void reset() {
        if (!isInitialised()) {
            logIntercomNotInitialised();
            return;
        }
        Bridge.getApi().removeGCMDeviceToken(DeviceData.getPushToken(Bridge.getContext()));
        Bridge.getIdentityStore().getAppConfig().resetRealTimeConfig();
        Bridge.getNexusClient().disconnect();
        Bridge.getIdentityStore().resetUserIdentity();
        Bridge.getBus().post(new CloseIAMEvent());
        Bridge.getPreviewManager().reset();
        Bridge.getMetricsStore().increment(MetricType.RESET);
        IntercomLogger.INFO("Successfully reset user. To resume communicating with Intercom, you can register a user");
    }

    private boolean isInitialised() {
        return this.initialised;
    }

    private void logIntercomNotInitialised() {
        IntercomLogger.ERROR("It appears Intercom has not been initialized correctly. Please make sure the first Intercom method you call is initialize and that you're passing in the correct app id and api-key");
    }
}
