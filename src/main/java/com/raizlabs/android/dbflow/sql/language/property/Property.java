package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ITypeConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Collection;

public class Property<T> extends BaseProperty<Property<T>> implements ITypeConditional<T> {
    public static final Property ALL_PROPERTY = new Property((Class) null, Condition.Operation.MULTIPLY) {
        public /* bridge */ /* synthetic */ IProperty as(String x0) {
            return Property.super.as(x0);
        }

        public /* bridge */ /* synthetic */ IProperty concatenate(IProperty x0) {
            return Property.super.concatenate(x0);
        }

        public /* bridge */ /* synthetic */ IProperty distinct() {
            return Property.super.distinct();
        }

        public /* bridge */ /* synthetic */ IProperty dividedBy(IProperty x0) {
            return Property.super.dividedBy(x0);
        }

        public /* bridge */ /* synthetic */ IProperty minus(IProperty x0) {
            return Property.super.minus(x0);
        }

        public /* bridge */ /* synthetic */ IProperty mod(IProperty x0) {
            return Property.super.mod(x0);
        }

        public /* bridge */ /* synthetic */ IProperty multipliedBy(IProperty x0) {
            return Property.super.multipliedBy(x0);
        }

        public /* bridge */ /* synthetic */ IProperty plus(IProperty x0) {
            return Property.super.plus(x0);
        }

        public /* bridge */ /* synthetic */ IProperty withTable(NameAlias x0) {
            return Property.super.withTable(x0);
        }

        public String toString() {
            return this.nameAlias.nameRaw();
        }
    };

    public Property(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public Property(Class<? extends Model> table, String columnName) {
        super(table, (NameAlias) null);
        if (columnName != null) {
            this.nameAlias = new NameAlias.Builder(columnName).build();
        }
    }

    public Property<T> plus(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> minus(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> dividedBy(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> multipliedBy(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> mod(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> concatenate(IProperty iProperty) {
        return new Property<>((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public Property<T> as(String aliasName) {
        return new Property<>((Class<? extends Model>) this.table, getNameAlias().newBuilder().as(aliasName).build());
    }

    public Property<T> distinct() {
        return new Property<>((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public Property<T> withTable(NameAlias tableNameAlias) {
        return new Property<>((Class<? extends Model>) this.table, getNameAlias().newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(T value) {
        return Condition.column(getNameAlias()).is((Object) value);
    }

    public Condition eq(T value) {
        return Condition.column(getNameAlias()).eq((Object) value);
    }

    public Condition isNot(T value) {
        return Condition.column(getNameAlias()).isNot((Object) value);
    }

    public Condition notEq(T value) {
        return Condition.column(getNameAlias()).notEq((Object) value);
    }

    public Condition like(String value) {
        return Condition.column(getNameAlias()).like(value);
    }

    public Condition glob(String value) {
        return Condition.column(getNameAlias()).glob(value);
    }

    public Condition greaterThan(T value) {
        return Condition.column(getNameAlias()).greaterThan((Object) value);
    }

    public Condition greaterThanOrEq(T value) {
        return Condition.column(getNameAlias()).greaterThanOrEq((Object) value);
    }

    public Condition lessThan(T value) {
        return Condition.column(getNameAlias()).lessThan((Object) value);
    }

    public Condition lessThanOrEq(T value) {
        return Condition.column(getNameAlias()).lessThanOrEq((Object) value);
    }

    public Condition.Between between(T value) {
        return Condition.column(getNameAlias()).between((Object) value);
    }

    public Condition.In in(T firstValue, T... values) {
        return Condition.column(getNameAlias()).in((Object) firstValue, (Object[]) values);
    }

    public Condition.In notIn(T firstValue, T... values) {
        return Condition.column(getNameAlias()).notIn((Object) firstValue, (Object[]) values);
    }

    public Condition.In in(Collection<T> values) {
        return Condition.column(getNameAlias()).in(values);
    }

    public Condition.In notIn(Collection<T> values) {
        return Condition.column(getNameAlias()).notIn(values);
    }

    public Condition concatenate(T value) {
        return Condition.column(getNameAlias()).concatenate((Object) value);
    }
}
