package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.util.AttributeSet;

public class ThinTextView extends BaseTextView {
    public ThinTextView(Context context) {
        super(context);
    }

    public ThinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThinTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThinTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public int getFontName() {
        return 2;
    }
}
