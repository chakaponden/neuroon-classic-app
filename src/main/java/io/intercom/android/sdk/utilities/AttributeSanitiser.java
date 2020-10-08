package io.intercom.android.sdk.utilities;

import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.Map;

public class AttributeSanitiser {
    private static final String EMAIL = "email";
    private static final String USER_ID = "user_id";

    public static void anonymousSanitisation(Map<String, ?> attributes) {
        if (attributes.containsKey("email")) {
            IntercomLogger.ERROR(String.format("You cannot update the email of an anonymous user. Please call registerIdentified user instead. The email: %s was NOT applied", new Object[]{attributes.remove("email")}));
        }
        if (attributes.containsKey("user_id")) {
            IntercomLogger.ERROR(String.format("You cannot update the user_id of an anonymous user. Please call registerIdentified user instead. The user_id: %s was NOT applied", new Object[]{attributes.remove("user_id")}));
        }
    }
}
