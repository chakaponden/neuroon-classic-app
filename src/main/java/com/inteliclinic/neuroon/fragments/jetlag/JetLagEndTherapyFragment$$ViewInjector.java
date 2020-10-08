package com.inteliclinic.neuroon.fragments.jetlag;

import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.JetLagChartView;

public class JetLagEndTherapyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagEndTherapyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.changeJourney = (ThinTextView) finder.findRequiredView(source, R.id.change_journey, "field 'changeJourney'");
        target.cancelTherapy = (ThinTextView) finder.findRequiredView(source, R.id.cancel_therapy, "field 'cancelTherapy'");
        target.jetLagChart = (JetLagChartView) finder.findRequiredView(source, R.id.jet_lag_chart, "field 'jetLagChart'");
        target.jetLagPercent = (ThinTextView) finder.findRequiredView(source, R.id.jet_lag_percent, "field 'jetLagPercent'");
        target.jetLagLeft = (ThinTextView) finder.findRequiredView(source, R.id.jet_lag_left, "field 'jetLagLeft'");
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        target.toolbar = (Toolbar) finder.findRequiredView(source, R.id.toolbar, "field 'toolbar'");
        View view = finder.findRequiredView(source, R.id.end_therapy, "field 'endTherapy' and method 'endTherapyClick'");
        target.endTherapy = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.endTherapyClick();
            }
        });
    }

    public static void reset(JetLagEndTherapyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.changeJourney = null;
        target.cancelTherapy = null;
        target.jetLagChart = null;
        target.jetLagPercent = null;
        target.jetLagLeft = null;
        target.mBottomToolbar = null;
        target.toolbar = null;
        target.endTherapy = null;
    }
}
