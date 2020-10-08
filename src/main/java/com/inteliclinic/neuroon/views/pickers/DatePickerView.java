package com.inteliclinic.neuroon.views.pickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.pickers.PickerView;
import java.util.Calendar;
import java.util.Date;

public class DatePickerView extends PickerView {
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    private DaysAdapter mDaysAdapter;
    private RecyclerView mDaysRecyclerView;
    private OnDatePickerListener mListener;
    private MonthAdapter mMonthAdapter;
    private RecyclerView mMonthRecyclerView;
    private YearAdapter mYearAdapter;
    private RecyclerView mYearRecyclerView;

    public interface OnDatePickerListener {
        void saveDate(Date date);
    }

    public DatePickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static void show(View view, Date date, OnDatePickerListener listener) {
        if (isShowing()) {
            throw new UnsupportedOperationException("Only one date picker can be visible on screen");
        }
        ContentFrameLayout viewById = (ContentFrameLayout) view.getRootView().findViewById(16908290);
        DatePickerView pickerView = new DatePickerView(viewById.getContext());
        if (date != null) {
            pickerView.mCalendar.setTime(date);
        }
        viewById.addView(pickerView);
        pickerView.mListener = listener;
        setIsShowing(true);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        this.mCalendar = Calendar.getInstance();
        hideSwitch();
        initPickers();
    }

    private void initPickers() {
        inflate(getContext(), R.layout.view_date_picker, this.container);
        this.mDaysAdapter = new DaysAdapter(this.indicator, new Date());
        this.mDaysRecyclerView = initPicker(R.id.date_picker, this.mDaysAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                DatePickerView.this.mCalendar.set(5, position + 1);
            }
        });
        this.mMonthAdapter = new MonthAdapter(this.indicator);
        this.mMonthRecyclerView = initPicker(R.id.month_picker, this.mMonthAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                DatePickerView.this.mCalendar.set(2, position);
                if (DatePickerView.this.mCalendar.get(2) != position) {
                    DatePickerView.this.mCalendar.set(2, position);
                    DatePickerView.this.mCalendar.set(5, DatePickerView.this.mCalendar.getActualMaximum(5));
                }
                DatePickerView.this.setDay();
            }
        });
        this.mYearAdapter = new YearAdapter(this.indicator);
        this.mYearRecyclerView = initPicker(R.id.year_picker, this.mYearAdapter, true, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int month = DatePickerView.this.mCalendar.get(2);
                DatePickerView.this.mCalendar.set(1, Integer.parseInt(((ThinTextView) centerView).getText().toString()));
                if (DatePickerView.this.mCalendar.get(2) != month) {
                    DatePickerView.this.mCalendar.set(2, month);
                    DatePickerView.this.mCalendar.set(5, DatePickerView.this.mCalendar.getActualMaximum(5));
                }
                DatePickerView.this.setDay();
            }
        });
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                DatePickerView.this.setDate();
                DatePickerView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void save() {
        if (this.mListener != null) {
            this.mListener.saveDate(this.mCalendar.getTime());
        }
    }

    public void setDate() {
        setDay();
        setMonth();
        setYear();
    }

    private void setYear() {
        this.mYearRecyclerView.scrollToPosition(Calendar.getInstance().get(1) - this.mCalendar.get(1));
    }

    private void setMonth() {
        this.mMonthRecyclerView.scrollToPosition(this.mCalendar.get(2));
    }

    /* access modifiers changed from: private */
    public void setDay() {
        this.mDaysAdapter.setDate(this.mCalendar.getTime());
        this.mDaysRecyclerView.scrollToPosition(this.mCalendar.get(5) - 1);
    }
}
