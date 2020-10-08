package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.R;

public class SleepScoreView extends View {
    private int mBarWidth = 55;
    private RectF mCircleBounds;
    private Paint mCirclePaint = new Paint();
    private int mEndColor = ViewCompat.MEASURED_STATE_MASK;
    private Paint mEndOfCirclePaint = new Paint();
    private Paint mEndOfCircleWhitePaint = new Paint();
    private RectF mInnerCircleBounds;
    private Paint mInnerCirclePaint = new Paint();
    private int mLayoutHeight;
    private int mLayoutWidth;
    private Bitmap mMaskBitmap;
    private Rect mMaskRectDest;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mProgressMax = 100;
    private int mProgressValue = 0;
    private float mShadowBarWidth = ((float) (this.mBarWidth + 50));
    private Paint mShadowCirclePaint = new Paint();
    private int mStartColor = -1;

    public SleepScoreView(Context context) {
        super(context);
        init();
    }

    public SleepScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SleepScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mMaskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maska_high_cropped);
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
        this.mEndOfCirclePaint.setStrokeWidth((float) this.mBarWidth);
        this.mEndOfCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mEndOfCirclePaint.setColor(this.mEndColor);
        this.mEndOfCirclePaint.setAntiAlias(true);
        this.mEndOfCirclePaint.setStyle(Paint.Style.STROKE);
        this.mEndOfCircleWhitePaint.setStrokeWidth((float) this.mBarWidth);
        this.mEndOfCircleWhitePaint.setStrokeCap(Paint.Cap.BUTT);
        this.mEndOfCircleWhitePaint.setColor(this.mStartColor);
        this.mEndOfCircleWhitePaint.setAntiAlias(true);
        this.mEndOfCircleWhitePaint.setStyle(Paint.Style.STROKE);
        this.mInnerCirclePaint.setStrokeWidth((float) (this.mBarWidth / 2));
        this.mInnerCirclePaint.setAntiAlias(true);
        this.mInnerCirclePaint.setStrokeCap(Paint.Cap.BUTT);
        this.mInnerCirclePaint.setStyle(Paint.Style.STROKE);
        int[] colors = {this.mStartColor, this.mEndColor, this.mStartColor};
        this.mInnerCirclePaint.setShader(new SweepGradient((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), colors, new float[]{0.0f, 0.5f, 1.0f}));
        this.mShadowCirclePaint.setStrokeWidth(this.mShadowBarWidth);
        this.mShadowCirclePaint.setAntiAlias(true);
        this.mShadowCirclePaint.setStrokeCap(Paint.Cap.BUTT);
        this.mShadowCirclePaint.setStyle(Paint.Style.STROKE);
        this.mCirclePaint.setStrokeWidth((float) this.mBarWidth);
        this.mCirclePaint.setAntiAlias(true);
        this.mCirclePaint.setStrokeCap(Paint.Cap.BUTT);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        setupShadowShader();
        setupCircleShaders();
    }

    private void setupCircleShaders() {
        this.mCirclePaint.setShader(new SweepGradient((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), new int[]{this.mStartColor, this.mEndColor, this.mStartColor}, new float[]{0.0f, getCalculatedProgress(), 1.0f}));
    }

    private void setupShadowShader() {
        int[] shadowColors = {Color.parseColor("#FFFFFF"), Color.parseColor("#74AEFF"), Color.parseColor("#2559FF"), Color.parseColor("#6F6FFF"), Color.parseColor("#4D66FF"), Color.parseColor("#573BCD"), Color.parseColor("#364AED"), Color.parseColor("#614AED"), Color.parseColor("#3963FF"), Color.parseColor("#74AEFF"), Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")};
        float[] shadowColorPositions = new float[shadowColors.length];
        shadowColorPositions[0] = 0.0f;
        for (int i = 1; i < shadowColors.length - 1; i++) {
            shadowColorPositions[i] = ((float) i) / ((float) (shadowColors.length - 2));
        }
        shadowColorPositions[shadowColors.length - 1] = 1.0f;
        this.mShadowCirclePaint.setShader(new SweepGradient((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), shadowColors, shadowColorPositions));
    }

    public int getProgress() {
        return this.mProgressValue;
    }

    public void setProgress(int value) {
        this.mProgressValue = value;
        setupCircleShaders();
        invalidate();
    }

    public int getProgressMax() {
        return this.mProgressMax;
    }

    public void setProgressMax(int progressMax) {
        this.mProgressMax = progressMax;
        setupCircleShaders();
        invalidate();
    }

    private float getCalculatedProgress() {
        if (((float) this.mProgressValue) / ((float) this.mProgressMax) <= 1.0f) {
            return ((float) this.mProgressValue) / ((float) this.mProgressMax);
        }
        return 1.0f;
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
        this.mMaskRectDest = new Rect(0, 0, width - 1, height - 1);
        this.mBarWidth = (int) (((double) width) * 0.09d);
        this.mShadowBarWidth = (float) ((int) (1.5d * ((double) this.mBarWidth)));
        int i = (int) (((float) width) * 0.04f);
        int j = (int) (((float) width) * 0.018f);
        this.mCircleBounds = new RectF((float) (this.mPaddingLeft + i + this.mBarWidth), (float) (this.mPaddingTop + this.mBarWidth + i), (float) (((width - this.mPaddingRight) - this.mBarWidth) - i), (float) (((height - this.mPaddingBottom) - this.mBarWidth) - i));
        this.mInnerCircleBounds = new RectF(((float) (this.mPaddingLeft + j)) + (((float) this.mBarWidth) * 1.5f), ((float) (this.mPaddingTop + j)) + (((float) this.mBarWidth) * 1.5f), ((float) ((width - this.mPaddingRight) - j)) - (((float) this.mBarWidth) * 1.5f), ((float) ((height - this.mPaddingBottom) - j)) - (((float) this.mBarWidth) * 1.5f));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(-90.0f, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        canvas.drawArc(this.mCircleBounds, 0.0f, getCalculatedProgress() * 360.0f, false, this.mShadowCirclePaint);
        if (this.mMaskBitmap != null) {
            canvas.drawBitmap(this.mMaskBitmap, (Rect) null, this.mMaskRectDest, (Paint) null);
        }
        canvas.drawArc(this.mCircleBounds, getCalculatedProgress() * 360.0f, 0.1f, false, this.mEndOfCirclePaint);
        if (((double) getCalculatedProgress()) < 0.5d) {
            canvas.drawArc(this.mCircleBounds, -20.0f, 20.0f, false, this.mEndOfCircleWhitePaint);
        }
        canvas.drawArc(this.mCircleBounds, 0.0f, getCalculatedProgress() * 360.0f, false, this.mCirclePaint);
        canvas.rotate(-45.0f, (float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2));
        canvas.drawArc(this.mInnerCircleBounds, 0.0f, 360.0f, false, this.mInnerCirclePaint);
    }
}
