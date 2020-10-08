package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.utils.UnitConverter;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.text.DateFormat;
import java.util.Date;

public class ProfileFragment extends BaseFragment {
    @InjectView(2131755403)
    ThinTextView about;
    @InjectView(2131755423)
    LightTextView changePassword;
    @InjectView(2131755425)
    ThinTextView dateOfBirth;
    @InjectView(2131755424)
    ThinTextView gender;
    @InjectView(2131755422)
    LightTextView logOut;
    @InjectView(2131755426)
    ThinTextView mHeight;
    private OnFragmentInteractionListener mListener;
    private String mUserName;
    @InjectView(2131755427)
    ThinTextView mWeight;
    @InjectView(2131755337)
    LightTextView username;

    public interface OnFragmentInteractionListener {
        void changePassword();

        void logOut();

        void openAboutYou();

        void openDateOfBirth();

        void openGender();

        void openHeight();

        void openWeight();
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject((Object) this, inflate);
        this.logOut.setPaintFlags(8);
        this.changePassword.setPaintFlags(8);
        if (this.mUserName != null) {
            this.username.setText(this.mUserName);
        }
        return inflate;
    }

    private void getUser() {
        if (AccountManager.getInstance().isLogged()) {
            this.mUserName = AccountManager.getInstance().getUsername();
        }
    }

    public void onResume() {
        super.onResume();
        this.gender.setText(AccountManager.getInstance().getSexAsStringRes());
        Integer birthDate = AccountManager.getInstance().getBirthDate();
        if (birthDate != null) {
            this.dateOfBirth.setText(DateFormat.getDateInstance().format(new Date(((long) birthDate.intValue()) * 1000)));
        }
        double height = AccountManager.getInstance().getHeight();
        int heightInch = (int) UnitConverter.cmToInch(height);
        this.mHeight.setText(String.format("%s / %s", new Object[]{getString(R.string.some_cm, new Object[]{Integer.valueOf((int) height)}), getString(R.string.some_height, new Object[]{Integer.valueOf(heightInch / 12), Integer.valueOf(heightInch % 12)})}));
        double weight = AccountManager.getInstance().getWeight();
        int weightLbs = UnitConverter.kgToLbs(weight);
        this.mWeight.setText(String.format("%s / %s", new Object[]{getString(R.string.some_kg, new Object[]{Integer.valueOf((int) weight)}), getString(R.string.some_lbs, new Object[]{Integer.valueOf(weightLbs)})}));
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

    @OnClick({2131755422})
    public void onLogOutClick() {
        if (this.mListener != null) {
            this.mListener.logOut();
        }
    }

    @OnClick({2131755423})
    public void onChangePasswordClick() {
        if (this.mListener != null) {
            this.mListener.changePassword();
        }
    }

    @OnClick({2131755424})
    public void onGenderClick() {
        if (this.mListener != null) {
            this.mListener.openGender();
        }
    }

    @OnClick({2131755425})
    public void onDateOfBirthClick() {
        if (this.mListener != null) {
            this.mListener.openDateOfBirth();
        }
    }

    @OnClick({2131755426})
    public void onHeightClick() {
        if (this.mListener != null) {
            this.mListener.openHeight();
        }
    }

    @OnClick({2131755427})
    public void onWeightClick() {
        if (this.mListener != null) {
            this.mListener.openWeight();
        }
    }

    @OnClick({2131755403})
    public void onAboutYouClick() {
        if (this.mListener != null) {
            this.mListener.openAboutYou();
        }
    }
}
