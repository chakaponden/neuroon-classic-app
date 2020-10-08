package com.raizlabs.android.dbflow.sql.language;

import android.content.ContentValues;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.ValueQueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Insert<TModel extends Model> extends BaseQueriable<TModel> {
    private IProperty[] columns;
    private ConflictAction conflictAction = ConflictAction.NONE;
    private From<? extends Model> selectFrom;
    private Object[] values;

    public Insert(Class<TModel> table) {
        super(table);
    }

    public Insert<TModel> columns(String... columns2) {
        this.columns = new IProperty[columns2.length];
        ModelAdapter<TModel> modelClassModelAdapter = FlowManager.getModelAdapter(getTable());
        for (int i = 0; i < columns2.length; i++) {
            this.columns[i] = modelClassModelAdapter.getProperty(columns2[i]);
        }
        return this;
    }

    public Insert<TModel> columns(IProperty... properties) {
        this.columns = new IProperty[properties.length];
        for (int i = 0; i < properties.length; i++) {
            this.columns[i] = properties[i];
        }
        return this;
    }

    public Insert<TModel> columns(List<IProperty> properties) {
        if (properties != null) {
            this.columns = new IProperty[properties.size()];
            for (int i = 0; i < properties.size(); i++) {
                this.columns[i] = properties.get(i);
            }
        }
        return this;
    }

    public Insert<TModel> asColumns() {
        columns(FlowManager.getModelAdapter(getTable()).getAllColumnProperties());
        return this;
    }

    public Insert<TModel> values(Object... values2) {
        this.values = values2;
        return this;
    }

    public Insert<TModel> columnValues(SQLCondition... conditions) {
        String[] columns2 = new String[conditions.length];
        Object[] values2 = new Object[conditions.length];
        for (int i = 0; i < conditions.length; i++) {
            SQLCondition condition = conditions[i];
            columns2[i] = condition.columnName();
            values2[i] = condition.value();
        }
        return columns(columns2).values(values2);
    }

    public Insert<TModel> columnValues(ConditionGroup conditionGroup) {
        int size = conditionGroup.size();
        String[] columns2 = new String[size];
        Object[] values2 = new Object[size];
        for (int i = 0; i < size; i++) {
            SQLCondition condition = conditionGroup.getConditions().get(i);
            columns2[i] = condition.columnName();
            values2[i] = condition.value();
        }
        return columns(columns2).values(values2);
    }

    public Insert<TModel> columnValues(ContentValues contentValues) {
        Set<Map.Entry<String, Object>> entries = contentValues.valueSet();
        int count = 0;
        String[] columns2 = new String[contentValues.size()];
        Object[] values2 = new Object[contentValues.size()];
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            columns2[count] = key;
            values2[count] = contentValues.get(key);
            count++;
        }
        return columns(columns2).values(values2);
    }

    public Insert<TModel> select(From<? extends Model> selectFrom2) {
        this.selectFrom = selectFrom2;
        return this;
    }

    public Insert<TModel> or(ConflictAction action) {
        this.conflictAction = action;
        return this;
    }

    public Insert<TModel> orReplace() {
        return or(ConflictAction.REPLACE);
    }

    public Insert<TModel> orRollback() {
        return or(ConflictAction.ROLLBACK);
    }

    public Insert<TModel> orAbort() {
        return or(ConflictAction.ABORT);
    }

    public Insert<TModel> orFail() {
        return or(ConflictAction.FAIL);
    }

    public Insert<TModel> orIgnore() {
        return or(ConflictAction.IGNORE);
    }

    public String getQuery() {
        ValueQueryBuilder queryBuilder = new ValueQueryBuilder("INSERT ");
        if (this.conflictAction != null && !this.conflictAction.equals(ConflictAction.NONE)) {
            ((ValueQueryBuilder) queryBuilder.append(Condition.Operation.OR)).appendSpaceSeparated(this.conflictAction);
        }
        ((ValueQueryBuilder) ((ValueQueryBuilder) queryBuilder.append("INTO")).appendSpace()).appendTableName(getTable());
        if (this.columns != null) {
            ((ValueQueryBuilder) ((ValueQueryBuilder) queryBuilder.append("(")).appendArray((Object[]) this.columns)).append(")");
        }
        if (this.selectFrom != null) {
            ((ValueQueryBuilder) queryBuilder.appendSpace()).append(this.selectFrom.getQuery());
        } else if (this.columns != null && this.values != null && this.columns.length != this.values.length) {
            throw new IllegalStateException("The Insert of " + FlowManager.getTableName(getTable()) + " when specifying" + "columns needs to have the same amount of values and columns");
        } else if (this.values == null) {
            throw new IllegalStateException("The insert of " + FlowManager.getTableName(getTable()) + " should have" + "at least one value specified for the insert");
        } else {
            ((ValueQueryBuilder) queryBuilder.append(" VALUES(")).appendModelArray(this.values).append(")");
        }
        return queryBuilder.getQuery();
    }
}
