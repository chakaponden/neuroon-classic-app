package com.inteliclinic.neuroon.views.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.calendar.MonthView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarMonthView extends RelativeLayout {
    private static DateFormat mMonthFormat = new SimpleDateFormat("MMMM yyyy");
    /* access modifiers changed from: private */
    public OnDatePickerListener mListener;
    /* access modifiers changed from: private */
    public Calendar mMonthCalendar;
    @InjectView(2131755592)
    MonthView mMonthView;
    private Date mPickedDate;
    private int mTheme;
    @InjectView(2131755590)
    BoldTextView monthName;

    public interface OnDatePickerListener {
        void setDatePicked(Date date);
    }

    public CalendarMonthView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public CalendarMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_month_view, this);
        ButterKnife.inject((View) this);
        this.mMonthCalendar = Calendar.getInstance();
        setTheme(this.mTheme);
        this.mMonthView.setListener(new MonthView.OnDayPickListener() {
            public boolean onDayTouch(MonthView monthView, int day) {
                CalendarMonthView.this.mMonthCalendar.set(5, day);
                if (CalendarMonthView.this.mListener == null) {
                    return true;
                }
                CalendarMonthView.this.mListener.setDatePicked(CalendarMonthView.this.mMonthCalendar.getTime());
                return true;
            }
        });
    }

    public void setMonth(Calendar calendar) {
        this.mMonthCalendar.setTime(calendar.getTime());
        this.mMonthView.setMonth(this.mMonthCalendar.getTime(), 0);
        this.monthName.setText(mMonthFormat.format(this.mMonthCalendar.getTime()).toUpperCase());
    }

    public void setMonthWithDay(Date value) {
        this.mMonthCalendar.setTime(value);
        this.mMonthView.setMonth(this.mMonthCalendar.getTime(), this.mMonthCalendar.get(5));
        this.monthName.setText(mMonthFormat.format(this.mMonthCalendar.getTime()).toUpperCase());
    }

    public void setListener(OnDatePickerListener listener) {
        this.mListener = listener;
    }

    public void setTheme(int theme) {
        this.mTheme = theme;
        if (theme == 1) {
            this.monthName.setTextColor(getResources().getColor(R.color.jet_lag_color));
        } else if (theme == 0) {
            this.monthName.setTextColor(getResources().getColor(R.color.blue_3054FA));
        }
        this.mMonthView.setTheme(theme);
    }
}
