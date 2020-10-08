package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class ByteProperty extends BaseProperty<ByteProperty> {
    public ByteProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public ByteProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public ByteProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public ByteProperty as(String aliasName) {
        return new ByteProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public ByteProperty plus(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty minus(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty dividedBy(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty multipliedBy(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty mod(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty concatenate(IProperty iProperty) {
        return new ByteProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public ByteProperty distinct() {
        return new ByteProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public ByteProperty withTable(NameAlias tableNameAlias) {
        return new ByteProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(byte value) {
        return Condition.column(this.nameAlias).is((Object) Byte.valueOf(value));
    }

    public Condition eq(byte value) {
        return Condition.column(this.nameAlias).eq((Object) Byte.valueOf(value));
    }

    public Condition isNot(byte value) {
        return Condition.column(this.nameAlias).isNot((Object) Byte.valueOf(value));
    }

    public Condition notEq(byte value) {
        return Condition.column(this.nameAlias).notEq((Object) Byte.valueOf(value));
    }

    public Condition like(byte value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(byte value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(byte value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Byte.valueOf(value));
    }

    public Condition greaterThanOrEq(byte value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Byte.valueOf(value));
    }

    public Condition lessThan(byte value) {
        return Condition.column(this.nameAlias).lessThan((Object) Byte.valueOf(value));
    }

    public Condition lessThanOrEq(byte value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Byte.valueOf(value));
    }

    public Condition.Between between(byte value) {
        return Condition.column(this.nameAlias).between((Object) Byte.valueOf(value));
    }

    public Condition.In in(byte firstValue, byte... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Byte.valueOf(firstValue), new Object[0]);
        for (byte value : values) {
            in.and(Byte.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(byte firstValue, byte... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Byte.valueOf(firstValue), new Object[0]);
        for (byte value : values) {
            in.and(Byte.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(byte value) {
        return Condition.column(this.nameAlias).concatenate((Object) Byte.valueOf(value));
    }

    public Condition is(ByteProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(ByteProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(ByteProperty property) {
        return is(property);
    }

    public Condition notEq(ByteProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(ByteProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(ByteProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(ByteProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(ByteProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
