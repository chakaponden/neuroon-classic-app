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
import com.inteliclinic.neuroon.mask.MaskFirmwareCheck;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import de.greenrobot.event.EventBus;

public class MaskNewSoftwareFragment extends BaseFragment {
    @InjectView(2131755368)
    ThinTextView currentSoftware;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755369)
    ThinTextView newSoftware;
    @InjectView(2131755370)
    ThinButton update;

    public interface OnFragmentInteractionListener {
        void closeFragments();

        void showUpdateSoftwareScreen();
    }

    public static MaskNewSoftwareFragment newInstance() {
        return new MaskNewSoftwareFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_new_software, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onResume() {
        super.onResume();
        this.currentSoftware.setText(getString(R.string.current_version_value, new Object[]{MaskManager.getInstance().getAppVersionAsString()}));
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

    public void onEventMainThread(DeviceStateEvent event) {
        this.update.setEnabled(event.isConnected());
        this.update.invalidate();
    }

    public void onEvent(MaskFirmwareCheck event) {
        this.newSoftware.setText(getString(R.string.new_version_version, new Object[]{integerAsVersionString(Integer.valueOf(event.getAppVersion()))}));
    }

    private String integerAsVersionString(Integer integer) {
        if (integer == null || integer.intValue() == -1) {
            return "0.0.0.0";
        }
        byte fourth = integer.byteValue();
        byte third = (byte) (integer.intValue() >> 8);
        return String.format("%d.%d.%d.%d", new Object[]{Byte.valueOf((byte) (integer.intValue() >> 24)), Byte.valueOf((byte) (integer.intValue() >> 16)), Byte.valueOf(third), Byte.valueOf(fourth)});
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755164})
    public void onCloseButton() {
        if (this.mListener != null) {
            this.mListener.closeFragments();
        }
    }

    @OnClick({2131755370})
    public void onUpdateClick() {
        if (this.mListener != null) {
            this.mListener.showUpdateSoftwareScreen();
        }
    }
}
