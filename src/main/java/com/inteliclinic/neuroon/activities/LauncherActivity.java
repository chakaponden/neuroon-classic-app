package com.inteliclinic.neuroon.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.events.TriggerConfigUpdate;
import com.inteliclinic.neuroon.events.TriggerSleepUpdate;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.LauncherActivityFragment;
import com.inteliclinic.neuroon.fragments.TermsAndConditionsFragment;
import com.inteliclinic.neuroon.fragments.onboarding.AreYouSureToLeaveFragment;
import com.inteliclinic.neuroon.fragments.onboarding.LoginFragment;
import com.inteliclinic.neuroon.fragments.onboarding.RegisterFragment;
import com.inteliclinic.neuroon.fragments.onboarding.ResetPasswordFragment;
import com.inteliclinic.neuroon.fragments.onboarding.SetUpFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.ConfigsUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.InvalidRefreshTokenEvent;
import com.inteliclinic.neuroon.managers.account.events.NetworkErrorEvent;
import com.inteliclinic.neuroon.managers.network.NetworkManager;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.models.network.User;
import com.inteliclinic.neuroon.service.ServiceStartedEvent;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import de.greenrobot.event.EventBus;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class LauncherActivity extends BaseActivity implements SetUpFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, ResetPasswordFragment.OnFragmentInteractionListener, BaseFragment.OnFragmentInteractionListener, AreYouSureToLeaveFragment.OnFragmentInteractionListener, TermsAndConditionsFragment.OnFragmentInteractionListener {
    private static final int REQ_ACCESS_COARSE_LOCATION = 71;
    private static final int REQ_PAIR_MASK = 9030;
    private static final String TAG = LauncherActivity.class.getSimpleName();
    private AreYouSureToLeaveFragment mAreYouSureFragment;
    private Handler mHandler;
    private boolean mOpenedMain;
    private long mStartTime;
    private boolean mTriggerConfigUpdate;

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (locationMode != 0) {
                return true;
            }
            return false;
        } else if (TextUtils.isEmpty(Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed"))) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_launcher);
        ButterKnife.inject((Activity) this);
        this.mHandler = new Handler(Looper.getMainLooper());
        startLauncher();
    }

    private void startLauncher() {
        this.mStartTime = System.currentTimeMillis();
        getFragmentManager().beginTransaction().add(R.id.container, new LauncherActivityFragment()).commitAllowingStateLoss();
    }

    private boolean requestBlePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isLocationEnabled(this)) {
                new AlertDialog.Builder(this).setMessage(R.string.open_location_settings_matter).setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LauncherActivity.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LauncherActivity.this.checkAndOpenMainActivity(false);
                    }
                }).show();
                return false;
            } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_COARSE_LOCATION")) {
                    Snackbar.make(getWindow().getDecorView(), (int) R.string.permissions_information, 0).show();
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 71);
                    return false;
                }
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 71);
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 71:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    finish();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQ_PAIR_MASK) {
            return;
        }
        if (resultCode == -1) {
            checkAndOpenMainActivity(true);
        } else {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        EventBus.getDefault().registerSticky(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onBackPressed() {
        if (!goBack()) {
            getFragmentManager().popBackStack();
        }
    }

    public boolean goBack() {
        if (!(getFragmentManager().findFragmentById(R.id.container) instanceof SetUpFragment)) {
            return super.goBack();
        }
        finish();
        return true;
    }

    public void onOpenSignUpClick() {
        getFragmentManager().beginTransaction().replace(R.id.container, RegisterFragment.newInstance()).addToBackStack((String) null).commitAllowingStateLoss();
    }

    public void onOpenLogInClick() {
        getFragmentManager().beginTransaction().replace(R.id.container, LoginFragment.newInstance()).addToBackStack((String) null).commitAllowingStateLoss();
    }

    public void loginSuccess(String username) {
        Crashlytics.setUserName(AccountManager.getInstance().getUsername());
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().registerIdentifiedUser(new Registration().withEmail(username));
        }
        EventBus.getDefault().post(new TriggerConfigUpdate());
        EventBus.getDefault().post(new TriggerSleepUpdate());
        this.mTriggerConfigUpdate = true;
    }

    public final void onEvent(ServiceStartedEvent event) {
        long l = 1800 - (System.currentTimeMillis() - this.mStartTime);
        Handler handler = this.mHandler;
        AnonymousClass3 r5 = new Runnable() {
            public void run() {
                LauncherActivity.this.checkAndOpenMainActivity(true);
            }
        };
        if (l <= 0) {
            l = 0;
        }
        handler.postDelayed(r5, l);
    }

    public final void onEvent(ConfigsUpdatedEvent event) {
        if (this.mTriggerConfigUpdate) {
            this.mTriggerConfigUpdate = false;
            setProgress(false);
            if (!AccountManager.getInstance().hasPersonalData()) {
                startActivity(new Intent(this, FirstSetUpActivity.class));
                return;
            }
            if (MaskManager.getInstance().pairedWithDevice()) {
                BleManager.getInstance().scanForDevices();
            }
            checkAndOpenMainActivity(true);
        }
    }

    public void onEvent(NetworkErrorEvent event) {
        if (event.getRequestKey() == NetworkManager.RequestKey.USER_CONFIG) {
            setProgress(false);
            if (AccountManager.getInstance().hasConfig()) {
                checkAndOpenMainActivity(true);
                return;
            }
            AccountManager.getInstance().signOut();
            if (getFragmentManager().findFragmentById(R.id.container) instanceof LoginFragment) {
                Snackbar.make(getWindow().getDecorView(), (int) R.string.unexpected_error_occurred, 0).show();
            } else {
                checkAndOpenMainActivity(true);
            }
            Crashlytics.logException(new UnsupportedOperationException("User logged in and config not downloaded"));
        } else if (event.getRequestKey() == NetworkManager.RequestKey.TIPS) {
            setProgress(false);
        }
    }

    public void onEvent(InvalidRefreshTokenEvent event) {
        if (getFragmentManager().findFragmentById(R.id.container) instanceof LoginFragment) {
            Snackbar.make(getWindow().getDecorView(), (int) R.string.unexpected_error_occurred, 0).show();
        } else {
            checkAndOpenMainActivity(true);
        }
    }

    /* access modifiers changed from: private */
    public void checkAndOpenMainActivity(boolean checkPermissions) {
        KeyboardUtils.hideKeyboard(this);
        if (!this.mOpenedMain) {
            if (checkPermissions && !requestBlePermissions()) {
                return;
            }
            if (!AccountManager.getInstance().isLogged()) {
                if (AccountManager.getInstance().isIntercomEnabled()) {
                    Intercom.client().registerUnidentifiedUser();
                }
                setActualFragment(SetUpFragment.newInstance());
                if (!isFinishing() && !isDestroyed()) {
                    getFragmentManager().beginTransaction().setCustomAnimations(17498112, 17498113).replace(R.id.container, getActualFragment()).commitAllowingStateLoss();
                    return;
                }
                return;
            }
            Crashlytics.setUserName(AccountManager.getInstance().getUsername());
            if (AccountManager.getInstance().isIntercomEnabled()) {
                Intercom.client().registerIdentifiedUser(new Registration().withEmail(AccountManager.getInstance().getUsername()));
            }
            if (!AccountManager.getInstance().hasConfig()) {
                this.mTriggerConfigUpdate = true;
                EventBus.getDefault().post(new TriggerConfigUpdate());
                setProgress(true);
            } else if (!MaskManager.getInstance().pairedWithDevice()) {
                startActivityForResult(new Intent(this, PairMaskActivity.class), REQ_PAIR_MASK);
            } else {
                this.mOpenedMain = true;
                setProgress(false);
                if (EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().unregister(this);
                }
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    public void requestReset() {
        getFragmentManager().beginTransaction().addToBackStack((String) null).replace(R.id.container, ResetPasswordFragment.newInstance()).commit();
    }

    public void registerSuccess(User user, String password) {
        getFragmentManager().beginTransaction().addToBackStack((String) null).replace(R.id.container, LoginFragment.newInstance(user.getUsername(), password)).commit();
    }

    public void openTermsAndConditions() {
        getFragmentManager().beginTransaction().addToBackStack((String) null).replace(R.id.container, TermsAndConditionsFragment.newInstance()).commit();
    }

    public void passwordReset() {
        Snackbar.make(getWindow().getDecorView(), (int) R.string.password_reset, 0).show();
        getFragmentManager().popBackStack();
    }

    public void closeFragments() {
    }

    public void onLeaveClick() {
        getFragmentManager().popBackStack();
        checkAndOpenMainActivity(true);
    }

    public void onContinueClick() {
        getFragmentManager().popBackStack();
        this.mAreYouSureFragment = null;
    }
}
