package io.intercom.com.squareup.okhttp.internal.http;

import io.fabric.sdk.android.services.network.HttpRequest;

public final class HttpMethod {
    public static boolean invalidatesCache(String method) {
        return method.equals(HttpRequest.METHOD_POST) || method.equals("PATCH") || method.equals(HttpRequest.METHOD_PUT) || method.equals("DELETE") || method.equals("MOVE");
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals(HttpRequest.METHOD_POST) || method.equals(HttpRequest.METHOD_PUT) || method.equals("PATCH") || method.equals("PROPPATCH") || method.equals("REPORT");
    }

    public static boolean permitsRequestBody(String method) {
        return requiresRequestBody(method) || method.equals(HttpRequest.METHOD_OPTIONS) || method.equals("DELETE") || method.equals("PROPFIND") || method.equals("MKCOL") || method.equals("LOCK");
    }

    public static boolean redirectsToGet(String method) {
        return !method.equals("PROPFIND");
    }

    private HttpMethod() {
    }
}
