package com.raizlabs.android.dbflow.structure;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.Model;

public abstract class BaseModelView<TModel extends Model> extends NoModificationModel {
    private transient ModelViewAdapter<? extends Model, BaseModelView<TModel>> adapter;

    public /* bridge */ /* synthetic */ void delete() {
        super.delete();
    }

    public /* bridge */ /* synthetic */ void insert() {
        super.insert();
    }

    public /* bridge */ /* synthetic */ void save() {
        super.save();
    }

    public /* bridge */ /* synthetic */ void update() {
        super.update();
    }

    public boolean exists() {
        return getModelViewAdapter().exists(this);
    }

    public ModelViewAdapter<? extends Model, BaseModelView<TModel>> getModelViewAdapter() {
        if (this.adapter == null) {
            this.adapter = FlowManager.getModelViewAdapter(getClass());
        }
        return this.adapter;
    }
}
