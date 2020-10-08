package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public interface IProperty<P extends IProperty> extends Query {
    P as(String str);

    P concatenate(IProperty iProperty);

    P distinct();

    P dividedBy(IProperty iProperty);

    String getContainerKey();

    String getCursorKey();

    NameAlias getNameAlias();

    Class<? extends Model> getTable();

    P minus(IProperty iProperty);

    P mod(IProperty iProperty);

    P multipliedBy(IProperty iProperty);

    P plus(IProperty iProperty);

    P withTable();

    P withTable(NameAlias nameAlias);
}
