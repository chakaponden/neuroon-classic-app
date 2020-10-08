package io.intercom.android.sdk.blocks;

import java.util.Locale;

public enum VideoProvider {
    YOUTUBE,
    VIMEO,
    WISTIA,
    STREAMIO,
    UNKNOWN;

    public static VideoProvider videoValueOf(String provider) {
        VideoProvider result = UNKNOWN;
        try {
            return valueOf(provider.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return result;
        }
    }
}
