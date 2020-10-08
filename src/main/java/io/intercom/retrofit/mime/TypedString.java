package io.intercom.retrofit.mime;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.UnsupportedEncodingException;

public class TypedString extends TypedByteArray {
    public TypedString(String string) {
        super("text/plain; charset=UTF-8", convertToBytes(string));
    }

    private static byte[] convertToBytes(String string) {
        try {
            return string.getBytes(HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        try {
            return "TypedString[" + new String(getBytes(), HttpRequest.CHARSET_UTF8) + "]";
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Must be able to decode UTF-8");
        }
    }
}
