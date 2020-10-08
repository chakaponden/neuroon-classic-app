package io.intercom.android.sdk.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.Locale;

public class LinkUtil {
    public static void openUrl(String trackingUrl, Context context) {
        Intent intent;
        Uri uri = Uri.parse(trackingUrl);
        if (uri.getScheme() == null || (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https"))) {
            IntercomLogger.INTERNAL("Tracking Error", "Tracking url requires a http or https scheme");
        } else {
            Bridge.getApi().hitTrackingUrl(trackingUrl);
        }
        Uri targetUri = getTargetUriFromTrackingUrl(trackingUrl);
        if ("mailto".equals(targetUri.getScheme())) {
            intent = new Intent("android.intent.action.SENDTO", targetUri);
        } else {
            intent = new Intent("android.intent.action.VIEW", targetUri);
        }
        try {
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            IntercomLogger.ERROR("No activity found to handle that link");
        }
    }

    public static Uri getTargetUriFromTrackingUrl(String trackingUrl) {
        String queryUrl;
        String targetUrl;
        Uri uri = Uri.parse(trackingUrl);
        if (uri.isHierarchical()) {
            queryUrl = uri.getQueryParameter("url");
        } else {
            queryUrl = trackingUrl;
        }
        if (queryUrl == null) {
            targetUrl = trackingUrl;
        } else {
            targetUrl = queryUrl;
        }
        Uri uri2 = Uri.parse(targetUrl);
        if (uri2.getScheme() == null) {
            uri2 = Uri.parse("http://" + targetUrl);
        }
        if (uri2.getScheme().equalsIgnoreCase("http") || uri2.getScheme().equalsIgnoreCase("https")) {
            return normalizeScheme(uri2);
        }
        return uri2;
    }

    private static Uri normalizeScheme(Uri uri) {
        String scheme = uri.getScheme();
        if (scheme == null) {
            return uri;
        }
        String lowerScheme = scheme.toLowerCase(Locale.ROOT);
        return !scheme.equals(lowerScheme) ? uri.buildUpon().scheme(lowerScheme).build() : uri;
    }
}
