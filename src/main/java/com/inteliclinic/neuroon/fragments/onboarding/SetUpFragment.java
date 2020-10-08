package com.inteliclinic.neuroon.fragments.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.ThinTextView;
import io.intercom.android.sdk.Intercom;

public class SetUpFragment extends BaseFragment {
    private static final String TAG = SetUpFragment.class.getSimpleName();
    @InjectView(2131755340)
    FrameLayout logIn;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755408)
    ThinTextView questions;
    @InjectView(2131755409)
    FrameLayout questionsBtn;
    @InjectView(2131755450)
    FrameLayout signUp;

    public interface OnFragmentInteractionListener {
        void onOpenLogInClick();

        void onOpenSignUpClick();
    }

    public static SetUpFragment newInstance() {
        return new SetUpFragment();
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("join");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i;
        int i2 = 0;
        View view = inflater.inflate(R.layout.fragment_set_up, container, false);
        ButterKnife.inject((Object) this, view);
        ThinTextView thinTextView = this.questions;
        if (AccountManager.getInstance().isIntercomEnabled()) {
            i = 0;
        } else {
            i = 4;
        }
        thinTextView.setVisibility(i);
        FrameLayout frameLayout = this.questionsBtn;
        if (!AccountManager.getInstance().isIntercomEnabled()) {
            i2 = 4;
        }
        frameLayout.setVisibility(i2);
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

    @OnClick({2131755450})
    public void signUp() {
        if (this.mListener != null) {
            this.mListener.onOpenSignUpClick();
        }
    }

    @OnClick({2131755340})
    public void logIn() {
        if (this.mListener != null) {
            this.mListener.onOpenLogInClick();
        }
    }

    @OnClick({2131755409})
    public void onQuestionsClick() {
        if (AccountManager.getInstance().isIntercomEnabled()) {
            Intercom.client().displayMessageComposer();
        }
    }
}
