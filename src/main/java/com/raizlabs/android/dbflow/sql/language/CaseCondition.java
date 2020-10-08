package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

public class CaseCondition<TReturn> implements Query {
    private final Case<TReturn> caze;
    private boolean isThenPropertySet;
    private IProperty property;
    private SQLCondition sqlCondition;
    private IProperty thenProperty;
    private TReturn thenValue;
    private TReturn whenValue;

    CaseCondition(Case<TReturn> caze2, @NonNull SQLCondition sqlCondition2) {
        this.caze = caze2;
        this.sqlCondition = sqlCondition2;
    }

    CaseCondition(Case<TReturn> caze2, TReturn whenValue2) {
        this.caze = caze2;
        this.whenValue = whenValue2;
    }

    CaseCondition(Case<TReturn> caze2, @NonNull IProperty property2) {
        this.caze = caze2;
        this.property = property2;
    }

    public Case<TReturn> then(TReturn value) {
        this.thenValue = value;
        return this.caze;
    }

    public Case<TReturn> then(IProperty value) {
        this.thenProperty = value;
        this.isThenPropertySet = true;
        return this.caze;
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(" WHEN ");
        if (this.caze.isEfficientCase()) {
            queryBuilder.append(BaseCondition.convertValueToString(this.property != null ? this.property : this.whenValue, false));
        } else {
            this.sqlCondition.appendConditionToQuery(queryBuilder);
        }
        queryBuilder.append(" THEN ").append(BaseCondition.convertValueToString(this.isThenPropertySet ? this.thenProperty : this.thenValue, false));
        return queryBuilder.getQuery();
    }

    public String toString() {
        return getQuery();
    }
}
