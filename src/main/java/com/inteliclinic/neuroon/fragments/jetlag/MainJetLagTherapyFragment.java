package com.inteliclinic.neuroon.fragments.jetlag;

import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.BaseMainFragmentRight;
import com.inteliclinic.neuroon.managers.therapy.TherapyDeletedEvent;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import de.greenrobot.event.EventBus;

public class MainJetLagTherapyFragment extends BaseMainFragmentRight<BaseFragment, JetLagProgressFragment> {
    public static MainJetLagTherapyFragment newInstance() {
        return new MainJetLagTherapyFragment();
    }

    /* access modifiers changed from: protected */
    public BaseFragment createCenterFragment() {
        if (TherapyManager.getInstance().getCurrentTherapy() != null) {
            getPager().setLocked(false);
            if (TherapyManager.getInstance().getTherapyProgress() != 1.0f) {
                return JetlagTherapyFragment.newInstance();
            }
            return JetLagEndTherapyFragment.newInstance();
        }
        getPager().setLocked(true);
        return StartJetLagTherapyFragment.newInstance();
    }

    /* access modifiers changed from: protected */
    public JetLagProgressFragment createRightFragment() {
        return JetLagProgressFragment.newInstance();
    }

    /* access modifiers changed from: protected */
    public String getScreenName(int position) {
        switch (position) {
            case 1:
                return "therapy_progress";
            default:
                if (TherapyManager.getInstance().getCurrentTherapy() == null) {
                    return "jet_lag";
                }
                if (TherapyManager.getInstance().getTherapyProgress() != 1.0f) {
                    return "jet_lag_in_progress";
                }
                return "jet_lag_end";
        }
    }

    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        checkView();
    }

    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(TherapyDeletedEvent event) {
        checkView();
    }

    private void checkView() {
        if (TherapyManager.getInstance().getCurrentTherapy() != null) {
            if (TherapyManager.getInstance().getTherapyProgress() != 1.0f) {
                if (!(getCenterFragment() instanceof JetlagTherapyFragment)) {
                    resetAdapter();
                }
            } else if (!(getCenterFragment() instanceof JetLagEndTherapyFragment)) {
                resetAdapter();
            }
        } else if (!(getCenterFragment() instanceof StartJetLagTherapyFragment)) {
            resetAdapter();
        }
    }
}
