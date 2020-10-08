package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.Model;

public abstract class SimpleModelContainer<TModel extends Model, DataClass> extends BaseModelContainer<TModel, DataClass> {
    public SimpleModelContainer(Class<TModel> table) {
        super(table);
    }

    public SimpleModelContainer(Class<TModel> table, DataClass data) {
        super(table, data);
    }

    public SimpleModelContainer(@NonNull ModelContainer<TModel, ?> existingContainer) {
        super(existingContainer);
    }

    public Integer getIntegerValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        return null;
    }

    public int getIntValue(String key) {
        Integer value = getIntegerValue(key);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    public Long getLongValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        return null;
    }

    public long getLngValue(String key) {
        Long value = getLongValue(key);
        if (value == null) {
            return 0;
        }
        return value.longValue();
    }

    public Boolean getBooleanValue(String key) {
        boolean z = true;
        Object value = getValue(key);
        if (value instanceof String) {
            return Boolean.valueOf((String) value);
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (!(value instanceof Number)) {
            return null;
        }
        if (((Number) value).byteValue() != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public boolean getBoolValue(String key) {
        Boolean value = getBooleanValue(key);
        return value != null && value.booleanValue();
    }

    public String getStringValue(String key) {
        return String.valueOf(getValue(key));
    }

    public Float getFloatValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Float.valueOf((String) value);
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        return null;
    }

    public float getFltValue(String key) {
        Float value = getFloatValue(key);
        if (value == null) {
            return 0.0f;
        }
        return value.floatValue();
    }

    public Double getDoubleValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        return null;
    }

    public double getDbleValue(String key) {
        Double value = getDoubleValue(key);
        if (value == null) {
            return 0.0d;
        }
        return value.doubleValue();
    }

    public Byte[] getBlobValue(String key) {
        return (Byte[]) getValue(key);
    }

    public byte[] getBlbValue(String key) {
        return (byte[]) getValue(key);
    }

    public Short getShortValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Short.valueOf((String) value);
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        return null;
    }

    public short getShrtValue(String key) {
        Short value = getShortValue(key);
        if (value == null) {
            return 0;
        }
        return value.shortValue();
    }

    public Byte getByteValue(String key) {
        Object value = getValue(key);
        if (value instanceof String) {
            return Byte.valueOf((String) value);
        }
        if (value instanceof Byte) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        return null;
    }

    public byte getBytValue(String key) {
        Byte value = getByteValue(key);
        if (value == null) {
            return 0;
        }
        return value.byteValue();
    }
}
