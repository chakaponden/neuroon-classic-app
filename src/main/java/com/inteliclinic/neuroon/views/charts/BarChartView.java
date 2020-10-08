package com.inteliclinic.neuroon.views.charts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.ActivityChooserView;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int mDistanceX;
    private float mDistanceXPixels;
    private int mDistanceY;
    private float mDistanceYPixels;
    private boolean mInvalidatePaints;
    private boolean mIsInvalidBitmap;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private OnBarChartTouchListener mListener;
    private Integer mMaxDefinedY;
    private int mMaxY;
    private Integer mMinDefinedY;
    private int mMinY;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private List<Pair<Integer, Integer>> mValues;
    private int[] mValuesColors;
    private Paint[] mValuesPaints;

    public interface OnBarChartTouchListener {
        void onItemTouch(int i, Rect rect, float f, float f2);
    }

    public BarChartView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mValuesPaints = new Paint[0];
        this.mValues = new ArrayList();
        init();
    }

    @TargetApi(21)
    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mValuesPaints = new Paint[0];
        this.mValues = new ArrayList();
        init();
    }

    private void init() {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mLayoutWidth = w;
        this.mLayoutHeight = h;
        this.mPaddingTop = getPaddingTop();
        this.mPaddingBottom = getPaddingBottom();
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        setupChartLines();
    }

    private void setupChartLines() {
        if (this.mValues != null && this.mValues.size() != 0 && this.mValuesColors != null && this.mValuesColors.length == this.mValues.size()) {
            if (getMinX() < 0) {
                throw new UnsupportedOperationException("X values should be positive");
            }
            this.mIsInvalidBitmap = true;
            int length = this.mValues.size();
            this.mDistanceXPixels = ((float) this.mLayoutWidth) / ((float) getSumX());
            if (this.mMaxDefinedY != null) {
                this.mMaxY = this.mMaxDefinedY.intValue();
            } else {
                this.mMaxY = getMaxY();
            }
            if (this.mMinDefinedY != null) {
                this.mMinY = this.mMinDefinedY.intValue();
            } else {
                this.mMinY = getMinY();
            }
            if (this.mMinY > 0) {
                this.mMinY = 0;
            }
            this.mDistanceY = this.mMaxY - this.mMinY;
            if (this.mDistanceY != 0) {
                this.mDistanceYPixels = (float) (this.mLayoutHeight / this.mDistanceY);
            } else {
                this.mDistanceYPixels = (float) (this.mLayoutHeight / 4);
            }
            if (this.mValuesPaints.length != length || this.mInvalidatePaints) {
                this.mInvalidatePaints = false;
                if (this.mValuesPaints.length != length) {
                    this.mValuesPaints = new Paint[length];
                }
                for (int i = 0; i < length; i++) {
                    this.mValuesPaints[i] = createPaint(this.mValuesColors[i]);
                }
            }
        }
    }

    private int getSumX() {
        int sum = 0;
        for (Pair<Integer, Integer> mValue : this.mValues) {
            sum += ((Integer) mValue.first).intValue();
        }
        return sum;
    }

    private int getMinX() {
        int min = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        for (Pair<Integer, Integer> mValue : this.mValues) {
            if (((Integer) mValue.first).intValue() < min) {
                min = ((Integer) mValue.first).intValue();
            }
        }
        return min;
    }

    private int getMaxX() {
        int max = Integer.MIN_VALUE;
        for (Pair<Integer, Integer> mValue : this.mValues) {
            if (((Integer) mValue.first).intValue() > max) {
                max = ((Integer) mValue.first).intValue();
            }
        }
        return max;
    }

    private int getMinY() {
        int min = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        for (Pair<Integer, Integer> mValue : this.mValues) {
            if (((Integer) mValue.second).intValue() < min) {
                min = ((Integer) mValue.second).intValue();
            }
        }
        return min;
    }

    private int getMaxY() {
        int max = Integer.MIN_VALUE;
        for (Pair<Integer, Integer> mValue : this.mValues) {
            if (((Integer) mValue.second).intValue() > max) {
                max = ((Integer) mValue.second).intValue();
            }
        }
        return max;
    }

    private Paint createPaint(int color) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeCap(Paint.Cap.BUTT);
        p.setAntiAlias(true);
        return p;
    }

    public void setValues(List<Pair<Integer, Integer>> values, int[] colors) {
        this.mValues = values;
        this.mValuesColors = colors;
        this.mInvalidatePaints = true;
        setupChartLines();
        invalidate();
    }

    public void setAnimateValues(List<Pair<Integer, Integer>> values, int[] colors) {
        this.mValues = values;
        this.mValuesColors = colors;
        this.mInvalidatePaints = true;
        setupChartLines();
        Animation animator = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f);
        animator.setStartOffset(300);
        animator.setDuration(600);
        startAnimation(animator);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case 1:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (handleRealTouchEvent(event)) {
                    return true;
                }
                break;
            case 2:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (handleRealTouchEvent(event)) {
                    return true;
                }
                break;
            case 3:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean handleRealTouchEvent(MotionEvent event) {
        if (this.mListener == null) {
            return false;
        }
        int x = (int) Math.ceil((double) (event.getX() / this.mDistanceXPixels));
        int maxY = ((int) Math.ceil((double) ((((float) this.mLayoutHeight) - event.getY()) / this.mDistanceYPixels))) + this.mMinY;
        int minY = ((int) Math.floor((double) ((((float) this.mLayoutHeight) - event.getY()) / this.mDistanceYPixels))) + this.mMinY;
        int i = 0;
        int sum = 0;
        for (Pair<Integer, Integer> value : this.mValues) {
            if (((Integer) value.first).intValue() + sum >= x) {
                int[] location = new int[2];
                getLocationOnScreen(location);
                if (((Integer) value.second).intValue() > 0 && ((Integer) value.second).intValue() == maxY) {
                    this.mListener.onItemTouch(i, new Rect((int) (((float) location[0]) + (((float) sum) * this.mDistanceXPixels)), (int) (((((float) this.mMinY) * this.mDistanceYPixels) + ((float) (location[1] + this.mLayoutHeight))) - (((float) ((Integer) value.second).intValue()) * this.mDistanceYPixels)), (int) ((((float) (((Integer) value.first).intValue() + sum)) * this.mDistanceXPixels) + ((float) location[0])), (int) ((((((float) this.mMinY) * this.mDistanceYPixels) + ((float) (location[1] + this.mLayoutHeight))) - (((float) ((Integer) value.second).intValue()) * this.mDistanceYPixels)) + this.mDistanceYPixels)), event.getRawX(), event.getRawY());
                    return true;
                } else if (((Integer) value.second).intValue() >= 0 || ((Integer) value.second).intValue() != minY) {
                    this.mListener.onItemTouch(-1, (Rect) null, event.getRawX(), event.getRawY());
                    return true;
                } else {
                    this.mListener.onItemTouch(i, new Rect((int) (((float) location[0]) + (((float) sum) * this.mDistanceXPixels)), (int) ((((((float) this.mMinY) * this.mDistanceYPixels) + ((float) (location[1] + this.mLayoutHeight))) - (((float) ((Integer) value.second).intValue()) * this.mDistanceYPixels)) - this.mDistanceYPixels), (int) ((((float) (((Integer) value.first).intValue() + sum)) * this.mDistanceXPixels) + ((float) location[0])), (int) (((((float) this.mMinY) * this.mDistanceYPixels) + ((float) (location[1] + this.mLayoutHeight))) - (((float) ((Integer) value.second).intValue()) * this.mDistanceYPixels))), event.getRawX(), event.getRawY());
                    return true;
                }
            } else {
                sum += ((Integer) value.first).intValue();
                i++;
            }
        }
        this.mListener.onItemTouch(-1, (Rect) null, event.getRawX(), event.getRawY());
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBitmap == null) {
            this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            this.mCanvas = new Canvas(this.mBitmap);
        }
        if (this.mIsInvalidBitmap) {
            this.mIsInvalidBitmap = false;
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            float distance = 0.0f;
            float zeroYPoint = ((float) this.mLayoutHeight) + (((float) this.mMinY) * this.mDistanceYPixels);
            int i = 0;
            for (Pair<Integer, Integer> value : this.mValues) {
                float width = this.mDistanceXPixels * ((float) ((Integer) value.first).intValue());
                float height = ((float) ((Integer) value.second).intValue()) * this.mDistanceYPixels;
                if (height < 0.0f) {
                    this.mCanvas.drawRect(distance, (zeroYPoint - height) - this.mDistanceYPixels, distance + width, zeroYPoint - height, this.mValuesPaints[i]);
                } else {
                    this.mCanvas.drawRect(distance, zeroYPoint - height, distance + width, (zeroYPoint - height) + this.mDistanceYPixels, this.mValuesPaints[i]);
                }
                distance += width;
                i++;
            }
        }
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
    }

    public void setMaxYValue(int maxValue) {
        this.mMaxDefinedY = Integer.valueOf(maxValue);
    }

    public void setMinYValue(int minYValue) {
        this.mMinDefinedY = Integer.valueOf(minYValue);
    }

    public void setListener(OnBarChartTouchListener listener) {
        this.mListener = listener;
    }

    public void alignWidthTo(BarChartView barChart) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        getLocationOnScreen(location1);
        barChart.getLocationOnScreen(location2);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.leftMargin += location2[0] - location1[0];
        setLayoutParams(layoutParams);
    }
}
