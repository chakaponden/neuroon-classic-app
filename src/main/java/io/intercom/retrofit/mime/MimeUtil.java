package io.intercom.retrofit.mime;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MimeUtil {
    private static final Pattern CHARSET = Pattern.compile("\\Wcharset=([^\\s;]+)", 2);

    @Deprecated
    public static String parseCharset(String mimeType) {
        return parseCharset(mimeType, HttpRequest.CHARSET_UTF8);
    }

    public static String parseCharset(String mimeType, String defaultCharset) {
        Matcher match = CHARSET.matcher(mimeType);
        if (match.find()) {
            return match.group(1).replaceAll("[\"\\\\]", "");
        }
        return defaultCharset;
    }

    private MimeUtil() {
    }
}
