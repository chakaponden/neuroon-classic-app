package com.inteliclinic.neuroon.fragments.dashboard;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SleepScoreView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.dashboard.DashboardStatsView;
import com.inteliclinic.neuroon.views.dashboard.NapView;

public class DashboardFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final DashboardFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.time_stats, "field 'mTimeStats' and method 'onTimeStatsClick'");
        target.mTimeStats = (DashboardStatsView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onTimeStatsClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.heart_stats, "field 'mHeartStats' and method 'onHeartStatsClick'");
        target.mHeartStats = (DashboardStatsView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onHeartStatsClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.brain_stats, "field 'mBrainStats' and method 'onBrainStatsClick'");
        target.mBrainStats = (DashboardStatsView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBrainStatsClick();
            }
        });
        target.mSleepScore = (SleepScoreView) finder.findRequiredView(source, R.id.sleep_score, "field 'mSleepScore'");
        target.mSleepScoreText = (ThinTextView) finder.findRequiredView(source, R.id.sleep_score_text, "field 'mSleepScoreText'");
        target.mSleepScoreHint = (ThinTextView) finder.findRequiredView(source, R.id.sleep_score_hint, "field 'mSleepScoreHint'");
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        target.mNextEventIcon = (ImageView) finder.findRequiredView(source, R.id.next_event_icon, "field 'mNextEventIcon'");
        target.mNextEventTitle = (LightTextView) finder.findRequiredView(source, R.id.next_event_title, "field 'mNextEventTitle'");
        target.mNextEventTime = (LightTextView) finder.findRequiredView(source, R.id.next_event_time, "field 'mNextEventTime'");
        target.mFirstSuggestion = (NapView) finder.findRequiredView(source, R.id.first_suggestion, "field 'mFirstSuggestion'");
        target.mFirstDivider = finder.findRequiredView(source, R.id.first_divider, "field 'mFirstDivider'");
        target.mSecondSuggestion = (NapView) finder.findRequiredView(source, R.id.second_suggestion, "field 'mSecondSuggestion'");
        target.mSecondDivider = finder.findRequiredView(source, R.id.second_divider, "field 'mSecondDivider'");
        target.mThirdSuggestion = (NapView) finder.findRequiredView(source, R.id.third_suggestion, "field 'mThirdSuggestion'");
        target.mThirdDivider = finder.findRequiredView(source, R.id.third_divider, "field 'mThirdDivider'");
        View view4 = finder.findRequiredView(source, R.id.alarms, "field 'mAlarms' and method 'onAlarmsClick'");
        target.mAlarms = (FrameLayout) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAlarmsClick();
            }
        });
        target.mTodayTipBox = (LinearLayout) finder.findRequiredView(source, R.id.today_tip_box, "field 'mTodayTipBox'");
        target.mTodayTip = (BoldTextView) finder.findRequiredView(source, R.id.today_tip, "field 'mTodayTip'");
        target.mTipTitle = (ThinTextView) finder.findRequiredView(source, R.id.best_tip_title, "field 'mTipTitle'");
        target.mTipContent = (ThinTextView) finder.findRequiredView(source, R.id.best_tip_content, "field 'mTipContent'");
        target.mTipLink = (LightTextView) finder.findRequiredView(source, R.id.best_tip_link, "field 'mTipLink'");
        target.mTodayRecommendations = (BoldTextView) finder.findRequiredView(source, R.id.today_recommendations, "field 'mTodayRecommendations'");
        target.mMaskConnection = (ImageView) finder.findRequiredView(source, R.id.mask_connection, "field 'mMaskConnection'");
        target.mBatteryIndeter = (ProgressBar) finder.findRequiredView(source, R.id.battery_indeter, "field 'mBatteryIndeter'");
        target.mBatteryDeter = (ImageView) finder.findRequiredView(source, R.id.battery_deter, "field 'mBatteryDeter'");
        finder.findRequiredView(source, R.id.menu, "method 'onMenuClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMenuClick();
            }
        });
        finder.findRequiredView(source, R.id.mask_info_bar, "method 'onMaskInfoClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMaskInfoClick();
            }
        });
        finder.findRequiredView(source, R.id.start_sleep, "method 'onStartSleepClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartSleepClick();
            }
        });
        finder.findRequiredView(source, R.id.left_arrow, "method 'onLeftArrowClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLeftArrowClick();
            }
        });
        finder.findRequiredView(source, R.id.right_arrow, "method 'onRightArrowClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onRightArrowClick();
            }
        });
    }

    public static void reset(DashboardFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mTimeStats = null;
        target.mHeartStats = null;
        target.mBrainStats = null;
        target.mSleepScore = null;
        target.mSleepScoreText = null;
        target.mSleepScoreHint = null;
        target.mBottomToolbar = null;
        target.mNextEventIcon = null;
        target.mNextEventTitle = null;
        target.mNextEventTime = null;
        target.mFirstSuggestion = null;
        target.mFirstDivider = null;
        target.mSecondSuggestion = null;
        target.mSecondDivider = null;
        target.mThirdSuggestion = null;
        target.mThirdDivider = null;
        target.mAlarms = null;
        target.mTodayTipBox = null;
        target.mTodayTip = null;
        target.mTipTitle = null;
        target.mTipContent = null;
        target.mTipLink = null;
        target.mTodayRecommendations = null;
        target.mMaskConnection = null;
        target.mBatteryIndeter = null;
        target.mBatteryDeter = null;
    }
}
