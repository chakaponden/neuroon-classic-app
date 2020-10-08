package com.inteliclinic.neuroon.views.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.old_guava.Ints;
import com.inteliclinic.neuroon.utils.FontUtils;
import java.util.Calendar;
import java.util.Date;

public class MonthView extends View {
    private Rect bounds;
    private int mDaysAfterMonth;
    private int mDaysBeforeMonth;
    private int mDaysInMonth;
    private Paint mGreyTextPaint;
    private int mItemCount;
    private OnDayPickListener mListener;
    private Calendar mMonthCalendar;
    private Paint mNormalTextPaint;
    private Paint mSelectedBoxPaint;
    private int mSelectedValue;
    private int mTheme;
    private int[] mValues;

    public interface OnDayPickListener {
        boolean onDayTouch(MonthView monthView, int i);
    }

    public MonthView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mItemCount = 0;
        this.mDaysBeforeMonth = 0;
        this.mDaysAfterMonth = 0;
        this.mGreyTextPaint = new Paint(1);
        this.mNormalTextPaint = new Paint(1);
        this.mSelectedBoxPaint = new Paint(1);
        this.mValues = new int[0];
        this.bounds = new Rect();
        this.mDaysInMonth = 0;
        init();
    }

    @TargetApi(21)
    public MonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mItemCount = 0;
        this.mDaysBeforeMonth = 0;
        this.mDaysAfterMonth = 0;
        this.mGreyTextPaint = new Paint(1);
        this.mNormalTextPaint = new Paint(1);
        this.mSelectedBoxPaint = new Paint(1);
        this.mValues = new int[0];
        this.bounds = new Rect();
        this.mDaysInMonth = 0;
        init();
    }

    private void init() {
        this.mMonthCalendar = Calendar.getInstance();
        this.mSelectedValue = 3;
        this.mTheme = 0;
        doTheme();
        if (isInEditMode()) {
            setItemCount();
        }
        Typeface fromAsset = FontUtils.getFont(getContext(), 2);
        this.mNormalTextPaint.setTypeface(fromAsset);
        int i = (int) (12.0f * getResources().getDisplayMetrics().density);
        this.mNormalTextPaint.setTextSize((float) i);
        this.mGreyTextPaint.setTypeface(fromAsset);
        this.mGreyTextPaint.setTextSize((float) i);
        this.mGreyTextPaint.setColor(getResources().getColor(R.color.grey_8e));
        this.mSelectedBoxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void doTheme() {
        if (this.mTheme == 0) {
            this.mNormalTextPaint.setColor(getResources().getColor(17170444));
            this.mSelectedBoxPaint.setColor(getResources().getColor(R.color.blue_3054FA));
        } else if (this.mTheme == 1) {
            this.mNormalTextPaint.setColor(getResources().getColor(17170444));
            this.mSelectedBoxPaint.setColor(getResources().getColor(R.color.jet_lag_color));
        }
    }

    public void setItemCount() {
        this.mMonthCalendar.set(5, 1);
        int firstDayWeekDay = this.mMonthCalendar.get(7);
        this.mDaysInMonth = this.mMonthCalendar.getActualMaximum(5);
        this.mMonthCalendar.set(5, this.mDaysInMonth);
        int j = this.mMonthCalendar.get(7);
        this.mDaysBeforeMonth = ((firstDayWeekDay + 7) - 2) % 7;
        this.mDaysAfterMonth = 6 - (((j + 7) - 2) % 7);
        this.mItemCount = this.mDaysInMonth + this.mDaysBeforeMonth + this.mDaysAfterMonth;
    }

    public void setMonth(Date monthDate, int selectedDay) {
        this.mMonthCalendar.setTime(monthDate);
        this.mSelectedValue = selectedDay;
        setItemCount();
        this.mMonthCalendar.set(5, 1 - this.mDaysBeforeMonth);
        int day = this.mMonthCalendar.get(5);
        this.mMonthCalendar.add(5, this.mDaysBeforeMonth + 1);
        if (this.mItemCount != this.mValues.length) {
            this.mValues = new int[this.mItemCount];
        }
        int i = 0;
        while (i < this.mDaysBeforeMonth) {
            this.mValues[i] = day;
            i++;
            day++;
        }
        for (int i2 = 1; i2 <= this.mDaysInMonth; i2++) {
            this.mValues[(this.mDaysBeforeMonth + i2) - 1] = i2;
        }
        for (int i3 = 0; i3 < this.mDaysAfterMonth; i3++) {
            this.mValues[this.mDaysBeforeMonth + this.mDaysInMonth + i3] = i3 + 1;
        }
        requestLayout();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, ((int) ((((double) measuredWidth) / 7.0d) * ((double) (this.mItemCount / 7)))) / 2);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int itemNumber;
        if (event.getAction() == 0) {
            return true;
        }
        if (event.getAction() == 1 && (itemNumber = ((int) (event.getX() / ((float) (getMeasuredWidth() / 7)))) + (((int) (event.getY() / ((float) (getMeasuredHeight() / (this.mItemCount / 7))))) * 7)) > this.mDaysBeforeMonth - 1 && itemNumber < this.mDaysBeforeMonth + this.mDaysInMonth) {
            performClick(this.mValues[itemNumber]);
        }
        return super.onTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int distanceX = getMeasuredWidth() / 7;
        int distanceY = getMeasuredHeight() / (this.mItemCount / 7);
        int x = 0;
        int y = distanceY;
        boolean greyed = true;
        for (int i = 0; i < this.mValues.length; i++) {
            if (greyed && this.mValues[i] == 1) {
                greyed = false;
            }
            if (!greyed && this.mValues.length - this.mDaysAfterMonth == i) {
                greyed = true;
            }
            String s = String.valueOf(this.mValues[i]);
            Paint p = greyed ? this.mGreyTextPaint : this.mNormalTextPaint;
            p.getTextBounds(s, 0, s.length(), this.bounds);
            if (greyed || this.mValues[i] != this.mSelectedValue) {
                canvas.drawText(s, (float) (((distanceX / 2) + x) - (this.bounds.width() / 2)), (float) ((y - (distanceY / 2)) + (this.bounds.height() / 2)), p);
            } else {
                canvas.drawRect((float) (((distanceX / 2) + x) - (distanceY / 2)), (float) (y - distanceY), (float) ((distanceX / 2) + x + (distanceY / 2)), (float) y, this.mSelectedBoxPaint);
                int color = p.getColor();
                p.setColor(getResources().getColor(17170443));
                canvas.drawText(s, (float) (((distanceX / 2) + x) - (this.bounds.width() / 2)), (float) ((y - (distanceY / 2)) + (this.bounds.height() / 2)), p);
                p.setColor(color);
            }
            x += distanceX;
            if ((i + 1) % 7 == 0) {
                y += distanceY;
                x = 0;
            }
        }
    }

    public void setListener(OnDayPickListener listener) {
        this.mListener = listener;
    }

    public Calendar getCalendar() {
        return this.mMonthCalendar;
    }

    public void setTheme(int theme) {
        this.mTheme = theme;
        doTheme();
    }

    public void performClick(int value) {
        if (this.mListener != null && this.mListener.onDayTouch(this, value)) {
            this.mSelectedValue = value;
        }
        invalidate();
    }

    public float[] getCoordinatesOtItem(int value) {
        int i = Ints.indexOf(this.mValues, value);
        int i1 = Ints.lastIndexOf(this.mValues, value);
        if (i == i1) {
            return getCoordinatesOtItemByIndex(i);
        }
        if (value > 15) {
            return getCoordinatesOtItemByIndex(i1);
        }
        return getCoordinatesOtItemByIndex(i);
    }

    private float[] getCoordinatesOtItemByIndex(int i) {
        return new float[]{(float) (i % 7), (float) ((i + 1) / 7)};
    }
}
