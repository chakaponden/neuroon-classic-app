package io.intercom.android.sdk.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import io.intercom.android.sdk.logger.IntercomLogger;

@TargetApi(11)
public class UploadProgressBar extends View {
    private static final int MAX = 100;
    private static final int START_ANGLE = -90;
    private static final int STROKE_WIDTH = 8;
    private Paint backgroundPaint;
    private boolean endAnimating;
    private Paint foregroundPaint;
    private int progress;
    private RectF rectF;

    public UploadProgressBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public UploadProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.endAnimating = false;
        this.progress = 0;
        this.rectF = new RectF();
        this.backgroundPaint = new Paint(1);
        this.backgroundPaint.setColor(-2013265920);
        this.backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.backgroundPaint.setStrokeWidth(8.0f);
        this.foregroundPaint = new Paint(1);
        this.foregroundPaint.setColor(-1);
        this.foregroundPaint.setStyle(Paint.Style.STROKE);
        this.foregroundPaint.setStrokeWidth(8.0f);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        smoothStartAnimation();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideBar();
    }

    public void setProgress(int progress2) {
        IntercomLogger.INTERNAL("animation", "received progress of  " + progress2);
        if (progress2 > this.progress) {
            this.progress = progress2;
            invalidate();
        }
    }

    public void hideBar() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f, 0.0f});
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = (float) ((this.progress * 360) / 100);
        canvas.drawOval(this.rectF, this.backgroundPaint);
        canvas.drawArc(this.rectF, -90.0f, angle, false, this.foregroundPaint);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int min = Math.min(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        setMeasuredDimension(min, min);
        this.rectF.set(4.0f, 4.0f, (float) (min - 4), (float) (min - 4));
    }

    private void smoothStartAnimation() {
        IntercomLogger.INTERNAL("animation", "starting upload start animation");
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", new int[]{0, 10});
        animator.setDuration(1000);
        animator.start();
    }

    public void smoothEndAnimation() {
        ObjectAnimator animator = getObjectAnimator();
        animator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                UploadProgressBar.this.hideBar();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    public void smoothEndAnimation(Animator.AnimatorListener is) {
        ObjectAnimator animator = getObjectAnimator();
        animator.addListener(is);
        animator.start();
    }

    private ObjectAnimator getObjectAnimator() {
        IntercomLogger.INTERNAL("animation", "starting upload end animation");
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", new int[]{90, 99});
        animator.setDuration(1000);
        return animator;
    }
}
