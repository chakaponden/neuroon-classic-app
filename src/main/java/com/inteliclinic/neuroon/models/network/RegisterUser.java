package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;
import com.inteliclinic.neuroon.old_guava.Strings;

public class RegisterUser extends ResetPasswordUser {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String password;

    public static RegisterUser create(String email, String password2) {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail(email);
        registerUser.password = password2;
        return registerUser;
    }

    public static RegisterUser create(String firstName2, String lastName2, String email, String password2) {
        RegisterUser registerUser = new RegisterUser();
        registerUser.firstName = firstName2;
        registerUser.lastName = lastName2;
        registerUser.setEmail(email);
        registerUser.password = password2;
        return registerUser;
    }

    public static boolean isValidPassword(CharSequence text) {
        return !Strings.isNullOrEmpty(text.toString()) && text.length() >= 8;
    }

    public String getUsername() {
        return getEmail();
    }
}
