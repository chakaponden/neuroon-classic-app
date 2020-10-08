package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BottomToolbar;

public class NapDetailsFragment extends BaseFragment {
    @InjectView(2131755390)
    ImageView lightboostIcon;
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755392)
    ImageView startLightboostButton;

    public interface OnFragmentInteractionListener extends OnBottomToolbarFragmentInteractionListener {
        void startTherapy();
    }

    public static NapDetailsFragment newInstance(String text, int timeMilliseconds) {
        NapDetailsFragment napDetailsFragment = new NapDetailsFragment();
        napDetailsFragment.setArguments(new Bundle());
        return napDetailsFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nap_details, container, false);
        ButterKnife.inject((Object) this, view);
        setData();
        setListeners();
        return view;
    }

    private void setData() {
        this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.ENERGY);
        Animation animation = AnimationUtils.loadAnimation(this.startLightboostButton.getContext(), R.anim.rotate_indefinitly);
        animation.setDuration(10000);
        this.startLightboostButton.setAnimation(animation);
    }

    private void setListeners() {
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (NapDetailsFragment.this.mListener != null) {
                    NapDetailsFragment.this.mListener.jetlagClick();
                }
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (NapDetailsFragment.this.mListener != null) {
                    NapDetailsFragment.this.mListener.sleepScoreClick();
                }
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }
        });
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

    @OnClick({2131755392})
    public void onStartTherapyClick() {
        if (this.mListener != null) {
            this.mListener.startTherapy();
        }
    }
}
