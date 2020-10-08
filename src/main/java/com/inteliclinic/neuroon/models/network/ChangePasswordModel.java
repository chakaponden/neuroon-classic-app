package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {
    @SerializedName("new_password")
    private String newPassword;
    @SerializedName("old_password")
    private String oldPassword;

    public ChangePasswordModel(String oldPassword2, String newPassword2) {
        this.oldPassword = oldPassword2;
        this.newPassword = newPassword2;
    }
}
