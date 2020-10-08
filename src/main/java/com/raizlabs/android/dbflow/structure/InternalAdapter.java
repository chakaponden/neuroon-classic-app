package com.raizlabs.android.dbflow.structure;

import android.content.ContentValues;
import android.support.annotation.IntRange;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Collection;

public interface InternalAdapter<TModel extends Model> {
    void bindToContentValues(ContentValues contentValues, TModel tmodel);

    void bindToInsertStatement(DatabaseStatement databaseStatement, TModel tmodel);

    void bindToInsertStatement(DatabaseStatement databaseStatement, TModel tmodel, @IntRange(from = 0, to = 1) int i);

    void bindToInsertValues(ContentValues contentValues, TModel tmodel);

    void bindToStatement(DatabaseStatement databaseStatement, TModel tmodel);

    boolean cachingEnabled();

    void delete(TModel tmodel);

    void delete(TModel tmodel, DatabaseWrapper databaseWrapper);

    Number getAutoIncrementingId(TModel tmodel);

    String getTableName();

    void insert(TModel tmodel);

    void insert(TModel tmodel, DatabaseWrapper databaseWrapper);

    void insertAll(Collection<TModel> collection);

    void insertAll(Collection<TModel> collection, DatabaseWrapper databaseWrapper);

    void save(TModel tmodel);

    void save(TModel tmodel, DatabaseWrapper databaseWrapper);

    void saveAll(Collection<TModel> collection);

    void saveAll(Collection<TModel> collection, DatabaseWrapper databaseWrapper);

    void update(TModel tmodel);

    void update(TModel tmodel, DatabaseWrapper databaseWrapper);

    void updateAll(Collection<TModel> collection);

    void updateAll(Collection<TModel> collection, DatabaseWrapper databaseWrapper);

    void updateAutoIncrement(TModel tmodel, Number number);
}
