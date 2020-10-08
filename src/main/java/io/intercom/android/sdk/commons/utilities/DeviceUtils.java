package io.intercom.android.sdk.commons.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.UUID;

public class DeviceUtils {
    public static String generateDeviceId(Context context) {
        String uuid = getTelephonyDeviceId(context);
        if (!TextUtils.isEmpty(uuid)) {
            return uuid;
        }
        String macAddress = getWifiMacAddress(context);
        String androidId = getAndroidId(context);
        if (!TextUtils.isEmpty(macAddress) && !TextUtils.isEmpty(androidId)) {
            uuid = new UUID((long) androidId.hashCode(), (long) macAddress.hashCode()).toString();
        } else if (!TextUtils.isEmpty(androidId)) {
            uuid = androidId;
        } else if (!TextUtils.isEmpty(macAddress)) {
            uuid = macAddress;
        }
        if (TextUtils.isEmpty(uuid)) {
            return UUID.randomUUID().toString();
        }
        return uuid;
    }

    static String getTelephonyDeviceId(Context context) {
        String result = null;
        if (hasPermission(context, "android.permission.READ_PHONE_STATE")) {
            try {
                result = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            } catch (Exception e) {
            }
        }
        return result == null ? "" : result;
    }

    static String getWifiMacAddress(Context context) {
        WifiManager wifiManager;
        WifiInfo wifiInfo;
        String result = null;
        if (!(!hasPermission(context, "android.permission.ACCESS_WIFI_STATE") || (wifiManager = (WifiManager) context.getSystemService("wifi")) == null || (wifiInfo = wifiManager.getConnectionInfo()) == null)) {
            result = wifiInfo.getMacAddress();
        }
        return result == null ? "" : result;
    }

    static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return (androidId == null || androidId.equalsIgnoreCase("9774d56d682e549c")) ? "" : androidId;
    }

    public static boolean hasPermission(Context context, String permissionType) {
        return context.checkCallingOrSelfPermission(permissionType) == 0;
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionName != null) {
                return packageInfo.versionName;
            }
            return "";
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static String getAppName(Context context) {
        String packageName = context.getPackageName();
        return packageName == null ? "" : packageName;
    }
}
