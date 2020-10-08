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
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.views.LightTextView;

public class AlarmsFragment extends BaseFragment {
    @InjectView(2131755159)
    LightTextView mAutoSleepOff;
    @InjectView(2131755158)
    LightTextView mAutoSleepOn;
    @InjectView(2131755155)
    LightTextView mBrightDawn;
    @InjectView(2131755153)
    LightTextView mDimmedDawn;
    @InjectView(2131755151)
    LightTextView mDontUseArtificialDawn;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755154)
    LightTextView mNormalDawn;
    @InjectView(2131755150)
    LightTextView mUseArtificialDawn;

    public interface OnFragmentInteractionListener {
        void openBiorhythm();

        void openOneDayAlarm();
    }

    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);
        ButterKnife.inject((Object) this, view);
        setArtificialButtons();
        setArtificialIntensityButtons();
        setAutomaticSleepButtons();
        return view;
    }

    private void setArtificialButtons() {
        if (MaskManager.getInstance().useArtificialDawn()) {
            this.mDontUseArtificialDawn.setEnabled(true);
            this.mUseArtificialDawn.setEnabled(false);
            return;
        }
        this.mDontUseArtificialDawn.setEnabled(false);
        this.mUseArtificialDawn.setEnabled(true);
    }

    private void setArtificialIntensityButtons() {
        switch (MaskManager.getInstance().getArtificialDawnIntensity()) {
            case 0:
                this.mDimmedDawn.setEnabled(false);
                this.mNormalDawn.setEnabled(true);
                this.mBrightDawn.setEnabled(true);
                return;
            case 1:
                this.mDimmedDawn.setEnabled(true);
                this.mNormalDawn.setEnabled(false);
                this.mBrightDawn.setEnabled(true);
                return;
            case 2:
                this.mDimmedDawn.setEnabled(true);
                this.mNormalDawn.setEnabled(true);
                this.mBrightDawn.setEnabled(false);
                return;
            default:
                return;
        }
    }

    private void setAutomaticSleepButtons() {
        if (MaskManager.getInstance().isAutomaticSleepStartActive()) {
            this.mAutoSleepOff.setEnabled(true);
            this.mAutoSleepOn.setEnabled(false);
            return;
        }
        this.mAutoSleepOff.setEnabled(false);
        this.mAutoSleepOn.setEnabled(true);
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("alarms");
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

    @OnClick({2131755141})
    public void onBiorhythmClick() {
        if (this.mListener != null) {
            this.mListener.openBiorhythm();
        }
    }

    @OnClick({2131755145})
    public void onOneDayAlarmClick() {
        if (this.mListener != null) {
            this.mListener.openOneDayAlarm();
        }
    }

    @OnClick({2131755151})
    public void onDontUseArtificialClick() {
        MaskManager.getInstance().setUseArtificialDawn(false);
        setArtificialButtons();
    }

    @OnClick({2131755150})
    public void onUseArtificialClick() {
        MaskManager.getInstance().setUseArtificialDawn(true);
        setArtificialButtons();
    }

    @OnClick({2131755153})
    public void onDimmedArtificialDawn() {
        MaskManager.getInstance().setArtificialDawnIntensity(0);
        setArtificialIntensityButtons();
    }

    @OnClick({2131755154})
    public void onNormalArtificialDawn() {
        MaskManager.getInstance().setArtificialDawnIntensity(1);
        setArtificialIntensityButtons();
    }

    @OnClick({2131755155})
    public void onBrightArtificialDawn() {
        MaskManager.getInstance().setArtificialDawnIntensity(2);
        setArtificialIntensityButtons();
    }

    @OnClick({2131755158})
    public void onAutoSleepStartOnClick() {
        MaskManager.getInstance().setAutomaticSleepStart(true);
        setAutomaticSleepButtons();
    }

    @OnClick({2131755159})
    public void onAutoSleepStartOffClick() {
        MaskManager.getInstance().setAutomaticSleepStart(false);
        setAutomaticSleepButtons();
    }
}
