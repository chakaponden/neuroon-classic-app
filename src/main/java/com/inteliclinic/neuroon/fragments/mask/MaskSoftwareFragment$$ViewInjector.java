package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;

public class MaskSoftwareFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskSoftwareFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.currentSoftware = (ThinTextView) finder.findRequiredView(source, R.id.current_software, "field 'currentSoftware'");
        View view = finder.findRequiredView(source, R.id.check_for_update, "field 'checkForUpdate' and method 'onCheckForUpdateClick'");
        target.checkForUpdate = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCheckForUpdateClick();
            }
        });
    }

    public static void reset(MaskSoftwareFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.currentSoftware = null;
        target.checkForUpdate = null;
    }
}
