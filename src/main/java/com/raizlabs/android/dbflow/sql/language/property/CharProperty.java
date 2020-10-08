package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.structure.Model;

public class CharProperty extends BaseProperty<CharProperty> {
    public CharProperty(Class<? extends Model> table, NameAlias nameAlias) {
        super(table, nameAlias);
    }

    public CharProperty(Class<? extends Model> table, String columnName) {
        this(table, new NameAlias.Builder(columnName).build());
    }

    public CharProperty(Class<? extends Model> table, String columnName, String aliasName) {
        this(table, new NameAlias.Builder(columnName).as(aliasName).build());
    }

    public CharProperty plus(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.PLUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty minus(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MINUS, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty dividedBy(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.DIVISION, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty multipliedBy(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MULTIPLY, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty mod(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.MOD, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty concatenate(IProperty iProperty) {
        return new CharProperty((Class<? extends Model>) this.table, NameAlias.joinNames(Condition.Operation.CONCATENATE, this.nameAlias.fullName(), iProperty.toString()));
    }

    public CharProperty as(String aliasName) {
        return new CharProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().as(aliasName).build());
    }

    public CharProperty distinct() {
        return new CharProperty((Class<? extends Model>) this.table, getDistinctAliasName());
    }

    public CharProperty withTable(NameAlias tableNameAlias) {
        return new CharProperty((Class<? extends Model>) this.table, this.nameAlias.newBuilder().withTable(tableNameAlias.getQuery()).build());
    }

    public Condition is(char value) {
        return Condition.column(this.nameAlias).is((Object) Character.valueOf(value));
    }

    public Condition eq(char value) {
        return Condition.column(this.nameAlias).eq((Object) Character.valueOf(value));
    }

    public Condition isNot(char value) {
        return Condition.column(this.nameAlias).isNot((Object) Character.valueOf(value));
    }

    public Condition notEq(char value) {
        return Condition.column(this.nameAlias).notEq((Object) Character.valueOf(value));
    }

    public Condition like(char value) {
        return Condition.column(this.nameAlias).like(String.valueOf(value));
    }

    public Condition glob(char value) {
        return Condition.column(this.nameAlias).glob(String.valueOf(value));
    }

    public Condition greaterThan(char value) {
        return Condition.column(this.nameAlias).greaterThan((Object) Character.valueOf(value));
    }

    public Condition greaterThanOrEq(char value) {
        return Condition.column(this.nameAlias).greaterThanOrEq((Object) Character.valueOf(value));
    }

    public Condition lessThan(char value) {
        return Condition.column(this.nameAlias).lessThan((Object) Character.valueOf(value));
    }

    public Condition lessThanOrEq(char value) {
        return Condition.column(this.nameAlias).lessThanOrEq((Object) Character.valueOf(value));
    }

    public Condition.Between between(char value) {
        return Condition.column(this.nameAlias).between((Object) Character.valueOf(value));
    }

    public Condition.In in(char firstValue, char... values) {
        Condition.In in = Condition.column(this.nameAlias).in((Object) Character.valueOf(firstValue), new Object[0]);
        for (char value : values) {
            in.and(Character.valueOf(value));
        }
        return in;
    }

    public Condition.In notIn(char firstValue, char... values) {
        Condition.In in = Condition.column(this.nameAlias).notIn((Object) Character.valueOf(firstValue), new Object[0]);
        for (char value : values) {
            in.and(Character.valueOf(value));
        }
        return in;
    }

    public Condition concatenate(char value) {
        return Condition.column(this.nameAlias).concatenate((Object) Character.valueOf(value));
    }

    public Condition is(CharProperty property) {
        return Condition.column(this.nameAlias).is((IConditional) property);
    }

    public Condition isNot(CharProperty property) {
        return Condition.column(this.nameAlias).isNot((IConditional) property);
    }

    public Condition eq(CharProperty property) {
        return is(property);
    }

    public Condition notEq(CharProperty property) {
        return isNot(property);
    }

    public Condition greaterThan(CharProperty property) {
        return Condition.column(this.nameAlias).greaterThan((IConditional) property);
    }

    public Condition greaterThanOrEq(CharProperty property) {
        return Condition.column(this.nameAlias).greaterThanOrEq((IConditional) property);
    }

    public Condition lessThan(CharProperty property) {
        return Condition.column(this.nameAlias).lessThan((IConditional) property);
    }

    public Condition lessThanOrEq(CharProperty property) {
        return Condition.column(this.nameAlias).lessThanOrEq((IConditional) property);
    }
}
