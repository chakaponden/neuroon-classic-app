package io.intercom.android.sdk.utilities;

import android.app.Application;
import android.text.TextUtils;
import io.intercom.android.sdk.logger.IntercomLogger;

public class ValidatorUtil {
    private static final String API_PREFIX = "android_sdk-";

    public static boolean isValidConstructorParams(Application application, String apiKey, String appId) {
        boolean isValid = true;
        if (application == null) {
            IntercomLogger.ERROR("The application passed in was null.We require an application to enable Intercom in your app");
            isValid = false;
        }
        if (TextUtils.isEmpty(appId)) {
            IntercomLogger.ERROR("The appId you provided is either null or empty. We require a correct appId to enable Intercom in your app");
            isValid = false;
        }
        if (isValidApiKey(apiKey)) {
            return isValid;
        }
        IntercomLogger.ERROR("The api key provided either is too short or did not begin with 'android_sdk-'.\nPlease check that you are using an Intercom Android SDK key and have not passed the appId into the apiKey field\n");
        return false;
    }

    static boolean isValidApiKey(String apiKey) {
        return apiKey != null && apiKey.length() >= 52 && apiKey.substring(0, 12).equals(API_PREFIX);
    }
}
