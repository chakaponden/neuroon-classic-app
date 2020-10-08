package com.raizlabs.android.dbflow.config;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.queriable.ListModelLoader;
import com.raizlabs.android.dbflow.sql.queriable.ModelContainerLoader;
import com.raizlabs.android.dbflow.sql.queriable.SingleModelLoader;
import com.raizlabs.android.dbflow.sql.saveable.ModelSaver;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.raizlabs.android.dbflow.structure.container.ModelContainerAdapter;

public final class TableConfig<TModel extends Model> {
    private final ListModelLoader<TModel> listModelLoader;
    private final ModelContainerLoader<TModel> modelContainerLoader;
    private final ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelContainerModelSaver;
    private final ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelSaver;
    private final SingleModelLoader<TModel> singleModelLoader;
    private final Class<TModel> tableClass;

    TableConfig(Builder<TModel> builder) {
        this.tableClass = builder.tableClass;
        this.modelSaver = builder.modelAdapterModelSaver;
        this.singleModelLoader = builder.singleModelLoader;
        this.listModelLoader = builder.listModelLoader;
        this.modelContainerLoader = builder.modelContainerLoader;
        this.modelContainerModelSaver = builder.modelContainerModelSaver;
    }

    public Class<? extends Model> tableClass() {
        return this.tableClass;
    }

    public ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelSaver() {
        return this.modelSaver;
    }

    public ListModelLoader<TModel> listModelLoader() {
        return this.listModelLoader;
    }

    public SingleModelLoader<TModel> singleModelLoader() {
        return this.singleModelLoader;
    }

    public ModelContainerLoader<TModel> modelContainerLoader() {
        return this.modelContainerLoader;
    }

    public ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelContainerModelSaver() {
        return this.modelContainerModelSaver;
    }

    public static final class Builder<TModel extends Model> {
        ListModelLoader<TModel> listModelLoader;
        ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelAdapterModelSaver;
        ModelContainerLoader<TModel> modelContainerLoader;
        ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelContainerModelSaver;
        SingleModelLoader<TModel> singleModelLoader;
        final Class<TModel> tableClass;

        public Builder(Class<TModel> tableClass2) {
            this.tableClass = tableClass2;
        }

        public Builder<TModel> modelAdapterModelSaver(@NonNull ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelSaver) {
            this.modelAdapterModelSaver = modelSaver;
            return this;
        }

        public Builder<TModel> modelContainerModelSaver(@NonNull ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelSaver) {
            this.modelContainerModelSaver = modelSaver;
            return this;
        }

        public Builder<TModel> singleModelLoader(@NonNull SingleModelLoader<TModel> singleModelLoader2) {
            this.singleModelLoader = singleModelLoader2;
            return this;
        }

        public Builder<TModel> listModelLoader(@NonNull ListModelLoader<TModel> listModelLoader2) {
            this.listModelLoader = listModelLoader2;
            return this;
        }

        public Builder<TModel> modelContainerLoader(@NonNull ModelContainerLoader<TModel> modelContainerModelLoader) {
            this.modelContainerLoader = modelContainerModelLoader;
            return this;
        }

        public TableConfig build() {
            return new TableConfig(this);
        }
    }
}
