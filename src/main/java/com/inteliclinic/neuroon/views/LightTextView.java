package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.util.AttributeSet;

public class LightTextView extends BaseTextView {
    public LightTextView(Context context) {
        super(context);
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public int getFontName() {
        return 1;
    }
}
