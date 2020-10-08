package com.inteliclinic.neuroon.views.charts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.old_guava.Floats;
import java.util.Vector;

public class LineChartView extends View {
    private float[] mChartPoints;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private Paint mLinePaint;
    private Paint mLineShadowPaint;
    private float mLineStroke;
    private Paint mMaxPointColor;
    private float mMaxPointX;
    private float mMaxPointY;
    private Paint mMinPointColor;
    private float mMinPointX;
    private float mMinPointY;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private float maxValue;
    private float minValue;
    private float[] values;

    public LineChartView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLinePaint = new Paint();
        this.mLineShadowPaint = new Paint();
        this.mChartPoints = new float[4];
        this.values = new float[]{0.0f};
        this.minValue = -1.0f;
        this.maxValue = -1.0f;
        this.mLineStroke = 5.0f;
        init();
    }

    @TargetApi(21)
    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mLinePaint = new Paint();
        this.mLineShadowPaint = new Paint();
        this.mChartPoints = new float[4];
        this.values = new float[]{0.0f};
        this.minValue = -1.0f;
        this.maxValue = -1.0f;
        this.mLineStroke = 5.0f;
        init();
    }

    private void init() {
        setLayerType(1, (Paint) null);
        this.mLinePaint.setColor(getResources().getColor(R.color.blue_3054FA));
        this.mLinePaint.setStrokeWidth(this.mLineStroke);
        this.mLinePaint.setStyle(Paint.Style.STROKE);
        this.mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mLinePaint.setAntiAlias(true);
        this.mLinePaint.setShadowLayer(5.0f, 0.0f, 5.0f, -3355444);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mLayoutWidth = w;
        this.mLayoutHeight = h;
        this.mPaddingTop = (int) (((float) getPaddingTop()) + (this.mLineStroke / 2.0f));
        this.mPaddingBottom = (int) (((float) getPaddingBottom()) + (this.mLineStroke / 2.0f));
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        setupChartLines();
    }

    private void setupChartLines() {
        if (this.values.length != 0) {
            this.mMinPointX = -1.0f;
            this.mMinPointY = -1.0f;
            this.mMaxPointX = -1.0f;
            this.mMaxPointY = -1.0f;
            float minVal = this.minValue;
            if (minVal == -1.0f) {
                minVal = Floats.min(this.values);
            }
            float maxVal = this.maxValue;
            if (maxVal == -1.0f) {
                maxVal = Floats.max(this.values);
            }
            float distanceX = ((float) (((this.mLayoutWidth - this.mPaddingRight) - this.mPaddingLeft) - 6)) / ((float) (this.values.length - 1));
            float distanceY = ((float) (((this.mLayoutHeight - this.mPaddingTop) - this.mPaddingBottom) - 6)) / (maxVal - minVal);
            float x = (float) this.mPaddingLeft;
            float y = (float) ((this.mLayoutHeight - this.mPaddingBottom) - 6);
            Vector<Float> tempChartValues = new Vector<>((this.values.length * 2) + ((this.values.length - 2) * 2));
            float realMin = Floats.min(this.values);
            float realMax = Floats.max(this.values);
            for (int i = 0; i < this.values.length; i++) {
                if (!(i == 0 || i == this.values.length - 1)) {
                    tempChartValues.add(Float.valueOf(x));
                    tempChartValues.add(Float.valueOf(y - ((this.values[i] - minVal) * distanceY)));
                }
                if (this.values[i] == realMin && this.mMinPointX == -1.0f) {
                    this.mMinPointX = x;
                    this.mMinPointY = y - ((this.values[i] - minVal) * distanceY);
                } else if (this.values[i] == realMax && this.mMaxPointX == -1.0f) {
                    this.mMaxPointX = x;
                    this.mMaxPointY = y - ((this.values[i] - minVal) * distanceY);
                }
                tempChartValues.add(Float.valueOf(x));
                tempChartValues.add(Float.valueOf(y - ((this.values[i] - minVal) * distanceY)));
                x += distanceX;
            }
            this.mChartPoints = Floats.toArray(tempChartValues);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!(this.mChartPoints == null || this.mChartPoints.length <= 4 || this.mLinePaint == null)) {
            canvas.drawLines(this.mChartPoints, this.mLinePaint);
        }
        if (this.mMaxPointColor != null) {
            canvas.drawCircle(this.mMaxPointX, this.mMaxPointY, 12.0f, this.mMaxPointColor);
        }
        if (this.mMinPointColor != null) {
            canvas.drawCircle(this.mMinPointX, this.mMinPointY, 12.0f, this.mMinPointColor);
        }
    }

    public void setValues(@NonNull float[] values2) {
        if (values2.length > 2) {
            this.values = values2;
        } else {
            this.values = new float[]{values2[0], (values2[0] + values2[values2.length - 1]) / 2.0f, values2[values2.length - 1]};
        }
        setupChartLines();
        invalidate();
    }

    public void setMinValue(float minValue2) {
        this.minValue = minValue2;
        setupChartLines();
        invalidate();
    }

    public void setMaxValue(float maxValue2) {
        this.maxValue = maxValue2;
        setupChartLines();
        invalidate();
    }

    public void setMaxPointColor(@ColorRes int color) {
        if (color == 0) {
            this.mMaxPointColor = null;
        } else {
            this.mMaxPointColor = new Paint();
            this.mMaxPointColor.setColor(getResources().getColor(color));
        }
        invalidate();
    }

    public void setMinPointColor(@ColorRes int color) {
        if (color == 0) {
            this.mMinPointColor = null;
        } else {
            this.mMinPointColor = new Paint();
            this.mMinPointColor.setColor(getResources().getColor(color));
        }
        invalidate();
    }
}
