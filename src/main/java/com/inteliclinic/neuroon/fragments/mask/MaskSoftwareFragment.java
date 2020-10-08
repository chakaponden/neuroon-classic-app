package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.mask.MaskFirmwareCheck;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import de.greenrobot.event.EventBus;

public class MaskSoftwareFragment extends BaseFragment {
    @InjectView(2131755384)
    ThinButton checkForUpdate;
    @InjectView(2131755368)
    ThinTextView currentSoftware;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onCheckFirmware();
    }

    public static MaskSoftwareFragment newInstance() {
        return new MaskSoftwareFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_software, container, false);
        ButterKnife.inject((Object) this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    public void onResume() {
        super.onResume();
        this.checkForUpdate.setEnabled(true);
        this.currentSoftware.setText(getString(R.string.current_version_value, new Object[]{MaskManager.getInstance().getAppVersionAsString()}));
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
        EventBus.getDefault().unregister(this);
        ButterKnife.reset(this);
    }

    @OnClick({2131755384})
    public void onCheckForUpdateClick() {
        this.checkForUpdate.setEnabled(false);
        if (this.mListener != null) {
            this.mListener.onCheckFirmware();
        }
    }

    public void onEvent(MaskFirmwareCheck event) {
        View view;
        this.checkForUpdate.setEnabled(true);
        if (!event.isNewAvailable() && (view = getView()) != null) {
            Snackbar.make(view, (int) R.string.the_software_is_up_to_date, 0).show();
        }
    }
}
