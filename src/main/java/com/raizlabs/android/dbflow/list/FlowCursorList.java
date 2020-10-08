package com.raizlabs.android.dbflow.list;

import android.database.Cursor;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.InstanceAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.cache.ModelCache;
import com.raizlabs.android.dbflow.structure.cache.ModelLruCache;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlowCursorList<TModel extends Model> {
    public static final int DEFAULT_CACHE_SIZE = 50;
    public static final int MIN_CACHE_SIZE = 20;
    private boolean cacheModels;
    private int cacheSize;
    @Nullable
    private Cursor cursor;
    private final Set<OnCursorRefreshListener<TModel>> cursorRefreshListenerSet;
    private InstanceAdapter<TModel, TModel> modelAdapter;
    private ModelCache<TModel, ?> modelCache;
    private ModelQueriable<TModel> modelQueriable;
    private Class<TModel> table;

    public interface OnCursorRefreshListener<TModel extends Model> {
        void onCursorRefreshed(FlowCursorList<TModel> flowCursorList);
    }

    public FlowCursorList(ModelQueriable<TModel> modelQueriable2) {
        this(true, modelQueriable2);
    }

    public FlowCursorList(int cacheSize2, ModelQueriable<TModel> modelQueriable2) {
        this(false, modelQueriable2);
        setCacheModels(true, cacheSize2);
    }

    public FlowCursorList(boolean cacheModels2, ModelQueriable<TModel> modelQueriable2) {
        this.cursorRefreshListenerSet = new HashSet();
        this.modelQueriable = modelQueriable2;
        this.cursor = this.modelQueriable.query();
        this.table = modelQueriable2.getTable();
        this.modelAdapter = FlowManager.getInstanceAdapter(this.table);
        this.cacheModels = cacheModels2;
        setCacheModels(cacheModels2);
    }

    public void addOnCursorRefreshListener(OnCursorRefreshListener<TModel> onCursorRefreshListener) {
        synchronized (this.cursorRefreshListenerSet) {
            this.cursorRefreshListenerSet.add(onCursorRefreshListener);
        }
    }

    public void removeOnCursorRefreshListener(OnCursorRefreshListener<TModel> onCursorRefreshListener) {
        synchronized (this.cursorRefreshListenerSet) {
            this.cursorRefreshListenerSet.remove(onCursorRefreshListener);
        }
    }

    public void setCacheModels(boolean cacheModels2) {
        int i = 0;
        if (cacheModels2) {
            throwIfCursorClosed();
            if (this.cursor != null) {
                i = this.cursor.getCount();
            }
            setCacheModels(true, i);
            return;
        }
        setCacheModels(false, this.cursor == null ? 0 : this.cursor.getCount());
    }

    public void setCacheModels(boolean cacheModels2, int cacheSize2) {
        this.cacheModels = cacheModels2;
        if (!cacheModels2) {
            clearCache();
            return;
        }
        throwIfCursorClosed();
        if (cacheSize2 <= 20) {
            if (cacheSize2 == 0) {
                cacheSize2 = 50;
            } else {
                cacheSize2 = 20;
            }
        }
        this.cacheSize = cacheSize2;
        this.modelCache = getBackingCache();
    }

    /* access modifiers changed from: protected */
    public ModelCache<TModel, ?> getBackingCache() {
        return ModelLruCache.newInstance(this.cacheSize);
    }

    public void clearCache() {
        if (this.cacheModels) {
            this.modelCache.clear();
        }
    }

    public synchronized void refresh() {
        warnEmptyCursor();
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = this.modelQueriable.query();
        if (this.cacheModels) {
            this.modelCache.clear();
            setCacheModels(true, this.cursor == null ? 0 : this.cursor.getCount());
        }
        synchronized (this.cursorRefreshListenerSet) {
            for (OnCursorRefreshListener<TModel> listener : this.cursorRefreshListenerSet) {
                listener.onCursorRefreshed(this);
            }
        }
    }

    public TModel getItem(long position) {
        throwIfCursorClosed();
        warnEmptyCursor();
        if (this.cacheModels) {
            TModel model = this.modelCache.get(Long.valueOf(position));
            if (model != null || this.cursor == null || !this.cursor.moveToPosition((int) position)) {
                return model;
            }
            TModel model2 = this.modelAdapter.getSingleModelLoader().convertToData(this.cursor, null, false);
            this.modelCache.addModel(Long.valueOf(position), model2);
            return model2;
        } else if (this.cursor == null || !this.cursor.moveToPosition((int) position)) {
            return null;
        } else {
            return this.modelAdapter.getSingleModelLoader().convertToData(this.cursor, null, false);
        }
    }

    public List<TModel> getAll() {
        throwIfCursorClosed();
        warnEmptyCursor();
        return this.cursor == null ? new ArrayList() : FlowManager.getModelAdapter(this.table).getListModelLoader().convertToData(this.cursor, (List) null);
    }

    public boolean isEmpty() {
        throwIfCursorClosed();
        warnEmptyCursor();
        return getCount() == 0;
    }

    public int getCount() {
        throwIfCursorClosed();
        warnEmptyCursor();
        if (this.cursor != null) {
            return this.cursor.getCount();
        }
        return 0;
    }

    public void close() {
        warnEmptyCursor();
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = null;
    }

    @Nullable
    public Cursor getCursor() {
        throwIfCursorClosed();
        warnEmptyCursor();
        return this.cursor;
    }

    public Class<TModel> getTable() {
        return this.table;
    }

    private void throwIfCursorClosed() {
        if (this.cursor != null && this.cursor.isClosed()) {
            throw new IllegalStateException("Cursor has been closed for FlowCursorList");
        }
    }

    private void warnEmptyCursor() {
        if (this.cursor == null) {
            FlowLog.log(FlowLog.Level.W, "Cursor was null for FlowCursorList");
        }
    }
}
