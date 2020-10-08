package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.structure.Model;

public interface WhereBase<TModel extends Model> extends Query {
    Query getQueryBuilderBase();

    Class<TModel> getTable();
}
