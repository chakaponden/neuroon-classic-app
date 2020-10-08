package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;

public class ProfileFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final ProfileFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.username = (LightTextView) finder.findRequiredView(source, R.id.username, "field 'username'");
        View view = finder.findRequiredView(source, R.id.log_out, "field 'logOut' and method 'onLogOutClick'");
        target.logOut = (LightTextView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLogOutClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.change_password, "field 'changePassword' and method 'onChangePasswordClick'");
        target.changePassword = (LightTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onChangePasswordClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.gender, "field 'gender' and method 'onGenderClick'");
        target.gender = (ThinTextView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onGenderClick();
            }
        });
        View view4 = finder.findRequiredView(source, R.id.date_of_birth, "field 'dateOfBirth' and method 'onDateOfBirthClick'");
        target.dateOfBirth = (ThinTextView) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onDateOfBirthClick();
            }
        });
        View view5 = finder.findRequiredView(source, R.id.height, "field 'mHeight' and method 'onHeightClick'");
        target.mHeight = (ThinTextView) view5;
        view5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onHeightClick();
            }
        });
        View view6 = finder.findRequiredView(source, R.id.weight, "field 'mWeight' and method 'onWeightClick'");
        target.mWeight = (ThinTextView) view6;
        view6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onWeightClick();
            }
        });
        View view7 = finder.findRequiredView(source, R.id.about, "field 'about' and method 'onAboutYouClick'");
        target.about = (ThinTextView) view7;
        view7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAboutYouClick();
            }
        });
    }

    public static void reset(ProfileFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.username = null;
        target.logOut = null;
        target.changePassword = null;
        target.gender = null;
        target.dateOfBirth = null;
        target.mHeight = null;
        target.mWeight = null;
        target.about = null;
    }
}
