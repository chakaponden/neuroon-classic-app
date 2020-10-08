package com.inteliclinic.neuroon.activities;

import android.content.Intent;
import android.os.Bundle;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.mask.MaskSleepDownloadFragment;
import com.inteliclinic.neuroon.mask.MaskDownloadStoppedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import de.greenrobot.event.EventBus;

public class MaskSynchronizationActivity extends BaseActivity implements MaskSleepDownloadFragment.OnFragmentInteractionListener {
    public static final String ACTION_STOP = "com.inteliclinic.neuroon.mask.ACTION_STOP";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_mask_synchronization);
        if (getIntent() == null || !ACTION_STOP.equals(getIntent().getAction())) {
            getFragmentManager().beginTransaction().add(R.id.container, MaskSleepDownloadFragment.newInstance(MaskManager.getInstance().getLastSavedSleep(), MaskManager.getInstance().getMaskSleepCount())).addToBackStack((String) null).commit();
            EventBus.getDefault().register(this);
            return;
        }
        MaskManager.getInstance().stopDownloadingSleep();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            MaskManager.getInstance().stopDownloadingSleep();
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void closeFragments() {
        finish();
    }

    public void dismiss() {
        finish();
    }

    public void onEventMainThread(MaskDownloadStoppedEvent event) {
        finish();
    }
}
