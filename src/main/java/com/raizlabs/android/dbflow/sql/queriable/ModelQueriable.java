package com.raizlabs.android.dbflow.sql.queriable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.List;

public interface ModelQueriable<TModel extends Model> extends Queriable {
    AsyncQuery<TModel> async();

    FlowCursorList<TModel> cursorList();

    FlowQueryList<TModel> flowQueryList();

    Class<TModel> getTable();

    <TQueryModel extends BaseQueryModel> List<TQueryModel> queryCustomList(Class<TQueryModel> cls);

    <TQueryModel extends BaseQueryModel> TQueryModel queryCustomSingle(Class<TQueryModel> cls);

    @NonNull
    List<TModel> queryList();

    @NonNull
    List<TModel> queryList(DatabaseWrapper databaseWrapper);

    <ModelContainerClass extends ModelContainer<TModel, ?>> ModelContainerClass queryModelContainer(@NonNull ModelContainerClass modelcontainerclass);

    @NonNull
    CursorResult<TModel> queryResults();

    @Nullable
    TModel querySingle();

    @Nullable
    TModel querySingle(DatabaseWrapper databaseWrapper);
}
