package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivingSleepEvent;
import com.inteliclinic.neuroon.views.LightTextView;
import de.greenrobot.event.EventBus;

public class MaskSleepDownloadFragment extends BaseFragment {
    private static final String ARG_SLEEP_ON_MASK = "mask_sleep_idx";
    private static final String ARG_SLEEP_SAVED = "sleep_saved_idx";
    private static final String TAG = MaskSleepDownloadFragment.class.getSimpleName();
    @InjectView(2131755382)
    ProgressBar downloadProgressProgress;
    @InjectView(2131755383)
    LightTextView downloadProgressText;
    private int mLastSaved;
    private int mLastSleep;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void dismiss();
    }

    public MaskSleepDownloadFragment() {
        EventBus.getDefault().registerSticky(this);
    }

    public static MaskSleepDownloadFragment newInstance(int lastSaved, int lastSleep) {
        MaskSleepDownloadFragment fragment = new MaskSleepDownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SLEEP_SAVED, lastSaved);
        bundle.putInt(ARG_SLEEP_ON_MASK, lastSleep);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mLastSaved = getArguments().getInt(ARG_SLEEP_SAVED);
            this.mLastSleep = getArguments().getInt(ARG_SLEEP_ON_MASK);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_sleep_download, container, false);
        ButterKnife.inject((Object) this, view);
        if (this.mLastSleep - this.mLastSaved == 1) {
            this.downloadProgressProgress.setMax(100);
        } else {
            this.downloadProgressProgress.setMax((this.mLastSleep - this.mLastSaved) * 100);
        }
        this.downloadProgressText.setText(getString(R.string.sleep_number_of_number, new Object[]{1, Integer.valueOf(this.mLastSleep - this.mLastSaved), Integer.valueOf((this.mLastSleep - this.mLastSaved) * 3)}));
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

    public void onEvent(DeviceStateEvent event) {
        if (!event.isConnected() && this.mListener != null) {
            this.mListener.dismiss();
        }
    }

    public void onEventMainThread(ReceivingSleepEvent event) {
        if (this.downloadProgressProgress != null) {
            this.downloadProgressProgress.setProgress((int) (((float) ((event.getSleepNum() - this.mLastSaved) * 100)) - (100.0f - ((((float) event.getActualFrame()) / ((float) event.getAllFrames())) * 100.0f))));
        }
        if (this.downloadProgressText != null) {
        }
    }

    public void onEventMainThread(ReceivedSleepEvent event) {
        int sleepNum = event.getSleepNum();
        if (this.mLastSleep - this.mLastSaved != 1) {
            if (this.downloadProgressProgress != null) {
                this.downloadProgressProgress.setProgress((sleepNum - this.mLastSaved) * 100);
            }
            if (this.downloadProgressText != null) {
                this.downloadProgressText.setText(getString(R.string.sleep_number_of_number, new Object[]{Integer.valueOf(sleepNum - this.mLastSaved), Integer.valueOf(this.mLastSleep - this.mLastSaved), Integer.valueOf((((this.mLastSleep - this.mLastSaved) - (sleepNum - this.mLastSaved)) + 1) * 3)}));
            }
        }
        if ((sleepNum >= this.mLastSleep || sleepNum == -1) && this.mListener != null) {
            this.mListener.dismiss();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755381})
    public void onCancelButtonClick() {
        MaskManager.getInstance().stopDownloadingSleep();
        if (this.mListener != null) {
            this.mListener.dismiss();
        }
    }
}
