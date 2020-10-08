package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinTextView;

public class JetLagTherapyEventsDescriptionFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagTherapyEventsDescriptionFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.imageBrainwaves = (ImageView) finder.findRequiredView(source, R.id.image_brainwaves, "field 'imageBrainwaves'");
        target.awake = (ThinTextView) finder.findRequiredView(source, R.id.awake, "field 'awake'");
        target.asleep = (ThinTextView) finder.findRequiredView(source, R.id.asleep, "field 'asleep'");
        target.startTherapy = (ThinTextView) finder.findRequiredView(source, R.id.start_therapy, "field 'startTherapy'");
        finder.findRequiredView(source, R.id.button, "method 'onCloseClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCloseClick();
            }
        });
    }

    public static void reset(JetLagTherapyEventsDescriptionFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.imageBrainwaves = null;
        target.awake = null;
        target.asleep = null;
        target.startTherapy = null;
    }
}
