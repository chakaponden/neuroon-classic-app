package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SwipeToCloseView;
import com.inteliclinic.neuroon.views.TherapyProgressView;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.text.DateFormat;
import java.util.Date;

public class TherapyFragment extends BaseFragment {
    private static final String ARG_THERAPY_DURATION = "therapy_duration";
    private static final String ARG_THERAPY_NAME = "therapy_name";
    private static final String ARG_THERAPY_START_DATE = "therapy_start_date";
    private static final String ARG_THERAPY_TYPE = "therapy_type";
    @InjectView(2131755478)
    LightTextView endsTime;
    private CountDownTimer mCountDownTimer;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    private long mTherapyDuration;
    private String mTherapyName;
    private Date mTherapyStartDate;
    private Event.EventType mTherapyType;
    @InjectView(2131755475)
    TherapyProgressView progressView;
    @InjectView(2131755471)
    SwipeToCloseView slider;
    @InjectView(2131755474)
    ViewGroup therapyBack;
    @InjectView(2131755477)
    LightTextView therapyEnds;
    @InjectView(2131755314)
    ThinTextView therapyTitle;
    @InjectView(2131755476)
    LightTextView timer;

    public interface OnFragmentInteractionListener {
        void onCancelTherapy();
    }

    public static TherapyFragment newInstance(String therapyName, int therapyDuration, Event.EventType type) {
        TherapyFragment fragment = new TherapyFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_THERAPY_START_DATE, new Date().getTime());
        args.putString(ARG_THERAPY_NAME, therapyName);
        args.putLong(ARG_THERAPY_DURATION, (long) therapyDuration);
        args.putString(ARG_THERAPY_TYPE, String.valueOf(type));
        fragment.setArguments(args);
        return fragment;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mTherapyName = getArguments().getString(ARG_THERAPY_NAME);
            this.mTherapyDuration = getArguments().getLong(ARG_THERAPY_DURATION);
            this.mTherapyType = Event.EventType.valueOf(getArguments().getString(ARG_THERAPY_TYPE));
            this.mTherapyStartDate = new Date(getArguments().getLong(ARG_THERAPY_START_DATE, -1));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_therapy, container, false);
        ButterKnife.inject((Object) this, view);
        this.progressView.setProgressMax(1000);
        setTherapy();
        this.slider.setOnSwipeListener(new SwipeToCloseView.SwipeToCloseListener() {
            public void onClose() {
                if (TherapyFragment.this.mListener != null) {
                    TherapyFragment.this.mListener.onCancelTherapy();
                }
            }
        });
        return view;
    }

    public void setTherapy(String therapyName, int therapyDuration, Event.EventType type) {
        this.mTherapyName = therapyName;
        this.mTherapyDuration = ((long) therapyDuration) * 1000;
        this.mTherapyType = type;
        this.mTherapyStartDate = new Date();
        setTherapy();
        destroyTimer();
        setTimer();
    }

    private void setTherapy() {
        if (this.mTherapyName != null) {
            this.therapyTitle.setText(this.mTherapyName.toUpperCase());
            this.therapyEnds.setText(getString(R.string.smth_ends_at, new Object[]{this.mTherapyName.toUpperCase()}));
        }
        if (this.mTherapyStartDate != null) {
            this.endsTime.setText(DateFormat.getTimeInstance(3).format(DateUtils.dateAddSeconds(this.mTherapyStartDate, (int) (this.mTherapyDuration / 1000))));
        }
        if (this.mTherapyType != null) {
            this.progressView.setType(this.mTherapyType);
            switch (this.mTherapyType) {
                case ETBLT:
                    this.therapyTitle.setTextColor(getResources().getColor(R.color.yellow_FFAC00));
                    this.therapyBack.setBackgroundResource(R.drawable.bg_yellow);
                    return;
                case ETNapUltimate:
                case ETNapPower:
                case ETNapRem:
                case ETNapBody:
                    this.therapyTitle.setTextColor(getResources().getColor(R.color.dark_violet_7900B9));
                    this.therapyBack.setBackgroundResource(R.drawable.nap_bg_1080x1920);
                    return;
                case ETSleep:
                    this.slider.setTextColor(getResources().getColor(17170443));
                    this.therapyBack.setBackgroundResource(R.drawable.bg_blue);
                    return;
                default:
                    return;
            }
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onResume() {
        super.onResume();
        setTimer();
    }

    /* access modifiers changed from: private */
    public void setTimer() {
        if (this.mCountDownTimer == null && this.mTherapyStartDate != null) {
            int secondsTo = -DateUtils.secondsToNow(DateUtils.dateAddSeconds(this.mTherapyStartDate, (int) (this.mTherapyDuration / 1000)));
            if (secondsTo > 600) {
                this.mCountDownTimer = new CountDownTimer(((long) (secondsTo - 600)) * 1000, 60000) {
                    public void onTick(long millisUntilFinished) {
                        TherapyFragment.this.setHourMinuteTimer();
                    }

                    public void onFinish() {
                        TherapyFragment.this.setTimer();
                    }
                };
                this.mCountDownTimer.start();
            } else if (secondsTo <= 0) {
                if (this.progressView != null) {
                    this.progressView.setProgress(1000);
                }
                if (this.timer != null) {
                    this.timer.setText(R.string.zero_time);
                }
            } else {
                this.mCountDownTimer = new CountDownTimer((((long) secondsTo) * 1000) + 2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        TherapyFragment.this.setMinuteSecondTimer();
                    }

                    public void onFinish() {
                        TherapyFragment.this.setTimer();
                    }
                };
                this.mCountDownTimer.start();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setMinuteSecondTimer() {
        int secondsTo = -DateUtils.secondsToNow(DateUtils.dateAddSeconds(this.mTherapyStartDate, (int) (this.mTherapyDuration / 1000)));
        if (this.progressView != null) {
            this.progressView.setProgress(1000 - ((int) (((long) (secondsTo * 1000)) / (this.mTherapyDuration / 1000))));
        }
        short minutes = (short) (secondsTo / 60);
        short seconds = (short) (secondsTo - (minutes * 60));
        if (this.timer != null) {
            this.timer.setText(String.format("%02d:%02d", new Object[]{Short.valueOf(minutes), Short.valueOf(seconds)}));
        }
    }

    /* access modifiers changed from: private */
    public void setHourMinuteTimer() {
        short minutes = DateUtils.minutesTo(DateUtils.dateAddSeconds(this.mTherapyStartDate, (int) (this.mTherapyDuration / 1000)));
        if (this.progressView != null) {
            this.progressView.setProgress(1000 - ((int) (((long) (minutes * 1000)) / (this.mTherapyDuration / 60000))));
        }
        short hours = (short) (minutes / 60);
        short minutes2 = (short) (minutes - (hours * 60));
        if (this.timer != null) {
            this.timer.setText(String.format("%02d:%02d", new Object[]{Short.valueOf(hours), Short.valueOf(minutes2)}));
        }
    }

    public void onPause() {
        super.onPause();
        destroyTimer();
    }

    private void destroyTimer() {
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
            this.mCountDownTimer = null;
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
