package io.intercom.com.squareup.okhttp;

import io.intercom.okio.ByteString;
import java.io.UnsupportedEncodingException;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String userName, String password) {
        try {
            return "Basic " + ByteString.of((userName + ":" + password).getBytes("ISO-8859-1")).base64();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
