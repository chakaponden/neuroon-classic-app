package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class FloatProperty extends BaseProperty<FloatProperty> {
    public FloatProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public FloatProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public FloatProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public FloatProperty plus(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty minus(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty dividedBy(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty multipliedBy(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty mod(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty concatenate(IProperty iProperty) {
        return new FloatProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public FloatProperty as(String aliasName) {
        return new FloatProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public FloatProperty distinct() {
        return new FloatProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public FloatProperty withTable(NameAlias tableNameAlias) {
        return new FloatProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(float value) {
        return Condition.column(this.nameAlias).is((Object) Float.valueOf(value));
    }

    public Condition eq(float value) {
        return Condition.column(this.nameAlias).eq((Object) Float.valueOf(value));
    }

    public Condition isNot(float value) {
        return Condition.column(this.nameAlias).isNot((Object) Float.valueOf(value));
    }

    public Condition notEq(float value) {
        return Condition.column(this.nameAlias).notEq((Object) Float.valueOf(value));
    }

    public Condition like(float value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(float value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(float value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Float.valueOf(value));
    }

    public Condition greaterThanOrEq(float value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Float.valueOf(value));
    }

    public Condition lessThan(float value) {
        return Condition.column(this.nameAlias).lessThan((Object) Float.valueOf(value));
    }

    public Condition lessThanOrEq(float value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Float.valueOf(value));
    }

    public Condition.Between between(float value) {
        return Condition.column(this.nameAlias).between((Object) Float.valueOf(value));
    }

    public Condition.In in(float firstValue, float... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Float.valueOf(firstValue), new Object[0]);
        for (float value : values) {
            in.and(Float.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(float firstValue, float... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Float.valueOf(firstValue), new Object[0]);
        for (float value : values) {
            in.and(Float.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(float value) {
        return Condition.column(this.nameAlias).concatenate((Object) Float.valueOf(value));
    }

    public Condition is(FloatProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(FloatProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(FloatProperty property) {
        return is(property);
    }

    public Condition notEq(FloatProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(FloatProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(FloatProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(FloatProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(FloatProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
