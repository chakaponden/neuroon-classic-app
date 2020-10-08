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
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.views.ThinButton;

public class MaskSoftwareUpdateFailedFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755385)
    ThinButton startUpdate;

    public interface OnFragmentInteractionListener {
        void tryAgain();
    }

    public static MaskSoftwareUpdateFailedFragment newInstance() {
        return new MaskSoftwareUpdateFailedFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_software_update_failed, container, false);
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

    @OnClick({2131755385})
    public void onStartUpdate() {
        if (this.mListener != null) {
            this.mListener.tryAgain();
        }
    }
}
