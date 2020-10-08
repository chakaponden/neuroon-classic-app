package com.inteliclinic.neuroon.views.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.calendar.SleepDatesCalendarMonthView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

public class SleepDatesCalendarPickerView extends RelativeLayout implements SleepDatesCalendarMonthView.OnDatePickerListener {
    public static final int CALENDAR_FUTURE = 1;
    public static final int CALENDAR_PAST = 0;
    /* access modifiers changed from: private */
    public static boolean isShowing;
    @InjectView(2131755594)
    FrameLayout accept;
    @InjectView(2131755595)
    ImageView buttonImage;
    private CalendarListAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mCalendarTime;
    private ItemDecoration mItemDecoration;
    private OnDatePickerListener mListener;
    /* access modifiers changed from: private */
    public Date mValue;
    /* access modifiers changed from: private */
    public Calendar mValueCalendar;
    @InjectView(2131755133)
    RecyclerView recyclerView;
    @InjectView(2131755593)
    ImageView shadow;
    @InjectView(2131755078)
    ThinTextView title;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CalendarTime {
    }

    public interface OnDatePickerListener {
        void pickDate(Date date);
    }

    public SleepDatesCalendarPickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SleepDatesCalendarPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepDatesCalendarPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public static void show(View view, String title2, Date date, int calendarTime, OnDatePickerListener listener) {
        if (isShowing) {
            throw new UnsupportedOperationException("Only one picker can be visible on screen");
        }
        ContentFrameLayout viewById = (ContentFrameLayout) view.getRootView().findViewById(16908290);
        SleepDatesCalendarPickerView pickerView = new SleepDatesCalendarPickerView(viewById.getContext());
        pickerView.title.setText(title2);
        pickerView.setCalendarTime(calendarTime);
        if (date != null) {
            pickerView.setValue(date);
        }
        viewById.addView(pickerView);
        pickerView.mListener = listener;
        isShowing = true;
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_picker, this);
        setId(R.id.calendar_picker_view);
        ButterKnife.inject((View) this);
        setClickable(true);
        doTheme();
        setAdapter();
        setButton();
    }

    private void setAdapter() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, this.mCalendarTime == 0));
        this.recyclerView.setHasFixedSize(false);
        if (this.mItemDecoration == null) {
            this.mItemDecoration = new ItemDecoration(this.shadow);
            this.recyclerView.addItemDecoration(this.mItemDecoration);
        }
        this.mAdapter = new CalendarListAdapter(this.mCalendarTime);
        this.recyclerView.setAdapter(this.mAdapter);
        if (this.mCalendarTime == 0) {
            this.recyclerView.scrollToPosition(this.mAdapter.getItemCount());
        }
    }

    public void setCalendarTime(int calendarTime) {
        this.mCalendarTime = calendarTime;
        setAdapter();
    }

    public MonthView getMonthView(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar nowCalendar = Calendar.getInstance();
        if (this.mCalendarTime == 0) {
            return ((CalendarMonthView) this.recyclerView.getLayoutManager().getChildAt((((nowCalendar.get(1) - calendar.get(1)) * 12) + nowCalendar.get(2)) - calendar.get(2))).mMonthView;
        }
        return ((CalendarMonthView) this.recyclerView.getLayoutManager().getChildAt((((calendar.get(1) - nowCalendar.get(1)) * 12) + calendar.get(2)) - nowCalendar.get(2))).mMonthView;
    }

    private void doTheme() {
        setBackgroundResource(R.color.white_calendar_background);
        this.shadow.setImageResource(R.drawable.white_calendar);
    }

    public void dismiss() {
        dismiss(true);
    }

    private void dismiss(boolean animate) {
        if (animate) {
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
            anim.setDuration(300);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    ViewGroup parent = (ViewGroup) SleepDatesCalendarPickerView.this.getParent();
                    if (parent != null) {
                        parent.removeView(SleepDatesCalendarPickerView.this);
                    }
                    boolean unused = SleepDatesCalendarPickerView.isShowing = false;
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            startAnimation(anim);
            return;
        }
        ((ViewGroup) getParent()).removeView(this);
        isShowing = false;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        anim.setDuration(300);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SleepDatesCalendarPickerView.this.requestFocus();
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(anim);
    }

    public void setValue(@NonNull Date value) {
        this.mValue = value;
        if (this.mValueCalendar == null) {
            this.mValueCalendar = Calendar.getInstance();
        }
        this.mValueCalendar.setTime(value);
        setButton();
        this.mAdapter.notifyDataSetChanged();
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return super.dispatchKeyEventPreIme(event);
        }
        dismiss();
        return true;
    }

    @OnClick({2131755164})
    public void onCloseButtonClick() {
        dismiss();
    }

    @OnClick({2131755594})
    public void onAcceptButtonClick() {
        if (this.mListener != null) {
            this.mListener.pickDate(this.mValue);
        }
        dismiss();
    }

    public void setDatePicked(Date time) {
        setValue(time);
    }

    private void setButton() {
        if (this.mValue != null) {
            this.buttonImage.setEnabled(true);
        } else {
            this.buttonImage.setEnabled(false);
        }
    }

    public static class SleepDatesCalendarViewHolder extends RecyclerView.ViewHolder {
        SleepDatesCalendarMonthView mCalendarView;

        public SleepDatesCalendarViewHolder(SleepDatesCalendarMonthView v) {
            super(v);
            this.mCalendarView = v;
        }
    }

    protected class ItemDecoration extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private View mask;

        public ItemDecoration(View mask2) {
            this.mask = mask2;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (this.mBottomOffset == 0) {
                this.mBottomOffset = this.mask.getMeasuredHeight();
            }
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            if (SleepDatesCalendarPickerView.this.mCalendarTime == 0) {
                if (childAdapterPosition == 0) {
                    outRect.set(0, 0, 0, this.mBottomOffset);
                    return;
                }
            } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
                outRect.set(0, 0, 0, this.mBottomOffset);
                return;
            }
            outRect.set(0, 0, 0, 0);
        }
    }

    public final class CalendarListAdapter extends RecyclerView.Adapter<SleepDatesCalendarViewHolder> {
        private final Calendar mCalendar = Calendar.getInstance();
        private int mCalendarTime;

        public CalendarListAdapter(int calendarTime) {
            this.mCalendarTime = calendarTime;
        }

        public SleepDatesCalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SleepDatesCalendarMonthView calendarMonthView = new SleepDatesCalendarMonthView(parent.getContext());
            calendarMonthView.setListener(SleepDatesCalendarPickerView.this);
            return new SleepDatesCalendarViewHolder(calendarMonthView);
        }

        public void onBindViewHolder(SleepDatesCalendarViewHolder holder, int position) {
            holder.mCalendarView.setTheme(0);
            if (this.mCalendarTime == 0) {
                this.mCalendar.add(2, -position);
                if (SleepDatesCalendarPickerView.this.mValueCalendar != null && this.mCalendar.get(1) == SleepDatesCalendarPickerView.this.mValueCalendar.get(1) && this.mCalendar.get(2) == SleepDatesCalendarPickerView.this.mValueCalendar.get(2)) {
                    holder.mCalendarView.setMonthWithDay(SleepDatesCalendarPickerView.this.mValue);
                } else {
                    holder.mCalendarView.setMonth(this.mCalendar);
                }
                this.mCalendar.add(2, position);
            } else if (this.mCalendarTime == 1) {
                this.mCalendar.add(2, position);
                if (SleepDatesCalendarPickerView.this.mValueCalendar != null && this.mCalendar.get(1) == SleepDatesCalendarPickerView.this.mValueCalendar.get(1) && this.mCalendar.get(2) == SleepDatesCalendarPickerView.this.mValueCalendar.get(2)) {
                    holder.mCalendarView.setMonthWithDay(SleepDatesCalendarPickerView.this.mValue);
                } else {
                    holder.mCalendarView.setMonth(this.mCalendar);
                }
                this.mCalendar.add(2, -position);
            }
        }

        public int getItemCount() {
            return 200;
        }
    }
}
