package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;

public class BottomToolbar extends FrameLayout {
    @InjectView(2131755588)
    ImageView lightboostButton;
    @InjectView(2131755391)
    LightTextView lightboostText;
    private int mHeight = 0;
    private OnToolbarClickListener mListener;
    private Buttons mSelectedButton = Buttons.SLEEP_SCORE;
    private int mWidth = 0;
    @InjectView(2131755585)
    ImageView napsButton;
    @InjectView(2131755586)
    LightTextView personalPauseText;
    @InjectView(2131755587)
    ImageView sleepScoreButton;
    @InjectView(2131755212)
    LightTextView sleepScoreText;
    @InjectView(2131755584)
    ImageView toolbarBackground;

    public enum Buttons {
        JET_LAG,
        SLEEP_SCORE,
        ENERGY
    }

    public interface OnToolbarClickListener {
        void onJetLagClick(BottomToolbar bottomToolbar, Buttons buttons);

        void onLightBoostClick(BottomToolbar bottomToolbar, Buttons buttons);

        void onSleepScoreClick(BottomToolbar bottomToolbar, Buttons buttons);
    }

    public BottomToolbar(Context context) {
        super(context);
        init();
    }

    public BottomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = (int) ((((float) this.mWidth) / 1280.0f) * 234.0f);
        setMeasuredDimension(this.mWidth, this.mHeight);
        this.toolbarBackground.measure(widthMeasureSpec, this.mHeight);
        this.napsButton.measure((int) ((((float) this.mWidth) / 1280.0f) * 484.0f), this.mHeight);
        this.sleepScoreButton.measure((int) ((((float) this.mWidth) / 1280.0f) * 492.0f), this.mHeight);
        this.lightboostButton.measure((int) ((((float) this.mWidth) / 1280.0f) * 438.0f), this.mHeight);
        ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = -((int) ((((float) this.mHeight) / 234.0f) * 34.0f));
    }

    private void init() {
        inflate(getContext(), R.layout.view_bottom_toolbar, this);
        setClickable(true);
        ButterKnife.inject((View) this);
        setButtonsVisibility();
        setToolbarBackground();
    }

    public void setCurrentButton(Buttons currentButton) {
        this.mSelectedButton = currentButton;
        setButtonsVisibility();
    }

    private void setButtonsVisibility() {
        setButton(Buttons.JET_LAG, this.napsButton, this.personalPauseText);
        setButton(Buttons.SLEEP_SCORE, this.sleepScoreButton, this.sleepScoreText);
        setButton(Buttons.ENERGY, this.lightboostButton, this.lightboostText);
    }

    private void setButton(Buttons buttons, View view, BaseTextView textView) {
        setToolbarBackground();
        setVisibility(buttons, view);
        setTextColor(buttons, textView);
    }

    private void setToolbarBackground() {
        this.toolbarBackground.setImageResource(R.drawable.toolbar_grey_bg);
    }

    private void setTextColor(Buttons buttons, BaseTextView textView) {
        if (this.mSelectedButton == buttons) {
            textView.setTextColor(-1);
        } else {
            textView.setTextColor(getResources().getColor(R.color.grey_8e));
        }
        textView.invalidate();
    }

    private void setVisibility(Buttons buttons, View view) {
        if (this.mSelectedButton == buttons) {
            view.setVisibility(0);
        } else {
            view.setVisibility(8);
        }
        view.invalidate();
    }

    public void setOnClickListener(OnToolbarClickListener listener) {
        this.mListener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            float v = event.getX() / ((float) this.mWidth);
            if (((double) v) < 0.333d) {
                onNapClick();
            } else if (((double) v) < 0.666d) {
                onSleepScoreClick();
            } else {
                onLightBoostClick();
            }
        }
        return super.onTouchEvent(event);
    }

    @OnClick({2131755585})
    public void onNapClick() {
        Buttons oldButton = this.mSelectedButton;
        this.mSelectedButton = Buttons.JET_LAG;
        setButtonsVisibility();
        if (this.mListener != null) {
            this.mListener.onJetLagClick(this, oldButton);
        }
    }

    @OnClick({2131755587})
    public void onSleepScoreClick() {
        Buttons oldButton = this.mSelectedButton;
        this.mSelectedButton = Buttons.SLEEP_SCORE;
        setButtonsVisibility();
        if (this.mListener != null) {
            this.mListener.onSleepScoreClick(this, oldButton);
        }
    }

    @OnClick({2131755588})
    public void onLightBoostClick() {
        Buttons oldButton = this.mSelectedButton;
        this.mSelectedButton = Buttons.ENERGY;
        setButtonsVisibility();
        if (this.mListener != null) {
            this.mListener.onLightBoostClick(this, oldButton);
        }
    }
}
