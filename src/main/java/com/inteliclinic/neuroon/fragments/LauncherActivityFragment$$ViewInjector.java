package com.inteliclinic.neuroon.fragments;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.pnikosis.materialishprogress.ProgressWheel;

public class LauncherActivityFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, LauncherActivityFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.progressWheel = (ProgressWheel) finder.findRequiredView(source, R.id.progress_wheel, "field 'progressWheel'");
    }

    public static void reset(LauncherActivityFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.progressWheel = null;
    }
}
