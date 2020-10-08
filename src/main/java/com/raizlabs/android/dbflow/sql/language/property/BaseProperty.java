package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.IConditional;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public abstract class BaseProperty<P extends IProperty> implements IProperty<P>, IConditional {
    protected NameAlias nameAlias;
    final Class<? extends Model> table;

    protected BaseProperty(Class<? extends Model> table2, NameAlias nameAlias2) {
        this.table = table2;
        this.nameAlias = nameAlias2;
    }

    public P withTable() {
        return withTable(new NameAlias.Builder(FlowManager.getTableName(this.table)).build());
    }

    public Condition is(IConditional conditional) {
        return Condition.column(getNameAlias()).is(conditional);
    }

    public Condition eq(IConditional conditional) {
        return Condition.column(getNameAlias()).eq(conditional);
    }

    public Condition isNot(IConditional conditional) {
        return Condition.column(getNameAlias()).isNot(conditional);
    }

    public Condition notEq(IConditional conditional) {
        return Condition.column(getNameAlias()).notEq(conditional);
    }

    public Condition like(IConditional conditional) {
        return Condition.column(getNameAlias()).like(conditional);
    }

    public Condition glob(IConditional conditional) {
        return Condition.column(getNameAlias()).glob(conditional);
    }

    public Condition like(String value) {
        return Condition.column(getNameAlias()).like(value);
    }

    public Condition glob(String value) {
        return Condition.column(getNameAlias()).glob(value);
    }

    public Condition greaterThan(IConditional conditional) {
        return Condition.column(getNameAlias()).greaterThan(conditional);
    }

    public Condition greaterThanOrEq(IConditional conditional) {
        return Condition.column(getNameAlias()).greaterThanOrEq(conditional);
    }

    public Condition lessThan(IConditional conditional) {
        return Condition.column(getNameAlias()).lessThan(conditional);
    }

    public Condition lessThanOrEq(IConditional conditional) {
        return Condition.column(getNameAlias()).lessThanOrEq(conditional);
    }

    public Condition.Between between(IConditional conditional) {
        return Condition.column(getNameAlias()).between(conditional);
    }

    public Condition.In in(IConditional firstConditional, IConditional... conditionals) {
        return Condition.column(getNameAlias()).in(firstConditional, conditionals);
    }

    public Condition.In notIn(IConditional firstConditional, IConditional... conditionals) {
        return Condition.column(getNameAlias()).notIn(firstConditional, conditionals);
    }

    public Condition is(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).is(baseModelQueriable);
    }

    public Condition isNull() {
        return Condition.column(getNameAlias()).isNull();
    }

    public Condition eq(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).eq(baseModelQueriable);
    }

    public Condition isNot(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).isNot(baseModelQueriable);
    }

    public Condition isNotNull() {
        return Condition.column(getNameAlias()).isNotNull();
    }

    public Condition notEq(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).notEq(baseModelQueriable);
    }

    public Condition like(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).like(baseModelQueriable);
    }

    public Condition glob(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).glob(baseModelQueriable);
    }

    public Condition greaterThan(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).greaterThan(baseModelQueriable);
    }

    public Condition greaterThanOrEq(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).greaterThanOrEq(baseModelQueriable);
    }

    public Condition lessThan(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).lessThan(baseModelQueriable);
    }

    public Condition lessThanOrEq(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).lessThanOrEq(baseModelQueriable);
    }

    public Condition.Between between(BaseModelQueriable baseModelQueriable) {
        return Condition.column(getNameAlias()).between(baseModelQueriable);
    }

    public Condition.In in(BaseModelQueriable firstBaseModelQueriable, BaseModelQueriable... baseModelQueriables) {
        return Condition.column(getNameAlias()).in(firstBaseModelQueriable, baseModelQueriables);
    }

    public Condition.In notIn(BaseModelQueriable firstBaseModelQueriable, BaseModelQueriable... baseModelQueriables) {
        return Condition.column(getNameAlias()).notIn(firstBaseModelQueriable, baseModelQueriables);
    }

    public Condition concatenate(IConditional conditional) {
        return Condition.column(getNameAlias()).concatenate(conditional);
    }

    public Class<? extends Model> getTable() {
        return this.table;
    }

    public NameAlias getNameAlias() {
        return this.nameAlias;
    }

    public String getContainerKey() {
        return getNameAlias().getNameAsKey();
    }

    public String getQuery() {
        return getNameAlias().getQuery();
    }

    public String getCursorKey() {
        return getNameAlias().getQuery();
    }

    public String getDefinition() {
        return getNameAlias().getFullQuery();
    }

    public String toString() {
        return getNameAlias().toString();
    }

    /* access modifiers changed from: protected */
    public NameAlias getDistinctAliasName() {
        return getNameAlias().newBuilder().distinct().build();
    }
}
