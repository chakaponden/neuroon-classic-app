package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnBottomToolbarFragmentInteractionListener;
import com.inteliclinic.neuroon.fragments.OnMenuFragmentInteractionListener;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.mask.BatteryLevelReceivedEvent;
import com.inteliclinic.neuroon.mask.MaskConnectedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.JetLagChartView;
import de.greenrobot.event.EventBus;

public class JetlagTherapyFragment extends BaseFragment {
    @InjectView(2131755270)
    BaseTextView cancelTherapy;
    @InjectView(2131755269)
    BaseTextView changeJourney;
    @InjectView(2131755272)
    JetLagChartView jetLagChart;
    @InjectView(2131755274)
    ThinTextView jetLagLeft;
    @InjectView(2131755273)
    ThinTextView jetLagPercent;
    @InjectView(2131755335)
    LightTextView jetLagToBeat;
    @InjectView(2131755204)
    ImageView mBatteryDeter;
    @InjectView(2131755203)
    ProgressBar mBatteryIndeter;
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    @InjectView(2131755334)
    LightTextView mCurrentTherapy;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755202)
    ImageView mMaskConnection;

    public interface OnFragmentInteractionListener extends OnMenuFragmentInteractionListener, OnBottomToolbarFragmentInteractionListener {
        void goToJetLagProgress();

        void openJetLagBlockerInfo();

        void openJetLagFirstTimeHint();

        void openPlanJourney();
    }

    public static JetlagTherapyFragment newInstance() {
        return new JetlagTherapyFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jetlag_therapy, container, false);
        ButterKnife.inject((Object) this, view);
        setTherapy();
        if (!AccountManager.getInstance().isJetLagMainScreenHelpShown() && this.mListener != null) {
            this.mListener.openJetLagFirstTimeHint();
        }
        this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.JET_LAG);
        setListeners();
        return view;
    }

    public void onEventMainThread(MaskConnectedEvent event) {
        setMaskConnectionInfo(event.isConnected());
    }

    private void setMaskConnectionInfo(boolean connected) {
        if (this.mMaskConnection != null) {
            if (connected) {
                this.mMaskConnection.setImageResource(R.drawable.mask_signal_good);
            } else {
                this.mMaskConnection.setImageResource(R.drawable.mask_signal_bad);
            }
        }
    }

    private void setBatteryLevel(int batteryLevel) {
        if (batteryLevel != -1) {
            if (this.mBatteryIndeter != null) {
                this.mBatteryIndeter.setVisibility(8);
            }
            if (this.mBatteryDeter != null) {
                this.mBatteryDeter.setVisibility(0);
                if (batteryLevel > 60) {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_full));
                } else if (batteryLevel > 20) {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_mid));
                } else {
                    this.mBatteryDeter.setImageDrawable(getResources().getDrawable(R.drawable.battery_low));
                }
            }
        } else {
            if (this.mBatteryIndeter != null) {
                this.mBatteryIndeter.setVisibility(0);
            }
            if (this.mBatteryDeter != null) {
                this.mBatteryDeter.setVisibility(8);
            }
        }
    }

    public void onEventMainThread(BatteryLevelReceivedEvent event) {
        setBatteryLevel(event.getLevel());
    }

    private void setListeners() {
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (JetlagTherapyFragment.this.mListener != null) {
                    JetlagTherapyFragment.this.mListener.sleepScoreClick();
                }
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (JetlagTherapyFragment.this.mListener != null) {
                    JetlagTherapyFragment.this.mListener.lightBoostClick();
                }
            }
        });
    }

    private void setTherapy() {
        CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
        if (currentTherapy != null) {
            setButtonsEnabled(false);
            this.jetLagToBeat.setText(getString(R.string.hours_to_beat, new Object[]{currentTherapy.getTimeDifferenceAsString()}));
            this.jetLagLeft.setText(getResources().getQuantityString(R.plurals.some_days_left, currentTherapy.getTherapyDaysLeft(), new Object[]{Integer.valueOf(currentTherapy.getTherapyDaysLeft())}));
            this.jetLagChart.setProgress(TherapyManager.getInstance().getTherapyProgress());
            this.jetLagPercent.setText(getString(R.string.some_percent, new Object[]{Integer.valueOf((int) (TherapyManager.getInstance().getTherapyProgress() * 100.0f))}));
            this.mCurrentTherapy.setText(currentTherapy.getOrigin().getCity().toUpperCase() + " - " + currentTherapy.getDestination().getCity().toUpperCase());
            return;
        }
        setButtonsEnabled(true);
        this.mCurrentTherapy.setText(R.string.no_therapy_planned);
    }

    private void setButtonsEnabled(boolean enabled) {
        boolean z;
        boolean z2 = true;
        BaseTextView baseTextView = this.changeJourney;
        if (!enabled) {
            z = true;
        } else {
            z = false;
        }
        baseTextView.setEnabled(z);
        BaseTextView baseTextView2 = this.cancelTherapy;
        if (enabled) {
            z2 = false;
        }
        baseTextView2.setEnabled(z2);
    }

    public void onResume() {
        super.onResume();
        setMaskConnectionInfo(MaskManager.getInstance().isFullyConnected());
        setBatteryLevel(MaskManager.getInstance().getBatteryLevel());
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
            EventBus.getDefault().register(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
        EventBus.getDefault().unregister(this);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755269})
    public void onChangeJourneyClick() {
        if (this.mListener != null) {
            this.mListener.openPlanJourney();
        }
    }

    @OnClick({2131755270})
    public void onCancelTherapyClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("jet_lag").setAction("cancel_pressed").setLabel("cancel_pressed").build());
        TherapyManager.getInstance().deleteTherapy();
    }

    @OnClick({2131755200})
    public void onMenuClick() {
        if (this.mListener != null) {
            this.mListener.toggleMenu();
        }
    }

    @OnClick({2131755333})
    public void onHelpClick() {
        if (this.mListener != null) {
            this.mListener.openJetLagBlockerInfo();
        }
    }

    @OnClick({2131755215})
    public void onRightArrowClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("jet_lag").setAction("next_button_pressed").setLabel("next_button_pressed").build());
        if (this.mListener != null) {
            this.mListener.goToJetLagProgress();
        }
    }
}
