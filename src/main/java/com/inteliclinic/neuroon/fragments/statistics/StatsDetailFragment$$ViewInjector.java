package com.inteliclinic.neuroon.fragments.statistics;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.calendar.SleepDatesHorizontalListView;

public class StatsDetailFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final StatsDetailFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.indicator = (ImageView) finder.findRequiredView(source, R.id.indicator, "field 'indicator'");
        target.iconsLayout = (LinearLayout) finder.findRequiredView(source, R.id.icons_layout, "field 'iconsLayout'");
        View view = finder.findRequiredView(source, R.id.time_stats, "field 'timeStats' and method 'onTimeStatsClick'");
        target.timeStats = (ImageView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onTimeStatsClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.heart_stats, "field 'heartStats' and method 'onHeartStatsClick'");
        target.heartStats = (ImageView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onHeartStatsClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.brain_stats, "field 'brainStats' and method 'onBrainStatsClick'");
        target.brainStats = (ImageView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBrainStatsClick();
            }
        });
        target.viewPager = (ViewPager) finder.findRequiredView(source, R.id.view_pager, "field 'viewPager'");
        target.calendarList = (SleepDatesHorizontalListView) finder.findRequiredView(source, R.id.calendar_list, "field 'calendarList'");
        target.calendarListLine = (ImageView) finder.findRequiredView(source, R.id.calendar_list_line, "field 'calendarListLine'");
        View view4 = finder.findRequiredView(source, R.id.calendar_view_ico, "field 'calendarViewIco' and method 'onCalendarViewClick'");
        target.calendarViewIco = (ImageView) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCalendarViewClick();
            }
        });
        target.slider = (ImageView) finder.findRequiredView(source, R.id.slider, "field 'slider'");
        finder.findRequiredView(source, R.id.back_button, "method 'onBackButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBackButtonClick();
            }
        });
    }

    public static void reset(StatsDetailFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.indicator = null;
        target.iconsLayout = null;
        target.timeStats = null;
        target.heartStats = null;
        target.brainStats = null;
        target.viewPager = null;
        target.calendarList = null;
        target.calendarListLine = null;
        target.calendarViewIco = null;
        target.slider = null;
    }
}
