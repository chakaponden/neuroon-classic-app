package com.inteliclinic.neuroon.views.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.inteliclinic.neuroon.R;
import java.util.Calendar;
import java.util.Date;

public class PartialMonthView extends View {
    private Rect bounds;
    private int mCalendarEndDateInd;
    private int mCalendarStartDateInd;
    private int mDaysAfterMonth;
    private int mDaysBeforeMonth;
    private int mDaysInMonth;
    private int mFlightDayInt;
    private Paint mGreyTextPaint;
    private int mItemCount;
    private Calendar mMonthCalendar;
    private Paint mNormalTextPaint;
    private Paint mSelectedBoxPaint;
    private int mSelectedValues;
    private Date mStartDate;
    private int mStartDateInt;
    private int mTheme;
    private int[] mValues;

    public PartialMonthView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PartialMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PartialMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public PartialMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        this.mTheme = 1;
        doTheme();
        if (isInEditMode()) {
            setItemCount();
        }
        Typeface fromAsset = Typeface.createFromAsset(getContext().getAssets(), "fonts/Antenna Thin.otf");
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

    public void setSelectedDays(Date startDate, int selectedDays, int flightDate) {
        this.mStartDate = startDate;
        this.mFlightDayInt = flightDate;
        this.mMonthCalendar.setTime(startDate);
        int startDateDayOfWeek = this.mMonthCalendar.get(7);
        int daysFromMondayToStartDate = ((startDateDayOfWeek + 7) - 2) % 7;
        int daysToSundayFromEndDate = 6 - (((((startDateDayOfWeek + 7) - 1) + selectedDays) - 2) % 7);
        this.mStartDateInt = this.mMonthCalendar.get(5);
        this.mSelectedValues = selectedDays;
        setItemCount();
        if (this.mSelectedValues > 0) {
            this.mCalendarStartDateInd = (((this.mDaysBeforeMonth + this.mStartDateInt) - daysFromMondayToStartDate) - 7) - 1;
            if (this.mCalendarStartDateInd < 0) {
                this.mCalendarStartDateInd = 0;
            }
            this.mCalendarEndDateInd = (((this.mDaysBeforeMonth + this.mStartDateInt) + selectedDays) - 1) + daysToSundayFromEndDate + 7;
            if (this.mCalendarEndDateInd > this.mItemCount) {
                this.mCalendarEndDateInd = this.mItemCount;
            }
        } else {
            this.mCalendarStartDateInd = (((((this.mDaysBeforeMonth + this.mStartDateInt) + selectedDays) - 1) - daysFromMondayToStartDate) - 7) - 1;
            if (this.mCalendarStartDateInd < 0) {
                this.mCalendarStartDateInd = 0;
            }
            this.mCalendarEndDateInd = (((this.mDaysBeforeMonth + this.mStartDateInt) + daysToSundayFromEndDate) + 7) - 1;
            if (this.mCalendarEndDateInd > this.mItemCount) {
                this.mCalendarEndDateInd = this.mItemCount;
            }
        }
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
        setMeasuredDimension(measuredWidth, ((int) ((((double) measuredWidth) / 7.0d) * ((double) getRowsCount()))) / 2);
    }

    private int getRowsCount() {
        if (isInEditMode()) {
            return 1;
        }
        return (this.mCalendarEndDateInd - this.mCalendarStartDateInd) / 7;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0115  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0119 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(android.graphics.Canvas r19) {
        /*
            r18 = this;
            super.onDraw(r19)
            int r1 = r18.getMeasuredWidth()
            int r8 = r1 / 7
            int r1 = r18.getMeasuredHeight()
            int r2 = r18.getRowsCount()
            int r9 = r1 / r2
            r16 = 0
            r17 = r9
            r0 = r18
            int r1 = r0.mCalendarStartDateInd
            r0 = r18
            int r2 = r0.mDaysBeforeMonth
            if (r1 >= r2) goto L_0x011d
            r10 = 1
        L_0x0022:
            r0 = r18
            int r11 = r0.mCalendarStartDateInd
        L_0x0026:
            r0 = r18
            int r1 = r0.mCalendarEndDateInd
            if (r11 >= r1) goto L_0x014c
            if (r10 == 0) goto L_0x0038
            r0 = r18
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r2 = 1
            if (r1 != r2) goto L_0x0038
            r10 = 0
        L_0x0038:
            if (r10 != 0) goto L_0x0047
            r0 = r18
            int[] r1 = r0.mValues
            int r1 = r1.length
            r0 = r18
            int r2 = r0.mDaysAfterMonth
            int r1 = r1 - r2
            if (r1 != r11) goto L_0x0047
            r10 = 1
        L_0x0047:
            r0 = r18
            int[] r1 = r0.mValues
            r1 = r1[r11]
            java.lang.String r13 = java.lang.String.valueOf(r1)
            if (r10 == 0) goto L_0x0120
            r0 = r18
            android.graphics.Paint r12 = r0.mGreyTextPaint
        L_0x0057:
            r1 = 0
            int r2 = r13.length()
            r0 = r18
            android.graphics.Rect r3 = r0.bounds
            r12.getTextBounds(r13, r1, r2, r3)
            if (r10 != 0) goto L_0x0126
            r0 = r18
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r0 = r18
            boolean r1 = r0.isInSelectedDays(r1)
            if (r1 == 0) goto L_0x0126
            int r1 = r8 / 2
            int r1 = r1 + r16
            int r2 = r9 / 2
            int r1 = r1 - r2
            int r1 = r1 + 5
            float r2 = (float) r1
            int r1 = r17 - r9
            int r1 = r1 + 5
            float r3 = (float) r1
            int r1 = r8 / 2
            int r1 = r1 + r16
            int r4 = r9 / 2
            int r1 = r1 + r4
            int r1 = r1 + -5
            float r4 = (float) r1
            int r1 = r17 + -5
            float r5 = (float) r1
            r0 = r18
            android.graphics.Paint r6 = r0.mSelectedBoxPaint
            r1 = r19
            r1.drawRect(r2, r3, r4, r5, r6)
            int r7 = r12.getColor()
            r1 = -1
            r12.setColor(r1)
            int r1 = r8 / 2
            int r1 = r1 + r16
            r0 = r18
            android.graphics.Rect r2 = r0.bounds
            int r2 = r2.width()
            int r2 = r2 / 2
            int r1 = r1 - r2
            float r1 = (float) r1
            int r2 = r9 / 2
            int r2 = r17 - r2
            r0 = r18
            android.graphics.Rect r3 = r0.bounds
            int r3 = r3.height()
            int r3 = r3 / 2
            int r2 = r2 + r3
            float r2 = (float) r2
            r0 = r19
            r0.drawText(r13, r1, r2, r12)
            r0 = r18
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r0 = r18
            int r2 = r0.mFlightDayInt
            if (r1 != r2) goto L_0x010a
            android.graphics.Paint$Style r15 = r12.getStyle()
            android.graphics.Paint$Style r1 = android.graphics.Paint.Style.STROKE
            r12.setStyle(r1)
            float r14 = r12.getStrokeWidth()
            r1 = 1084227584(0x40a00000, float:5.0)
            r12.setStrokeWidth(r1)
            r1 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r12.setColor(r1)
            int r1 = r8 / 2
            int r1 = r1 + r16
            int r2 = r9 / 2
            int r1 = r1 - r2
            float r2 = (float) r1
            int r1 = r17 - r9
            float r3 = (float) r1
            int r1 = r8 / 2
            int r1 = r1 + r16
            int r4 = r9 / 2
            int r1 = r1 + r4
            float r4 = (float) r1
            r0 = r17
            float r5 = (float) r0
            r1 = r19
            r6 = r12
            r1.drawRect(r2, r3, r4, r5, r6)
            r12.setStyle(r15)
            r12.setStrokeWidth(r14)
        L_0x010a:
            r12.setColor(r7)
        L_0x010d:
            int r16 = r16 + r8
            int r1 = r11 + 1
            int r1 = r1 % 7
            if (r1 != 0) goto L_0x0119
            int r17 = r17 + r9
            r16 = 0
        L_0x0119:
            int r11 = r11 + 1
            goto L_0x0026
        L_0x011d:
            r10 = 0
            goto L_0x0022
        L_0x0120:
            r0 = r18
            android.graphics.Paint r12 = r0.mNormalTextPaint
            goto L_0x0057
        L_0x0126:
            int r1 = r8 / 2
            int r1 = r1 + r16
            r0 = r18
            android.graphics.Rect r2 = r0.bounds
            int r2 = r2.width()
            int r2 = r2 / 2
            int r1 = r1 - r2
            float r1 = (float) r1
            int r2 = r9 / 2
            int r2 = r17 - r2
            r0 = r18
            android.graphics.Rect r3 = r0.bounds
            int r3 = r3.height()
            int r3 = r3 / 2
            int r2 = r2 + r3
            float r2 = (float) r2
            r0 = r19
            r0.drawText(r13, r1, r2, r12)
            goto L_0x010d
        L_0x014c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.views.calendar.PartialMonthView.onDraw(android.graphics.Canvas):void");
    }

    private boolean isInSelectedDays(int value) {
        if (this.mSelectedValues > 0) {
            if (value < this.mStartDateInt || value > (this.mStartDateInt + this.mSelectedValues) - 1) {
                return false;
            }
            return true;
        } else if (value > this.mStartDateInt || value < this.mStartDateInt + this.mSelectedValues + 1) {
            return false;
        } else {
            return true;
        }
    }

    public Calendar getCalendar() {
        return this.mMonthCalendar;
    }

    public void setTheme(int theme) {
        this.mTheme = theme;
        doTheme();
    }
}
