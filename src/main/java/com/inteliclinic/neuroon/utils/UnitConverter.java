package com.inteliclinic.neuroon.utils;

import android.content.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class UnitConverter {
    private UnitConverter() {
    }

    public static double cmToInch(double value) {
        return round(value / 2.54d, 0);
    }

    public static double inchToCm(double inch) {
        return round(2.54d * inch, 2);
    }

    public static int kgToLbs(double value) {
        return (int) (2.20462d * value);
    }

    public static double lbsToKg(int value) {
        return round(((double) value) / 2.20462d, 4);
    }

    private static double round(double value, int places) {
        if (places >= 0) {
            return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
        }
        throw new IllegalArgumentException();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return ((float) (context.getResources().getDisplayMetrics().densityDpi / 160)) * dp;
    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) (context.getResources().getDisplayMetrics().densityDpi / 160));
    }

    public static long millisToSec(long millis) {
        return millis / 1000;
    }
}
