package com.raizlabs.android.dbflow.structure.database.transaction;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ProcessModelTransaction<TModel extends Model> implements ITransaction {
    final List<TModel> models;
    final OnModelProcessListener<TModel> processListener;
    final ProcessModel<TModel> processModel;
    final boolean runProcessListenerOnSameThread;

    public interface OnModelProcessListener<TModel extends Model> {
        void onModelProcessed(long j, long j2, TModel tmodel);
    }

    public interface ProcessModel<TModel extends Model> {
        void processModel(TModel tmodel);
    }

    ProcessModelTransaction(Builder<TModel> builder) {
        this.processListener = builder.processListener;
        this.models = builder.models;
        this.processModel = builder.processModel;
        this.runProcessListenerOnSameThread = builder.runProcessListenerOnSameThread;
    }

    public void execute(DatabaseWrapper databaseWrapper) {
        if (this.models != null) {
            final int size = this.models.size();
            for (int i = 0; i < size; i++) {
                final TModel model = (Model) this.models.get(i);
                this.processModel.processModel(model);
                if (this.processListener != null) {
                    if (this.runProcessListenerOnSameThread) {
                        this.processListener.onModelProcessed((long) i, (long) size, model);
                    } else {
                        final int finalI = i;
                        Transaction.TRANSACTION_HANDLER.post(new Runnable() {
                            public void run() {
                                ProcessModelTransaction.this.processListener.onModelProcessed((long) finalI, (long) size, model);
                            }
                        });
                    }
                }
            }
        }
    }

    public static final class Builder<TModel extends Model> {
        List<TModel> models = new ArrayList();
        OnModelProcessListener<TModel> processListener;
        /* access modifiers changed from: private */
        public final ProcessModel<TModel> processModel;
        /* access modifiers changed from: private */
        public boolean runProcessListenerOnSameThread;

        public Builder(@NonNull ProcessModel<TModel> processModel2) {
            this.processModel = processModel2;
        }

        public Builder(Collection<TModel> models2, @NonNull ProcessModel<TModel> processModel2) {
            this.processModel = processModel2;
            this.models = new ArrayList(models2);
        }

        public Builder<TModel> add(TModel model) {
            this.models.add(model);
            return this;
        }

        @SafeVarargs
        public final Builder<TModel> addAll(TModel... models2) {
            this.models.addAll(Arrays.asList(models2));
            return this;
        }

        public Builder<TModel> addAll(Collection<? extends TModel> models2) {
            if (models2 != null) {
                this.models.addAll(models2);
            }
            return this;
        }

        public Builder<TModel> processListener(OnModelProcessListener<TModel> processListener2) {
            this.processListener = processListener2;
            return this;
        }

        public Builder<TModel> runProcessListenerOnSameThread(boolean runProcessListenerOnSameThread2) {
            this.runProcessListenerOnSameThread = runProcessListenerOnSameThread2;
            return this;
        }

        public ProcessModelTransaction<TModel> build() {
            return new ProcessModelTransaction<>(this);
        }
    }
}
