package com.inteliclinic.neuroon.utils;

import android.content.Context;
import android.graphics.Typeface;

public final class FontUtils {
    public static final int FONT_BOLD = 0;
    public static final int FONT_LIGHT = 1;
    public static final int FONT_THIN = 2;
    private static Typeface[] mFonts = new Typeface[3];

    private FontUtils() {
    }

    public static Typeface getFont(Context context, int fontId) {
        Typeface typeface = mFonts[fontId];
        if (typeface != null) {
            return typeface;
        }
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), getFontName(fontId));
        mFonts[fontId] = typeface2;
        return typeface2;
    }

    private static String getFontName(int fontId) {
        switch (fontId) {
            case 0:
                return "fonts/Antenna Bold.otf";
            case 2:
                return "fonts/Antenna Thin.otf";
            default:
                return "fonts/Antenna ExtraLight.otf";
        }
    }
}
