package com.inteliclinic.neuroon.fragments.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.events.DbSleepsUpdatedEvent;
import com.inteliclinic.neuroon.events.DbTipsUpdatedEvent;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.EnergyPlusFragment;
import com.inteliclinic.neuroon.fragments.OnBottomToolbarFragmentInteractionListener;
import com.inteliclinic.neuroon.fragments.OnMenuFragmentInteractionListener;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.StatsManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.managers.tip.TipManager;
import com.inteliclinic.neuroon.mask.BatteryLevelReceivedEvent;
import com.inteliclinic.neuroon.mask.MaskConnectedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.data.Tip;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SleepScoreView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.dashboard.DashboardStatsView;
import com.inteliclinic.neuroon.views.dashboard.NapView;
import de.greenrobot.event.EventBus;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends BaseFragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();
    @InjectView(2131755209)
    FrameLayout mAlarms;
    @InjectView(2131755204)
    ImageView mBatteryDeter;
    @InjectView(2131755203)
    ProgressBar mBatteryIndeter;
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    @InjectView(2131755219)
    DashboardStatsView mBrainStats;
    @InjectView(2131755229)
    View mFirstDivider;
    @InjectView(2131755228)
    NapView mFirstSuggestion;
    @InjectView(2131755218)
    DashboardStatsView mHeartStats;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755202)
    ImageView mMaskConnection;
    @InjectView(2131755206)
    ImageView mNextEventIcon;
    @InjectView(2131755208)
    LightTextView mNextEventTime;
    @InjectView(2131755207)
    LightTextView mNextEventTitle;
    @InjectView(2131755231)
    View mSecondDivider;
    @InjectView(2131755230)
    NapView mSecondSuggestion;
    private long mSleepId;
    @InjectView(2131755211)
    SleepScoreView mSleepScore;
    @InjectView(2131755213)
    ThinTextView mSleepScoreHint;
    @InjectView(2131755212)
    ThinTextView mSleepScoreText;
    @InjectView(2131755233)
    View mThirdDivider;
    @InjectView(2131755232)
    NapView mThirdSuggestion;
    @InjectView(2131755217)
    DashboardStatsView mTimeStats;
    @InjectView(2131755224)
    ThinTextView mTipContent;
    @InjectView(2131755225)
    LightTextView mTipLink;
    @InjectView(2131755223)
    ThinTextView mTipTitle;
    @InjectView(2131755226)
    BoldTextView mTodayRecommendations;
    @InjectView(2131755222)
    BoldTextView mTodayTip;
    @InjectView(2131755221)
    LinearLayout mTodayTipBox;

    public interface OnFragmentInteractionListener extends OnBottomToolbarFragmentInteractionListener, OnMenuFragmentInteractionListener, EnergyPlusFragment.OnFragmentInteractionListener {
        void brainStatsClick();

        void goToActivityFeed();

        void goToProgress();

        void heartStatsClick();

        void openAlarms();

        void openMask();

        void startSleepTherapy();

        void timeStatsClick();
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.inject((Object) this, rootView);
        setListeners();
        return rootView;
    }

    private void setBatteryLevel(int batteryLevel) {
        if (batteryLevel != -1) {
            if (this.mBatteryIndeter != null) {
                this.mBatteryIndeter.setVisibility(8);
            }
            if (this.mBatteryDeter != null) {
                this.mBatteryDeter.setVisibility(0);
                if (batteryLevel > 60) {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_full));
                } else if (batteryLevel > 20) {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_mid));
                } else {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_low));
                }
            }
        } else {
            if (this.mBatteryIndeter != null) {
                this.mBatteryIndeter.setVisibility(0);
            }
            if (this.mBatteryDeter != null) {
                this.mBatteryDeter.setVisibility(8);
            }
        }
    }

    public void onEventMainThread(BatteryLevelReceivedEvent event) {
        setBatteryLevel(event.getLevel());
    }

    private void setNextAlarm() {
        this.mNextEventTime.setText(DateFormat.getTimeInstance(3).format(AccountManager.getInstance().getNextAlarmTime()));
        if (AccountManager.getInstance().isOneDayAlarmTurned()) {
            this.mNextEventIcon.setImageResource(R.drawable.alarm_maly);
            this.mNextEventTime.setTextColor(getResources().getColor(R.color.one_day_alarm));
            return;
        }
        this.mNextEventIcon.setImageResource(R.drawable.biorythm_maly);
        this.mNextEventTime.setTextColor(getResources().getColor(R.color.biorhythm_alarm));
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
            EventBus.getDefault().register(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onResume() {
        super.onResume();
        setNextAlarm();
        setMaskConnectionInfo(MaskManager.getInstance().isFullyConnected());
        setBatteryLevel(MaskManager.getInstance().getBatteryLevel());
        fillWithSleepData();
        setTips();
        if (this.mBottomToolbar != null) {
            this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.SLEEP_SCORE);
        }
    }

    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        this.mListener = null;
    }

    private void setListeners() {
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (DashboardFragment.this.mListener != null) {
                    DashboardFragment.this.mListener.jetlagClick();
                }
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (DashboardFragment.this.mListener != null) {
                    DashboardFragment.this.mListener.lightBoostClick();
                }
            }
        });
    }

    public void onEventMainThread(DbSleepsUpdatedEvent event) {
        fillWithSleepData();
    }

    public void onEventMainThread(MaskConnectedEvent event) {
        setMaskConnectionInfo(event.isConnected());
    }

    private void setMaskConnectionInfo(boolean connected) {
        if (this.mMaskConnection != null) {
            if (connected) {
                this.mMaskConnection.setImageResource(R.drawable.mask_signal_good);
            } else {
                this.mMaskConnection.setImageResource(R.drawable.mask_signal_bad);
            }
        }
    }

    private void fillWithSleepData() {
        Sleep lastSleep = DataManager.getInstance().getLastSleepByDateForDashboard();
        if (lastSleep != null) {
            this.mSleepId = lastSleep.getId();
            fillSleepFields(lastSleep);
        } else if (this.mSleepId <= 0) {
            fillSleepFieldsWithEmptyData();
        }
        fillRecommendations();
    }

    private void fillSleepFieldsWithEmptyData() {
        if (this.mSleepScore != null) {
            this.mSleepScore.setProgress(100);
        }
        if (this.mSleepScoreText != null) {
            this.mSleepScoreText.setProgress(0);
        }
        if (this.mSleepScoreHint != null) {
            this.mSleepScoreHint.setText(R.string.no_sleep_score_yet);
        }
    }

    private void fillSleepFields(Sleep lastSleep) {
        if (lastSleep == null) {
            fillSleepFieldsWithEmptyData();
            return;
        }
        if (!(this.mSleepScore == null || this.mSleepScore.getProgress() == lastSleep.getSleepScore())) {
            this.mSleepScore.setProgress(lastSleep.getSleepScore());
        }
        if (this.mSleepScoreText != null) {
            this.mSleepScoreText.setProgress(lastSleep.getSleepScore());
        }
        if (this.mSleepScoreHint != null) {
            this.mSleepScoreHint.setVisibility(8);
        }
        if (this.mTimeStats != null) {
            this.mTimeStats.setValue(String.format("%02d:%02d", new Object[]{Integer.valueOf(lastSleep.getSleepDuration() / 3600), Integer.valueOf((lastSleep.getSleepDuration() / 60) % 60)}));
        }
        if (this.mHeartStats != null) {
            this.mHeartStats.setValue(String.valueOf(lastSleep.getSleepPulseAverage()));
        }
        if (this.mBrainStats != null) {
            this.mBrainStats.setValue(String.format("%d%%", new Object[]{Integer.valueOf(lastSleep.getRemDurationProcentage())}));
        }
    }

    private void setTips() {
        final Tip bestTip = TipManager.getInstance().getBestTip();
        if (bestTip != null) {
            this.mTipTitle.setText(bestTip.getTitle());
            String content = bestTip.getContent();
            if (content != null) {
                this.mTipContent.setText(content);
                if (bestTip.hasLink()) {
                    this.mTipLink.setVisibility(0);
                    this.mTipLink.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            DashboardFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(bestTip.getLink())));
                        }
                    });
                    return;
                }
                this.mTipLink.setVisibility(8);
                this.mTipLink.setOnClickListener((View.OnClickListener) null);
            } else if (this.mTodayTipBox != null) {
                this.mTodayTipBox.setVisibility(8);
            }
        } else if (this.mTodayTipBox != null) {
            this.mTodayTipBox.setVisibility(8);
        }
    }

    public void onEventMainThread(DbTipsUpdatedEvent event) {
        setTips();
    }

    private void fillRecommendations() {
        if (this.mThirdSuggestion != null) {
            List<Event> napRecommendations = StatsManager.getInstance().getNapRecommendations();
            List<Event> recommendations = new ArrayList<>();
            for (Event napRecommendation : napRecommendations) {
                if ((napRecommendation.getStartDate().before(new Date()) && napRecommendation.getEndDate().after(new Date())) || napRecommendation.getType() == Event.EventType.ETBLT || napRecommendation.getType() == Event.EventType.ETNapPower) {
                    recommendations.add(napRecommendation);
                }
            }
            CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
            if (currentTherapy != null) {
                for (Event event : currentTherapy.getTodayEvents()) {
                    recommendations.add(event);
                }
            }
            fillRecommendation(recommendations, 0, this.mFirstSuggestion, this.mFirstDivider);
            fillRecommendation(recommendations, 1, this.mSecondSuggestion, this.mSecondDivider);
            fillRecommendation(recommendations, 2, this.mThirdSuggestion, this.mThirdDivider);
        }
    }

    private void fillRecommendation(List<Event> recommendations, int index, NapView recommendationView, View reccomendationDivider) {
        if (recommendations.size() > index) {
            fillRecommendation(recommendationView, recommendations.get(index));
            recommendationView.setVisibility(0);
            reccomendationDivider.setVisibility(0);
            return;
        }
        recommendationView.setVisibility(8);
        reccomendationDivider.setVisibility(8);
    }

    private void fillRecommendation(NapView view, final Event event) {
        view.fillWithEvent(event);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (DashboardFragment.this.mListener != null) {
                    switch (AnonymousClass4.$SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[event.getType().ordinal()]) {
                        case 1:
                            DashboardFragment.this.mListener.startLightTherapy(1200000);
                            return;
                        case 2:
                            DashboardFragment.this.mListener.startNapTherapy("Power Nap", 1200, Event.EventType.ETNapPower);
                            return;
                        case 3:
                            DashboardFragment.this.mListener.startNapTherapy("Body Nap", 1800, Event.EventType.ETNapBody);
                            return;
                        case 4:
                            DashboardFragment.this.mListener.startNapTherapy("Rem Nap", 3600, Event.EventType.ETNapRem);
                            return;
                        case 5:
                            DashboardFragment.this.mListener.startNapTherapy("Ultimate Nap", 5400, Event.EventType.ETNapUltimate);
                            return;
                        default:
                            return;
                    }
                }
            }
        });
    }

    /* renamed from: com.inteliclinic.neuroon.fragments.dashboard.DashboardFragment$4  reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType = new int[Event.EventType.values().length];

        static {
            try {
                $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[Event.EventType.ETBLT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[Event.EventType.ETNapPower.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[Event.EventType.ETNapBody.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[Event.EventType.ETNapRem.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$inteliclinic$neuroon$models$data$Event$EventType[Event.EventType.ETNapUltimate.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755200})
    public void onMenuClick() {
        if (this.mListener != null) {
            this.mListener.toggleMenu();
        }
    }

    @OnClick({2131755201})
    public void onMaskInfoClick() {
        if (this.mListener != null) {
            this.mListener.openMask();
        }
    }

    @OnClick({2131755209})
    public void onAlarmsClick() {
        if (this.mListener != null) {
            this.mListener.openAlarms();
        }
    }

    @OnClick({2131755210})
    public void onStartSleepClick() {
        if (this.mListener != null) {
            this.mListener.startSleepTherapy();
        }
    }

    @OnClick({2131755217})
    public void onTimeStatsClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("main").setAction("sleep_duration_button").setLabel("sleep_duration_button").build());
        if (this.mListener != null) {
            this.mListener.timeStatsClick();
        }
    }

    @OnClick({2131755218})
    public void onHeartStatsClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("main").setAction("heart_rate_button").setLabel("heart_rate_button").build());
        if (this.mListener != null) {
            this.mListener.heartStatsClick();
        }
    }

    @OnClick({2131755219})
    public void onBrainStatsClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("main").setAction("sleep_staging_button").setLabel("sleep_staging_button").build());
        if (this.mListener != null) {
            this.mListener.brainStatsClick();
        }
    }

    @OnClick({2131755214})
    public void onLeftArrowClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("main").setAction("main_left_button").setLabel("main_left_button").build());
        if (this.mListener != null) {
            this.mListener.goToActivityFeed();
        }
    }

    @OnClick({2131755215})
    public void onRightArrowClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("main").setAction("main_right_button").setLabel("main_right_button").build());
        if (this.mListener != null) {
            this.mListener.goToProgress();
        }
    }
}
