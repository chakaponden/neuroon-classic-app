package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;

public class JetLagFirstHelpFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void setHintAsShown();
    }

    public static JetLagFirstHelpFragment newInstance() {
        return new JetLagFirstHelpFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_first_help, container, false);
        ButterKnife.inject((Object) this, view);
        view.setClickable(true);
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

    @OnClick({2131755277})
    public void onCloseFragment() {
        if (this.mListener != null) {
            this.mListener.setHintAsShown();
            getFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
