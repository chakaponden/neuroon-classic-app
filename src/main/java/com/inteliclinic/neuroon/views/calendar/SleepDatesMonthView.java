package com.inteliclinic.neuroon.views.calendar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.old_guava.Ints;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.utils.FontUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SleepDatesMonthView extends View {
    private Rect bounds;
    private int mDaysAfterMonth;
    private int mDaysBeforeMonth;
    private int mDaysInMonth;
    private int[] mEnabledDays;
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
        boolean onDayTouch(SleepDatesMonthView sleepDatesMonthView, int i);
    }

    public SleepDatesMonthView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SleepDatesMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepDatesMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private void init() {
        this.mMonthCalendar = Calendar.getInstance();
        this.mSelectedValue = 3;
        this.mTheme = 0;
        doTheme();
        if (isInEditMode()) {
            setItemCount();
        }
        this.mNormalTextPaint.setTypeface(FontUtils.getFont(getContext(), 0));
        int i = (int) (12.0f * getResources().getDisplayMetrics().density);
        this.mNormalTextPaint.setTextSize((float) i);
        this.mGreyTextPaint.setTypeface(FontUtils.getFont(getContext(), 1));
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
        this.mEnabledDays = getEnabledDays(DataManager.getInstance().getSleepsFromMonth(this.mMonthCalendar.getTime()));
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

    private int[] getEnabledDays(List<Sleep> sleeps) {
        List<Integer> days = new ArrayList<>();
        for (Sleep sleep : sleeps) {
            if (sleep.getSleepType() == 1) {
                days.add(Integer.valueOf(DateUtils.getDayOfMonth(sleep.getStartDate())));
            }
        }
        return Ints.toArray((Collection<Integer>) days);
    }

    private boolean isEnabledDay(int value) {
        for (int mEnabledDay : this.mEnabledDays) {
            if (mEnabledDay == value) {
                return true;
            }
        }
        return false;
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
        if (event.getAction() == 1 && (itemNumber = ((int) (event.getX() / ((float) (getMeasuredWidth() / 7)))) + (((int) (event.getY() / ((float) (getMeasuredHeight() / (this.mItemCount / 7))))) * 7)) > this.mDaysBeforeMonth - 1 && itemNumber < this.mDaysBeforeMonth + this.mDaysInMonth && isEnabledDay(this.mValues[itemNumber])) {
            performClick(this.mValues[itemNumber]);
        }
        return super.onTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d0 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDraw(android.graphics.Canvas r17) {
        /*
            r16 = this;
            super.onDraw(r17)
            int r1 = r16.getMeasuredWidth()
            int r8 = r1 / 7
            int r1 = r16.getMeasuredHeight()
            r0 = r16
            int r2 = r0.mItemCount
            int r2 = r2 / 7
            int r9 = r1 / r2
            r14 = 0
            r15 = r9
            r10 = 1
            r11 = 0
        L_0x0019:
            r0 = r16
            int[] r1 = r0.mValues
            int r1 = r1.length
            if (r11 >= r1) goto L_0x00fe
            if (r10 == 0) goto L_0x002c
            r0 = r16
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r2 = 1
            if (r1 != r2) goto L_0x002c
            r10 = 0
        L_0x002c:
            if (r10 != 0) goto L_0x003b
            r0 = r16
            int[] r1 = r0.mValues
            int r1 = r1.length
            r0 = r16
            int r2 = r0.mDaysAfterMonth
            int r1 = r1 - r2
            if (r1 != r11) goto L_0x003b
            r10 = 1
        L_0x003b:
            r0 = r16
            int[] r1 = r0.mValues
            r1 = r1[r11]
            java.lang.String r13 = java.lang.String.valueOf(r1)
            if (r10 != 0) goto L_0x0055
            r0 = r16
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r0 = r16
            boolean r1 = r0.isEnabledDay(r1)
            if (r1 != 0) goto L_0x00d4
        L_0x0055:
            r0 = r16
            android.graphics.Paint r12 = r0.mGreyTextPaint
        L_0x0059:
            r1 = 0
            int r2 = r13.length()
            r0 = r16
            android.graphics.Rect r3 = r0.bounds
            r12.getTextBounds(r13, r1, r2, r3)
            if (r10 != 0) goto L_0x00d9
            r0 = r16
            int[] r1 = r0.mValues
            r1 = r1[r11]
            r0 = r16
            int r2 = r0.mSelectedValue
            if (r1 != r2) goto L_0x00d9
            int r1 = r8 / 2
            int r1 = r1 + r14
            int r2 = r9 / 2
            int r1 = r1 - r2
            float r2 = (float) r1
            int r1 = r15 - r9
            float r3 = (float) r1
            int r1 = r8 / 2
            int r1 = r1 + r14
            int r4 = r9 / 2
            int r1 = r1 + r4
            float r4 = (float) r1
            float r5 = (float) r15
            r0 = r16
            android.graphics.Paint r6 = r0.mSelectedBoxPaint
            r1 = r17
            r1.drawRect(r2, r3, r4, r5, r6)
            int r7 = r12.getColor()
            android.content.res.Resources r1 = r16.getResources()
            r2 = 17170443(0x106000b, float:2.4611944E-38)
            int r1 = r1.getColor(r2)
            r12.setColor(r1)
            int r1 = r8 / 2
            int r1 = r1 + r14
            r0 = r16
            android.graphics.Rect r2 = r0.bounds
            int r2 = r2.width()
            int r2 = r2 / 2
            int r1 = r1 - r2
            float r1 = (float) r1
            int r2 = r9 / 2
            int r2 = r15 - r2
            r0 = r16
            android.graphics.Rect r3 = r0.bounds
            int r3 = r3.height()
            int r3 = r3 / 2
            int r2 = r2 + r3
            float r2 = (float) r2
            r0 = r17
            r0.drawText(r13, r1, r2, r12)
            r12.setColor(r7)
        L_0x00c7:
            int r14 = r14 + r8
            int r1 = r11 + 1
            int r1 = r1 % 7
            if (r1 != 0) goto L_0x00d0
            int r15 = r15 + r9
            r14 = 0
        L_0x00d0:
            int r11 = r11 + 1
            goto L_0x0019
        L_0x00d4:
            r0 = r16
            android.graphics.Paint r12 = r0.mNormalTextPaint
            goto L_0x0059
        L_0x00d9:
            int r1 = r8 / 2
            int r1 = r1 + r14
            r0 = r16
            android.graphics.Rect r2 = r0.bounds
            int r2 = r2.width()
            int r2 = r2 / 2
            int r1 = r1 - r2
            float r1 = (float) r1
            int r2 = r9 / 2
            int r2 = r15 - r2
            r0 = r16
            android.graphics.Rect r3 = r0.bounds
            int r3 = r3.height()
            int r3 = r3 / 2
            int r2 = r2 + r3
            float r2 = (float) r2
            r0 = r17
            r0.drawText(r13, r1, r2, r12)
            goto L_0x00c7
        L_0x00fe:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.views.calendar.SleepDatesMonthView.onDraw(android.graphics.Canvas):void");
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
