package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;

public class DaysChooserView extends RelativeLayout {
    @InjectView(2131755600)
    ThinTextView days30;
    @InjectView(2131755601)
    ThinTextView days7;
    private OnDaysChooserStateChangedListener mListener;
    private DaysChooserState mState;
    @InjectView(2131755471)
    ImageView slider;

    public enum DaysChooserState {
        DAYS30,
        DAYS7
    }

    public interface OnDaysChooserStateChangedListener {
        void onStateChange(DaysChooserState daysChooserState);
    }

    public DaysChooserView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DaysChooserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DaysChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mState = DaysChooserState.DAYS7;
        init();
    }

    @TargetApi(21)
    public DaysChooserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mState = DaysChooserState.DAYS7;
        init();
    }

    public void setListener(OnDaysChooserStateChangedListener listener) {
        this.mListener = listener;
    }

    private void init() {
        inflate(getContext(), R.layout.view_days_chooser, this);
        ButterKnife.inject((View) this);
        setState();
    }

    private void setState() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.slider.getLayoutParams();
        switch (this.mState) {
            case DAYS30:
                this.days30.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.days7.setTextColor(getResources().getColor(R.color.grey_8e));
                layoutParams.removeRule(11);
                layoutParams.addRule(9);
                this.slider.setLayoutParams(layoutParams);
                return;
            default:
                this.days7.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.days30.setTextColor(getResources().getColor(R.color.grey_8e));
                layoutParams.removeRule(9);
                layoutParams.addRule(11);
                this.slider.setLayoutParams(layoutParams);
                return;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (((double) event.getRawX()) < ((double) getLeft()) + (((double) getWidth()) / 2.0d)) {
            if (this.mState != DaysChooserState.DAYS30) {
                this.mState = DaysChooserState.DAYS30;
                if (this.mListener != null) {
                    this.mListener.onStateChange(this.mState);
                }
                setState();
                return true;
            }
        } else if (this.mState != DaysChooserState.DAYS7) {
            this.mState = DaysChooserState.DAYS7;
            if (this.mListener != null) {
                this.mListener.onStateChange(this.mState);
            }
            setState();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
