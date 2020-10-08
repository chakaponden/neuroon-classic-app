package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.LightTextView;
import io.intercom.android.sdk.Intercom;

public class PairMaskStartFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755408)
    LightTextView questions;
    @InjectView(2131755409)
    FrameLayout questionsBtn;

    public interface OnFragmentInteractionListener {
        void onBuyNowClick();

        void pairMask();
    }

    public static PairMaskStartFragment newInstance() {
        return new PairMaskStartFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i;
        int i2 = 0;
        View view = inflater.inflate(R.layout.fragment_pair_mask_start, container, false);
        ButterKnife.inject((Object) this, view);
        LightTextView lightTextView = this.questions;
        if (AccountManager.getInstance().isIntercomEnabled()) {
            i = 0;
        } else {
            i = 4;
        }
        lightTextView.setVisibility(i);
        FrameLayout frameLayout = this.questionsBtn;
        if (!AccountManager.getInstance().isIntercomEnabled()) {
            i2 = 4;
        }
        frameLayout.setVisibility(i2);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("pairing");
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

    @OnClick({2131755377})
    public void onPairClick() {
        if (this.mListener != null) {
            this.mListener.pairMask();
        }
    }

    @OnClick({2131755407})
    public void onBuyNowClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("pairing").setAction("buy_now_pressed").setLabel("buy_now_pressed").build());
        if (this.mListener != null) {
            this.mListener.onBuyNowClick();
        }
    }

    @OnClick({2131755409})
    public void onQuestionsClick() {
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().displayMessageComposer();
        }
    }
}
