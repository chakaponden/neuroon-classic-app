package com.inteliclinic.neuroon.fragments.jetlag;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnBottomToolbarFragmentInteractionListener;
import com.inteliclinic.neuroon.fragments.OnMenuFragmentInteractionListener;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.JetLagChartView;

public class JetLagEndTherapyFragment extends BaseFragment {
    @InjectView(2131755270)
    ThinTextView cancelTherapy;
    @InjectView(2131755269)
    ThinTextView changeJourney;
    @InjectView(2131755275)
    ThinButton endTherapy;
    @InjectView(2131755272)
    JetLagChartView jetLagChart;
    @InjectView(2131755274)
    ThinTextView jetLagLeft;
    @InjectView(2131755273)
    ThinTextView jetLagPercent;
    @InjectView(2131755205)
    BottomToolbar mBottomToolbar;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755130)
    Toolbar toolbar;

    public interface OnFragmentInteractionListener extends OnMenuFragmentInteractionListener, OnBottomToolbarFragmentInteractionListener {
    }

    public static JetLagEndTherapyFragment newInstance() {
        return new JetLagEndTherapyFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_end_therapy, container, false);
        ButterKnife.inject((Object) this, view);
        this.jetLagChart.setProgress(1.0f);
        CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
        this.jetLagLeft.setText(getString(R.string.therapy_completed_text, new Object[]{currentTherapy.getTimeDifferenceAsString(), Integer.valueOf(currentTherapy.getTherapyDuration())}));
        this.mBottomToolbar.setCurrentButton(BottomToolbar.Buttons.JET_LAG);
        this.mBottomToolbar.setOnClickListener(new BottomToolbar.OnToolbarClickListener() {
            public void onJetLagClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
            }

            public void onSleepScoreClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (JetLagEndTherapyFragment.this.mListener != null) {
                    JetLagEndTherapyFragment.this.mListener.sleepScoreClick();
                }
            }

            public void onLightBoostClick(BottomToolbar bottomToolbar, BottomToolbar.Buttons oldButton) {
                if (JetLagEndTherapyFragment.this.mListener != null) {
                    JetLagEndTherapyFragment.this.mListener.lightBoostClick();
                }
            }
        });
        doAnimations();
        return view;
    }

    public void doAnimations() {
        Color.colorToHSV(getResources().getColor(17170444), new float[3]);
        final float[] endColor = new float[3];
        Color.colorToHSV(getResources().getColor(R.color.light_grey_e8), endColor);
        final Drawable drawable1 = this.changeJourney.getCompoundDrawables()[0];
        final Drawable drawable2 = this.cancelTherapy.getCompoundDrawables()[2];
        ValueAnimator anim2 = ObjectAnimator.ofInt(new int[]{0, 255});
        anim2.setDuration((long) 300);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (drawable1 != null) {
                    drawable1.setColorFilter(Color.HSVToColor(((Integer) animation.getAnimatedValue()).intValue(), endColor), PorterDuff.Mode.SRC_ATOP);
                }
                if (drawable2 != null) {
                    drawable2.setColorFilter(Color.HSVToColor(((Integer) animation.getAnimatedValue()).intValue(), endColor), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
        anim2.start();
        Animation anim3 = AnimationUtils.loadAnimation(this.endTherapy.getContext(), 17432576);
        anim3.setFillAfter(true);
        anim3.setDuration((long) 300);
        this.endTherapy.startAnimation(anim3);
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

    @OnClick({2131755275})
    public void endTherapyClick() {
        TherapyManager.getInstance().deleteTherapy();
    }
}
