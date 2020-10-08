package com.raizlabs.android.dbflow.structure.listener;

import android.database.Cursor;

public interface LoadFromCursorListener {
    void onLoadFromCursor(Cursor cursor);
}
