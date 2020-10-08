package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.ThinTextView;
import io.intercom.android.sdk.Intercom;

public class NavigationDrawerFragment extends Fragment {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private NavigationDrawerCallbacks mCallbacks;
    private MenuItem mCurrentSelectedPosition = null;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    @InjectView(2131755400)
    ThinTextView talkWithUs;
    @InjectView(2131755399)
    View talkWithUsDivider;

    public enum MenuItem {
        NEUROON_MASK,
        PROFILE,
        ALARMS,
        JET_LAG_BLOCKER,
        SCIENCE_OF_SLEEP,
        FAQ,
        TERMS_AND_CONDITIONS,
        PRIVACY_POLICY,
        TALK_WITH_US,
        ABOUT,
        REPORT_A_PROBLEM
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(MenuItem menuItem);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                this.mCurrentSelectedPosition = MenuItem.valueOf(savedInstanceState.getString(STATE_SELECTED_POSITION));
            } catch (IllegalArgumentException e) {
                this.mCurrentSelectedPosition = null;
            }
        }
        selectItem(this.mCurrentSelectedPosition);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i;
        int i2 = 0;
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.inject((Object) this, view);
        ThinTextView thinTextView = this.talkWithUs;
        if (AccountManager.getInstance().isIntercomEnabled()) {
            i = 0;
        } else {
            i = 8;
        }
        thinTextView.setVisibility(i);
        View view2 = this.talkWithUsDivider;
        if (!AccountManager.getInstance().isIntercomEnabled()) {
            i2 = 8;
        }
        view2.setVisibility(i2);
        return view;
    }

    public boolean isDrawerOpen() {
        return this.mDrawerLayout != null && this.mDrawerLayout.isDrawerOpen(this.mFragmentContainerView);
    }

    public void toggleDrawer() {
        if (this.mDrawerLayout != null) {
            if (isDrawerOpen()) {
                this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
            } else {
                this.mDrawerLayout.openDrawer(this.mFragmentContainerView);
            }
        }
    }

    public void disableMenuSwipe() {
        if (this.mDrawerLayout != null) {
            this.mDrawerLayout.setDrawerLockMode(1);
        }
    }

    public void enableMenuSwipe() {
        if (this.mDrawerLayout != null) {
            this.mDrawerLayout.setDrawerLockMode(0);
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        this.mFragmentContainerView = getActivity().findViewById(fragmentId);
        this.mDrawerLayout = drawerLayout;
        this.mDrawerLayout.setDrawerShadow((int) R.drawable.drawer_shadow, 8388611);
    }

    private void selectItem(MenuItem position) {
        this.mCurrentSelectedPosition = position;
        if (this.mDrawerLayout != null) {
            this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
        }
        if (this.mCallbacks != null && position != null) {
            this.mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SELECTED_POSITION, String.valueOf(this.mCurrentSelectedPosition));
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755394})
    public void onNeuroonClick() {
        selectItem(MenuItem.NEUROON_MASK);
    }

    @OnClick({2131755395})
    public void onProfileClick() {
        selectItem(MenuItem.PROFILE);
    }

    @OnClick({2131755209})
    public void onAlarmsClick() {
        selectItem(MenuItem.ALARMS);
    }

    @OnClick({2131755397})
    public void onScienceOfSleepClick() {
        selectItem(MenuItem.SCIENCE_OF_SLEEP);
    }

    @OnClick({2131755402})
    public void onTermsAndConditionsClick() {
        selectItem(MenuItem.TERMS_AND_CONDITIONS);
    }

    @OnClick({2131755403})
    public void onAboutClick() {
        selectItem(MenuItem.ABOUT);
    }

    @OnClick({2131755404})
    public void onPrivacyPolicyClick() {
        selectItem(MenuItem.PRIVACY_POLICY);
    }

    @OnClick({2131755400})
    public void onTalkWithUsClick() {
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().displayMessageComposer();
        }
    }

    @OnClick({2131755398})
    public void onFAQClick() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.faq_uri))));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(getView(), (CharSequence) "No application can handle this request. Please install a web browser", 0).show();
            e.printStackTrace();
        }
    }

    @OnClick({2131755401})
    public void onReportAProblemClick() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.report_problem_uri))));
        } catch (ActivityNotFoundException e) {
            Snackbar.make(getView(), (CharSequence) "No application can handle this request. Please install a web browser", 0).show();
            e.printStackTrace();
        }
    }
}
