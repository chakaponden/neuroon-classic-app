package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.app.Fragment;
import butterknife.OnClick;
import butterknife.Optional;
import com.google.android.gms.analytics.Tracker;
import com.inteliclinic.neuroon.NeuroonApplication;
import com.inteliclinic.neuroon.utils.KeyboardUtils;

public abstract class BaseFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Tracker mTracker;

    public interface OnFragmentInteractionListener {
        void closeFragments();

        boolean goBack();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
            this.mTracker = ((NeuroonApplication) activity.getApplication()).getDefaultTracker();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @OnClick({2131755164})
    @Optional
    public void onCloseButtonClick() {
        if (this.mListener != null) {
            this.mListener.closeFragments();
        }
    }

    @OnClick({2131755131})
    @Optional
    public void onBackButtonClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (this.mListener != null && !this.mListener.goBack()) {
            getFragmentManager().popBackStack();
        }
    }

    public Tracker getTracker() {
        return this.mTracker;
    }
}
