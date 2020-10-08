package com.inteliclinic.neuroon.views.charts;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ArcUtils;
import java.util.ArrayList;
import java.util.List;

public class CircleChartView extends View {
    private int mAnimate;
    private RectF mCircleBounds;
    private PointF mCircleCenter;
    private Bitmap mInnerCircleBitmap;
    private RectF mInnerCircleBounds;
    private Paint mInnerCirclePaint;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mLineStroke;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int[] mValues;
    private int[] mValuesColors;
    private List<Pair<Float, Paint>> mValuesPaints;

    public CircleChartView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CircleChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mValuesPaints = new ArrayList();
        this.mInnerCirclePaint = new Paint();
        init();
    }

    @TargetApi(21)
    public CircleChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mValuesPaints = new ArrayList();
        this.mInnerCirclePaint = new Paint();
        init();
    }

    private void init() {
        setLayerType(1, (Paint) null);
        this.mInnerCircleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kolko_wykres_fazy_snu);
        this.mInnerCirclePaint.setAntiAlias(true);
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
        this.mLineStroke = (int) (0.1d * ((double) w));
        this.mPaddingTop = getPaddingTop() + (this.mLineStroke / 2);
        this.mPaddingBottom = getPaddingBottom() + (this.mLineStroke / 2);
        this.mPaddingLeft = getPaddingLeft() + (this.mLineStroke / 2);
        this.mPaddingRight = getPaddingRight() + (this.mLineStroke / 2);
        setupChartLines();
        setupBounds();
    }

    private void setupBounds() {
        this.mCircleBounds = new RectF((float) this.mPaddingLeft, (float) this.mPaddingTop, (float) (this.mLayoutWidth - this.mPaddingRight), (float) (this.mLayoutHeight - this.mPaddingBottom));
        this.mCircleCenter = new PointF(this.mCircleBounds.centerX(), this.mCircleBounds.centerY());
        this.mInnerCircleBounds = new RectF((float) (this.mPaddingLeft + (this.mLineStroke / 2)), (float) (this.mPaddingTop + (this.mLineStroke / 2)), (float) ((this.mLayoutWidth - this.mPaddingRight) - (this.mLineStroke / 2)), (float) ((this.mLayoutHeight - this.mPaddingBottom) - (this.mLineStroke / 2)));
    }

    private void setupChartLines() {
        this.mValuesPaints.clear();
        if (this.mValues != null && this.mValues.length != 0 && this.mValuesColors != null && this.mValuesColors.length == this.mValues.length) {
            int length = this.mValues.length;
            int max = getMax();
            for (int i = 0; i < length; i++) {
                this.mValuesPaints.add(new Pair(Float.valueOf((float) ((((double) this.mValues[i]) * 360.0d) / ((double) max))), createPaint(this.mValuesColors[i])));
            }
        }
    }

    private int getMax() {
        int max = 0;
        for (int mValue : this.mValues) {
            max += mValue;
        }
        return max;
    }

    private Paint createPaint(int color) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth((float) this.mLineStroke);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeCap(Paint.Cap.BUTT);
        p.setAntiAlias(true);
        return p;
    }

    public void setValues(int[] values, int[] colors) {
        this.mValues = values;
        this.mValuesColors = colors;
        this.mAnimate = 100;
        setupChartLines();
        invalidate();
    }

    public void setAnimateValues(int[] values, int[] colors) {
        this.mValues = values;
        this.mValuesColors = colors;
        this.mAnimate = 0;
        setupChartLines();
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "animate", new int[]{100});
        animator.setStartDelay(300);
        animator.setDuration(600);
        animator.start();
    }

    public int getAnimate() {
        return this.mAnimate;
    }

    public void setAnimate(int animate) {
        this.mAnimate = animate;
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = -90.0f;
        for (Pair<Float, Paint> point : this.mValuesPaints) {
            float v = (float) (((double) ((Float) point.first).floatValue()) * (((double) this.mAnimate) / 100.0d));
            ArcUtils.drawArc(canvas, this.mCircleCenter, (this.mCircleBounds.centerX() - ((float) (this.mLineStroke / 2))) - 1.0f, angle, v, (Paint) point.second);
            angle += v;
        }
        canvas.drawBitmap(this.mInnerCircleBitmap, (Rect) null, this.mInnerCircleBounds, this.mInnerCirclePaint);
    }
}
