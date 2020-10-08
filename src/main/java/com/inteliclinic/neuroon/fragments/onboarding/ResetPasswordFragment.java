package com.inteliclinic.neuroon.fragments.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnProgressListener;
import com.inteliclinic.neuroon.managers.network.NetworkManager;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseCallback;
import com.inteliclinic.neuroon.models.network.ResetPasswordUser;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import com.inteliclinic.neuroon.views.LightEditText;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ResetPasswordFragment extends BaseFragment {
    @InjectView(2131755451)
    LightEditText email;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener extends OnProgressListener {
        void passwordReset();
    }

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("reset_password");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
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

    @OnClick({2131755339})
    public void onRequestResetClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (this.mListener != null) {
            this.mListener.setProgress(true);
        }
        NetworkManager.getInstance().resetPassword(ResetPasswordUser.create(this.email.getText().toString()), new BaseCallback<String>() {
            public void success(String o, Response response) {
                if (ResetPasswordFragment.this.mListener != null) {
                    ResetPasswordFragment.this.mListener.setProgress(false);
                    ResetPasswordFragment.this.mListener.passwordReset();
                }
            }

            /* access modifiers changed from: protected */
            public boolean notFound() {
                View view = ResetPasswordFragment.this.getView();
                if (view == null) {
                    return true;
                }
                Snackbar.make(view, (int) R.string.incorrect_email, 0).show();
                return true;
            }

            /* access modifiers changed from: protected */
            public boolean notPassValidation() {
                View view = ResetPasswordFragment.this.getView();
                if (view == null) {
                    return true;
                }
                Snackbar.make(view, (int) R.string.incorrect_email, 0).show();
                return true;
            }

            /* access modifiers changed from: protected */
            public boolean handleFailure(RetrofitError error) {
                View view;
                if (!super.handleFailure(error) && (view = ResetPasswordFragment.this.getView()) != null) {
                    Snackbar.make(view, (int) R.string.connection_timed_out, 0).show();
                }
                return false;
            }

            public void failure(RetrofitError error) {
                if (ResetPasswordFragment.this.mListener != null) {
                    ResetPasswordFragment.this.mListener.setProgress(false);
                }
                super.failure(error);
            }
        });
    }
}
