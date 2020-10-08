package com.inteliclinic.neuroon.views.dashboard;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BaseTextView;

public class DashboardStatsView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, DashboardStatsView target, Object source) {
        target.mIconView = (ImageView) finder.findRequiredView(source, R.id.stats_ico, "field 'mIconView'");
        target.mValueView = (BaseTextView) finder.findRequiredView(source, R.id.stats_value, "field 'mValueView'");
        target.mUnitView = (BaseTextView) finder.findRequiredView(source, R.id.stats_unit, "field 'mUnitView'");
    }

    public static void reset(DashboardStatsView target) {
        target.mIconView = null;
        target.mValueView = null;
        target.mUnitView = null;
    }
}
