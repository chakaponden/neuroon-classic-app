package com.inteliclinic.neuroon.views.dashboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BaseTextView;

public class DashboardStatsView extends RelativeLayout {
    private Drawable mIcon;
    @InjectView(2131755597)
    ImageView mIconView;
    private String mUnit;
    @InjectView(2131755599)
    BaseTextView mUnitView;
    private String mValue;
    @InjectView(2131755598)
    BaseTextView mValueView;

    public DashboardStatsView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DashboardStatsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardStatsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs, defStyleAttr, 0);
        init();
    }

    @TargetApi(21)
    public DashboardStatsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStyle(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_dashboard_stats, this);
        ButterKnife.inject((View) this);
        setValues();
    }

    private void setValues() {
        this.mIconView.setImageDrawable(this.mIcon);
        this.mValueView.setText(this.mValue);
        this.mUnitView.setText(this.mUnit);
    }

    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashboardStatsView, defStyleAttr, defStyleRes);
        try {
            this.mIcon = a.getDrawable(0);
            this.mValue = a.getString(1);
            this.mUnit = a.getString(2);
        } finally {
            a.recycle();
        }
    }

    public void setValue(String string) {
        this.mValue = string;
        setValues();
    }

    public void setValueColor(@ColorInt int color) {
        this.mValueView.setTextColor(color);
    }
}
