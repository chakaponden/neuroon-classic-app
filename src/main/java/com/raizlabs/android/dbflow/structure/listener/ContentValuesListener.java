package com.raizlabs.android.dbflow.structure.listener;

import android.content.ContentValues;

public interface ContentValuesListener {
    void onBindToContentValues(ContentValues contentValues);

    void onBindToInsertValues(ContentValues contentValues);
}
