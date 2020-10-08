package com.raizlabs.android.dbflow.sql.language.property;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;

public class PropertyFactory {
    public static CharProperty from(char c) {
        return new CharProperty((Class<? extends Model>) null, NameAlias.rawBuilder("'" + c + "'").build());
    }

    public static IntProperty from(int i) {
        return new IntProperty((Class<? extends Model>) null, NameAlias.rawBuilder(i + "").build());
    }

    public static DoubleProperty from(double d) {
        return new DoubleProperty((Class<? extends Model>) null, NameAlias.rawBuilder(d + "").build());
    }

    public static LongProperty from(long l) {
        return new LongProperty((Class<? extends Model>) null, NameAlias.rawBuilder(l + "").build());
    }

    public static FloatProperty from(float f) {
        return new FloatProperty((Class<? extends Model>) null, NameAlias.rawBuilder(f + "").build());
    }

    public static ShortProperty from(short s) {
        return new ShortProperty((Class<? extends Model>) null, NameAlias.rawBuilder(s + "").build());
    }

    public static ByteProperty from(byte b) {
        return new ByteProperty((Class<? extends Model>) null, NameAlias.rawBuilder(b + "").build());
    }

    public static <T> Property<T> from(@Nullable T type) {
        return new Property<>((Class<? extends Model>) null, NameAlias.rawBuilder(Condition.convertValueToString(type)).build());
    }

    public static <TModel extends Model> Property<TModel> from(@NonNull ModelQueriable<TModel> queriable) {
        return from(queriable.getTable(), "(" + queriable.getQuery() + ")");
    }

    public static <T> Property<T> from(@Nullable Class<T> cls, String stringRepresentation) {
        return new Property<>((Class<? extends Model>) null, NameAlias.rawBuilder(stringRepresentation).build());
    }
}
