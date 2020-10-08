package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.models.data.Event;

public class TherapyProgressView extends View {
    private static final int[] VIOLET_COLORS = {-1905921, -4939777, -2563585, -5134593, -1777153, -4281857, -3356161, -3490817, -3159297, -1576705, -3889921, -2700033, -4741124, -1516547, -2766337, -1905921};
    private static final float[] VIOLET_POSITIONS = {0.0f, 0.066f, 0.133f, 0.2f, 0.266f, 0.333f, 0.4f, 0.466f, 0.533f, 0.6f, 0.666f, 0.733f, 0.8f, 0.866f, 0.933f, 1.0f};
    private static final int[] YELLOW_COLORS = {-87, -11165, -5453, -10632, -112, -269428, -6281, -4142, -9139, -7323, -11948, -7544, -87};
    private static final float[] YELLOW_POSITIONS = {0.0f, 0.083f, 0.166f, 0.25f, 0.333f, 0.416f, 0.5f, 0.58f, 0.666f, 0.75f, 0.833f, 0.916f, 1.0f};
    private int mBarWidth = 0;
    private RectF mCircleBounds;
    private Paint mCirclePaint = new Paint();
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mProgressMax = 360;
    private int mProgressValue = 0;
    private int mShadowAngle;
    private float mShadowBarWidth = 0.0f;
    private Paint mShadowCirclePaint = new Paint();
    private Event.EventType mType;

    public TherapyProgressView(Context context) {
        super(context);
        init();
    }

    public TherapyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TherapyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, (Paint) null);
        }
        setProgress(320);
        this.mType = Event.EventType.ETBLT;
    }

    public void setType(Event.EventType type) {
        this.mType = type;
        invalidate();
    }

    private void setShadowProgress(int value) {
        this.mShadowAngle = value;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = (width - getPaddingLeft()) - getPaddingRight();
        int heightWithoutPadding = (height - getPaddingTop()) - getPaddingBottom();
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
        setupBounds();
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        this.mShadowCirclePaint.setStrokeWidth(this.mShadowBarWidth);
        this.mShadowCirclePaint.setAntiAlias(true);
        this.mShadowCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mShadowCirclePaint.setStyle(Paint.Style.STROKE);
        this.mShadowCirclePaint.setMaskFilter(new BlurMaskFilter(50.0f, BlurMaskFilter.Blur.NORMAL));
        this.mCirclePaint.setColor(-1);
        this.mCirclePaint.setStrokeWidth((float) this.mBarWidth);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        setupShadowShader();
    }

    private void setupShadowShader() {
        switch (this.mType) {
            case ETBLT:
                this.mShadowCirclePaint.setShader(new SweepGradient((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), YELLOW_COLORS, YELLOW_POSITIONS));
                return;
            case ETNapBody:
            case ETNapPower:
            case ETNapUltimate:
            case ETNapRem:
                this.mShadowCirclePaint.setShader(new SweepGradient((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), VIOLET_COLORS, VIOLET_POSITIONS));
                return;
            default:
                return;
        }
    }

    public int getProgress() {
        return this.mProgressValue;
    }

    public void setProgress(int value) {
        this.mProgressValue = value;
        invalidate();
    }

    public int getProgressMax() {
        return this.mProgressMax;
    }

    public void setProgressMax(int progressMax) {
        this.mProgressMax = progressMax;
        invalidate();
    }

    private float getCalculatedProgress() {
        return ((float) this.mProgressValue) / ((float) this.mProgressMax);
    }

    private void setupBounds() {
        int minValue = Math.min(this.mLayoutWidth, this.mLayoutHeight);
        int xOffset = this.mLayoutWidth - minValue;
        int yOffset = this.mLayoutHeight - minValue;
        this.mPaddingTop = getPaddingTop() + (yOffset / 2);
        this.mPaddingBottom = getPaddingBottom() + (yOffset / 2);
        this.mPaddingLeft = getPaddingLeft() + (xOffset / 2);
        this.mPaddingRight = getPaddingRight() + (xOffset / 2);
        int width = getWidth();
        int height = getHeight();
        this.mBarWidth = (int) (((double) width) * 0.09d);
        this.mShadowBarWidth = (float) ((int) (1.3d * ((double) this.mBarWidth)));
        this.mCircleBounds = new RectF((float) (this.mPaddingLeft + this.mBarWidth), (float) (this.mPaddingTop + this.mBarWidth), (float) ((width - this.mPaddingRight) - this.mBarWidth), (float) ((height - this.mPaddingBottom) - this.mBarWidth));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(-90.0f, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        canvas.drawArc(this.mCircleBounds, 0.0f, 360.0f * (-getCalculatedProgress()), false, this.mCirclePaint);
    }
}
