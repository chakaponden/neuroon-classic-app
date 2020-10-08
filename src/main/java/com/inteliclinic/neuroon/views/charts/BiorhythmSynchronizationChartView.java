package com.inteliclinic.neuroon.views.charts;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.inteliclinic.neuroon.R;

public class BiorhythmSynchronizationChartView extends View {
    private ObjectAnimator mAnimation;
    private float mDiff;
    private float mHoursDifference;
    private Paint mPaint;
    private Path mPath;
    private int mProgress;

    public BiorhythmSynchronizationChartView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BiorhythmSynchronizationChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BiorhythmSynchronizationChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPath = new Path();
        this.mPaint = new Paint(1);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupPath(w, h);
    }

    private void setupPath(int width, int height) {
        this.mPath.reset();
        float length = (float) (height / 2);
        this.mDiff = (float) (((double) length) * 1.5d);
        float count = (((float) width) / length) + 4.0f;
        this.mPath.moveTo(0.0f, this.mDiff * 1.1f);
        this.mPath.lineTo(0.0f, this.mDiff);
        for (int i = 0; ((float) i) < count; i++) {
            float tempWidth = ((float) i) * this.mDiff;
            this.mPath.cubicTo((float) (((double) tempWidth) - (0.5d * ((double) this.mDiff))), i % 2 == 0 ? this.mDiff - length : this.mDiff, (float) (((double) tempWidth) - (0.5d * ((double) this.mDiff))), i % 2 == 0 ? this.mDiff : this.mDiff - length, tempWidth, i % 2 == 0 ? this.mDiff : this.mDiff - length);
        }
        this.mPath.lineTo(this.mDiff * count, this.mDiff * 1.1f);
    }

    private int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAnimation = ObjectAnimator.ofInt(this, "progress", new int[]{100});
        this.mAnimation.setDuration(3000);
        this.mAnimation.setInterpolator(new LinearInterpolator());
        this.mAnimation.setStartDelay(0);
        this.mAnimation.setRepeatCount(-1);
        this.mAnimation.setRepeatMode(1);
        this.mAnimation.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAnimation.cancel();
        this.mAnimation = null;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((this.mDiff * -2.0f) + ((((float) this.mProgress) / 100.0f) * 2.0f * this.mDiff), 0.0f);
        this.mPaint.setColor(getResources().getColor(R.color.jet_lag_color));
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.translate((this.mDiff * -2.0f) + (this.mDiff * (this.mHoursDifference / 24.0f)), 0.0f);
        this.mPaint.setColor(getResources().getColor(R.color.yellow_ffb400_alpha));
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public void setHoursDifference(float hoursDifference) {
        this.mHoursDifference = hoursDifference;
    }
}
