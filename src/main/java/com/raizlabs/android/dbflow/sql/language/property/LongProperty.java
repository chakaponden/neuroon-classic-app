package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class LongProperty extends BaseProperty<LongProperty> {
    public LongProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public LongProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public LongProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public LongProperty plus(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty minus(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty dividedBy(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty multipliedBy(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty mod(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty concatenate(IProperty iProperty) {
        return new LongProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public LongProperty as(String aliasName) {
        return new LongProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public LongProperty distinct() {
        return new LongProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public LongProperty withTable(NameAlias tableNameAlias) {
        return new LongProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(long value) {
        return Condition.column(this.nameAlias).is((Object) Long.valueOf(value));
    }

    public Condition eq(long value) {
        return Condition.column(this.nameAlias).eq((Object) Long.valueOf(value));
    }

    public Condition isNot(long value) {
        return Condition.column(this.nameAlias).isNot((Object) Long.valueOf(value));
    }

    public Condition notEq(long value) {
        return Condition.column(this.nameAlias).notEq((Object) Long.valueOf(value));
    }

    public Condition like(long value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(long value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(long value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Long.valueOf(value));
    }

    public Condition greaterThanOrEq(long value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Long.valueOf(value));
    }

    public Condition lessThan(long value) {
        return Condition.column(this.nameAlias).lessThan((Object) Long.valueOf(value));
    }

    public Condition lessThanOrEq(long value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Long.valueOf(value));
    }

    public Condition.Between between(long value) {
        return Condition.column(this.nameAlias).between((Object) Long.valueOf(value));
    }

    public Condition.In in(long firstValue, long... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Long.valueOf(firstValue), new Object[0]);
        for (long value : values) {
            in.and(Long.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(long firstValue, long... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Long.valueOf(firstValue), new Object[0]);
        for (long value : values) {
            in.and(Long.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(long value) {
        return Condition.column(this.nameAlias).concatenate((Object) Long.valueOf(value));
    }

    public Condition is(LongProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(LongProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(LongProperty property) {
        return is(property);
    }

    public Condition notEq(LongProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(LongProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(LongProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(LongProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(LongProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
