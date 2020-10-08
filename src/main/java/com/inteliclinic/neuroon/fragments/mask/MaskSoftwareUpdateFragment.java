package com.inteliclinic.neuroon.fragments.mask;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.network.MaskSoftwareDownloadedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.DfuProgressEvent;
import com.inteliclinic.neuroon.mask.bluetooth.DfuSentEvent;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import de.greenrobot.event.EventBus;

public class MaskSoftwareUpdateFragment extends BaseFragment {
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755385)
    ThinButton startUpdate;
    @InjectView(2131755386)
    LinearLayout updateProgress;
    @InjectView(2131755388)
    ProgressBar updateProgressProgress;
    @InjectView(2131755387)
    ThinTextView updateProgressText;

    public interface OnFragmentInteractionListener {
        void downloadUpdate();

        void updateComplete();

        void uploadApplication();
    }

    public static MaskSoftwareUpdateFragment newInstance() {
        return new MaskSoftwareUpdateFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_software_update, container, false);
        ButterKnife.inject((Object) this, view);
        if (MaskManager.getInstance().isMaskUpdating()) {
            showProgress();
        } else {
            onStartUpdate();
        }
        return view;
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

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755385})
    public void onStartUpdate() {
        showProgress();
        downloadSoftware();
    }

    private void showProgress() {
        this.startUpdate.setVisibility(8);
        this.updateProgress.setVisibility(0);
    }

    private void downloadSoftware() {
        if (this.mListener != null) {
            this.mListener.downloadUpdate();
        }
        this.updateProgressProgress.setProgress(0);
        this.updateProgressText.setText(R.string.downloading_software);
    }

    private void sendDataToNeuroon() {
        if (this.updateProgressProgress != null) {
            this.updateProgressProgress.setProgress(0);
        }
        if (this.updateProgressText != null) {
            this.updateProgressText.setText(R.string.connecting);
        }
        if (this.mListener != null) {
            this.mListener.uploadApplication();
        }
    }

    public void onEventMainThread(MaskSoftwareDownloadedEvent event) {
        sendDataToNeuroon();
    }

    public void onEventMainThread(DfuProgressEvent event) {
        if (this.updateProgressText != null) {
            this.updateProgressText.setText(R.string.sending_data_to_neuroon);
        }
        if (this.updateProgressProgress != null) {
            this.updateProgressProgress.setProgress(event.getProgress(100));
        }
    }

    public void onEventMainThread(DfuSentEvent event) {
        this.updateProgressProgress.setProgress(0);
        this.updateProgressText.setText(R.string.restarting_neuroon);
        ObjectAnimator anim = ObjectAnimator.ofInt(this.updateProgressProgress, "progress", new int[]{100});
        anim.setDuration(5000);
        anim.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                if (MaskSoftwareUpdateFragment.this.mListener != null) {
                    MaskSoftwareUpdateFragment.this.mListener.updateComplete();
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        anim.start();
        EventBus.getDefault().removeStickyEvent((Object) event);
    }
}
