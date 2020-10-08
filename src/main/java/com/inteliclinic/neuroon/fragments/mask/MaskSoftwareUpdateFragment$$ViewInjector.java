package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;

public class MaskSoftwareUpdateFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskSoftwareUpdateFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.updateProgressText = (ThinTextView) finder.findRequiredView(source, R.id.update_progress_text, "field 'updateProgressText'");
        target.updateProgressProgress = (ProgressBar) finder.findRequiredView(source, R.id.update_progress_progress, "field 'updateProgressProgress'");
        target.updateProgress = (LinearLayout) finder.findRequiredView(source, R.id.update_progress, "field 'updateProgress'");
        View view = finder.findRequiredView(source, R.id.start_update, "field 'startUpdate' and method 'onStartUpdate'");
        target.startUpdate = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartUpdate();
            }
        });
    }

    public static void reset(MaskSoftwareUpdateFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.updateProgressText = null;
        target.updateProgressProgress = null;
        target.updateProgress = null;
        target.startUpdate = null;
    }
}
