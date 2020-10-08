package com.inteliclinic.neuroon.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public final class TextUtils {
    private TextUtils() {
    }

    public static Rect measureText(Paint paint, String text) {
        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);
        return textRect;
    }

    public static void setColor(TextView view, String subtext, int color) {
        CharSequence fullText = view.getText();
        view.setText(fullText, TextView.BufferType.SPANNABLE);
        int i = fullText.toString().indexOf(subtext);
        ((Spannable) view.getText()).setSpan(new ForegroundColorSpan(color), i, subtext.length() + i, 33);
    }
}
