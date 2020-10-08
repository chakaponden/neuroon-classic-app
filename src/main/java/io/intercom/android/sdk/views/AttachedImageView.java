package io.intercom.android.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AttachedImageView extends ImageView {
    private OnAttachedToWindowListener callback;

    public interface OnAttachedToWindowListener {
        void callback();
    }

    public AttachedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.callback.callback();
    }

    public void setOnAttachedToWindowListener(OnAttachedToWindowListener callback2) {
        this.callback = callback2;
    }
}
