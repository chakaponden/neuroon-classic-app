package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.ThinButton;

public class SexFragment extends BaseFragment {
    private static final String ARG_STANDALONE = "is_standalone";
    private boolean mIsStandalone;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755452)
    LinearLayout men;
    @InjectView(2131755132)
    ThinButton next;
    @InjectView(2131755134)
    LinearLayout stepIndicator;
    @InjectView(2131755453)
    LinearLayout women;

    public interface OnFragmentInteractionListener {
        void openDateOfBirth();
    }

    public static SexFragment newInstance() {
        return new SexFragment();
    }

    public static SexFragment newInstance(boolean isStandalone) {
        SexFragment fragment = new SexFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STANDALONE, isStandalone);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIsStandalone = getArguments().getBoolean(ARG_STANDALONE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sex, container, false);
        ButterKnife.inject((Object) this, view);
        setSex(AccountManager.getInstance().getSex());
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("sex");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
        if (this.mIsStandalone) {
            this.next.setText(R.string.save);
            this.stepIndicator.setVisibility(4);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
        }
    }

    public void setSex(boolean isWomen) {
        disableEnableControls(isWomen, this.men);
        disableEnableControls(!isWomen, this.women);
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755452})
    public void onMenClick() {
        setSex(false);
        getTracker().send(new HitBuilders.EventBuilder().setCategory("sex").setAction("male").setLabel("male").build());
        AccountManager.getInstance().setSex(false);
    }

    @OnClick({2131755453})
    public void onWomenClick() {
        setSex(true);
        getTracker().send(new HitBuilders.EventBuilder().setCategory("sex").setAction("female").setLabel("female").build());
        AccountManager.getInstance().setSex(true);
    }

    @OnClick({2131755132})
    public void onNextClick() {
        if (this.mListener != null) {
            this.mListener.openDateOfBirth();
        } else if (this.mIsStandalone) {
            getFragmentManager().popBackStack();
        }
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }
}
