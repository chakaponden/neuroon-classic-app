package com.inteliclinic.neuroon.fragments.dashboard;

import com.inteliclinic.neuroon.fragments.BaseMainFragment;

public class MainFragment extends BaseMainFragment<ActivityFeedFragment, DashboardFragment, ProgressFragment> {
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    /* access modifiers changed from: protected */
    public String getScreenName(int position) {
        switch (position) {
            case 0:
                return "activity_feed";
            case 2:
                return "sleep_analytics";
            default:
                return "main_screen";
        }
    }

    /* access modifiers changed from: protected */
    public DashboardFragment createCenterFragment() {
        return DashboardFragment.newInstance();
    }

    /* access modifiers changed from: protected */
    public ProgressFragment createRightFragment() {
        return ProgressFragment.newInstance();
    }

    /* access modifiers changed from: protected */
    public ActivityFeedFragment createLeftFragment() {
        return ActivityFeedFragment.newInstance();
    }
}
