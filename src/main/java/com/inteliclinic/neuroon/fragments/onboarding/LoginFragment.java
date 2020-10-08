package com.inteliclinic.neuroon.fragments.onboarding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnProgressListener;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.network.callbacks.LoginCallback;
import com.inteliclinic.neuroon.models.network.AccessToken;
import com.inteliclinic.neuroon.old_guava.Strings;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragment extends BaseFragment implements TextWatcher {
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    @InjectView(2131755338)
    ThinTextView mError;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755340)
    Button mLogIn;
    @InjectView(2131755197)
    LightEditText mPassword;
    @InjectView(2131755339)
    LightTextView mPasswordReset;
    private String mPasswordValue;
    @InjectView(2131755337)
    LightEditText mUsername;
    private String mUsernameValue;

    public interface OnFragmentInteractionListener extends OnProgressListener {
        void loginSuccess(String str);

        void requestReset();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public static LoginFragment newInstance(String username, String password) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME, username);
        bundle.putString(PASSWORD, password);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mUsernameValue = getArguments().getString(USERNAME);
            this.mPasswordValue = getArguments().getString(PASSWORD);
        }
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("sign_in");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject((Object) this, view);
        this.mUsername.addTextChangedListener(this);
        this.mPassword.addTextChangedListener(this);
        if (!(this.mUsernameValue == null || this.mPasswordValue == null)) {
            this.mUsername.setText(this.mUsernameValue);
            this.mPassword.setText(this.mPasswordValue);
            onLogInClick();
        }
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

    @OnClick({2131755131})
    public void onBackButtonClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        getFragmentManager().popBackStack();
    }

    @OnClick({2131755340})
    public void onLogInClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (checkFields()) {
            if (this.mListener != null) {
                this.mListener.setProgress(true);
            }
            final String username2 = this.mUsername.getText().toString();
            AccountManager.getInstance().login(username2, this.mPassword.getText().toString(), new LoginCallback() {
                public void success(AccessToken accessToken, Response response) {
                    if (LoginFragment.this.mListener != null) {
                        LoginFragment.this.mListener.loginSuccess(username2);
                        Answers.getInstance().logLogin(new LoginEvent().putSuccess(true));
                    }
                }

                /* access modifiers changed from: protected */
                public boolean handleFailure(RetrofitError error) {
                    if (super.handleFailure(error)) {
                        return true;
                    }
                    new AlertDialog.Builder(LoginFragment.this.getActivity()).setMessage(R.string.connection_timed_out).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).show();
                    return true;
                }

                public void failure(RetrofitError error) {
                    super.failure(error);
                    if (LoginFragment.this.mListener != null) {
                        LoginFragment.this.mListener.setProgress(false);
                    }
                }

                /* access modifiers changed from: protected */
                public void unauthorized() {
                    super.unauthorized();
                    LoginFragment.this.getTracker().send(new HitBuilders.EventBuilder().setCategory("sign_in").setAction("incorrect").setLabel("incorrect").build());
                    Answers.getInstance().logLogin(new LoginEvent().putSuccess(false));
                    new AlertDialog.Builder(LoginFragment.this.getActivity()).setMessage(R.string.incorrect_email_or_password).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).show();
                }
            });
        }
    }

    @OnClick({2131755339})
    public void onResetPasswordClick() {
        if (this.mListener != null) {
            this.mListener.requestReset();
        }
    }

    private boolean checkFields() {
        if (Strings.isNullOrEmpty(this.mUsername.getText().toString())) {
            this.mError.setVisibility(0);
            this.mError.setText(R.string.incorrect_email);
            return false;
        } else if (!Strings.isNullOrEmpty(this.mPassword.getText().toString())) {
            return true;
        } else {
            this.mError.setVisibility(0);
            this.mError.setText(R.string.password_cannot_be_empty);
            return false;
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
        if (this.mError != null) {
            this.mError.setVisibility(8);
        }
    }
}
