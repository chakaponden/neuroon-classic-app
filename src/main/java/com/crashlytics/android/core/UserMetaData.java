package com.crashlytics.android.core;

public class UserMetaData {
    public static final UserMetaData EMPTY = new UserMetaData();
    public final String email;
    public final String id;
    public final String name;

    public UserMetaData() {
        this((String) null, (String) null, (String) null);
    }

    public UserMetaData(String id2, String name2, String email2) {
        this.id = id2;
        this.name = name2;
        this.email = email2;
    }

    public boolean isEmpty() {
        return this.id == null && this.name == null && this.email == null;
    }
}
