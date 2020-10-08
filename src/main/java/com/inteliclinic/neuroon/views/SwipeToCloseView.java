package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;

public class SwipeToCloseView extends FrameLayout {
    /* access modifiers changed from: private */
    public int mLeftAnimationStartMargin;
    /* access modifiers changed from: private */
    public int mLeftMargin;
    /* access modifiers changed from: private */
    public SwipeToCloseListener mListener;
    private int mRightMargin;
    private Animation mSliderAnimation;
    private int mSliderWidth;
    @InjectView(2131755619)
    LightTextView slideToClose;
    @InjectView(2131755620)
    ImageView slider;

    public interface SwipeToCloseListener {
        void onClose();
    }

    public SwipeToCloseView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeToCloseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeToCloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SwipeToCloseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_swipe_to_close, this);
        ButterKnife.inject((View) this);
        this.slider.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                    case 2:
                        SwipeToCloseView.this.animationStop();
                        SwipeToCloseView.this.setupSlider();
                        SwipeToCloseView.this.setSliderLeftMargin((int) ((event.getRawX() - ((float) SwipeToCloseView.this.getLeft())) - ((float) (SwipeToCloseView.this.slider.getWidth() / 2))));
                        return true;
                    case 1:
                        if (!SwipeToCloseView.this.tooRight((int) ((event.getRawX() - ((float) SwipeToCloseView.this.getLeft())) - ((float) (SwipeToCloseView.this.slider.getWidth() / 2))), 50)) {
                            SwipeToCloseView.this.animationStart();
                            return true;
                        } else if (SwipeToCloseView.this.mListener == null) {
                            return true;
                        } else {
                            SwipeToCloseView.this.mListener.onClose();
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });
    }

    public void setOnSwipeListener(SwipeToCloseListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: private */
    public void animationStart() {
        this.mSliderAnimation.reset();
        this.mLeftAnimationStartMargin = ((FrameLayout.LayoutParams) this.slider.getLayoutParams()).leftMargin;
        this.slider.startAnimation(this.mSliderAnimation);
    }

    /* access modifiers changed from: private */
    public void animationStop() {
        if (this.mSliderAnimation != null) {
            this.mSliderAnimation.cancel();
            this.slider.clearAnimation();
        }
    }

    public void setTextColor(@ColorInt int color) {
        if (this.slideToClose != null) {
            this.slideToClose.setTextColor(color);
        }
    }

    /* access modifiers changed from: private */
    public void setupSlider() {
        if (this.mLeftMargin == 0 || this.mRightMargin == 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.slider.getLayoutParams();
            this.mLeftMargin = layoutParams.leftMargin;
            this.mRightMargin = layoutParams.rightMargin;
        }
        if (this.mSliderAnimation == null) {
            this.mSliderAnimation = new Animation() {
                /* access modifiers changed from: protected */
                public void applyTransformation(float interpolatedTime, Transformation t) {
                    SwipeToCloseView.this.setSliderLeftMargin((int) (((float) SwipeToCloseView.this.mLeftMargin) + (((float) SwipeToCloseView.this.mLeftAnimationStartMargin) * (1.0f - interpolatedTime))));
                }
            };
            this.mSliderAnimation.setDuration(300);
        }
    }

    /* access modifiers changed from: private */
    public void setSliderLeftMargin(int margin) {
        if (margin >= this.mLeftMargin && !tooRight(margin, 0)) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.slider.getLayoutParams();
            layoutParams.leftMargin = margin;
            this.slider.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    public boolean tooRight(int margin, int tolerance) {
        return (margin + tolerance) + this.slider.getWidth() > getWidth() - this.mRightMargin;
    }
}
