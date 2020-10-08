package com.inteliclinic.neuroon.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.adapters.FragmentStatePagerAdapter;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.views.LockableViewPager;

public abstract class BaseMainFragmentRight<T extends BaseFragment, V extends BaseFragment> extends BaseFragment {
    @InjectView(2131755341)
    LockableViewPager mPager;

    /* access modifiers changed from: protected */
    public abstract T createCenterFragment();

    /* access modifiers changed from: protected */
    public abstract V createRightFragment();

    /* access modifiers changed from: protected */
    public abstract String getScreenName(int i);

    public LockableViewPager getPager() {
        return this.mPager;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject((Object) this, inflate);
        resetAdapter();
        return inflate;
    }

    /* access modifiers changed from: protected */
    public T getCenterFragment() {
        if (this.mPager.getAdapter() == null) {
            return null;
        }
        return (BaseFragment) ((ScreenSlidePagerAdapter) this.mPager.getAdapter()).getItem(0);
    }

    /* access modifiers changed from: protected */
    public void resetAdapter() {
        this.mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BaseMainFragmentRight.this.getTracker().setScreenName(BaseMainFragmentRight.this.getScreenName(position));
                BaseMainFragmentRight.this.getTracker().send(new HitBuilders.ScreenViewBuilder().build());
            }
        });
        this.mPager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager()));
        this.mPager.setCurrentItem(0);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void showPage(int i) {
        this.mPager.setCurrentItem(i);
    }

    public int getPage() {
        if (this.mPager == null) {
            return -1;
        }
        return this.mPager.getCurrentItem();
    }

    public ViewPager getViewPager() {
        return this.mPager;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private V mActivityFeedFragment;
        private T mMainDashboardFragment;

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    if (this.mActivityFeedFragment == null) {
                        this.mActivityFeedFragment = BaseMainFragmentRight.this.createRightFragment();
                    }
                    return this.mActivityFeedFragment;
                default:
                    if (this.mMainDashboardFragment == null) {
                        this.mMainDashboardFragment = BaseMainFragmentRight.this.createCenterFragment();
                    }
                    return this.mMainDashboardFragment;
            }
        }

        public int getCount() {
            return 2;
        }
    }
}
