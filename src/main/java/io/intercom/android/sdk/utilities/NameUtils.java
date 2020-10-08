package io.intercom.android.sdk.utilities;

import android.content.Context;
import android.text.TextUtils;
import io.intercom.android.sdk.R;
import java.util.List;

public class NameUtils {
    public static String getFormattedAdmins(List<String> adminNames, Context context) {
        int numAdmins = adminNames.size();
        if (numAdmins > 0) {
            String latestAdminName = adminNames.get(0).trim();
            if (latestAdminName.indexOf(32) != -1) {
                latestAdminName = latestAdminName.substring(0, latestAdminName.indexOf(" "));
            }
            if (numAdmins >= 2) {
                return latestAdminName + context.getResources().getQuantityString(R.plurals.intercomsdk_others, numAdmins - 1, new Object[]{Integer.valueOf(numAdmins - 1)});
            } else if (numAdmins == 1) {
                return latestAdminName;
            }
        }
        return context.getResources().getString(R.string.intercomsdk_new_conversation_title);
    }

    public static String getInitials(String displayName) {
        if (TextUtils.isEmpty(displayName)) {
            return "";
        }
        String[] parts = displayName.trim().split(" ");
        if (parts.length > 1) {
            return String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[parts.length - 1].charAt(0));
        }
        return String.valueOf(parts[0].charAt(0));
    }
}
