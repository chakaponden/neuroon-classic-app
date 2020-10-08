package com.inteliclinic.neuroon.activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.Tracker;
import com.inteliclinic.neuroon.NeuroonApplication;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnProgressListener;
import com.inteliclinic.neuroon.fragments.loading.LoadingFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.UserAccountStatusChangedEvent;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import io.intercom.android.sdk.Intercom;

public abstract class BaseActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, OnProgressListener, BaseFragment.OnFragmentInteractionListener {
    private BaseFragment mActualFragment;
    private LoadingFragment mLoadingFragment;
    private AlertDialog mThiefAlertDialog;
    private Tracker mTracker;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().addOnBackStackChangedListener(this);
        this.mTracker = ((NeuroonApplication) getApplication()).getDefaultTracker();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().removeOnBackStackChangedListener(this);
        if (this.mThiefAlertDialog != null) {
            this.mThiefAlertDialog.dismiss();
            this.mThiefAlertDialog = null;
        }
    }

    public void onBackStackChanged() {
        this.mActualFragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.container);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (AccountManager.getInstance().canUserUseApp() == 1) {
            showThiefInformation();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void setProgress(boolean isProgress) {
        if (this.mLoadingFragment == null) {
            if (isProgress) {
                this.mLoadingFragment = getLoadingFragment();
            } else {
                return;
            }
        }
        if (isFinishing()) {
            return;
        }
        if (isProgress) {
            if (this.mLoadingFragment.isAdded()) {
                getFragmentManager().beginTransaction().remove(this.mLoadingFragment).commit();
            }
            getFragmentManager().beginTransaction().add(R.id.container, this.mLoadingFragment).commit();
            return;
        }
        getFragmentManager().beginTransaction().remove(this.mLoadingFragment).commit();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public LoadingFragment getLoadingFragment() {
        return LoadingFragment.newInstance();
    }

    public boolean goBack() {
        return false;
    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getFragmentManager().findFragmentById(R.id.container);
    }

    public BaseFragment getActualFragment() {
        return this.mActualFragment;
    }

    public void setActualFragment(BaseFragment actualFragment) {
        this.mActualFragment = actualFragment;
    }

    public void logOut() {
        logOutInt();
        startActivity(new Intent(this, LauncherActivity.class));
        finish();
    }

    /* access modifiers changed from: private */
    public void logOutInt() {
        if (Fabric.isInitialized()) {
            Crashlytics.setUserName((String) null);
        }
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().reset();
        }
        DataManager.getInstance().deleteAllSleeps();
        DataManager.getInstance().deleteAllEvents();
        AccountManager.getInstance().signOut();
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().registerUnidentifiedUser();
        }
    }

    public void onEventMainThread(UserAccountStatusChangedEvent event) {
        if (AccountManager.getInstance().canUserUseApp() == 1) {
            showThiefInformation();
        }
    }

    /* access modifiers changed from: protected */
    public void showThiefInformation() {
        if (this.mThiefAlertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) getString(R.string.neuroon_blocked_title));
            builder.setMessage((CharSequence) getString(R.string.neuroon_blocked_message));
            builder.setNeutralButton((CharSequence) getString(R.string.contact_us), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivity.this.logOutInt();
                    Intent emailIntent = new Intent("android.intent.action.SEND");
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra("android.intent.extra.EMAIL", new String[]{"contact@neuroon.com"});
                    emailIntent.putExtra("android.intent.extra.SUBJECT", BaseActivity.this.getString(R.string.neuroon_blocked_title));
                    emailIntent.putExtra("android.intent.extra.TEXT", "");
                    BaseActivity.this.startActivity(Intent.createChooser(emailIntent, BaseActivity.this.getString(R.string.contact_us)));
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    BaseActivity.this.finish();
                }
            });
            if (!isFinishing() && !isDestroyed()) {
                this.mThiefAlertDialog = builder.show();
            }
        }
    }

    public Tracker getTracker() {
        return this.mTracker;
    }
}
