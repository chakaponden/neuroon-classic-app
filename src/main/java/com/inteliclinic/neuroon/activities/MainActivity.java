package com.inteliclinic.neuroon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.AboutAppFragment;
import com.inteliclinic.neuroon.fragments.AlarmsFragment;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.BiorhythmFragment;
import com.inteliclinic.neuroon.fragments.ChangePasswordFragment;
import com.inteliclinic.neuroon.fragments.EnergyPlusFragment;
import com.inteliclinic.neuroon.fragments.NapDetailsFragment;
import com.inteliclinic.neuroon.fragments.NavigationDrawerFragment;
import com.inteliclinic.neuroon.fragments.OneDayAlarmFragment;
import com.inteliclinic.neuroon.fragments.ProfileFragment;
import com.inteliclinic.neuroon.fragments.TermsAndConditionsFragment;
import com.inteliclinic.neuroon.fragments.dashboard.ActivityFeedFragment;
import com.inteliclinic.neuroon.fragments.dashboard.DashboardFragment;
import com.inteliclinic.neuroon.fragments.dashboard.MainFragment;
import com.inteliclinic.neuroon.fragments.dashboard.ProgressFragment;
import com.inteliclinic.neuroon.fragments.first_time.AboutYouFragment;
import com.inteliclinic.neuroon.fragments.first_time.DateOfBirthFragment;
import com.inteliclinic.neuroon.fragments.first_time.HeightFragment;
import com.inteliclinic.neuroon.fragments.first_time.SexFragment;
import com.inteliclinic.neuroon.fragments.first_time.WeightFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagEndTherapyFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagFirstHelpFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagProgressFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagTherapyEventsDescriptionFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetlagTherapyFragment;
import com.inteliclinic.neuroon.fragments.jetlag.MainJetLagTherapyFragment;
import com.inteliclinic.neuroon.fragments.jetlag.StartJetLagTherapyFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskFragment;
import com.inteliclinic.neuroon.fragments.statistics.StatsDetailFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.InvalidRefreshTokenEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.NewSleepAvailableEvent;
import com.inteliclinic.neuroon.models.data.Event;
import de.greenrobot.event.EventBus;
import java.util.Date;

public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ProfileFragment.OnFragmentInteractionListener, DashboardFragment.OnFragmentInteractionListener, EnergyPlusFragment.OnFragmentInteractionListener, JetlagTherapyFragment.OnFragmentInteractionListener, NapDetailsFragment.OnFragmentInteractionListener, AlarmsFragment.OnFragmentInteractionListener, JetLagProgressFragment.OnFragmentInteractionListener, ProgressFragment.OnFragmentInteractionListener, MaskFragment.OnFragmentInteractionListener, ActivityFeedFragment.OnFragmentInteractionListener, ChangePasswordFragment.OnFragmentInteractionListener, StatsDetailFragment.OnFragmentInteractionListener, JetLagEndTherapyFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener, AboutAppFragment.OnFragmentInteractionListener, TermsAndConditionsFragment.OnFragmentInteractionListener, StartJetLagTherapyFragment.OnFragmentInteractionListener, JetLagFirstHelpFragment.OnFragmentInteractionListener, BaseFragment.OnFragmentInteractionListener {
    private static final String BUNDLE_ALARM_OPEN = "bundle_alarm_open";
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean mIsDismissing;
    private int mMainFragmentIdentifier;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static Intent openAlarmsIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(BUNDLE_ALARM_OPEN, true);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        this.mMainFragmentIdentifier = getFragmentManager().beginTransaction().add(R.id.container, MainFragment.newInstance()).addToBackStack((String) null).commit();
        this.mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(BUNDLE_ALARM_OPEN, false)) {
            openAlarms();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (this.mIsDismissing) {
            getFragmentManager().popBackStackImmediate();
            this.mIsDismissing = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0 || (getActualFragment() instanceof MainFragment)) {
            ActivityCompat.finishAfterTransition(this);
        }
        if (!getFragmentManager().popBackStackImmediate()) {
            supportFinishAfterTransition();
        }
    }

    public void onBackStackChanged() {
        super.onBackStackChanged();
        if (!(getActualFragment() instanceof MainFragment) || ((MainFragment) getActualFragment()).getPage() != 1) {
            this.mNavigationDrawerFragment.disableMenuSwipe();
            return;
        }
        this.mNavigationDrawerFragment.enableMenuSwipe();
        ((MainFragment) getActualFragment()).getViewPager().addOnPageChangeListener(this);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        if (position == 1) {
            this.mNavigationDrawerFragment.enableMenuSwipe();
        } else {
            this.mNavigationDrawerFragment.disableMenuSwipe();
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onNavigationDrawerItemSelected(NavigationDrawerFragment.MenuItem position) {
        setFragment(pickFragment(position));
    }

    public void setFragment(BaseFragment baseFragment) {
        getFragmentManager().beginTransaction().replace(R.id.container, baseFragment).addToBackStack((String) null).commit();
    }

    public void setFragment(BaseFragment baseFragment, boolean addToBackstack) {
        if (addToBackstack) {
            setFragment(baseFragment);
        } else {
            getFragmentManager().beginTransaction().replace(R.id.container, baseFragment).commit();
        }
    }

    private BaseFragment pickFragment(NavigationDrawerFragment.MenuItem position) {
        switch (position) {
            case NEUROON_MASK:
                return MaskFragment.newInstance();
            case PROFILE:
                return ProfileFragment.newInstance();
            case ALARMS:
                return AlarmsFragment.newInstance();
            case JET_LAG_BLOCKER:
                return JetlagTherapyFragment.newInstance();
            case TERMS_AND_CONDITIONS:
                return TermsAndConditionsFragment.newInstance();
            case ABOUT:
                return AboutAppFragment.newInstance();
            default:
                Snackbar.make(getWindow().getDecorView(), (CharSequence) "Menu not found, opening sleep score", 0).show();
                return MainFragment.newInstance();
        }
    }

    public void onEventMainThread(NewSleepAvailableEvent event) {
        startActivity(new Intent(this, MaskSynchronizationActivity.class));
    }

    public void onEventMainThread(InvalidRefreshTokenEvent event) {
        logOut();
    }

    public void openGender() {
        setFragment(SexFragment.newInstance(true));
    }

    public void openDateOfBirth() {
        setFragment(DateOfBirthFragment.newInstance(true));
    }

    public void openHeight() {
        setFragment(HeightFragment.newInstance(true));
    }

    public void openWeight() {
        setFragment(WeightFragment.newInstance(true));
    }

    public void openAboutYou() {
        setFragment(AboutYouFragment.newInstance(true));
    }

    public void closeFragments() {
        getFragmentManager().popBackStack(this.mMainFragmentIdentifier, 0);
    }

    public void openPlanJourney() {
        startActivity(new Intent(this, JetLagPlannerActivity.class));
    }

    public void openJetLagFirstTimeHint() {
        getFragmentManager().beginTransaction().add(R.id.container, JetLagFirstHelpFragment.newInstance()).commit();
    }

    public void openJetLagBlockerInfo() {
        setFragment(JetLagTherapyEventsDescriptionFragment.newInstance(), true);
    }

    public void goToJetLagProgress() {
        if (getActualFragment() != null && (getActualFragment() instanceof MainJetLagTherapyFragment)) {
            ((MainJetLagTherapyFragment) getActualFragment()).showPage(1);
        }
    }

    public void openMaskSoftware() {
        startActivity(new Intent(this, MaskUpdateActivity.class));
    }

    public void unPairMask() {
        MaskManager.getInstance().unPairMask();
        DataManager.getInstance().deleteAllSleeps();
        DataManager.getInstance().revalidateMaskEvents();
        startActivity(new Intent(this, LauncherActivity.class));
        finish();
    }

    public void openLogs() {
    }

    public void changePassword() {
        setFragment(ChangePasswordFragment.newInstance());
    }

    public void lightBoostClick() {
        setFragment(EnergyPlusFragment.newInstance());
    }

    public void jetlagClick() {
        setFragment(MainJetLagTherapyFragment.newInstance());
    }

    public void sleepScoreClick() {
        setFragment(MainFragment.newInstance());
    }

    public void toggleMenu() {
        this.mNavigationDrawerFragment.toggleDrawer();
    }

    public void goToDashboard() {
        if (getActualFragment() != null && (getActualFragment() instanceof MainFragment)) {
            ((MainFragment) getActualFragment()).showPage(1);
        }
    }

    public void showEventDetails(Event event) {
        EventDetailsActivity.openEvent(this, event);
    }

    public void goToJetLagMain() {
        if (getActualFragment() != null && (getActualFragment() instanceof MainJetLagTherapyFragment)) {
            ((MainJetLagTherapyFragment) getActualFragment()).showPage(0);
        }
    }

    public void timeStatsClick() {
        EventDetailsActivity.openSleep(this, DataManager.getInstance().getLastSleepByDateId(), StatsDetailFragment.StatsPage.TIME);
    }

    public void heartStatsClick() {
        EventDetailsActivity.openSleep(this, DataManager.getInstance().getLastSleepByDateId(), StatsDetailFragment.StatsPage.HEART);
    }

    public void brainStatsClick() {
        EventDetailsActivity.openSleep(this, DataManager.getInstance().getLastSleepByDateId(), StatsDetailFragment.StatsPage.BRAIN);
    }

    public void openAlarms() {
        setFragment(AlarmsFragment.newInstance());
    }

    public void startSleepTherapy() {
        int minutes;
        long timeNow = new Date().getTime() % 86400000;
        long timeScheduled = AccountManager.getInstance().getNextEventTimeMillis() % 86400000;
        if (timeScheduled > timeNow) {
            minutes = (int) ((timeScheduled - timeNow) / 60000);
        } else {
            minutes = (int) (((86400000 - timeNow) + timeScheduled) / 60000);
        }
        TherapyActivity.startActivity(this, getString(R.string.sleep), minutes * 60, Event.EventType.ETSleep);
    }

    public void goToProgress() {
        if (getActualFragment() != null && (getActualFragment() instanceof MainFragment)) {
            ((MainFragment) getActualFragment()).showPage(2);
        }
    }

    public void goToActivityFeed() {
        if (getActualFragment() != null && (getActualFragment() instanceof MainFragment)) {
            ((MainFragment) getActualFragment()).showPage(0);
        }
    }

    public void openMask() {
        setFragment(MaskFragment.newInstance());
    }

    public void startNapTherapy(String name, int timeSeconds, Event.EventType eventType) {
        switch (eventType) {
            case ETNapBody:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personal_pause").setAction("body_nap").setLabel("body_nap").build());
                break;
            case ETNapUltimate:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personal_pause").setAction("ultimate_nap").setLabel("ultimate_nap").build());
                break;
            case ETNapPower:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personal_pause").setAction("power_nap").setLabel("power_nap").build());
                break;
            case ETNapRem:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personal_pause").setAction("rem_nap").setLabel("rem_nap").build());
                break;
        }
        TherapyActivity.startActivity(this, name, timeSeconds, eventType);
    }

    public void startLightTherapy(int timeMilliseconds) {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("personal_pause").setAction("light_boost").setLabel("light_boost").build());
        TherapyActivity.startActivity(this, getString(R.string.light_boost), 1200, Event.EventType.ETBLT);
    }

    public void startTherapy() {
    }

    public void openBiorhythm() {
        setFragment(BiorhythmFragment.newInstance());
    }

    public void openOneDayAlarm() {
        setFragment(OneDayAlarmFragment.newInstance());
    }

    public void setHintAsShown() {
        AccountManager.getInstance().setJetLagMainScreenHelpShown();
    }

    public void passwordChanged() {
        Snackbar.make(getWindow().getDecorView(), (int) R.string.password_reset, 0).show();
        getFragmentManager().popBackStack();
    }
}
