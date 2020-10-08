package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;

public class Case<TReturn> implements Query {
    private IProperty caseColumn;
    private List<CaseCondition<TReturn>> caseConditions = new ArrayList();
    private String columnName;
    private boolean efficientCase = false;
    private boolean elseSpecified = false;
    private TReturn elseValue;

    Case() {
    }

    Case(IProperty caseColumn2) {
        this.caseColumn = caseColumn2;
        this.efficientCase = true;
    }

    public CaseCondition<TReturn> when(SQLCondition sqlCondition) {
        if (this.efficientCase) {
            throw new IllegalStateException("When using the efficient CASE method,you must pass in value only, not condition.");
        }
        CaseCondition<TReturn> caseCondition = new CaseCondition<>(this, sqlCondition);
        this.caseConditions.add(caseCondition);
        return caseCondition;
    }

    public CaseCondition<TReturn> when(TReturn whenValue) {
        if (!this.efficientCase) {
            throw new IllegalStateException("When not using the efficient CASE method, you must pass in the SQLConditions as a parameter");
        }
        CaseCondition<TReturn> caseCondition = new CaseCondition<>(this, whenValue);
        this.caseConditions.add(caseCondition);
        return caseCondition;
    }

    public CaseCondition<TReturn> when(IProperty property) {
        if (!this.efficientCase) {
            throw new IllegalStateException("When not using the efficient CASE method, you must pass in the SQLCondition as a parameter");
        }
        CaseCondition<TReturn> caseCondition = new CaseCondition<>(this, property);
        this.caseConditions.add(caseCondition);
        return caseCondition;
    }

    public Case<TReturn> _else(TReturn elseValue2) {
        this.elseValue = elseValue2;
        this.elseSpecified = true;
        return this;
    }

    public Property<Case<TReturn>> end(String columnName2) {
        this.columnName = QueryBuilder.quoteIfNeeded(columnName2);
        return new Property<>((Class<? extends Model>) null, NameAlias.rawBuilder(getQuery()).build());
    }

    /* access modifiers changed from: package-private */
    public boolean isEfficientCase() {
        return this.efficientCase;
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(" CASE");
        if (isEfficientCase()) {
            queryBuilder.append(" " + BaseCondition.convertValueToString(this.caseColumn, false));
        }
        queryBuilder.appendList(this.caseConditions);
        if (this.elseSpecified) {
            queryBuilder.append(" ELSE ").append(BaseCondition.convertValueToString(this.elseValue, false));
        }
        if (this.columnName != null) {
            queryBuilder.append(" END " + this.columnName);
        }
        return queryBuilder.getQuery();
    }
}
