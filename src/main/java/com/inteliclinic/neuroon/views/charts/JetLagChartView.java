package com.inteliclinic.neuroon.views.charts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.R;

public class JetLagChartView extends View {
    private double mBackgroundDiff;
    private Bitmap mBitmapCompletion;
    private RectF mCircleBounds;
    private Paint mCirclePaint;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mLineStroke;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private float mProgress;

    public JetLagChartView(Context context) {
        this(context, (AttributeSet) null);
    }

    public JetLagChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JetLagChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLineStroke = 20;
        this.mCirclePaint = new Paint(1);
        this.mBackgroundDiff = 0.055d;
        init();
    }

    @TargetApi(21)
    public JetLagChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mLineStroke = 20;
        this.mCirclePaint = new Paint(1);
        this.mBackgroundDiff = 0.055d;
        init();
    }

    private void init() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        this.mBitmapCompletion = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.jetlag_completion_circle, options);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthWithoutPadding = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        int heightWithoutPadding = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        setMeasuredDimension(getPaddingLeft() + size + getPaddingRight(), getPaddingTop() + size + getPaddingBottom());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mLayoutWidth = w;
        this.mLayoutHeight = h;
        this.mPaddingTop = getPaddingTop() + (this.mLineStroke / 2);
        this.mPaddingBottom = getPaddingBottom() + (this.mLineStroke / 2);
        this.mPaddingLeft = getPaddingLeft() + (this.mLineStroke / 2);
        this.mPaddingRight = getPaddingRight() + (this.mLineStroke / 2);
        setupBounds();
    }

    private void setupBounds() {
        this.mCircleBounds = new RectF((float) (((double) this.mPaddingLeft) + (this.mBackgroundDiff * ((double) this.mLayoutWidth))), (float) (((double) this.mPaddingTop) + (this.mBackgroundDiff * ((double) this.mLayoutHeight))), (float) (((double) (this.mLayoutWidth - this.mPaddingRight)) - (this.mBackgroundDiff * ((double) this.mLayoutWidth))), (float) (((double) (this.mLayoutHeight - this.mPaddingBottom)) - (this.mBackgroundDiff * ((double) this.mLayoutHeight))));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.mBitmapCompletion, (Rect) null, this.mCircleBounds, this.mCirclePaint);
        canvas.drawArc(this.mCircleBounds, -90.0f, (1.0f - this.mProgress) * -360.0f, true, this.mCirclePaint);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
    }
}
