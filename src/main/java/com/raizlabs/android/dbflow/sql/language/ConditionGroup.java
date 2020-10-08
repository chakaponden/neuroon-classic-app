package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConditionGroup extends BaseCondition implements Query, Iterable<SQLCondition> {
    private boolean allCommaSeparated;
    private final List<SQLCondition> conditionsList;
    private boolean isChanged;
    private QueryBuilder query;
    private boolean useParenthesis;

    public static ConditionGroup clause() {
        return new ConditionGroup();
    }

    public static ConditionGroup nonGroupingClause() {
        return new ConditionGroup().setUseParenthesis(false);
    }

    protected ConditionGroup(NameAlias columnName) {
        super(columnName);
        this.conditionsList = new ArrayList();
        this.useParenthesis = true;
        this.separator = Condition.Operation.AND;
    }

    protected ConditionGroup() {
        this((NameAlias) null);
    }

    public ConditionGroup setAllCommaSeparated(boolean allCommaSeparated2) {
        this.allCommaSeparated = allCommaSeparated2;
        this.isChanged = true;
        return this;
    }

    public ConditionGroup setUseParenthesis(boolean useParenthesis2) {
        this.useParenthesis = useParenthesis2;
        this.isChanged = true;
        return this;
    }

    public ConditionGroup or(SQLCondition sqlCondition) {
        return operator(Condition.Operation.OR, sqlCondition);
    }

    public ConditionGroup and(SQLCondition sqlCondition) {
        return operator(Condition.Operation.AND, sqlCondition);
    }

    public ConditionGroup andAll(SQLCondition... sqlConditions) {
        for (SQLCondition sqlCondition : sqlConditions) {
            and(sqlCondition);
        }
        return this;
    }

    public ConditionGroup andAll(List<SQLCondition> sqlConditions) {
        for (SQLCondition sqlCondition : sqlConditions) {
            and(sqlCondition);
        }
        return this;
    }

    public ConditionGroup orAll(SQLCondition... sqlConditions) {
        for (SQLCondition sqlCondition : sqlConditions) {
            or(sqlCondition);
        }
        return this;
    }

    public ConditionGroup orAll(List<SQLCondition> sqlConditions) {
        for (SQLCondition sqlCondition : sqlConditions) {
            or(sqlCondition);
        }
        return this;
    }

    private ConditionGroup operator(String operator, SQLCondition sqlCondition) {
        setPreviousSeparator(operator);
        this.conditionsList.add(sqlCondition);
        this.isChanged = true;
        return this;
    }

    public void appendConditionToQuery(QueryBuilder queryBuilder) {
        int conditionListSize = this.conditionsList.size();
        if (this.useParenthesis && conditionListSize > 0) {
            queryBuilder.append("(");
        }
        for (int i = 0; i < conditionListSize; i++) {
            SQLCondition condition = this.conditionsList.get(i);
            condition.appendConditionToQuery(queryBuilder);
            if (condition.hasSeparator() && i < conditionListSize - 1) {
                queryBuilder.appendSpaceSeparated(condition.separator());
            }
        }
        if (this.useParenthesis && conditionListSize > 0) {
            queryBuilder.append(")");
        }
    }

    private void setPreviousSeparator(String separator) {
        if (this.conditionsList.size() > 0) {
            this.conditionsList.get(this.conditionsList.size() - 1).separator(separator);
        }
    }

    public String getQuery() {
        if (this.isChanged) {
            this.query = new QueryBuilder();
            int count = 0;
            int size = this.conditionsList.size();
            for (int i = 0; i < size; i++) {
                SQLCondition condition = this.conditionsList.get(i);
                condition.appendConditionToQuery(this.query);
                if (count < size - 1) {
                    if (!this.allCommaSeparated) {
                        this.query.appendSpace().append(condition.hasSeparator() ? condition.separator() : this.separator);
                    } else {
                        this.query.append(",");
                    }
                    this.query.appendSpace();
                }
                count++;
            }
        }
        return this.query == null ? "" : this.query.toString();
    }

    public String toString() {
        return getQuery();
    }

    public int size() {
        return this.conditionsList.size();
    }

    public List<SQLCondition> getConditions() {
        return this.conditionsList;
    }

    public Iterator<SQLCondition> iterator() {
        return this.conditionsList.iterator();
    }
}
