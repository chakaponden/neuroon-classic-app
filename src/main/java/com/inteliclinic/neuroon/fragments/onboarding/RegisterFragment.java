package com.inteliclinic.neuroon.fragments.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnProgressListener;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.network.callbacks.RegisterCallback;
import com.inteliclinic.neuroon.models.network.RegisterUser;
import com.inteliclinic.neuroon.models.network.User;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import com.inteliclinic.neuroon.utils.TextUtils;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.ThinCheckbox;
import com.inteliclinic.neuroon.views.ThinTextView;
import io.fabric.sdk.android.Fabric;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterFragment extends BaseFragment implements TextWatcher {
    @InjectView(2131755448)
    ThinCheckbox acceptTerms;
    @InjectView(2131755449)
    ThinTextView acceptTermsText;
    @InjectView(2131755338)
    ThinTextView mError;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755197)
    EditText password;
    @InjectView(2131755198)
    LightEditText repeatPassword;
    @InjectView(2131755337)
    EditText username;

    public interface OnFragmentInteractionListener extends OnProgressListener {
        void openTermsAndConditions();

        void registerSuccess(User user, String str);
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.inject((Object) this, view);
        this.username.addTextChangedListener(this);
        this.password.addTextChangedListener(this);
        this.repeatPassword.addTextChangedListener(this);
        TextUtils.setColor(this.acceptTermsText, getString(R.string.t_c_hightlight), getResources().getColor(R.color.blue_3054FA));
        this.acceptTermsText.setClickable(true);
        this.acceptTermsText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (RegisterFragment.this.mListener != null) {
                    RegisterFragment.this.mListener.openTermsAndConditions();
                }
            }
        });
        this.acceptTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RegisterFragment.this.mError.setVisibility(8);
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("sign_up");
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

    @OnClick({2131755131})
    public void onBackButtonClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        getFragmentManager().popBackStack();
    }

    @OnClick({2131755450})
    public void signUp() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (checkFields()) {
            if (this.mListener != null) {
                this.mListener.setProgress(true);
            }
            AccountManager.getInstance().register(RegisterUser.create((String) null, (String) null, this.username.getText().toString(), this.password.getText().toString()), new RegisterCallback() {
                /* access modifiers changed from: protected */
                public void handleBadData(RetrofitError error) {
                }

                /* access modifiers changed from: protected */
                public void handleConflict(RetrofitError error) {
                    View view = RegisterFragment.this.getView();
                    if (view != null) {
                        Snackbar.make(view, (int) R.string.account_exists, 0).show();
                    }
                }

                /* access modifiers changed from: protected */
                public void handleUnauthorizedClient(RetrofitError error) {
                    View view = RegisterFragment.this.getView();
                    if (view != null) {
                        Snackbar.make(view, (int) R.string.unexpected_error_occurred, 0).show();
                    }
                }

                /* access modifiers changed from: protected */
                public void handleBadRequest(RetrofitError error) {
                }

                /* access modifiers changed from: protected */
                public void handleUnexpectedOperation() {
                    View view = RegisterFragment.this.getView();
                    if (view != null) {
                        Snackbar.make(view, (int) R.string.connection_timed_out, 0).show();
                    }
                }

                public void success(User user, Response response) {
                    if (RegisterFragment.this.mListener != null) {
                        RegisterFragment.this.mListener.setProgress(false);
                        if (Fabric.isInitialized()) {
                            Answers.getInstance().logSignUp(new SignUpEvent());
                        }
                        RegisterFragment.this.mListener.registerSuccess(user, RegisterFragment.this.password.getText().toString());
                    }
                }

                public void failure(RetrofitError error) {
                    if (RegisterFragment.this.mListener != null) {
                        RegisterFragment.this.mListener.setProgress(false);
                    }
                    super.failure(error);
                }

                /* access modifiers changed from: protected */
                public void unexpectedError(RetrofitError error) {
                    Crashlytics.log(error.toString());
                    View view = RegisterFragment.this.getView();
                    if (view != null) {
                        Snackbar.make(view, (int) R.string.unexpected_error_occurred, 0).show();
                    }
                }
            });
        }
    }

    private boolean checkFields() {
        if (!User.isValidEmail(this.username.getText())) {
            this.mError.setVisibility(0);
            this.mError.setText(R.string.incorrect_email);
            return false;
        } else if (!User.isValidPassword(this.password.getText())) {
            this.mError.setVisibility(0);
            this.mError.setText(R.string.password_must_contain_at_least_8_characters);
            return false;
        } else if (!this.password.getText().toString().equals(this.repeatPassword.getText().toString())) {
            this.mError.setVisibility(0);
            this.mError.setText(R.string.password_do_not_match);
            return false;
        } else if (this.acceptTerms.isChecked()) {
            return true;
        } else {
            this.mError.setText(R.string.you_have_accept_t_and_c);
            this.mError.setVisibility(0);
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
