package com.inteliclinic.neuroon.views.charts.popovers;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.charts.BarChartView;
import java.text.DateFormat;
import java.util.Date;

public class BubbleView extends FrameLayout {
    @InjectView(2131755589)
    FrameLayout hightlight;
    @InjectView(2131755116)
    LinearLayout mContainer;
    private Rect mRect;
    @InjectView(2131755078)
    BaseTextView mTitle;
    @InjectView(2131755577)
    BaseTextView time;

    public BubbleView(Context context) {
        super(context);
        init((AttributeSet) null, 0);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_bubble, this);
        ButterKnife.inject((View) this);
    }

    public void setValues(String title, Date startDate, Date endDate) {
        this.mTitle.setText(title);
        this.time.setText(getResources().getString(R.string.time_dash_time, new Object[]{DateFormat.getTimeInstance(3).format(startDate), DateFormat.getTimeInstance(3).format(endDate)}));
    }

    public void setOnTopMiddleOfRect(Rect rect) {
        this.mRect = rect;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.mContainer.getLayoutParams();
        layoutParams.topMargin = (rect.top - this.time.getTop()) - this.time.getMeasuredHeight();
        layoutParams.rightMargin = (getMeasuredWidth() - ((rect.left + rect.right) / 2)) - (this.time.getMeasuredWidth() / 2);
        if (layoutParams.rightMargin < 0) {
            layoutParams.rightMargin = 0;
        }
        this.mContainer.setLayoutParams(layoutParams);
    }

    public void setOnBottomMiddleOfRect(Rect rect) {
        this.mRect = rect;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.mContainer.getLayoutParams();
        layoutParams.topMargin = rect.bottom;
        layoutParams.rightMargin = (getMeasuredWidth() - ((rect.left + rect.right) / 2)) - (this.time.getMeasuredWidth() / 2);
        if (layoutParams.rightMargin < 0) {
            layoutParams.rightMargin = 0;
        }
        this.mContainer.setLayoutParams(layoutParams);
    }

    public Rect getRect() {
        return this.mRect;
    }

    public void highlightElement(BarChartView barChart, Rect rect) {
        int[] position = new int[2];
        barChart.getLocationOnScreen(position);
        int measuredWidth = barChart.getMeasuredWidth();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.hightlight.getLayoutParams();
        layoutParams.leftMargin = rect.left;
        layoutParams.topMargin = position[1] < rect.top ? position[1] : rect.top;
        layoutParams.height = barChart.getMeasuredHeight();
        layoutParams.width = rect.width();
        this.hightlight.setLayoutParams(layoutParams);
    }
}
