package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.ThinCheckbox;

public class JetLagDialogFragment extends BaseFragment {
    @InjectView(2131755268)
    ThinCheckbox dontShow;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onJetLagDialogDismiss(JetLagDialogFragment jetLagDialogFragment);
    }

    public static JetLagDialogFragment newInstance() {
        return new JetLagDialogFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_dialog, container, false);
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

    @OnClick({2131755120})
    public void onClickClose() {
        if (this.mListener != null) {
            AccountManager.getInstance().dontShowJetLagDialog(this.dontShow.isChecked());
            this.mListener.onJetLagDialogDismiss(this);
        }
    }
}
