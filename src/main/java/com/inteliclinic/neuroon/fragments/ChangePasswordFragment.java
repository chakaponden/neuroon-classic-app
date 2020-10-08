package com.inteliclinic.neuroon.fragments;

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
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.UserPasswordUpdateErrorEvent;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseTokenCallback;
import com.inteliclinic.neuroon.models.network.User;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import com.inteliclinic.neuroon.views.LightEditText;
import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChangePasswordFragment extends BaseFragment {
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755196)
    LightEditText mOldPassword;
    @InjectView(2131755197)
    LightEditText mPassword;
    @InjectView(2131755198)
    LightEditText mRepeatPassword;

    public interface OnFragmentInteractionListener extends OnProgressListener {
        void passwordChanged();
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("change_password");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onEvent(UserPasswordUpdateErrorEvent event) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, (int) R.string.connection_timed_out, 0).show();
        }
    }

    @OnClick({2131755199})
    public void onChangePasswordClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (this.mListener != null) {
            this.mListener.setProgress(true);
        }
        String s = this.mPassword.getText().toString();
        if (!User.isValidPassword(s) || s.equals(this.mRepeatPassword.getText().toString())) {
        }
        AccountManager.getInstance().changePassword(this.mOldPassword.getText().toString(), s, new BaseTokenCallback<Object>() {
            /* access modifiers changed from: protected */
            public boolean handleFailure(RetrofitError error) {
                View view;
                if (!super.handleFailure(error) && (view = ChangePasswordFragment.this.getView()) != null) {
                    Snackbar.make(view, (int) R.string.connection_timed_out, 0).show();
                }
                return false;
            }

            public void failure(RetrofitError error) {
                if (ChangePasswordFragment.this.mListener != null) {
                    ChangePasswordFragment.this.mListener.setProgress(false);
                }
                super.failure(error);
            }

            public void success(Object o, Response response) {
                if (ChangePasswordFragment.this.mListener != null) {
                    ChangePasswordFragment.this.mListener.setProgress(false);
                    ChangePasswordFragment.this.mListener.passwordChanged();
                }
            }
        });
    }
}
