package com.raizlabs.android.dbflow.structure.database.transaction;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FastStoreModelTransaction<TModel extends Model> implements ITransaction {
    final InternalAdapter<TModel> internalAdapter;
    final List<TModel> models;
    final ProcessModelList<TModel> processModelList;

    interface ProcessModelList<TModel extends Model> {
        void processModel(@NonNull List<TModel> list, InternalAdapter<TModel> internalAdapter, DatabaseWrapper databaseWrapper);
    }

    public static <TModel extends Model> Builder<TModel> saveBuilder(@NonNull InternalAdapter<TModel> internalAdapter2) {
        return new Builder<>(new ProcessModelList<TModel>() {
            public void processModel(@NonNull List<TModel> tModels, InternalAdapter<TModel> adapter, DatabaseWrapper wrapper) {
                adapter.saveAll(tModels, wrapper);
            }
        }, internalAdapter2);
    }

    public static <TModel extends Model> Builder<TModel> insertBuilder(@NonNull InternalAdapter<TModel> internalAdapter2) {
        return new Builder<>(new ProcessModelList<TModel>() {
            public void processModel(@NonNull List<TModel> tModels, InternalAdapter<TModel> adapter, DatabaseWrapper wrapper) {
                adapter.insertAll(tModels, wrapper);
            }
        }, internalAdapter2);
    }

    public static <TModel extends Model> Builder<TModel> updateBuilder(@NonNull InternalAdapter<TModel> internalAdapter2) {
        return new Builder<>(new ProcessModelList<TModel>() {
            public void processModel(@NonNull List<TModel> tModels, InternalAdapter<TModel> adapter, DatabaseWrapper wrapper) {
                adapter.updateAll(tModels, wrapper);
            }
        }, internalAdapter2);
    }

    FastStoreModelTransaction(Builder<TModel> builder) {
        this.models = builder.models;
        this.processModelList = builder.processModelList;
        this.internalAdapter = builder.internalAdapter;
    }

    public void execute(DatabaseWrapper databaseWrapper) {
        if (this.models != null) {
            this.processModelList.processModel(this.models, this.internalAdapter, databaseWrapper);
        }
    }

    public static final class Builder<TModel extends Model> {
        /* access modifiers changed from: private */
        @NonNull
        public final InternalAdapter<TModel> internalAdapter;
        List<TModel> models = new ArrayList();
        /* access modifiers changed from: private */
        public final ProcessModelList<TModel> processModelList;

        Builder(@NonNull ProcessModelList<TModel> processModelList2, @NonNull InternalAdapter<TModel> internalAdapter2) {
            this.processModelList = processModelList2;
            this.internalAdapter = internalAdapter2;
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

        public FastStoreModelTransaction<TModel> build() {
            return new FastStoreModelTransaction<>(this);
        }
    }
}
