package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;

public class TurnOnBluetoothFragment extends BaseFragment {
    private static final String ARG_THERAPY_NAME = "param1";
    @InjectView(2131755374)
    FrameLayout cancel;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755375)
    FrameLayout tryAgain;

    public interface OnFragmentInteractionListener {
        void cancelBluetoothTurningOn();

        void enableBluetooth();
    }

    public static TurnOnBluetoothFragment newInstance() {
        TurnOnBluetoothFragment fragment = new TurnOnBluetoothFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turn_on_bluetooth, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
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

    @OnClick({2131755374})
    public void onCancelClick() {
        if (this.mListener != null) {
            this.mListener.cancelBluetoothTurningOn();
        }
    }

    @OnClick({2131755375})
    public void onEnableClick() {
        if (this.mListener != null) {
            this.mListener.enableBluetooth();
        }
    }
}
