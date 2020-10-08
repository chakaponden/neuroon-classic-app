package com.inteliclinic.neuroon.fragments;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LockableViewPager;

public class BaseMainFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, BaseMainFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mPager = (LockableViewPager) finder.findRequiredView(source, R.id.pager, "field 'mPager'");
    }

    public static void reset(BaseMainFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mPager = null;
    }
}
