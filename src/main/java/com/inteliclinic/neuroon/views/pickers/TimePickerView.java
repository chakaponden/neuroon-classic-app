package com.inteliclinic.neuroon.views.pickers;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.pickers.PickerView;

public class TimePickerView extends PickerView {
    private AMPMAdapter mAMPMAdapter;
    private RecyclerView mFirstRecyclerView;
    /* access modifiers changed from: private */
    public int mFirstValue;
    private UnitAdapter mHoursAdapter;
    private OnTimePickerListener mListener;
    private UnitAdapter mMinutesAdapter;
    private RecyclerView mSecondRecyclerView;
    /* access modifiers changed from: private */
    public int mSecondValue;
    private RecyclerView mThirdRecyclerView;
    /* access modifiers changed from: private */
    public int mThirdValue;

    public interface OnTimePickerListener {
        void onTimeSave(int i, int i2);
    }

    public TimePickerView(Context context) {
        super(context);
    }

    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static void show(View view, int theme, int mFirstValue2, int mSecondValue2, OnTimePickerListener listener) {
        if (isShowing()) {
            throw new UnsupportedOperationException("Only one picker can be visible on screen");
        }
        ContentFrameLayout viewById = (ContentFrameLayout) view.getRootView().findViewById(16908290);
        TimePickerView pickerView = new TimePickerView(viewById.getContext());
        pickerView.setTheme(theme);
        pickerView.setValue(mFirstValue2 - ((mFirstValue2 / 12) * 12), mSecondValue2, mFirstValue2 / 12);
        viewById.addView(pickerView);
        pickerView.mListener = listener;
        setIsShowing(true);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        hideSwitch();
        initPickers();
    }

    private void initPickers() {
        inflate(getContext(), R.layout.view_time_picker, this.container);
        setAdapters();
        this.mFirstRecyclerView = initPicker(R.id.first_picker, this.mHoursAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int unused = TimePickerView.this.mFirstValue = position + 1;
            }
        });
        this.mSecondRecyclerView = initPicker(R.id.second_picker, this.mMinutesAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int unused = TimePickerView.this.mSecondValue = position;
            }
        });
        this.mThirdRecyclerView = initPicker(R.id.third_picker, this.mAMPMAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int unused = TimePickerView.this.mThirdValue = position;
            }
        });
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TimePickerView.this.setValue();
                TimePickerView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    private void setAdapters() {
        this.mHoursAdapter = new UnitAdapter(this.indicator, "", 1, 12);
        this.mMinutesAdapter = new UnitAdapter(this.indicator, "", 0, 59);
        this.mAMPMAdapter = new AMPMAdapter(this.indicator);
        if (this.mFirstValue > 12) {
            this.mFirstValue -= 12;
            this.mThirdValue = 1;
        }
        if (this.mSecondValue > 59) {
            this.mSecondValue = 59;
        }
        setAdapter(this.mFirstRecyclerView, this.mHoursAdapter, this.mFirstValue - 1);
        setAdapter(this.mSecondRecyclerView, this.mMinutesAdapter, this.mSecondValue);
        setAdapter(this.mThirdRecyclerView, this.mAMPMAdapter, this.mThirdValue);
    }

    private void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, int startValue) {
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(startValue);
        }
    }

    /* access modifiers changed from: protected */
    public void save() {
        super.save();
        if (this.mListener != null) {
            this.mListener.onTimeSave(this.mFirstValue + (this.mThirdValue * 12), this.mSecondValue);
        }
    }

    private void setValue(int firstValue, int secondValue, int thirdValue) {
        this.mFirstValue = firstValue;
        this.mSecondValue = secondValue;
        this.mThirdValue = thirdValue;
        setValue();
    }

    /* access modifiers changed from: private */
    public void setValue() {
        setAdapters();
    }

    private class AMPMAdapter extends RecyclerView.Adapter<ViewHolder> {
        private View mIndicator;

        AMPMAdapter(View indicator) {
            this.mIndicator = indicator;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ThinTextView thinTextView = new ThinTextView(parent.getContext());
            thinTextView.setLayoutParams(new FrameLayout.LayoutParams(-1, this.mIndicator.getMeasuredHeight()));
            thinTextView.setGravity(17);
            thinTextView.setTextSize(22.0f);
            return new ViewHolder(thinTextView);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(position == 0 ? R.string.am : R.string.pm);
        }

        public int getItemCount() {
            return 2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ThinTextView mTextView;

            ViewHolder(ThinTextView v) {
                super(v);
                this.mTextView = v;
            }
        }
    }
}
