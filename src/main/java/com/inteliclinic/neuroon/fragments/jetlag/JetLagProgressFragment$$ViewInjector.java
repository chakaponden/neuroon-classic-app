package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.WorldMapRoadView;
import com.inteliclinic.neuroon.views.charts.BiorhythmSynchronizationChartView;

public class JetLagProgressFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagProgressFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.therapyLength = (ThinTextView) finder.findRequiredView(source, R.id.therapy_length, "field 'therapyLength'");
        target.therapyStartsLength = (ThinTextView) finder.findRequiredView(source, R.id.therapy_starts_length, "field 'therapyStartsLength'");
        target.therapyStartsBefore = (ThinTextView) finder.findRequiredView(source, R.id.therapy_starts_before, "field 'therapyStartsBefore'");
        target.therapyFrom = (ThinTextView) finder.findRequiredView(source, R.id.therapy_from, "field 'therapyFrom'");
        target.therapyTo = (ThinTextView) finder.findRequiredView(source, R.id.therapy_to, "field 'therapyTo'");
        target.timezones = (ThinTextView) finder.findRequiredView(source, R.id.timezones, "field 'timezones'");
        target.biorhythmSyncChart = (BiorhythmSynchronizationChartView) finder.findRequiredView(source, R.id.biorhythm_sync_chart, "field 'biorhythmSyncChart'");
        target.map = (WorldMapRoadView) finder.findRequiredView(source, R.id.map, "field 'map'");
        finder.findRequiredView(source, R.id.go_to_main, "method 'onBackButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBackButtonClick();
            }
        });
    }

    public static void reset(JetLagProgressFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.therapyLength = null;
        target.therapyStartsLength = null;
        target.therapyStartsBefore = null;
        target.therapyFrom = null;
        target.therapyTo = null;
        target.timezones = null;
        target.biorhythmSyncChart = null;
        target.map = null;
    }
}
