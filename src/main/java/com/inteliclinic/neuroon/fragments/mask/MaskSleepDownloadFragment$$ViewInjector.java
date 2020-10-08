package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;

public class MaskSleepDownloadFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskSleepDownloadFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.downloadProgressProgress = (ProgressBar) finder.findRequiredView(source, R.id.download_progress_progress, "field 'downloadProgressProgress'");
        target.downloadProgressText = (LightTextView) finder.findRequiredView(source, R.id.download_progress_text, "field 'downloadProgressText'");
        finder.findRequiredView(source, R.id.cancel_button, "method 'onCancelButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelButtonClick();
            }
        });
    }

    public static void reset(MaskSleepDownloadFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.downloadProgressProgress = null;
        target.downloadProgressText = null;
    }
}
