package com.inteliclinic.neuroon.fragments.statistics;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.adapters.FragmentStatePagerAdapter;
import com.inteliclinic.neuroon.events.DbSleepsUpdatedEvent;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.views.calendar.CalendarPickerDecoration;
import com.inteliclinic.neuroon.views.calendar.SleepDatesCalendarPickerView;
import com.inteliclinic.neuroon.views.calendar.SleepDatesHorizontalListView;
import de.greenrobot.event.EventBus;
import java.util.Date;

public class StatsDetailFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private static final String ARG_PAGE_TYPE = "page_type";
    private static final String ARG_SLEEP_ID = "arg_sleep_id";
    private static final String TAG = StatsDetailFragment.class.getSimpleName();
    @InjectView(2131755219)
    ImageView brainStats;
    @InjectView(2131755468)
    SleepDatesHorizontalListView calendarList;
    @InjectView(2131755470)
    ImageView calendarListLine;
    @InjectView(2131755469)
    ImageView calendarViewIco;
    @InjectView(2131755218)
    ImageView heartStats;
    @InjectView(2131755467)
    LinearLayout iconsLayout;
    @InjectView(2131755236)
    ImageView indicator;
    /* access modifiers changed from: private */
    public ScreenSlidePagerAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private StatsPage mPageType;
    /* access modifiers changed from: private */
    public long mSleepId;
    @InjectView(2131755471)
    ImageView slider;
    @InjectView(2131755217)
    ImageView timeStats;
    @InjectView(2131755472)
    ViewPager viewPager;

    public interface OnFragmentInteractionListener {
    }

    public enum StatsPage {
        TIME,
        HEART,
        BRAIN
    }

    public static StatsDetailFragment newInstance(long sleepId, StatsPage page) {
        StatsDetailFragment fragment = new StatsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SLEEP_ID, sleepId);
        args.putString(ARG_PAGE_TYPE, String.valueOf(page));
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mSleepId = getArguments().getLong(ARG_SLEEP_ID);
            this.mPageType = StatsPage.valueOf(getArguments().getString(ARG_PAGE_TYPE));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stats_detail, container, false);
        ButterKnife.inject((Object) this, view);
        setupPager();
        setupCalendarList();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                StatsDetailFragment.this.setupCalendarItem();
                if (!StatsDetailFragment.this.setupIndicator()) {
                    return true;
                }
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /* access modifiers changed from: private */
    public void setupCalendarItem() {
        if (this.mSleepId > 0) {
            this.calendarList.smoothScrollToSleep(this.mSleepId);
        }
    }

    private void setupCalendarList() {
        this.calendarList.initDateList();
        this.calendarList.addItemDecoration(new CalendarPickerDecoration(this.slider, this.calendarList, false));
        this.calendarList.setOnItemClickListener(new SleepDatesHorizontalListView.OnSleepClickListener() {
            public void onSleepClick(long sleepId) {
                if (sleepId == -1) {
                    StatsDetailFragment.this.onCloseButtonClick();
                } else if (StatsDetailFragment.this.mSleepId != sleepId) {
                    long unused = StatsDetailFragment.this.mSleepId = sleepId;
                    StatsDetailFragment.this.mAdapter.setSleepId(StatsDetailFragment.this.mSleepId);
                }
            }
        });
    }

    private void setupPager() {
        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                StatsDetailFragment.this.getTracker().setScreenName(StatsDetailFragment.this.getScreenName(position));
                StatsDetailFragment.this.getTracker().send(new HitBuilders.ScreenViewBuilder().build());
            }
        });
        this.mAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), this.mSleepId);
        this.viewPager.setAdapter(this.mAdapter);
        this.viewPager.addOnPageChangeListener(this);
        setupPage();
    }

    /* access modifiers changed from: protected */
    public String getScreenName(int position) {
        switch (position) {
            case 1:
                return "heart_rate";
            case 2:
                return "sleep_staging";
            default:
                return "sleep_duration";
        }
    }

    /* access modifiers changed from: private */
    public boolean setupIndicator() {
        FrameLayout.LayoutParams layoutParams;
        if (this.indicator == null || (layoutParams = (FrameLayout.LayoutParams) this.indicator.getLayoutParams()) == null) {
            return false;
        }
        layoutParams.leftMargin = (int) (((double) ((-this.indicator.getMeasuredWidth()) / 2)) + ((((double) this.iconsLayout.getMeasuredWidth()) / 3.0d) * (((double) this.mPageType.ordinal()) + 0.5d)));
        this.indicator.setLayoutParams(layoutParams);
        setupIcons();
        return true;
    }

    private void setupIcons() {
        switch (this.mPageType) {
            case TIME:
                this.timeStats.setEnabled(false);
                this.heartStats.setEnabled(true);
                this.brainStats.setEnabled(true);
                return;
            case HEART:
                this.timeStats.setEnabled(true);
                this.heartStats.setEnabled(false);
                this.brainStats.setEnabled(true);
                return;
            case BRAIN:
                this.timeStats.setEnabled(true);
                this.heartStats.setEnabled(true);
                this.brainStats.setEnabled(false);
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @OnClick({2131755469})
    public synchronized void onCalendarViewClick() {
        Sleep sleepById = DataManager.getInstance().getSleepById(this.mSleepId);
        Date date = null;
        if (sleepById != null) {
            date = sleepById.getStartDate();
        }
        try {
            SleepDatesCalendarPickerView.show(getView(), "", date, 0, new SleepDatesCalendarPickerView.OnDatePickerListener() {
                public void pickDate(Date time) {
                    if (time != null) {
                        long unused = StatsDetailFragment.this.mSleepId = DataManager.getInstance().getSleepIdByDate(time);
                        if (StatsDetailFragment.this.mSleepId == -1) {
                            long unused2 = StatsDetailFragment.this.mSleepId = DataManager.getInstance().getLastSleepByDate().getId();
                        }
                        StatsDetailFragment.this.calendarList.scrollToSleep(StatsDetailFragment.this.mSleepId);
                        StatsDetailFragment.this.mAdapter.setSleepId(StatsDetailFragment.this.mSleepId);
                    }
                }
            });
        } catch (UnsupportedOperationException e) {
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @OnClick({2131755131})
    public void onBackButtonClick() {
        onCloseButtonClick();
    }

    @OnClick({2131755217})
    public void onTimeStatsClick() {
        if (this.mPageType != StatsPage.TIME) {
            this.mPageType = StatsPage.TIME;
            setupPage();
        }
    }

    @OnClick({2131755218})
    public void onHeartStatsClick() {
        if (this.mPageType != StatsPage.HEART) {
            this.mPageType = StatsPage.HEART;
            setupPage();
        }
    }

    @OnClick({2131755219})
    public void onBrainStatsClick() {
        if (this.mPageType != StatsPage.BRAIN) {
            this.mPageType = StatsPage.BRAIN;
            setupPage();
        }
    }

    private void setupPage() {
        this.viewPager.setCurrentItem(this.mPageType.ordinal());
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.mPageType = StatsPage.values()[position];
        if (positionOffset == 0.0f) {
            setupIcons();
            return;
        }
        this.timeStats.setEnabled(true);
        this.heartStats.setEnabled(true);
        this.brainStats.setEnabled(true);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.indicator.getLayoutParams();
        layoutParams.leftMargin = (int) (((double) ((-this.indicator.getMeasuredWidth()) / 2)) + ((((double) this.iconsLayout.getMeasuredWidth()) / 3.0d) * (((double) this.mPageType.ordinal()) + 0.5d + ((double) positionOffset))));
        this.indicator.setLayoutParams(layoutParams);
    }

    public void onPageSelected(int position) {
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onEvent(DbSleepsUpdatedEvent event) {
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private BrainStatisticFragment mBrainFragment;
        private HeartStatisticFragment mHeartFragment;
        private long mSleepId;
        private TimeStatisticFragment mTimeFragment;

        ScreenSlidePagerAdapter(FragmentManager fm, long sleepId) {
            super(fm);
            this.mSleepId = sleepId;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    if (this.mHeartFragment == null) {
                        this.mHeartFragment = HeartStatisticFragment.newInstance(this.mSleepId);
                    }
                    return this.mHeartFragment;
                case 2:
                    if (this.mBrainFragment == null) {
                        this.mBrainFragment = BrainStatisticFragment.newInstance(this.mSleepId);
                    }
                    return this.mBrainFragment;
                default:
                    if (this.mTimeFragment == null) {
                        this.mTimeFragment = TimeStatisticFragment.newInstance(this.mSleepId);
                    }
                    return this.mTimeFragment;
            }
        }

        public int getCount() {
            return 3;
        }

        public void setSleepId(long sleepId) {
            this.mSleepId = sleepId;
            if (this.mBrainFragment != null) {
                this.mBrainFragment.presentSleep(this.mSleepId);
            }
            if (this.mHeartFragment != null) {
                this.mHeartFragment.presentSleep(this.mSleepId);
            }
            if (this.mTimeFragment != null) {
                this.mTimeFragment.presentSleep(this.mSleepId);
            }
        }
    }
}
