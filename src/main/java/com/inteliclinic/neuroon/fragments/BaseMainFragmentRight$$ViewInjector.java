package com.inteliclinic.neuroon.fragments;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LockableViewPager;

public class BaseMainFragmentRight$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, BaseMainFragmentRight target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mPager = (LockableViewPager) finder.findRequiredView(source, R.id.pager, "field 'mPager'");
    }

    public static void reset(BaseMainFragmentRight target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mPager = null;
    }
}
