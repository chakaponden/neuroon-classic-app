package com.raizlabs.android.dbflow.structure.container;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.Model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayModel<TModel extends Model> implements Model {
    private JSONArray jsonArray;
    private Class<TModel> table;

    public JSONArrayModel(JSONArray jsonArray2, Class<TModel> table2) {
        this.jsonArray = jsonArray2;
        this.table = table2;
    }

    public JSONArrayModel(Class<TModel> table2) {
        this(new JSONArray(), table2);
    }

    public void addJSONObject(JSONObject jsonObject) {
        this.jsonArray.put(jsonObject);
    }

    public JSONObject getJSONObject(int index) {
        try {
            return this.jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public TModel getModelObject(int index) {
        return getJsonModel(index).toModel();
    }

    public JSONModel<TModel> getJsonModel(int index) {
        return new JSONModel<>(getJSONObject(index), this.table);
    }

    public int length() {
        if (this.jsonArray != null) {
            return this.jsonArray.length();
        }
        return 0;
    }

    public void save() {
        if (this.jsonArray != null && this.jsonArray.length() > 0) {
            int length = this.jsonArray.length();
            JSONModel<TModel> jsonModel = new JSONModel<>(this.table);
            for (int i = 0; i < length; i++) {
                try {
                    jsonModel.data = this.jsonArray.getJSONObject(i);
                    jsonModel.save();
                } catch (JSONException e) {
                    FlowLog.logError(e);
                }
            }
        }
    }

    public void delete() {
        if (this.jsonArray != null && this.jsonArray.length() > 0) {
            int length = this.jsonArray.length();
            JSONModel<TModel> jsonModel = new JSONModel<>(this.table);
            for (int i = 0; i < length; i++) {
                try {
                    jsonModel.data = this.jsonArray.getJSONObject(i);
                    jsonModel.delete();
                } catch (JSONException e) {
                    FlowLog.logError(e);
                }
            }
        }
    }

    public void update() {
        if (this.jsonArray != null && this.jsonArray.length() > 0) {
            int length = this.jsonArray.length();
            JSONModel<TModel> jsonModel = new JSONModel<>(this.table);
            for (int i = 0; i < length; i++) {
                try {
                    jsonModel.data = this.jsonArray.getJSONObject(i);
                    jsonModel.update();
                } catch (JSONException e) {
                    FlowLog.logError(e);
                }
            }
        }
    }

    public void insert() {
        if (this.jsonArray != null && this.jsonArray.length() > 0) {
            int length = this.jsonArray.length();
            JSONModel<TModel> jsonModel = new JSONModel<>(this.table);
            for (int i = 0; i < length; i++) {
                try {
                    jsonModel.data = this.jsonArray.getJSONObject(i);
                    jsonModel.insert();
                } catch (JSONException e) {
                    FlowLog.logError(e);
                }
            }
        }
    }

    public boolean exists() {
        throw new RuntimeException("Cannot call exists() on a JsonArrayModel. Call exists(int) instead");
    }

    public boolean exists(int index) {
        try {
            return new JSONModel(this.jsonArray.getJSONObject(index), this.table).exists();
        } catch (JSONException e) {
            FlowLog.logError(e);
            return false;
        }
    }
}
