package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;

public class ListModelLoader<TModel extends Model> extends ModelLoader<TModel, List<TModel>> {
    public ListModelLoader(Class<TModel> modelClass) {
        super(modelClass);
    }

    public List<TModel> convertToData(@NonNull Cursor cursor, @Nullable List<TModel> data) {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        if (cursor.moveToFirst()) {
            do {
                TModel model = getInstanceAdapter().newInstance();
                getInstanceAdapter().loadFromCursor(cursor, model);
                data.add(model);
            } while (cursor.moveToNext());
        }
        return data;
    }
}
