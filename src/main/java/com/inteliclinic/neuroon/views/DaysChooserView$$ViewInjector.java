package com.inteliclinic.neuroon.views;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class DaysChooserView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, DaysChooserView target, Object source) {
        target.slider = (ImageView) finder.findRequiredView(source, R.id.slider, "field 'slider'");
        target.days30 = (ThinTextView) finder.findRequiredView(source, R.id.days_30, "field 'days30'");
        target.days7 = (ThinTextView) finder.findRequiredView(source, R.id.days_7, "field 'days7'");
    }

    public static void reset(DaysChooserView target) {
        target.slider = null;
        target.days30 = null;
        target.days7 = null;
    }
}
