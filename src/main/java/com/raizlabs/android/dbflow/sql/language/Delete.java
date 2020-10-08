package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.structure.Model;

public class Delete implements Query {
    public static <TModel extends Model> void table(Class<TModel> table, SQLCondition... conditions) {
        new Delete().from(table).where(conditions).query();
    }

    @SafeVarargs
    public static void tables(Class<? extends Model>... tables) {
        for (Class modelClass : tables) {
            table(modelClass, new SQLCondition[0]);
        }
    }

    public <TModel extends Model> From<TModel> from(Class<TModel> table) {
        return new From<>(this, table);
    }

    public String getQuery() {
        return new QueryBuilder().append("DELETE").appendSpace().getQuery();
    }
}
