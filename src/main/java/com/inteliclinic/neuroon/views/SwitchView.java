package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.utils.FontUtils;
import com.inteliclinic.neuroon.utils.TextUtils;

public class SwitchView extends View {
    private boolean isHolding;
    private Animation mAnimation;
    private Paint mBackgroundPaint;
    /* access modifiers changed from: private */
    public float mIndicatorDistance;
    /* access modifiers changed from: private */
    public float mIndicatorMaxX;
    /* access modifiers changed from: private */
    public float mIndicatorMinX;
    private Paint mIndicatorPaint;
    private SwitchListener mListener;
    private Rect mOffRect;
    private String mOffText;
    private Paint mOffTextPaint;
    private Rect mOnRect;
    private String mOnText;
    private Paint mOnTextPaint;
    /* access modifiers changed from: private */
    public float mStartAnimationDistance;
    /* access modifiers changed from: private */
    public boolean mState;

    public interface SwitchListener {
        void onSwitchStateChange(boolean z);
    }

    public SwitchView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initStyle(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initStyle(context, attrs, defStyleAttr, 0);
    }

    private void init() {
        this.mIndicatorPaint = new Paint();
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setColor(getResources().getColor(R.color.light_grey_e8));
        this.mOnTextPaint = new Paint();
        this.mOnTextPaint.setAntiAlias(true);
        this.mOnTextPaint.setTypeface(FontUtils.getFont(getContext(), 1));
        this.mOffTextPaint = new Paint();
        this.mOffTextPaint.setAntiAlias(true);
        this.mOffTextPaint.setTypeface(FontUtils.getFont(getContext(), 1));
    }

    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchView, defStyleAttr, defStyleRes);
        try {
            this.mState = a.getBoolean(0, false);
            if (this.mState) {
                this.mIndicatorDistance = this.mIndicatorMaxX - this.mIndicatorMinX;
            }
            this.mOffText = a.getString(7);
            if (this.mOffText == null) {
                this.mOffText = "";
            }
            this.mOffTextPaint.setColor(a.getColor(5, getResources().getColor(R.color.grey_8e)));
            this.mOffTextPaint.setTextSize(a.getDimension(6, getResources().getDisplayMetrics().density * 18.0f));
            this.mOnText = a.getString(4);
            if (this.mOnText == null) {
                this.mOnText = "";
            }
            this.mOnTextPaint.setColor(a.getColor(2, getResources().getColor(R.color.grey_8e)));
            this.mOnTextPaint.setTextSize(a.getDimension(3, getResources().getDisplayMetrics().density * 18.0f));
            this.mIndicatorPaint.setColor(a.getColor(1, ViewCompat.MEASURED_STATE_MASK));
        } finally {
            a.recycle();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean newState;
        boolean z = false;
        if (isTouchingIndicator(event)) {
            this.isHolding = true;
            stopAnimation();
            float distance = (event.getRawX() - ((float) getLeft())) - this.mIndicatorMinX;
            if (distance >= this.mIndicatorMaxX - this.mIndicatorMinX) {
                this.mIndicatorDistance = this.mIndicatorMaxX - this.mIndicatorMinX;
                this.mState = true;
                if (this.mListener != null) {
                    this.mListener.onSwitchStateChange(this.mState);
                }
            } else if (distance < 0.0f) {
                this.mIndicatorDistance = 0.0f;
                this.mState = false;
                if (this.mListener != null) {
                    this.mListener.onSwitchStateChange(this.mState);
                }
            } else {
                this.mIndicatorDistance = distance;
            }
            invalidate();
        } else if (this.isHolding) {
            this.isHolding = false;
            boolean oldState = this.mState;
            if (this.mIndicatorDistance > (this.mIndicatorMaxX - this.mIndicatorMinX) / 2.0f) {
                z = true;
            }
            this.mState = z;
            if (!(oldState == this.mState || this.mListener == null)) {
                this.mListener.onSwitchStateChange(this.mState);
            }
            startAnimation();
        } else if (event.getAction() == 1) {
            boolean oldState2 = this.mState;
            if ((event.getRawX() - ((float) getLeft())) - this.mIndicatorMinX > (this.mIndicatorMaxX - this.mIndicatorMinX) / 2.0f) {
                newState = true;
            } else {
                newState = false;
            }
            if (oldState2 != newState) {
                setOn(newState);
                invalidate();
                if (this.mListener != null) {
                    this.mListener.onSwitchStateChange(this.mState);
                }
            }
        }
        return true;
    }

    private void startAnimation() {
        this.mStartAnimationDistance = this.mIndicatorDistance;
        if (this.mAnimation == null) {
            this.mAnimation = new Animation() {
                /* access modifiers changed from: protected */
                public void applyTransformation(float interpolatedTime, Transformation t) {
                    float mEndAnimationState = SwitchView.this.mState ? SwitchView.this.mIndicatorMaxX - SwitchView.this.mIndicatorMinX : 0.0f;
                    float unused = SwitchView.this.mIndicatorDistance = mEndAnimationState - ((mEndAnimationState - SwitchView.this.mStartAnimationDistance) * (1.0f - interpolatedTime));
                    SwitchView.this.invalidate();
                }
            };
            this.mAnimation.setDuration(300);
        }
        this.mAnimation.reset();
        startAnimation(this.mAnimation);
    }

    private void stopAnimation() {
        if (this.mAnimation != null) {
            this.mAnimation.cancel();
            clearAnimation();
        }
    }

    private boolean isTouchingIndicator(MotionEvent event) {
        int[] location = new int[2];
        getLocationInWindow(location);
        float circleCenterX = this.mIndicatorMinX + (this.mIndicatorDistance % ((this.mIndicatorMaxX - this.mIndicatorMinX) + 1.0f)) + ((float) location[0]);
        float circleCenterY = (float) ((getMeasuredHeight() / 2) + location[1]);
        float circleRadius = (float) ((getMeasuredHeight() / 2) - 5);
        if (Math.abs(event.getRawX() - circleCenterX) >= circleRadius + 100.0f || Math.abs(event.getRawY() - circleCenterY) >= circleRadius + 100.0f || event.getAction() == 1) {
            return false;
        }
        return true;
    }

    public void setListener(SwitchListener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mIndicatorMinX = (float) (getMeasuredHeight() / 2);
        this.mIndicatorMaxX = ((float) getMeasuredWidth()) - this.mIndicatorMinX;
        this.mIndicatorDistance = this.mState ? this.mIndicatorMaxX - this.mIndicatorMinX : 0.0f;
        this.mOffRect = TextUtils.measureText(this.mOffTextPaint, this.mOffText);
        this.mOnRect = TextUtils.measureText(this.mOnTextPaint, this.mOnText);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float circleCenterX = this.mIndicatorMinX + (this.mIndicatorDistance % ((this.mIndicatorMaxX - this.mIndicatorMinX) + 1.0f));
        this.mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mBackgroundPaint.setStrokeWidth((float) getMeasuredHeight());
        canvas.drawLine((float) (getMeasuredHeight() / 2), (float) (getMeasuredHeight() / 2), (float) (getMeasuredWidth() - (getMeasuredHeight() / 2)), (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
        this.mBackgroundPaint.setStrokeCap(Paint.Cap.BUTT);
        if (this.mState) {
            if (circleCenterX < this.mIndicatorMinX + ((float) this.mOnRect.width())) {
                canvas.drawText(this.mOnText, this.mIndicatorMinX, (float) ((getMeasuredHeight() / 2) - this.mOnRect.centerY()), this.mOnTextPaint);
                canvas.drawLine(circleCenterX, (float) (getMeasuredHeight() / 2), this.mIndicatorMinX + ((float) this.mOnRect.width()), (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
                canvas.drawText(this.mOffText, this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) ((getMeasuredHeight() / 2) - this.mOffRect.centerY()), this.mOffTextPaint);
            } else {
                canvas.drawText(this.mOffText, this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) ((getMeasuredHeight() / 2) - this.mOffRect.centerY()), this.mOffTextPaint);
                canvas.drawLine(circleCenterX, (float) (getMeasuredHeight() / 2), this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
                canvas.drawText(this.mOnText, this.mIndicatorMinX, (float) ((getMeasuredHeight() / 2) - this.mOnRect.centerY()), this.mOnTextPaint);
            }
        } else if (circleCenterX > this.mIndicatorMaxX - ((float) this.mOffRect.width())) {
            canvas.drawText(this.mOffText, this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) ((getMeasuredHeight() / 2) - this.mOffRect.centerY()), this.mOffTextPaint);
            canvas.drawLine(circleCenterX, (float) (getMeasuredHeight() / 2), this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
            canvas.drawText(this.mOnText, this.mIndicatorMinX, (float) ((getMeasuredHeight() / 2) - this.mOnRect.centerY()), this.mOnTextPaint);
        } else {
            canvas.drawText(this.mOnText, this.mIndicatorMinX, (float) ((getMeasuredHeight() / 2) - this.mOnRect.centerY()), this.mOnTextPaint);
            canvas.drawLine(circleCenterX, (float) (getMeasuredHeight() / 2), this.mIndicatorMaxX, (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
            canvas.drawText(this.mOffText, this.mIndicatorMaxX - ((float) this.mOffRect.width()), (float) ((getMeasuredHeight() / 2) - this.mOffRect.centerY()), this.mOffTextPaint);
        }
        canvas.drawCircle(circleCenterX, (float) (getMeasuredHeight() / 2), (float) (getMeasuredHeight() / 2), this.mBackgroundPaint);
        canvas.drawCircle(circleCenterX, (float) (getMeasuredHeight() / 2), (float) ((getMeasuredHeight() / 2) - 5), this.mIndicatorPaint);
    }

    public void setOnText(String onText) {
        this.mOnText = onText;
    }

    public void setOffText(String offText) {
        this.mOffText = offText;
    }

    public void setOn(boolean on) {
        this.mState = on;
        if (on) {
            this.mIndicatorDistance = this.mIndicatorMaxX - this.mIndicatorMinX;
        } else {
            this.mIndicatorDistance = 0.0f;
        }
    }
}
