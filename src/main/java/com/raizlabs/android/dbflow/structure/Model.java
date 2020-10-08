package com.raizlabs.android.dbflow.structure;

public interface Model {
    void delete();

    boolean exists();

    void insert();

    void save();

    void update();
}
