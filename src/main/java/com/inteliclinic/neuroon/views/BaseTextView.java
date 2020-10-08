package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.inteliclinic.neuroon.utils.FontUtils;

public abstract class BaseTextView extends TextView {
    private int progress;

    /* access modifiers changed from: protected */
    public abstract int getFontName();

    public BaseTextView(Context context) {
        super(context);
        initFont();
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    @TargetApi(21)
    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFont();
    }

    private void initFont() {
        setTypeface(FontUtils.getFont(getContext(), getFontName()));
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int value) {
        this.progress = value;
        setText(String.format("%03d", new Object[]{Integer.valueOf(value)}));
    }
}
