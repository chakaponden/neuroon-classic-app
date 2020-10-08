package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class ShortProperty extends BaseProperty<ShortProperty> {
    public ShortProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public ShortProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public ShortProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public ShortProperty plus(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty minus(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty dividedBy(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty multipliedBy(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty mod(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty concatenate(IProperty iProperty) {
        return new ShortProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ShortProperty as(String aliasName) {
        return new ShortProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public ShortProperty distinct() {
        return new ShortProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public ShortProperty withTable(NameAlias tableNameAlias) {
        return new ShortProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(short value) {
        return Condition.column(this.nameAlias).is((Object) Short.valueOf(value));
    }

    public Condition eq(short value) {
        return Condition.column(this.nameAlias).eq((Object) Short.valueOf(value));
    }

    public Condition isNot(short value) {
        return Condition.column(this.nameAlias).isNot((Object) Short.valueOf(value));
    }

    public Condition notEq(short value) {
        return Condition.column(this.nameAlias).notEq((Object) Short.valueOf(value));
    }

    public Condition like(short value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(short value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(short value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Short.valueOf(value));
    }

    public Condition greaterThanOrEq(short value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Short.valueOf(value));
    }

    public Condition lessThan(short value) {
        return Condition.column(this.nameAlias).lessThan((Object) Short.valueOf(value));
    }

    public Condition lessThanOrEq(short value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Short.valueOf(value));
    }

    public Condition.Between between(short value) {
        return Condition.column(this.nameAlias).between((Object) Short.valueOf(value));
    }

    public Condition.In in(short firstValue, short... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Short.valueOf(firstValue), new Object[0]);
        for (short value : values) {
            in.and(Short.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(short firstValue, short... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Short.valueOf(firstValue), new Object[0]);
        for (short value : values) {
            in.and(Short.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(short value) {
        return Condition.column(this.nameAlias).concatenate((Object) Short.valueOf(value));
    }

    public Condition is(ShortProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(ShortProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(ShortProperty property) {
        return is(property);
    }

    public Condition notEq(ShortProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(ShortProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(ShortProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(ShortProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(ShortProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
