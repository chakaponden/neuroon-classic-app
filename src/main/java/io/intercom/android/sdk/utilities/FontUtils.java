package io.intercom.android.sdk.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import io.intercom.android.sdk.logger.IntercomLogger;

public class FontUtils {
    private static final String FONTS_FOLDER = "fonts/";
    public static final String ROBOTO_LIGHT = "intercomsdk_roboto_light.ttf";
    public static final String ROBOTO_MEDIUM = "intercomsdk_roboto_medium.ttf";

    public static void setTypeface(TextView textView, String fontName, Context context) {
        try {
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), FONTS_FOLDER + fontName));
        } catch (RuntimeException e) {
            IntercomLogger.ERROR("We could not load our custom font, using the default system font as a backup.");
        }
    }
}
