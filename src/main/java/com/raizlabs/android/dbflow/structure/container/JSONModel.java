package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONModel<TModel extends Model> extends BaseModelContainer<TModel, JSONObject> implements Model {
    public JSONModel(JSONObject jsonObject, Class<TModel> table) {
        super(table, jsonObject);
    }

    public JSONModel(Class<TModel> table) {
        super(table, new JSONObject());
    }

    public JSONModel(@NonNull ModelContainer<TModel, ?> existingContainer) {
        super(existingContainer);
    }

    public BaseModelContainer getInstance(Object inValue, Class<? extends Model> columnClass) {
        if (inValue instanceof ModelContainer) {
            return new JSONModel((ModelContainer) inValue);
        }
        return new JSONModel((JSONObject) inValue, columnClass);
    }

    @Nullable
    public Iterator<String> iterator() {
        if (this.data != null) {
            return ((JSONObject) this.data).keys();
        }
        return null;
    }

    @NonNull
    public JSONObject newDataInstance() {
        return new JSONObject();
    }

    public boolean containsValue(String key) {
        return (getData() == null || !((JSONObject) getData()).has(key) || ((JSONObject) getData()).opt(key) == null) ? false : true;
    }

    public Object getValue(String key) {
        Object value = getData() != null ? ((JSONObject) getData()).opt(key) : null;
        if (JSONObject.NULL.equals(value)) {
            return null;
        }
        return value;
    }

    public Integer getIntegerValue(String key) {
        try {
            if (getData() != null) {
                return Integer.valueOf(((JSONObject) getData()).getInt(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public int getIntValue(String key) {
        try {
            if (getData() != null) {
                return ((JSONObject) getData()).getInt(key);
            }
            return 0;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return 0;
        }
    }

    public Long getLongValue(String key) {
        try {
            if (getData() != null) {
                return Long.valueOf(((JSONObject) getData()).getLong(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public long getLngValue(String key) {
        try {
            if (getData() != null) {
                return ((JSONObject) getData()).getLong(key);
            }
            return 0;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return 0;
        }
    }

    public Boolean getBooleanValue(String key) {
        try {
            if (getData() != null) {
                return Boolean.valueOf(((JSONObject) getData()).getBoolean(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public boolean getBoolValue(String key) {
        try {
            if (getData() == null || !((JSONObject) getData()).getBoolean(key)) {
                return false;
            }
            return true;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return false;
        }
    }

    public String getStringValue(String key) {
        try {
            if (getData() != null) {
                return ((JSONObject) getData()).getString(key);
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public Float getFloatValue(String key) {
        Double d = getDoubleValue(key);
        if (d == null) {
            return null;
        }
        return Float.valueOf(d.floatValue());
    }

    public float getFltValue(String key) {
        Float f = getFloatValue(key);
        if (f == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    public Double getDoubleValue(String key) {
        try {
            if (getData() != null) {
                return Double.valueOf(((JSONObject) getData()).getDouble(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public double getDbleValue(String key) {
        try {
            if (getData() != null) {
                return ((JSONObject) getData()).getDouble(key);
            }
            return 0.0d;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return 0.0d;
        }
    }

    public Short getShortValue(String key) {
        try {
            if (getData() != null) {
                return Short.valueOf((short) ((JSONObject) getData()).getInt(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public short getShrtValue(String key) {
        try {
            if (getData() != null) {
                return (short) ((JSONObject) getData()).getInt(key);
            }
            return 0;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return 0;
        }
    }

    public Byte[] getBlobValue(String key) {
        try {
            if (getData() != null) {
                return (Byte[]) ((JSONObject) getData()).get(key);
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return null;
        }
    }

    public byte[] getBlbValue(String key) {
        try {
            if (getData() != null) {
                Object value = ((JSONObject) getData()).get(key);
                if (value instanceof Blob) {
                    return ((Blob) value).getBlob();
                }
                return (byte[]) value;
            }
        } catch (JSONException e) {
            FlowLog.logError(e);
        }
        return null;
    }

    public Byte getByteValue(String key) {
        try {
            if (getData() != null) {
                return Byte.valueOf((byte) ((JSONObject) getData()).getInt(key));
            }
            return null;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return (byte) 0;
        }
    }

    public byte getBytValue(String key) {
        try {
            if (getData() != null) {
                return (byte) ((JSONObject) getData()).getInt(key);
            }
            return 0;
        } catch (JSONException e) {
            FlowLog.logError(e);
            return 0;
        }
    }

    public void put(String columnName, Object value) {
        if (getData() == null) {
            setData(newDataInstance());
        }
        try {
            ((JSONObject) getData()).put(columnName, value);
        } catch (JSONException e) {
            FlowLog.logError(e);
        }
    }
}
