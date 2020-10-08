package com.raizlabs.android.dbflow.list;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.cache.ModelCache;
import com.raizlabs.android.dbflow.structure.cache.ModelLruCache;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FlowQueryList<TModel extends Model> extends FlowContentObserver implements List<TModel> {
    private static final Handler REFRESH_HANDLER = new Handler(Looper.myLooper());
    /* access modifiers changed from: private */
    public boolean changeInTransaction;
    private final ProcessModelTransaction.ProcessModel<TModel> deleteModel;
    /* access modifiers changed from: private */
    public Transaction.Error errorCallback;
    private FlowCursorList<TModel> internalCursorList;
    private final Transaction.Error internalErrorCallback;
    private final Transaction.Success internalSuccessCallback;
    /* access modifiers changed from: private */
    public boolean pendingRefresh;
    private final Runnable refreshRunnable;
    private final ProcessModelTransaction.ProcessModel<TModel> saveModel;
    /* access modifiers changed from: private */
    public Transaction.Success successCallback;
    private boolean transact;
    private final ProcessModelTransaction.ProcessModel<TModel> updateModel;

    public FlowQueryList(ModelQueriable<TModel> modelQueriable) {
        this(true, modelQueriable);
    }

    public FlowQueryList(boolean cacheModels, ModelQueriable<TModel> modelQueriable) {
        super((Handler) null);
        this.transact = false;
        this.changeInTransaction = false;
        this.pendingRefresh = false;
        this.saveModel = new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.save();
            }
        };
        this.updateModel = new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.update();
            }
        };
        this.deleteModel = new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.delete();
            }
        };
        this.internalErrorCallback = new Transaction.Error() {
            public void onError(Transaction transaction, Throwable error) {
                if (FlowQueryList.this.errorCallback != null) {
                    FlowQueryList.this.errorCallback.onError(transaction, error);
                }
            }
        };
        this.internalSuccessCallback = new Transaction.Success() {
            public void onSuccess(Transaction transaction) {
                if (!FlowQueryList.this.isInTransaction) {
                    FlowQueryList.this.refreshAsync();
                } else {
                    boolean unused = FlowQueryList.this.changeInTransaction = true;
                }
                if (FlowQueryList.this.successCallback != null) {
                    FlowQueryList.this.successCallback.onSuccess(transaction);
                }
            }
        };
        this.refreshRunnable = new Runnable() {
            public void run() {
                synchronized (this) {
                    boolean unused = FlowQueryList.this.pendingRefresh = false;
                }
                FlowQueryList.this.refresh();
            }
        };
        this.internalCursorList = new FlowCursorList<TModel>(cacheModels, modelQueriable) {
            /* access modifiers changed from: protected */
            public ModelCache<TModel, ?> getBackingCache() {
                return FlowQueryList.this.getBackingCache(FlowQueryList.this.getCacheSize());
            }
        };
    }

    public void setCacheModels(boolean cacheModels, int cacheSize) {
        this.internalCursorList.setCacheModels(cacheModels, cacheSize);
    }

    public void setCacheModels(boolean cacheModels) {
        this.internalCursorList.setCacheModels(cacheModels);
    }

    public ModelCache<TModel, ?> getBackingCache(int count) {
        return ModelLruCache.newInstance(count);
    }

    public int getCacheSize() {
        return 50;
    }

    public void registerForContentChanges(Context context) {
        super.registerForContentChanges(context, (Class<? extends Model>) this.internalCursorList.getTable());
    }

    public void addOnCursorRefreshListener(FlowCursorList.OnCursorRefreshListener<TModel> onCursorRefreshListener) {
        this.internalCursorList.addOnCursorRefreshListener(onCursorRefreshListener);
    }

    public void removeOnCursorRefreshListener(FlowCursorList.OnCursorRefreshListener<TModel> onCursorRefreshListener) {
        this.internalCursorList.removeOnCursorRefreshListener(onCursorRefreshListener);
    }

    public void registerForContentChanges(Context context, Class<? extends Model> cls) {
        throw new RuntimeException("This method is not to be used in the FlowQueryList. call registerForContentChanges(Context) instead");
    }

    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (!this.isInTransaction) {
            refreshAsync();
        } else {
            this.changeInTransaction = true;
        }
    }

    @TargetApi(16)
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (!this.isInTransaction) {
            refreshAsync();
        } else {
            this.changeInTransaction = true;
        }
    }

    public void setSuccessCallback(Transaction.Success successCallback2) {
        this.successCallback = successCallback2;
    }

    public void setErrorCallback(Transaction.Error errorCallback2) {
        this.errorCallback = errorCallback2;
    }

    public void setTransact(boolean transact2) {
        this.transact = transact2;
    }

    public List<TModel> getCopy() {
        return this.internalCursorList.getAll();
    }

    public FlowCursorList<TModel> getCursorList() {
        return this.internalCursorList;
    }

    public void refresh() {
        this.internalCursorList.refresh();
    }

    public void refreshAsync() {
        synchronized (this) {
            if (!this.pendingRefresh) {
                this.pendingRefresh = true;
                REFRESH_HANDLER.post(this.refreshRunnable);
            }
        }
    }

    @Deprecated
    public void enableSelfRefreshes(Context context) {
        registerForContentChanges(context);
    }

    public void endTransactionAndNotify() {
        if (this.changeInTransaction) {
            this.changeInTransaction = false;
            refresh();
        }
        super.endTransactionAndNotify();
    }

    public void add(int location, TModel model) {
        add(model);
    }

    public boolean add(TModel model) {
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.saveModel).add(model).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
            return true;
        }
        transaction.executeSync();
        return true;
    }

    public boolean addAll(int location, Collection<? extends TModel> collection) {
        return addAll(collection);
    }

    public boolean addAll(Collection<? extends TModel> collection) {
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.saveModel).addAll(collection).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
            return true;
        }
        transaction.executeSync();
        return true;
    }

    public void clear() {
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new QueryTransaction.Builder(SQLite.delete().from(this.internalCursorList.getTable())).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
        } else {
            transaction.executeSync();
        }
    }

    public boolean contains(Object object) {
        if (this.internalCursorList.getTable().isAssignableFrom(object.getClass())) {
            return ((Model) object).exists();
        }
        return false;
    }

    public boolean containsAll(@NonNull Collection<?> collection) {
        boolean contains = !collection.isEmpty();
        if (!contains) {
            return contains;
        }
        for (Object o : collection) {
            if (!contains(o)) {
                return false;
            }
        }
        return contains;
    }

    public TModel get(int row) {
        return this.internalCursorList.getItem((long) row);
    }

    public int indexOf(Object object) {
        throw new UnsupportedOperationException("We cannot determine which index in the table this item exists at efficiently");
    }

    public boolean isEmpty() {
        return this.internalCursorList.isEmpty();
    }

    @NonNull
    public Iterator<TModel> iterator() {
        return this.internalCursorList.getAll().iterator();
    }

    public int lastIndexOf(Object object) {
        throw new UnsupportedOperationException("We cannot determine which index in the table this item exists at efficiently");
    }

    @NonNull
    public ListIterator<TModel> listIterator() {
        return this.internalCursorList.getAll().listIterator();
    }

    @NonNull
    public ListIterator<TModel> listIterator(int location) {
        return this.internalCursorList.getAll().listIterator(location);
    }

    public TModel remove(int location) {
        TModel model = this.internalCursorList.getItem((long) location);
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.deleteModel).add(model).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
        } else {
            transaction.executeSync();
        }
        return model;
    }

    public boolean remove(Object object) {
        if (!this.internalCursorList.getTable().isAssignableFrom(object.getClass())) {
            return false;
        }
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.deleteModel).add((Model) object).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
        } else {
            transaction.executeSync();
        }
        return true;
    }

    public boolean removeAll(@NonNull Collection<?> collection) {
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.deleteModel).addAll(collection).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
            return true;
        }
        transaction.executeSync();
        return true;
    }

    public boolean retainAll(@NonNull Collection<?> collection) {
        List<TModel> tableList = this.internalCursorList.getAll();
        tableList.removeAll(collection);
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(tableList, this.deleteModel).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
            return true;
        }
        transaction.executeSync();
        return true;
    }

    public TModel set(int location, TModel object) {
        return set(object);
    }

    public TModel set(TModel object) {
        Transaction transaction = FlowManager.getDatabaseForTable(this.internalCursorList.getTable()).beginTransactionAsync(new ProcessModelTransaction.Builder(this.updateModel).add(object).build()).error(this.internalErrorCallback).success(this.internalSuccessCallback).build();
        if (this.transact) {
            transaction.execute();
        } else {
            transaction.executeSync();
        }
        return object;
    }

    public int size() {
        return this.internalCursorList.getCount();
    }

    @NonNull
    public List<TModel> subList(int start, int end) {
        return this.internalCursorList.getAll().subList(start, end);
    }

    @NonNull
    public Object[] toArray() {
        return this.internalCursorList.getAll().toArray();
    }

    @NonNull
    public <T> T[] toArray(T[] array) {
        return this.internalCursorList.getAll().toArray(array);
    }
}
