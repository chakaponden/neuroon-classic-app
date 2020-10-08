package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class IntProperty extends BaseProperty<IntProperty> {
    public IntProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public IntProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public IntProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public IntProperty plus(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty minus(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty dividedBy(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty multipliedBy(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty mod(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty concatenate(IProperty iProperty) {
        return new IntProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public IntProperty as(String aliasName) {
        return new IntProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public IntProperty distinct() {
        return new IntProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public IntProperty withTable(NameAlias tableNameAlias) {
        return new IntProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(int value) {
        return Condition.column(this.nameAlias).is((Object) Integer.valueOf(value));
    }

    public Condition eq(int value) {
        return Condition.column(this.nameAlias).eq((Object) Integer.valueOf(value));
    }

    public Condition isNot(int value) {
        return Condition.column(this.nameAlias).isNot((Object) Integer.valueOf(value));
    }

    public Condition notEq(int value) {
        return Condition.column(this.nameAlias).notEq((Object) Integer.valueOf(value));
    }

    public Condition like(int value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(int value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(int value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Integer.valueOf(value));
    }

    public Condition greaterThanOrEq(int value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Integer.valueOf(value));
    }

    public Condition lessThan(int value) {
        return Condition.column(this.nameAlias).lessThan((Object) Integer.valueOf(value));
    }

    public Condition lessThanOrEq(int value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Integer.valueOf(value));
    }

    public Condition.Between between(int value) {
        return Condition.column(this.nameAlias).between((Object) Integer.valueOf(value));
    }

    public Condition.In in(int firstValue, int... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Integer.valueOf(firstValue), new Object[0]);
        for (int value : values) {
            in.and(Integer.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(int firstValue, int... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Integer.valueOf(firstValue), new Object[0]);
        for (int value : values) {
            in.and(Integer.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(int value) {
        return Condition.column(this.nameAlias).concatenate((Object) Integer.valueOf(value));
    }

    public Condition is(IntProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(IntProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(IntProperty property) {
        return is(property);
    }

    public Condition notEq(IntProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(IntProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(IntProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(IntProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(IntProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
