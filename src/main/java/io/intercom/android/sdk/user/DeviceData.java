package io.intercom.android.sdk.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import io.intercom.android.sdk.commons.utilities.DeviceUtils;
import io.intercom.android.sdk.utilities.Constants;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DeviceData {
    private static final String PREFS_DEVICE_ID = "DeviceId";
    private static final String PREFS_PUSH_TOKEN = "push_token";

    public static Map<String, Object> generateDeviceData(Context context) {
        Map<String, Object> deviceData = new HashMap<>();
        deviceData.put("platform_version", Build.VERSION.RELEASE);
        deviceData.put("device", Build.CPU_ABI);
        deviceData.put("platform", Build.MODEL);
        deviceData.put("browser", "Intercom-Android-SDK");
        deviceData.put("version", DeviceUtils.getAppVersion(context));
        deviceData.put("application", DeviceUtils.getAppName(context));
        deviceData.put("language", Locale.getDefault().getDisplayLanguage());
        deviceData.put("device_id", getCachedOrNewId(context));
        String token = context.getSharedPreferences(Constants.INTERCOM_DATA_PREFS, 0).getString(PREFS_PUSH_TOKEN, "");
        if (!token.isEmpty()) {
            deviceData.put("device_token", token);
        }
        return deviceData;
    }

    public static boolean hasCachedPushToken(Context context, String pushToken) {
        return pushToken.equals(getPushToken(context));
    }

    public static void cachePushToken(Context context, String pushToken) {
        context.getSharedPreferences(Constants.INTERCOM_DATA_PREFS, 0).edit().putString(PREFS_PUSH_TOKEN, pushToken).apply();
    }

    public static String getPushToken(Context context) {
        return context.getSharedPreferences(Constants.INTERCOM_DATA_PREFS, 0).getString(PREFS_PUSH_TOKEN, "");
    }

    private static String getCachedOrNewId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.INTERCOM_DATA_PREFS, 0);
        String uuid = prefs.getString(PREFS_DEVICE_ID, "");
        if (!uuid.isEmpty()) {
            return uuid;
        }
        String uuid2 = DeviceUtils.generateDeviceId(context);
        prefs.edit().putString(PREFS_DEVICE_ID, uuid2).apply();
        return uuid2;
    }
}
