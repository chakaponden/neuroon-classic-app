package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private int expireTime;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("user_id")
    private int userId;

    public AccessToken() {
    }

    private AccessToken(String accessToken2, String tokenType2, String refreshToken2) {
        this.accessToken = accessToken2;
        this.tokenType = tokenType2;
        this.refreshToken = refreshToken2;
    }

    public static AccessToken createInstance(String accessToken2, String tokenType2, String refreshToken2) {
        return new AccessToken(accessToken2, tokenType2, refreshToken2);
    }

    public static AccessToken changeInstance(AccessToken originalAccessToken, AccessToken newAccessToken) {
        if (originalAccessToken == null) {
            return newAccessToken;
        }
        originalAccessToken.accessToken = newAccessToken.accessToken;
        originalAccessToken.expireTime = newAccessToken.expireTime;
        originalAccessToken.tokenType = newAccessToken.tokenType;
        originalAccessToken.refreshToken = newAccessToken.refreshToken;
        originalAccessToken.userId = newAccessToken.userId;
        return originalAccessToken;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }
}
