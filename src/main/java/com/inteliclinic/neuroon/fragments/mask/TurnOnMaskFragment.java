package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.loading.LoadingFragment;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;

public class TurnOnMaskFragment extends LoadingFragment {
    private static final String ARG_THERAPY_NAME = "param1";
    private OnFragmentInteractionListener mListener;
    private String mTherapyName;
    @InjectView(2131755466)
    ThinButton startButton;
    @InjectView(2131755410)
    LightTextView text2;
    @InjectView(2131755078)
    LightTextView title;

    public interface OnFragmentInteractionListener {
        void onCancelTherapyWithoutStart();
    }

    public static TurnOnMaskFragment newInstance(String therapyName) {
        TurnOnMaskFragment fragment = new TurnOnMaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_THERAPY_NAME, therapyName);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mTherapyName = getArguments().getString(ARG_THERAPY_NAME);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turn_on_mask, container, false);
        ButterKnife.inject((Object) this, view);
        if (this.mTherapyName != null) {
            this.text2.setText(getString(R.string.turn_on_mask_after_that, new Object[]{this.mTherapyName}));
        } else {
            this.title.setText(R.string.title_connecting);
            this.startButton.setVisibility(4);
        }
        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
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

    @OnClick({2131755466})
    public void onCancelClick() {
        if (this.mListener != null) {
            this.mListener.onCancelTherapyWithoutStart();
        }
    }
}
