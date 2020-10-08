package com.inteliclinic.neuroon.fragments.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;

public class AreYouSureToLeaveFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onContinueClick();

        void onLeaveClick();
    }

    public static AreYouSureToLeaveFragment newInstance() {
        return new AreYouSureToLeaveFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_are_you_sure_to_leave, container, false);
        ButterKnife.inject((Object) this, view);
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

    @OnClick({2131755162})
    public void onLeaveButtonClick() {
        if (this.mListener != null) {
            this.mListener.onLeaveClick();
        }
    }

    @OnClick({2131755163})
    public void onContinueButtonClick() {
        if (this.mListener != null) {
            this.mListener.onContinueClick();
        }
    }
}
