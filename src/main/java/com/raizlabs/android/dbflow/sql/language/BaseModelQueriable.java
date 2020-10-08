package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.queriable.AsyncQuery;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.InstanceAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModelQueriable<TModel extends Model> extends BaseQueriable<TModel> implements ModelQueriable<TModel>, Query {
    private final InstanceAdapter<?, TModel> retrievalAdapter;

    protected BaseModelQueriable(Class<TModel> table) {
        super(table);
        this.retrievalAdapter = FlowManager.getInstanceAdapter(table);
    }

    public CursorResult<TModel> queryResults() {
        return new CursorResult<>(this.retrievalAdapter.getModelClass(), query());
    }

    public List<TModel> queryList() {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (List) this.retrievalAdapter.getListModelLoader().load(query);
    }

    public TModel querySingle() {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (Model) this.retrievalAdapter.getSingleModelLoader().load(query);
    }

    public TModel querySingle(DatabaseWrapper wrapper) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (Model) this.retrievalAdapter.getSingleModelLoader().load(wrapper, query);
    }

    @NonNull
    public List<TModel> queryList(DatabaseWrapper wrapper) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        List<TModel> list = (List) this.retrievalAdapter.getListModelLoader().load(wrapper, query);
        return list == null ? new ArrayList<>() : list;
    }

    public <ModelContainerClass extends ModelContainer<TModel, ?>> ModelContainerClass queryModelContainer(@NonNull ModelContainerClass instance) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (ModelContainer) FlowManager.getContainerAdapter(getTable()).getModelContainerLoader().load(query, instance);
    }

    public FlowCursorList<TModel> cursorList() {
        return new FlowCursorList<>(false, this);
    }

    public FlowQueryList<TModel> flowQueryList() {
        return new FlowQueryList<>(this);
    }

    public AsyncQuery<TModel> async() {
        return new AsyncQuery<>(this);
    }

    public <QueryClass extends BaseQueryModel> List<QueryClass> queryCustomList(Class<QueryClass> queryModelClass) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (List) FlowManager.getQueryModelAdapter(queryModelClass).getListModelLoader().load(query);
    }

    public <QueryClass extends BaseQueryModel> QueryClass queryCustomSingle(Class<QueryClass> queryModelClass) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        return (BaseQueryModel) FlowManager.getQueryModelAdapter(queryModelClass).getSingleModelLoader().load(query);
    }
}
