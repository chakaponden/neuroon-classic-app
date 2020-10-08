package io.intercom.android.sdk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.utilities.Constants;

public class Migrations {
    private static final String MIGRATION_090 = "migration to 0.9.0!";
    private static final String MIGRATION_PREFS = "INTERCOM_MIGRATION_PREFS";
    protected static final String PREFS_OLD_INTERCOM_USER_ID = "IntercomUserId";

    public static void runAll(Context context) {
        runInitialMigrations(context);
    }

    private static void runInitialMigrations(Context context) {
        SharedPreferences migrationPrefs = context.getSharedPreferences(MIGRATION_PREFS, 0);
        if (!migrationPrefs.getBoolean(MIGRATION_090, false)) {
            IntercomLogger.DEBUG("starting migration from 0.8.x to 0.9.0");
            SharedPreferences dataPrefs = context.getSharedPreferences(Constants.INTERCOM_DATA_PREFS, 0);
            String intercomUserId = context.getSharedPreferences(Constants.INTERCOM_PREFS, 0).getString("intercomsdk-session-IntercomUserId", "");
            if (!TextUtils.isEmpty(intercomUserId)) {
                Bridge.getIdentityStore().getUserIdentity().register("", "", intercomUserId);
            }
            String data = dataPrefs.getString("intercomsdk-session-SecureMode_Data", "");
            String hmac = dataPrefs.getString("intercomsdk-session-SecureMode_HMAC", "");
            if (!data.isEmpty() && !hmac.isEmpty()) {
                Bridge.getIdentityStore().setSecureMode(hmac, data);
            }
            migrationPrefs.edit().putBoolean(MIGRATION_090, true).apply();
        }
    }
}
