package com.inteliclinic.neuroon.models.network;

import android.util.Patterns;
import com.inteliclinic.neuroon.old_guava.Strings;

public class ResetPasswordUser {
    private String email;

    public static ResetPasswordUser create(String email2) {
        ResetPasswordUser resetPasswordUser = new ResetPasswordUser();
        resetPasswordUser.email = email2;
        return resetPasswordUser;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !Strings.isNullOrEmpty(target.toString()) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /* access modifiers changed from: protected */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }
}
