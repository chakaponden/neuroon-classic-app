package io.intercom.android.sdk.commons.utilities;

import android.content.Context;

public class ScreenUtils {
    public static int convertDpToPixel(float densityPixels, Context context) {
        return (int) ((((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * densityPixels);
    }

    public static int convertPixelsToDp(float pixels, Context context) {
        return (int) (pixels / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }
}
