package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;

public class MaskNewSoftwareFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskNewSoftwareFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.currentSoftware = (ThinTextView) finder.findRequiredView(source, R.id.current_software, "field 'currentSoftware'");
        target.newSoftware = (ThinTextView) finder.findRequiredView(source, R.id.new_software, "field 'newSoftware'");
        View view = finder.findRequiredView(source, R.id.update, "field 'update' and method 'onUpdateClick'");
        target.update = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onUpdateClick();
            }
        });
        finder.findRequiredView(source, R.id.close_button, "method 'onCloseButton'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCloseButton();
            }
        });
    }

    public static void reset(MaskNewSoftwareFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.currentSoftware = null;
        target.newSoftware = null;
        target.update = null;
    }
}
