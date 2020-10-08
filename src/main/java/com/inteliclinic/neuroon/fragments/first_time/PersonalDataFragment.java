package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;

public class PersonalDataFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void openSexFragment();
    }

    public static PersonalDataFragment newInstance() {
        return new PersonalDataFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("personal_data");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
            return;
        }
        throw new RuntimeException(activity.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755045})
    public void onStartClick() {
        if (this.mListener != null) {
            this.mListener.openSexFragment();
        }
    }
}
