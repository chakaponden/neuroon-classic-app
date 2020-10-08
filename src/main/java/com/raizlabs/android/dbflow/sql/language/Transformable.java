package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

interface Transformable<T extends Model> {
    Where<T> groupBy(NameAlias... nameAliasArr);

    Where<T> groupBy(IProperty... iPropertyArr);

    Where<T> having(SQLCondition... sQLConditionArr);

    Where<T> limit(int i);

    Where<T> offset(int i);

    Where<T> orderBy(NameAlias nameAlias, boolean z);

    Where<T> orderBy(OrderBy orderBy);

    Where<T> orderBy(IProperty iProperty, boolean z);
}
