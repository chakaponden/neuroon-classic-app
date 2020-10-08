package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Join<TModel extends Model, TFromModel extends Model> implements Query {
    private NameAlias alias;
    private From<TFromModel> from;
    private boolean isNatural = false;
    private ConditionGroup on;
    private Class<TModel> table;
    private JoinType type;
    private List<IProperty> using = new ArrayList();

    public enum JoinType {
        LEFT_OUTER,
        INNER,
        CROSS
    }

    Join(From<TFromModel> from2, Class<TModel> table2, @NonNull JoinType joinType) {
        this.from = from2;
        this.table = table2;
        this.type = joinType;
        this.alias = new NameAlias.Builder(FlowManager.getTableName(table2)).build();
    }

    Join(From<TFromModel> from2, @NonNull JoinType joinType, ModelQueriable<TModel> modelQueriable) {
        this.from = from2;
        this.type = joinType;
        this.table = modelQueriable.getTable();
        this.alias = PropertyFactory.from(modelQueriable).getNameAlias();
    }

    public Join<TModel, TFromModel> as(String alias2) {
        this.alias = this.alias.newBuilder().as(alias2).build();
        return this;
    }

    public From<TFromModel> natural() {
        this.isNatural = true;
        return this.from;
    }

    public From<TFromModel> on(SQLCondition... onConditions) {
        this.on = new ConditionGroup();
        this.on.andAll(onConditions);
        return this.from;
    }

    public From<TFromModel> using(IProperty... columns) {
        Collections.addAll(this.using, columns);
        return this.from;
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder();
        if (this.isNatural) {
            queryBuilder.append("NATURAL ");
        }
        queryBuilder.append(this.type.name().replace(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR, " ")).appendSpace();
        queryBuilder.append("JOIN").appendSpace().append(this.alias.getFullQuery()).appendSpace();
        if (this.on != null) {
            queryBuilder.append("ON").appendSpace().append(this.on.getQuery()).appendSpace();
        } else if (!this.using.isEmpty()) {
            queryBuilder.append("USING (").appendArray(this.using).append(")").appendSpace();
        }
        return queryBuilder.getQuery();
    }
}
