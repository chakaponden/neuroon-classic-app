package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.pickers.TimePickerView;
import java.text.DateFormat;
import java.util.Calendar;

public class TimeDisplayBarView extends RelativeLayout {
    @InjectView(2131755621)
    ThinTextView day;
    /* access modifiers changed from: private */
    public int hours;
    private String mDay;
    /* access modifiers changed from: private */
    public OnTimeChangedListener mListener;
    /* access modifiers changed from: private */
    public int mPickerTheme;
    /* access modifiers changed from: private */
    public int minutes;
    @InjectView(2131755577)
    ThinTextView time;

    public interface OnTimeChangedListener {
        void onTimeChanged(TimeDisplayBarView timeDisplayBarView, int i, int i2);
    }

    public TimeDisplayBarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TimeDisplayBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeDisplayBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPickerTheme = 1;
        init();
        initStyle(context, attrs, defStyleAttr, 0);
    }

    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeDisplayBarView, defStyleAttr, defStyleRes);
        try {
            int color = a.getColor(0, 0);
            if (color != 0) {
                this.time.setTextColor(color);
            }
        } finally {
            a.recycle();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_time_displey_bar, this);
        ButterKnife.inject((View) this);
        setClickable(true);
        setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), (int) getResources().getDimension(R.dimen.time_display_view_padding_vertical), (int) getResources().getDimension(R.dimen.activity_horizontal_margin), (int) getResources().getDimension(R.dimen.time_display_view_padding_vertical));
        setBackgroundResource(R.drawable.time_display_background);
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!TimePickerView.isShowing()) {
                    TimePickerView.show(TimeDisplayBarView.this, TimeDisplayBarView.this.mPickerTheme, TimeDisplayBarView.this.hours, TimeDisplayBarView.this.minutes, new TimePickerView.OnTimePickerListener() {
                        public void onTimeSave(int firstValue, int secondValue) {
                            int unused = TimeDisplayBarView.this.hours = firstValue;
                            int unused2 = TimeDisplayBarView.this.minutes = secondValue;
                            if (TimeDisplayBarView.this.mListener != null) {
                                TimeDisplayBarView.this.mListener.onTimeChanged(TimeDisplayBarView.this, firstValue, secondValue);
                            }
                            TimeDisplayBarView.this.resetViewContent();
                        }
                    });
                }
            }
        });
        resetViewContent();
    }

    public void setTime(Integer timestamp) {
        this.minutes = (timestamp.intValue() / 60) % 60;
        this.hours = (timestamp.intValue() / 3600) % 24;
        resetViewContent();
    }

    public void setDay(String day2) {
        this.mDay = day2;
        resetViewContent();
    }

    public void setPickerTheme(int pickerTheme) {
        this.mPickerTheme = pickerTheme;
    }

    /* access modifiers changed from: private */
    public void resetViewContent() {
        Calendar instance = Calendar.getInstance();
        instance.set(11, this.hours);
        instance.set(12, this.minutes);
        this.time.setText(DateFormat.getTimeInstance(3).format(instance.getTime()));
        if (this.mDay != null) {
            this.day.setText(this.mDay);
            this.day.setVisibility(0);
            return;
        }
        this.day.setVisibility(8);
    }

    public void setListener(OnTimeChangedListener listener) {
        this.mListener = listener;
    }

    public void setDayFont(Typeface font) {
        this.day.setTypeface(font);
    }
}
