package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnBottomToolbarFragmentInteractionListener;
import com.inteliclinic.neuroon.fragments.OnMenuFragmentInteractionListener;
import com.inteliclinic.neuroon.views.BottomToolbar;

public class StartJetLagTherapyFragment extends BaseFragment {
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener extends OnBottomToolbarFragmentInteractionListener, OnMenuFragmentInteractionListener {
        void openJetLagBlockerInfo();

        void openPlanJourney();
    }

    public static StartJetLagTherapyFragment newInstance() {
        return new StartJetLagTherapyFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_jet_lag_therapy, container, false);
        ButterKnife.inject((Object) this, view);
        this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.JET_LAG);
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (StartJetLagTherapyFragment.this.mListener != null) {
                    StartJetLagTherapyFragment.this.mListener.sleepScoreClick();
                }
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (StartJetLagTherapyFragment.this.mListener != null) {
                    StartJetLagTherapyFragment.this.mListener.lightBoostClick();
                }
            }
        });
        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755200})
    public void onMenuClick() {
        if (this.mListener != null) {
            this.mListener.toggleMenu();
        }
    }

    @OnClick({2131755333})
    public void onHelpClick() {
        if (this.mListener != null) {
            this.mListener.openJetLagBlockerInfo();
        }
    }

    @OnClick({2131755466})
    public void onStartClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("Jet Lag View").setAction("Start therapy pressed (before planning)").setLabel("Start therapy pressed (before planning)").build());
        if (this.mListener != null) {
            this.mListener.openPlanJourney();
        }
    }
}
