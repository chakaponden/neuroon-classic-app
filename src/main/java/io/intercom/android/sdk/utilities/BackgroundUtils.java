package io.intercom.android.sdk.utilities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class BackgroundUtils {
    public static void setBackground(View background, Drawable drawable) {
        if (Build.VERSION.SDK_INT > 15) {
            background.setBackground(drawable);
        } else {
            background.setBackgroundDrawable(drawable);
        }
    }
}
