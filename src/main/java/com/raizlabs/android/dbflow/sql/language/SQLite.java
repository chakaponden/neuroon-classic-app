package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.trigger.Trigger;
import com.raizlabs.android.dbflow.structure.Model;

public class SQLite {
    public static Select select(IProperty... properties) {
        return new Select(properties);
    }

    public static Select selectCountOf(IProperty... properties) {
        return new Select(Method.count(properties));
    }

    public static <TModel extends Model> Update<TModel> update(Class<TModel> table) {
        return new Update<>(table);
    }

    public static <TModel extends Model> Insert<TModel> insert(Class<TModel> table) {
        return new Insert<>(table);
    }

    public static Delete delete() {
        return new Delete();
    }

    public static <TModel extends Model> From<TModel> delete(Class<TModel> table) {
        return delete().from(table);
    }

    public static <TModel extends Model> Index<TModel> index(String name) {
        return new Index<>(name);
    }

    public static Trigger createTrigger(String name) {
        return Trigger.create(name);
    }

    public static <TReturn> CaseCondition<TReturn> caseWhen(@NonNull SQLCondition condition) {
        return new Case().when(condition);
    }

    public static <TReturn> Case<TReturn> _case(Property<TReturn> caseColumn) {
        return new Case<>(caseColumn);
    }

    public static <TReturn> Case<TReturn> _case(IProperty caseColumn) {
        return new Case<>(caseColumn);
    }
}
