package com.raizlabs.android.dbflow.structure;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.BaseAsyncObject;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import java.lang.ref.WeakReference;

public class AsyncModel<TModel extends Model> extends BaseAsyncObject<AsyncModel<TModel>> implements Model {
    private final TModel model;
    private transient WeakReference<OnModelChangedListener> onModelChangedListener;

    public interface OnModelChangedListener {
        void onModelChanged(Model model);
    }

    public AsyncModel(@NonNull TModel referenceModel) {
        super(referenceModel.getClass());
        this.model = referenceModel;
    }

    public AsyncModel<TModel> withListener(OnModelChangedListener onModelChangedListener2) {
        this.onModelChangedListener = new WeakReference<>(onModelChangedListener2);
        return this;
    }

    public void save() {
        executeTransaction(new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.save();
            }
        }).add(this.model).build());
    }

    public void delete() {
        executeTransaction(new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.delete();
            }
        }).add(this.model).build());
    }

    public void update() {
        executeTransaction(new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.update();
            }
        }).add(this.model).build());
    }

    public void insert() {
        executeTransaction(new ProcessModelTransaction.Builder(new ProcessModelTransaction.ProcessModel<TModel>() {
            public void processModel(TModel model) {
                model.insert();
            }
        }).add(this.model).build());
    }

    public boolean exists() {
        return this.model.exists();
    }

    /* access modifiers changed from: protected */
    public void onSuccess(Transaction transaction) {
        if (this.onModelChangedListener != null && this.onModelChangedListener.get() != null) {
            ((OnModelChangedListener) this.onModelChangedListener.get()).onModelChanged(this.model);
        }
    }
}
