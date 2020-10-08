package com.crashlytics.android.beta;

import java.io.IOException;
import org.json.JSONObject;

class CheckForUpdatesResponseTransform {
    static final String BUILD_VERSION = "build_version";
    static final String DISPLAY_VERSION = "display_version";
    static final String IDENTIFIER = "identifier";
    static final String INSTANCE_IDENTIFIER = "instance_identifier";
    static final String URL = "url";
    static final String VERSION_STRING = "version_string";

    CheckForUpdatesResponseTransform() {
    }

    public CheckForUpdatesResponse fromJson(JSONObject json) throws IOException {
        if (json == null) {
            return null;
        }
        return new CheckForUpdatesResponse(json.optString("url", (String) null), json.optString(VERSION_STRING, (String) null), json.optString(DISPLAY_VERSION, (String) null), json.optString(BUILD_VERSION, (String) null), json.optString("identifier", (String) null), json.optString(INSTANCE_IDENTIFIER, (String) null));
    }
}
