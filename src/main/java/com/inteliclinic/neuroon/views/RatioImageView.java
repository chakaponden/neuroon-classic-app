package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class RatioImageView extends ImageView {
    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                setMeasuredDimension(0, 0);
                return;
            }
            int measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec);
            int measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec);
            if (measuredHeight == 0 && measuredWidth == 0) {
                setMeasuredDimension(measuredWidth, measuredHeight);
            } else if (measuredHeight == 0) {
                int width = measuredWidth;
                setMeasuredDimension(width, (drawable.getIntrinsicHeight() * width) / drawable.getIntrinsicWidth());
            } else if (measuredWidth == 0) {
                int height = measuredHeight;
                setMeasuredDimension((drawable.getIntrinsicWidth() * height) / drawable.getIntrinsicHeight(), height);
            } else {
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
