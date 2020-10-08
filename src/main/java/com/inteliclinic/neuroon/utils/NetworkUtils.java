package com.inteliclinic.neuroon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static boolean isWiFiOn(Context context) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting() || activeNetwork.getType() != 1) {
            return false;
        }
        return true;
    }
}
