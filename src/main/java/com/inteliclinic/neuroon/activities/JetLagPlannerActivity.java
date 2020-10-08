package com.inteliclinic.neuroon.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.jetlag.BookingReferenceNumberFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagTherapySummaryFragment;
import com.inteliclinic.neuroon.fragments.jetlag.JetLagTripSummaryFragment;
import com.inteliclinic.neuroon.fragments.jetlag.PlanJourneyFragment;
import com.inteliclinic.neuroon.fragments.jetlag.ReservationNumberInformationFragment;
import com.inteliclinic.neuroon.fragments.loading.JetLagLoadingFragment;
import com.inteliclinic.neuroon.fragments.loading.LoadingFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.service.NotificationsWatcher;
import com.inteliclinic.neuroon.utils.KeyboardUtils;

public class JetLagPlannerActivity extends BaseActivity implements PlanJourneyFragment.OnFragmentInteractionListener, JetLagTripSummaryFragment.OnFragmentInteractionListener, JetLagTherapySummaryFragment.OnFragmentInteractionListener, ReservationNumberInformationFragment.OnFragmentInteractionListener {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_jet_lag_planner);
        getWindow().setSoftInputMode(2);
        setActualFragment(PlanJourneyFragment.newInstance());
        getFragmentManager().beginTransaction().add(R.id.container, getActualFragment()).commit();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public LoadingFragment getLoadingFragment() {
        return JetLagLoadingFragment.newInstance();
    }

    public void tripScheduled() {
        getFragmentManager().beginTransaction().remove(getActualFragment()).add(R.id.container, JetLagTripSummaryFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void openBookingReferenceHelp() {
        KeyboardUtils.hideKeyboard(this);
        getFragmentManager().beginTransaction().add(R.id.container, BookingReferenceNumberFragment.newInstance()).addToBackStack((String) null).commit();
    }

    public void destinationButtonClicked() {
        if (AccountManager.getInstance().hasToShowReservationNumberDialog()) {
            KeyboardUtils.hideKeyboard(this);
            getFragmentManager().beginTransaction().add(R.id.container, ReservationNumberInformationFragment.newInstance()).addToBackStack((String) null).commit();
        }
    }

    public void closeFragments() {
        TherapyManager.getInstance().deleteTherapy();
        finish();
    }

    public void therapyCreated() {
        getFragmentManager().beginTransaction().replace(R.id.container, JetLagTherapySummaryFragment.newInstance()).addToBackStack((String) null).commit();
        NotificationsWatcher.check();
    }

    public boolean goBack() {
        if (!(getFragmentManager().findFragmentById(R.id.container) instanceof PlanJourneyFragment)) {
            return super.goBack();
        }
        finish();
        return true;
    }

    public void onStartTherapy() {
        finish();
    }
}
