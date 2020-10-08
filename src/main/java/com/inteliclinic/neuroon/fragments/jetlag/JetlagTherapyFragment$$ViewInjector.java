package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.JetLagChartView;

public class JetlagTherapyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetlagTherapyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.change_journey, "field 'changeJourney' and method 'onChangeJourneyClick'");
        target.changeJourney = (BaseTextView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onChangeJourneyClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.cancel_therapy, "field 'cancelTherapy' and method 'onCancelTherapyClick'");
        target.cancelTherapy = (BaseTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelTherapyClick();
            }
        });
        target.mCurrentTherapy = (LightTextView) finder.findRequiredView(source, R.id.current_therapy, "field 'mCurrentTherapy'");
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        target.jetLagChart = (JetLagChartView) finder.findRequiredView(source, R.id.jet_lag_chart, "field 'jetLagChart'");
        target.jetLagPercent = (ThinTextView) finder.findRequiredView(source, R.id.jet_lag_percent, "field 'jetLagPercent'");
        target.jetLagLeft = (ThinTextView) finder.findRequiredView(source, R.id.jet_lag_left, "field 'jetLagLeft'");
        target.jetLagToBeat = (LightTextView) finder.findRequiredView(source, R.id.jet_lag_to_beat, "field 'jetLagToBeat'");
        target.mMaskConnection = (ImageView) finder.findRequiredView(source, R.id.mask_connection, "field 'mMaskConnection'");
        target.mBatteryIndeter = (ProgressBar) finder.findRequiredView(source, R.id.battery_indeter, "field 'mBatteryIndeter'");
        target.mBatteryDeter = (ImageView) finder.findRequiredView(source, R.id.battery_deter, "field 'mBatteryDeter'");
        finder.findRequiredView(source, R.id.menu, "method 'onMenuClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMenuClick();
            }
        });
        finder.findRequiredView(source, R.id.help_icon, "method 'onHelpClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onHelpClick();
            }
        });
        finder.findRequiredView(source, R.id.right_arrow, "method 'onRightArrowClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onRightArrowClick();
            }
        });
    }

    public static void reset(JetlagTherapyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.changeJourney = null;
        target.cancelTherapy = null;
        target.mCurrentTherapy = null;
        target.mBottomToolbar = null;
        target.jetLagChart = null;
        target.jetLagPercent = null;
        target.jetLagLeft = null;
        target.jetLagToBeat = null;
        target.mMaskConnection = null;
        target.mBatteryIndeter = null;
        target.mBatteryDeter = null;
    }
}
