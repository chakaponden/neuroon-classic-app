package com.inteliclinic.neuroon.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskNewSoftwareFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskSoftwareFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskSoftwareUpdateFailedFragment;
import com.inteliclinic.neuroon.fragments.mask.MaskSoftwareUpdateFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.InvalidRefreshTokenEvent;
import com.inteliclinic.neuroon.managers.network.MaskSoftwareDownloadErrorEvent;
import com.inteliclinic.neuroon.mask.MaskFirmwareCheck;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.DfuUpdateFailEvent;
import com.inteliclinic.neuroon.models.network.MaskFirmwareMeta;
import de.greenrobot.event.EventBus;
import java.io.IOException;
import java.io.InputStream;

public class MaskUpdateActivity extends BaseActivity implements MaskSoftwareUpdateFragment.OnFragmentInteractionListener, MaskSoftwareUpdateFailedFragment.OnFragmentInteractionListener, MaskNewSoftwareFragment.OnFragmentInteractionListener, MaskSoftwareFragment.OnFragmentInteractionListener {
    private boolean mFail;
    private MaskFirmwareMeta mMaskUpdateInfo;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_mask_update);
        if (MaskManager.getInstance().isMaskUpdating()) {
            setFragment(MaskSoftwareUpdateFragment.newInstance());
            return;
        }
        setFragment(MaskSoftwareFragment.newInstance());
        EventBus.getDefault().registerSticky(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mFail) {
            this.mFail = false;
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if (getFragmentManager().findFragmentById(R.id.container) != null) {
                fragmentTransaction.replace(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
            } else {
                fragmentTransaction.add(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(DfuUpdateFailEvent event) {
        this.mFail = true;
        Answers.getInstance().logCustom((CustomEvent) ((CustomEvent) ((CustomEvent) new CustomEvent("Mask firmware update fail").putCustomAttribute("From Version Number", String.valueOf(MaskManager.getInstance().getAppVersion()))).putCustomAttribute("To Version Number", String.valueOf(this.mMaskUpdateInfo.getAppVersion()))).putCustomAttribute("Dfu Version Number", String.valueOf(MaskManager.getInstance().getDfuVersion())));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) != null) {
            fragmentTransaction.replace(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
        } else {
            fragmentTransaction.add(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
        }
        EventBus.getDefault().removeStickyEvent(DfuUpdateFailEvent.class);
    }

    public void onEventMainThread(MaskSoftwareDownloadErrorEvent event) {
        this.mFail = true;
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).add(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
        EventBus.getDefault().removeStickyEvent(MaskSoftwareUpdateFailedFragment.class);
    }

    public void onEventMainThread(MaskFirmwareCheck event) {
        if (this.mMaskUpdateInfo == null) {
            this.mMaskUpdateInfo = event.getUpdateInfo();
        }
        if (!MaskManager.getInstance().isMaskUpdating()) {
            this.mMaskUpdateInfo = event.getUpdateInfo();
            if (event.isNewAvailable() && MaskManager.getInstance().getAppVersion() != 0) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (getFragmentManager().findFragmentById(R.id.container) != null) {
                    fragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.container));
                }
                fragmentTransaction.add(R.id.container, MaskNewSoftwareFragment.newInstance()).commit();
            }
        }
    }

    public void onEventMainThread(InvalidRefreshTokenEvent event) {
        logOut();
    }

    private void setFragment(BaseFragment fragment) {
        getFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack((String) null).commit();
    }

    public void showUpdateSoftwareScreen() {
        setFragment(MaskSoftwareUpdateFragment.newInstance());
    }

    public void updateComplete() {
        Answers.getInstance().logCustom((CustomEvent) ((CustomEvent) ((CustomEvent) new CustomEvent("Mask firmware updated").putCustomAttribute("From Version Num", String.valueOf(MaskManager.getInstance().getAppVersion()))).putCustomAttribute("To Version Num", String.valueOf(this.mMaskUpdateInfo.getAppVersion()))).putCustomAttribute("Dfu Version Num", String.valueOf(MaskManager.getInstance().getDfuVersion())));
        MaskManager.getInstance().setAppVersion(this.mMaskUpdateInfo.getAppVersion());
        setFragment(MaskSoftwareFragment.newInstance());
    }

    public void uploadApplication() {
        MaskManager.getInstance().updateFirmwareOnDevice(loadFile(), 1, this.mMaskUpdateInfo.getLength(), this.mMaskUpdateInfo.getCrc());
    }

    public void downloadUpdate() {
        getWindow().addFlags(128);
        if (this.mMaskUpdateInfo == null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).add(R.id.container, MaskSoftwareUpdateFailedFragment.newInstance()).commit();
        } else {
            AccountManager.getInstance().getMaskFirmware(this.mMaskUpdateInfo.getVersion());
        }
    }

    private byte[] loadFile() {
        try {
            InputStream is = openFileInput("mask_firmware");
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();
            return fileBytes;
        } catch (IOException e) {
            Log.e("File", "Cannot open neuroon file bin");
            return null;
        }
    }

    public void closeFragments() {
        finish();
    }

    public boolean goBack() {
        if (!(getFragmentManager().findFragmentById(R.id.container) instanceof MaskSoftwareFragment)) {
            return super.goBack();
        }
        finish();
        return true;
    }

    public void tryAgain() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
    }

    public void onCheckFirmware() {
        if (!AccountManager.getInstance().isLogged()) {
            logOut();
        } else {
            AccountManager.getInstance().checkMaskFirmware();
        }
    }

    public void updateInfo(MaskFirmwareMeta meta) {
        this.mMaskUpdateInfo = meta;
    }
}
