package com.inteliclinic.neuroon.fragments;

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
import com.inteliclinic.neuroon.managers.StatsManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.dashboard.NapView;
import java.util.Date;
import java.util.List;

public class EnergyPlusFragment extends BaseFragment {
    @InjectView(2131755243)
    NapView bodyNap;
    @InjectView(2131755241)
    NapView lightBoost;
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755242)
    NapView rechargeNap;
    @InjectView(2131755244)
    NapView remNap;
    @InjectView(2131755245)
    NapView ultimateNap;

    public interface OnFragmentInteractionListener extends OnBottomToolbarFragmentInteractionListener, OnMenuFragmentInteractionListener {
        void startLightTherapy(int i);

        void startNapTherapy(String str, int i, Event.EventType eventType);
    }

    public static EnergyPlusFragment newInstance() {
        return new EnergyPlusFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_energy_plus, container, false);
        ButterKnife.inject((Object) this, view);
        setData();
        setListeners();
        fillWithRecommendations();
        return view;
    }

    private void fillWithRecommendations() {
        List<Event> napRecommendations = StatsManager.getInstance().getNapRecommendations();
        if (napRecommendations.size() < 5) {
            napRecommendations.add(Event.bodyNapTherapy(new Date(), DateUtils.dateAddSeconds(new Date(), 7200)));
            napRecommendations.add(Event.remNapTherapy(new Date(), DateUtils.dateAddSeconds(new Date(), 7200)));
            napRecommendations.add(Event.ultimateNapTherapy(new Date(), DateUtils.dateAddSeconds(new Date(), 7200)));
        }
        this.lightBoost.fillWithEvent(napRecommendations.get(0));
        this.rechargeNap.fillWithEvent(napRecommendations.get(1));
        this.bodyNap.fillWithEvent(napRecommendations.get(2));
        this.remNap.fillWithEvent(napRecommendations.get(3));
        this.ultimateNap.fillWithEvent(napRecommendations.get(4));
    }

    private void setData() {
        this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.ENERGY);
    }

    private void setListeners() {
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (EnergyPlusFragment.this.mListener != null) {
                    EnergyPlusFragment.this.mListener.jetlagClick();
                }
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (EnergyPlusFragment.this.mListener != null) {
                    EnergyPlusFragment.this.mListener.sleepScoreClick();
                }
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }
        });
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("energy_plus");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
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

    @OnClick({2131755241})
    public void onLightBoostClick() {
        if (this.mListener != null) {
            this.mListener.startLightTherapy(1200000);
        }
    }

    @OnClick({2131755242})
    public void onPowerNapClick() {
        if (this.mListener != null) {
            this.mListener.startNapTherapy("Power Nap", 1200, Event.EventType.ETNapPower);
        }
    }

    @OnClick({2131755243})
    public void onBodyNapClick() {
        if (this.mListener != null) {
            this.mListener.startNapTherapy("Body Nap", 1800, Event.EventType.ETNapBody);
        }
    }

    @OnClick({2131755244})
    public void onMindNapClick() {
        if (this.mListener != null) {
            this.mListener.startNapTherapy("Rem Nap", 3600, Event.EventType.ETNapRem);
        }
    }

    @OnClick({2131755245})
    public void onUltimateNapClick() {
        if (this.mListener != null) {
            this.mListener.startNapTherapy("Ultimate Nap", 5400, Event.EventType.ETNapUltimate);
        }
    }
}
