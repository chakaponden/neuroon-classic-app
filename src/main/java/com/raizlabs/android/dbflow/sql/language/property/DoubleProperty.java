package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class DoubleProperty extends BaseProperty<DoubleProperty> {
    public DoubleProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public DoubleProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public DoubleProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public DoubleProperty plus(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty minus(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty dividedBy(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty multipliedBy(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty mod(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty concatenate(IProperty iProperty) {
        return new DoubleProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public DoubleProperty as(String aliasName) {
        return new DoubleProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public DoubleProperty distinct() {
        return new DoubleProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public DoubleProperty withTable(NameAlias tableNameAlias) {
        return new DoubleProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(double value) {
        return Condition.column(this.nameAlias).is((Object) Double.valueOf(value));
    }

    public Condition eq(double value) {
        return Condition.column(this.nameAlias).eq((Object) Double.valueOf(value));
    }

    public Condition isNot(double value) {
        return Condition.column(this.nameAlias).isNot((Object) Double.valueOf(value));
    }

    public Condition notEq(double value) {
        return Condition.column(this.nameAlias).notEq((Object) Double.valueOf(value));
    }

    public Condition like(double value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(double value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(double value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Double.valueOf(value));
    }

    public Condition greaterThanOrEq(double value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Double.valueOf(value));
    }

    public Condition lessThan(double value) {
        return Condition.column(this.nameAlias).lessThan((Object) Double.valueOf(value));
    }

    public Condition lessThanOrEq(double value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Double.valueOf(value));
    }

    public Condition.Between between(double value) {
        return Condition.column(this.nameAlias).between((Object) Double.valueOf(value));
    }

    public Condition.In in(double firstValue, double... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Double.valueOf(firstValue), new Object[0]);
        for (double value : values) {
            in.and(Double.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(double firstValue, double... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Double.valueOf(firstValue), new Object[0]);
        for (double value : values) {
            in.and(Double.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(double value) {
        return Condition.column(this.nameAlias).concatenate((Object) Double.valueOf(value));
    }

    public Condition is(DoubleProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(DoubleProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(DoubleProperty property) {
        return is(property);
    }

    public Condition notEq(DoubleProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(DoubleProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(DoubleProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(DoubleProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(DoubleProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
