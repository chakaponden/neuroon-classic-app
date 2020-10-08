package com.inteliclinic.neuroon.fragments.statistics;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.stats.SleepStage;
import com.inteliclinic.neuroon.models.stats.StagingData;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.BarChartView;
import com.inteliclinic.neuroon.views.charts.CircleChartView;
import com.inteliclinic.neuroon.views.charts.popovers.BubbleView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrainStatisticFragment extends BaseFragment implements ISleepPresentable {
    private static final String ARG_SLEEP_ID = "statistics_date";
    @InjectView(2131755181)
    BarChartView barChart;
    @InjectView(2131755190)
    ThinTextView deepPercent;
    @InjectView(2131755180)
    LightTextView endTime;
    @InjectView(2131755192)
    ThinTextView lightPercent;
    BubbleView mBubbleView;
    private OnFragmentInteractionListener mListener;
    private long mSleepId;
    @InjectView(2131755187)
    ThinTextView overallSignal;
    @InjectView(2131755185)
    RelativeLayout overallSignalContainer;
    @InjectView(2131755189)
    CircleChartView percentChart;
    @InjectView(2131755191)
    ThinTextView remPercent;
    @InjectView(2131755184)
    BarChartView signalBarChart;
    @InjectView(2131755182)
    LinearLayout signalContainer;
    @InjectView(2131755179)
    LightTextView startTime;
    @InjectView(2131755193)
    ThinTextView wakePercent;

    public interface OnFragmentInteractionListener {
        void deleteSleep(long j);
    }

    public static BrainStatisticFragment newInstance(long sleepId) {
        BrainStatisticFragment fragment = new BrainStatisticFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SLEEP_ID, sleepId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mSleepId = getArguments().getLong(ARG_SLEEP_ID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brain_statistic, container, false);
        ButterKnife.inject((Object) this, view);
        fillWithData();
        return view;
    }

    private void fillWithData() {
        Sleep lastSleep = DataManager.getInstance().getSleepById(this.mSleepId);
        if (lastSleep != null) {
            int[] colors = null;
            if (isAdded()) {
                colors = new int[]{getResources().getColor(R.color.deep_stage), getResources().getColor(R.color.rem_stage), getResources().getColor(R.color.light_stage), getResources().getColor(R.color.awake_stage)};
            }
            if (this.startTime != null) {
                this.startTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getStartDate()));
            }
            if (this.endTime != null) {
                this.endTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getEndDate()));
            }
            if (this.barChart != null) {
                setUpHypnogram(lastSleep, colors);
            }
            if (this.signalContainer != null) {
                setUpSignal(lastSleep);
            }
            if (this.overallSignal != null) {
                StagingData hypnogram = lastSleep.getHypnogram();
                if (hypnogram != null && hypnogram.getHypnogram() != null) {
                    this.overallSignalContainer.setVisibility(0);
                    switch (hypnogram.getSleepSignalQualityLevel()) {
                        case -1:
                            this.overallSignalContainer.setVisibility(8);
                            break;
                        case 0:
                            this.overallSignal.setText(R.string.signal_good);
                            this.overallSignal.setTextColor(getResources().getColor(R.color.green_0CA993));
                            this.overallSignal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mask_signal_good, 0, 0, 0);
                            break;
                        case 1:
                            this.overallSignal.setText(R.string.signal_average);
                            this.overallSignal.setTextColor(getResources().getColor(R.color.yellow_ffb400));
                            this.overallSignal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mask_signal_medium, 0, 0, 0);
                            break;
                        case 2:
                            this.overallSignal.setText(R.string.signal_bad);
                            this.overallSignal.setTextColor(getResources().getColor(R.color.red_FE3E4D));
                            this.overallSignal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mask_signal_bad, 0, 0, 0);
                            break;
                    }
                } else {
                    this.overallSignalContainer.setVisibility(8);
                }
            }
            if (this.percentChart != null) {
                this.percentChart.setAnimateValues(new int[]{lastSleep.getDeepDurationProcentage(), lastSleep.getRemDurationProcentage(), lastSleep.getLightDurationProcentage(), lastSleep.getWakeDurationProcentage()}, colors);
            }
            if (this.deepPercent != null) {
                this.deepPercent.setText(getString(R.string.some_percent, new Object[]{Integer.valueOf(lastSleep.getDeepDurationProcentage())}));
            }
            if (this.remPercent != null) {
                this.remPercent.setText(getString(R.string.some_percent, new Object[]{Integer.valueOf(lastSleep.getRemDurationProcentage())}));
            }
            if (this.lightPercent != null) {
                this.lightPercent.setText(getString(R.string.some_percent, new Object[]{Integer.valueOf(lastSleep.getLightDurationProcentage())}));
            }
            if (this.wakePercent != null) {
                this.wakePercent.setText(getString(R.string.some_percent, new Object[]{Integer.valueOf(lastSleep.getWakeDurationProcentage())}));
            }
        }
    }

    private void setUpHypnogram(final Sleep lastSleep, int[] colors) {
        StagingData hypnogram = lastSleep.getHypnogram();
        if (hypnogram != null && hypnogram.getHypnogram() != null) {
            final List<Pair<Integer, Integer>> valuesList = new ArrayList<>();
            int[] colorsList = new int[hypnogram.getHypnogram().size()];
            int i = 0;
            for (SleepStage stage : hypnogram.getHypnogram()) {
                switch (stage.getStage()) {
                    case 1:
                        valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 4));
                        colorsList[i] = colors[3];
                        i++;
                        break;
                    case 2:
                        valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 2));
                        colorsList[i] = colors[2];
                        i++;
                        break;
                    case 3:
                        valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 3));
                        colorsList[i] = colors[1];
                        i++;
                        break;
                    case 4:
                        valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 1));
                        colorsList[i] = colors[0];
                        i++;
                        break;
                }
            }
            this.barChart.setMaxYValue(4);
            this.barChart.setMinYValue(0);
            this.barChart.setAnimateValues(valuesList, colorsList);
            this.barChart.setListener(new BarChartView.OnBarChartTouchListener() {
                public void onItemTouch(int index, final Rect rect, float x, float y) {
                    String stage;
                    if (index >= 0) {
                        Date startDate = lastSleep.getStartDate();
                        Date endDate = lastSleep.getStartDate();
                        for (int i = 0; i <= index; i++) {
                            Pair<Integer, Integer> pair = (Pair) valuesList.get(i);
                            if (i != 0) {
                                startDate = (Date) endDate.clone();
                            }
                            endDate = DateUtils.dateAddSeconds(endDate, ((Integer) pair.first).intValue() * 30);
                        }
                        final int value = ((Integer) ((Pair) valuesList.get(index)).second).intValue();
                        switch (value) {
                            case 1:
                                stage = BrainStatisticFragment.this.getString(R.string.deep);
                                break;
                            case 2:
                                stage = BrainStatisticFragment.this.getString(R.string.light);
                                break;
                            case 3:
                                stage = BrainStatisticFragment.this.getString(R.string.rem);
                                break;
                            default:
                                stage = BrainStatisticFragment.this.getString(R.string.awake);
                                break;
                        }
                        if (BrainStatisticFragment.this.mBubbleView == null) {
                            BrainStatisticFragment.this.mBubbleView = new BubbleView(BrainStatisticFragment.this.getActivity());
                            BrainStatisticFragment.this.mBubbleView.setOnTouchListener(new View.OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() != 0) {
                                        return false;
                                    }
                                    BrainStatisticFragment.this.mBubbleView.setVisibility(8);
                                    return false;
                                }
                            });
                            ((ContentFrameLayout) BrainStatisticFragment.this.getView().getRootView().findViewById(16908290)).addView(BrainStatisticFragment.this.mBubbleView);
                            BrainStatisticFragment.this.mBubbleView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    if (value != 4) {
                                        BrainStatisticFragment.this.mBubbleView.setOnTopMiddleOfRect(rect);
                                    } else {
                                        BrainStatisticFragment.this.mBubbleView.setOnBottomMiddleOfRect(rect);
                                    }
                                    BrainStatisticFragment.this.mBubbleView.highlightElement(BrainStatisticFragment.this.barChart, rect);
                                    BrainStatisticFragment.this.mBubbleView.removeOnLayoutChangeListener(this);
                                }
                            });
                            Animation animation = AnimationUtils.loadAnimation(BrainStatisticFragment.this.getActivity(), 17432576);
                            animation.setStartOffset(100);
                            BrainStatisticFragment.this.mBubbleView.startAnimation(animation);
                        } else {
                            BrainStatisticFragment.this.mBubbleView.setVisibility(0);
                            if (value != 4) {
                                BrainStatisticFragment.this.mBubbleView.setOnTopMiddleOfRect(rect);
                            } else {
                                BrainStatisticFragment.this.mBubbleView.setOnBottomMiddleOfRect(rect);
                            }
                            BrainStatisticFragment.this.mBubbleView.highlightElement(BrainStatisticFragment.this.barChart, rect);
                        }
                        BrainStatisticFragment.this.mBubbleView.setValues(stage, startDate, endDate);
                    } else if (BrainStatisticFragment.this.mBubbleView != null) {
                        BrainStatisticFragment.this.mBubbleView.setVisibility(8);
                    }
                }
            });
        }
    }

    private void setUpSignal(Sleep lastSleep) {
        StagingData hypnogram = lastSleep.getHypnogram();
        if (hypnogram == null || hypnogram.getHypnogram() == null) {
            this.signalContainer.setVisibility(8);
            return;
        }
        this.signalContainer.setVisibility(0);
        this.signalBarChart.setMaxYValue(1);
        this.signalBarChart.setMinYValue(0);
        List<Pair<Integer, Integer>> values = hypnogram.getSleepSignalQuality();
        List<Pair<Integer, Integer>> valuesList = new ArrayList<>(values.size());
        int[] colorsList = new int[values.size()];
        int i = 0;
        for (Pair<Integer, Integer> value : values) {
            valuesList.add(new Pair(value.first, 1));
            switch (((Integer) value.second).intValue()) {
                case -1:
                    colorsList[i] = getResources().getColor(17170445);
                    i++;
                    break;
                case 0:
                    colorsList[i] = getResources().getColor(R.color.green_0CA993);
                    i++;
                    break;
                case 1:
                    colorsList[i] = getResources().getColor(R.color.yellow_ffb400);
                    i++;
                    break;
                case 2:
                    colorsList[i] = getResources().getColor(R.color.red_FE3E4D);
                    i++;
                    break;
            }
        }
        this.signalBarChart.setAnimateValues(valuesList, colorsList);
        this.signalBarChart.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                BrainStatisticFragment.this.signalBarChart.postDelayed(new Runnable() {
                    public void run() {
                        BrainStatisticFragment.this.signalBarChart.alignWidthTo(BrainStatisticFragment.this.barChart);
                    }
                }, 20);
                BrainStatisticFragment.this.signalBarChart.removeOnLayoutChangeListener(this);
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void presentSleep(long sleepId) {
        getArguments().putLong(ARG_SLEEP_ID, sleepId);
        this.mSleepId = sleepId;
        fillWithData();
    }

    @OnClick({2131755194})
    public void onDeleteSleepClick() {
        if (this.mListener != null) {
            this.mListener.deleteSleep(this.mSleepId);
        }
    }
}
