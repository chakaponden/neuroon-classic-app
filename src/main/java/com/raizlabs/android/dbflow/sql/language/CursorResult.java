package com.raizlabs.android.dbflow.sql.language;

import android.database.Cursor;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.InstanceAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import java.io.Closeable;
import java.util.List;

public class CursorResult<TModel extends Model> implements Closeable {
    @Nullable
    private Cursor cursor;
    private final InstanceAdapter<?, TModel> retrievalAdapter;

    CursorResult(Class<TModel> modelClass, @Nullable Cursor cursor2) {
        this.cursor = cursor2;
        this.retrievalAdapter = FlowManager.getInstanceAdapter(modelClass);
    }

    public void swapCursor(@Nullable Cursor cursor2) {
        if (this.cursor != null && !this.cursor.isClosed()) {
            this.cursor.close();
        }
        this.cursor = cursor2;
    }

    @Nullable
    public List<TModel> toList() {
        if (this.cursor != null) {
            return this.retrievalAdapter.getListModelLoader().convertToData(this.cursor, (List) null);
        }
        return null;
    }

    @Nullable
    public List<TModel> toListClose() {
        if (this.cursor != null) {
            return (List) this.retrievalAdapter.getListModelLoader().load(this.cursor);
        }
        return null;
    }

    @Nullable
    public <TCustom extends BaseQueryModel> List<TCustom> toCustomList(Class<TCustom> customClass) {
        if (this.cursor != null) {
            return FlowManager.getQueryModelAdapter(customClass).getListModelLoader().convertToData(this.cursor, (List) null);
        }
        return null;
    }

    @Nullable
    public <TCustom extends BaseQueryModel> List<TCustom> toCustomListClose(Class<TCustom> customClass) {
        if (this.cursor != null) {
            return (List) FlowManager.getQueryModelAdapter(customClass).getListModelLoader().load(this.cursor);
        }
        return null;
    }

    @Nullable
    public TModel toModel() {
        if (this.cursor != null) {
            return this.retrievalAdapter.getSingleModelLoader().convertToData(this.cursor, null);
        }
        return null;
    }

    @Nullable
    public TModel toModelClose() {
        if (this.cursor != null) {
            return (Model) this.retrievalAdapter.getSingleModelLoader().load(this.cursor);
        }
        return null;
    }

    public long count() {
        if (this.cursor == null) {
            return 0;
        }
        return (long) this.cursor.getCount();
    }

    @Nullable
    public Cursor getCursor() {
        return this.cursor;
    }

    public void close() {
        if (this.cursor != null) {
            this.cursor.close();
        }
    }
}
