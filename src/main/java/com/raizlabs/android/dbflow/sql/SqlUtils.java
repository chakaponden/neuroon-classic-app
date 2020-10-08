package com.raizlabs.android.dbflow.sql;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.StringUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.FlowContentObserver;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.RetrievalAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Map;

public class SqlUtils {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void notifyModelChanged(Class<? extends Model> table, BaseModel.Action action, Iterable<SQLCondition> sqlConditions) {
        FlowManager.getContext().getContentResolver().notifyChange(getNotificationUri(table, action, sqlConditions), (ContentObserver) null, true);
    }

    public static <ModelClass extends Model, TableClass extends Model, AdapterClass extends RetrievalAdapter & InternalAdapter> void notifyModelChanged(TableClass model, AdapterClass adapter, ModelAdapter<ModelClass> modelAdapter, BaseModel.Action action) {
        if (FlowContentObserver.shouldNotify()) {
            notifyModelChanged(modelAdapter.getModelClass(), action, adapter.getPrimaryConditionClause(model).getConditions());
        }
    }

    public static Uri getNotificationUri(Class<? extends Model> modelClass, BaseModel.Action action, Iterable<SQLCondition> conditions) {
        Uri.Builder uriBuilder = new Uri.Builder().scheme("dbflow").authority(FlowManager.getTableName(modelClass));
        if (action != null) {
            uriBuilder.fragment(action.name());
        }
        if (conditions != null) {
            for (SQLCondition condition : conditions) {
                uriBuilder.appendQueryParameter(Uri.encode(condition.columnName()), Uri.encode(String.valueOf(condition.value())));
            }
        }
        return uriBuilder.build();
    }

    public static Uri getNotificationUri(Class<? extends Model> modelClass, BaseModel.Action action, SQLCondition[] conditions) {
        Uri.Builder uriBuilder = new Uri.Builder().scheme("dbflow").authority(FlowManager.getTableName(modelClass));
        if (action != null) {
            uriBuilder.fragment(action.name());
        }
        if (conditions != null && conditions.length > 0) {
            for (SQLCondition condition : conditions) {
                if (condition != null) {
                    uriBuilder.appendQueryParameter(Uri.encode(condition.columnName()), Uri.encode(String.valueOf(condition.value())));
                }
            }
        }
        return uriBuilder.build();
    }

    public static Uri getNotificationUri(Class<? extends Model> modelClass, BaseModel.Action action, String notifyKey, Object notifyValue) {
        Condition condition = null;
        if (StringUtils.isNotNullOrEmpty(notifyKey)) {
            condition = Condition.column(new NameAlias.Builder(notifyKey).build()).value(notifyValue);
        }
        return getNotificationUri(modelClass, action, new SQLCondition[]{condition});
    }

    public static Uri getNotificationUri(Class<? extends Model> modelClass, BaseModel.Action action) {
        return getNotificationUri(modelClass, action, (String) null, (Object) null);
    }

    public static <ModelClass extends Model> void dropTrigger(Class<ModelClass> mOnTable, String triggerName) {
        FlowManager.getDatabaseForTable(mOnTable).getWritableDatabase().execSQL(new QueryBuilder("DROP TRIGGER IF EXISTS ").append(triggerName).getQuery());
    }

    public static <ModelClass extends Model> void dropIndex(DatabaseWrapper databaseWrapper, String indexName) {
        databaseWrapper.execSQL(new QueryBuilder("DROP INDEX IF EXISTS ").append(QueryBuilder.quoteIfNeeded(indexName)).getQuery());
    }

    public static <ModelClass extends Model> void dropIndex(Class<ModelClass> onTable, String indexName) {
        dropIndex(FlowManager.getDatabaseForTable(onTable).getWritableDatabase(), indexName);
    }

    public static void addContentValues(@NonNull ContentValues contentValues, @NonNull ConditionGroup conditionGroup) {
        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            String key = entry.getKey();
            conditionGroup.and(Condition.column(new NameAlias.Builder(key).build()).is(contentValues.get(key)));
        }
    }

    public static String getContentValuesKey(ContentValues contentValues, String key) {
        String quoted = QueryBuilder.quoteIfNeeded(key);
        if (contentValues.containsKey(quoted)) {
            return quoted;
        }
        String stripped = QueryBuilder.stripQuotes(key);
        if (contentValues.containsKey(stripped)) {
            return stripped;
        }
        throw new IllegalArgumentException("Could not find the specified key in the Content Values object.");
    }

    public static long longForQuery(DatabaseWrapper wrapper, String query) {
        DatabaseStatement statement = wrapper.compileStatement(query);
        try {
            return statement.simpleQueryForLong();
        } finally {
            statement.close();
        }
    }

    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }
}
