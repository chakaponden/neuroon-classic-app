package com.inteliclinic.lucid;

import android.content.Context;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class LucidManager {
    public static final String LAST_CONFIG_CHANGE_TIME = "user-config-last-change";
    private static final String TAG = LucidManager.class.getSimpleName();
    private LucidConfig lucidConfig = LucidConfig.getInstance(this);

    public abstract String getLucidDelegateKey();

    /* access modifiers changed from: protected */
    public <T> T lucidRead(Class<T> aClass, String key) {
        Object s = getLucid().get(key, getLucidDelegateKey());
        if (s == null || ((s instanceof ArrayList) && ((ArrayList) s).size() == 0)) {
            return null;
        }
        if (aClass.equals(Date.class)) {
            if (s instanceof Integer) {
                return aClass.cast(new Date((long) (((Integer) s).intValue() * 1000)));
            }
            return aClass.cast(new Date((long) (((Double) s).intValue() * 1000)));
        } else if (aClass.isAssignableFrom(s.getClass())) {
            return aClass.cast(s);
        } else {
            if ((s instanceof Integer) && aClass.equals(Double.class)) {
                return aClass.cast(Double.valueOf(((Integer) s).doubleValue()));
            }
            if ((s instanceof Double) && aClass.equals(Integer.class)) {
                return aClass.cast(Integer.valueOf(((Double) s).intValue()));
            }
            throw new UnsupportedOperationException("Unexpected type returned of key:" + key + " in manager:" + getLucidDelegateKey() + " type:" + aClass.getName() + " but was:" + (s.getClass() != null ? s.getClass().getName() : "null"));
        }
    }

    /* access modifiers changed from: protected */
    public <T> T lucidSet(Class<T> aClass, String key, T value) {
        lucidSetGlobalInt(Integer.class, LAST_CONFIG_CHANGE_TIME, Integer.valueOf((int) (new Date().getTime() / 1000)));
        return aClass.cast(lucidSetInt(aClass, key, value));
    }

    /* access modifiers changed from: protected */
    public <T, Y> Y lucidSet(Class<T> cls, Class<Y> outClass, String key, T value) {
        lucidSetGlobalInt(Integer.class, LAST_CONFIG_CHANGE_TIME, Integer.valueOf((int) (new Date().getTime() / 1000)));
        return outClass.cast(lucidSetInt(outClass, key, value));
    }

    /* access modifiers changed from: protected */
    public <T> T lucidGlobalSet(Class<T> aClass, String key, T value) {
        lucidSetGlobalInt(Integer.class, LAST_CONFIG_CHANGE_TIME, Integer.valueOf((int) (new Date().getTime() / 1000)));
        return aClass.cast(lucidSetGlobalInt(aClass, key, value));
    }

    /* access modifiers changed from: protected */
    public void lucidBroadcast(String key, float value) {
        getLucid().get(String.format(Locale.ROOT, "(broadcast %s %f)", new Object[]{getValueString(key), Float.valueOf(value)}), getLucidDelegateKey());
    }

    private Object lucidSetGlobalInt(Class aClass, String key, Object value) {
        String setValue = getValueString(value);
        if (aClass.equals(Date.class)) {
            return new Date((long) (((Integer) getLucid().get("(set-global! " + key + " " + setValue + ")", getLucidDelegateKey())).intValue() * 1000));
        }
        return getLucid().get("(set-global! " + key + " " + setValue + ")", getLucidDelegateKey());
    }

    private Object lucidSetInt(Class aClass, String key, Object value) {
        String setValue = getValueString(value);
        if (aClass.equals(Date.class)) {
            return new Date((long) (((Integer) getLucid().get("(set! " + key + " " + setValue + ")", getLucidDelegateKey())).intValue() * 1000));
        }
        return getLucid().get("(set! " + key + " " + setValue + ")", getLucidDelegateKey());
    }

    /* access modifiers changed from: protected */
    public void lucidSave(Context context) {
        getLucid().saveConfig(context);
    }

    public Object methodWithLucidKey(String key, List<Object> list) {
        throw new UnsupportedOperationException("methodWithLucidKey should be overridden");
    }

    public LucidConfig getLucid() {
        return this.lucidConfig;
    }

    public String getValueString(Object value) {
        if (value instanceof Boolean) {
            if (((Boolean) value).booleanValue()) {
                return "#t";
            }
            return "#f";
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else {
            if ((value instanceof Double) || (value instanceof Integer)) {
                return String.valueOf(value);
            }
            if (value instanceof Date) {
                return String.valueOf(((int) ((Date) value).getTime()) / 1000);
            }
            if (value instanceof List) {
                StringBuilder stringBuilder = new StringBuilder("'(");
                if (((List) value).size() != 0) {
                    for (Object obj : (List) value) {
                        stringBuilder.append(getValueString(obj)).append(" ");
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
            throw new UnsupportedOperationException("Unsupported lucid type: " + value.getClass());
        }
    }
}
