package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;

public class MaskSoftwareUpdateFailedFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskSoftwareUpdateFailedFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.start_update, "field 'startUpdate' and method 'onStartUpdate'");
        target.startUpdate = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartUpdate();
            }
        });
    }

    public static void reset(MaskSoftwareUpdateFailedFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.startUpdate = null;
    }
}
