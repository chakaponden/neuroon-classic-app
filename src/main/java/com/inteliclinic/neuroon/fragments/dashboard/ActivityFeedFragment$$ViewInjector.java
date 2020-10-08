package com.inteliclinic.neuroon.fragments.dashboard;

import android.support.v7.widget.RecyclerView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class ActivityFeedFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, ActivityFeedFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mRecyclerView = (RecyclerView) finder.findRequiredView(source, R.id.recycler_view, "field 'mRecyclerView'");
    }

    public static void reset(ActivityFeedFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mRecyclerView = null;
    }
}
