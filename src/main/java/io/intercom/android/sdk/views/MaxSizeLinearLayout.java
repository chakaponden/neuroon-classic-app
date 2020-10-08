package io.intercom.android.sdk.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.ActivityChooserView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import io.intercom.android.sdk.R;

public class MaxSizeLinearLayout extends LinearLayout {
    private final int maxHeight;
    private final int maxWidth;

    public MaxSizeLinearLayout(Context context) {
        super(context);
        this.maxWidth = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.maxHeight = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    }

    public MaxSizeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaxSizeLinearLayout);
        this.maxWidth = a.getDimensionPixelSize(R.styleable.MaxSizeLinearLayout_intercomsdk_maxWidth, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        this.maxHeight = a.getDimensionPixelSize(R.styleable.MaxSizeLinearLayout_intercomsdk_maxHeight, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        a.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        if (this.maxWidth > 0 && this.maxWidth < measuredWidth) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.maxWidth, Integer.MIN_VALUE);
        }
        if (this.maxHeight > 0 && this.maxHeight < measuredHeight) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.maxHeight, Integer.MIN_VALUE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
