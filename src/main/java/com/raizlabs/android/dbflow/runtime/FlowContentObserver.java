package com.raizlabs.android.dbflow.runtime;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowContentObserver extends ContentObserver {
    private static final AtomicInteger REGISTERED_COUNT = new AtomicInteger(0);
    private static boolean forceNotify = false;
    /* access modifiers changed from: protected */
    public boolean isInTransaction = false;
    private final Set<OnModelStateChangedListener> modelChangeListeners = new CopyOnWriteArraySet();
    private final Set<Uri> notificationUris = new HashSet();
    private boolean notifyAllUris = false;
    private final Set<OnTableChangedListener> onTableChangedListeners = new CopyOnWriteArraySet();
    private final Map<String, Class<? extends Model>> registeredTables = new HashMap();
    private final Set<Uri> tableUris = new HashSet();

    public interface OnModelStateChangedListener {
        void onModelStateChanged(@Nullable Class<? extends Model> cls, BaseModel.Action action, @NonNull SQLCondition[] sQLConditionArr);
    }

    public interface OnTableChangedListener {
        void onTableChanged(@Nullable Class<? extends Model> cls, BaseModel.Action action);
    }

    public static boolean shouldNotify() {
        return forceNotify || REGISTERED_COUNT.get() > 0;
    }

    public static void clearRegisteredObserverCount() {
        REGISTERED_COUNT.set(0);
    }

    public static void setShouldForceNotify(boolean forceNotify2) {
        forceNotify = forceNotify2;
    }

    public FlowContentObserver() {
        super((Handler) null);
    }

    public FlowContentObserver(Handler handler) {
        super(handler);
    }

    public void setNotifyAllUris(boolean notifyAllUris2) {
        this.notifyAllUris = notifyAllUris2;
    }

    public void beginTransaction() {
        if (!this.isInTransaction) {
            this.isInTransaction = true;
        }
    }

    public void endTransactionAndNotify() {
        if (this.isInTransaction) {
            this.isInTransaction = false;
            if (Build.VERSION.SDK_INT < 16) {
                onChange(true);
                return;
            }
            synchronized (this.notificationUris) {
                for (Uri uri : this.notificationUris) {
                    onChange(true, uri, true);
                }
                this.notificationUris.clear();
            }
            synchronized (this.tableUris) {
                for (Uri uri2 : this.tableUris) {
                    for (OnTableChangedListener onTableChangedListener : this.onTableChangedListeners) {
                        onTableChangedListener.onTableChanged(this.registeredTables.get(uri2.getAuthority()), BaseModel.Action.valueOf(uri2.getFragment()));
                    }
                }
                this.tableUris.clear();
            }
        }
    }

    public void addModelChangeListener(OnModelStateChangedListener modelChangeListener) {
        this.modelChangeListeners.add(modelChangeListener);
    }

    public void removeModelChangeListener(OnModelStateChangedListener modelChangeListener) {
        this.modelChangeListeners.remove(modelChangeListener);
    }

    public void addOnTableChangedListener(OnTableChangedListener onTableChangedListener) {
        this.onTableChangedListeners.add(onTableChangedListener);
    }

    public void removeTableChangedListener(OnTableChangedListener onTableChangedListener) {
        this.onTableChangedListeners.remove(onTableChangedListener);
    }

    public void registerForContentChanges(Context context, Class<? extends Model> table) {
        registerForContentChanges(context.getContentResolver(), table);
    }

    public void registerForContentChanges(ContentResolver contentResolver, Class<? extends Model> table) {
        contentResolver.registerContentObserver(SqlUtils.getNotificationUri(table, (BaseModel.Action) null), true, this);
        REGISTERED_COUNT.incrementAndGet();
        if (!this.registeredTables.containsValue(table)) {
            this.registeredTables.put(FlowManager.getTableName(table), table);
        }
    }

    public void unregisterForContentChanges(Context context) {
        context.getContentResolver().unregisterContentObserver(this);
        REGISTERED_COUNT.decrementAndGet();
        this.registeredTables.clear();
    }

    public void onChange(boolean selfChange) {
        for (OnModelStateChangedListener modelChangeListener : this.modelChangeListeners) {
            modelChangeListener.onModelStateChanged((Class<? extends Model>) null, BaseModel.Action.CHANGE, new SQLCondition[0]);
        }
        for (OnTableChangedListener onTableChangedListener : this.onTableChangedListeners) {
            onTableChangedListener.onTableChanged((Class<? extends Model>) null, BaseModel.Action.CHANGE);
        }
    }

    @TargetApi(16)
    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange, uri, false);
    }

    @TargetApi(16)
    private void onChange(boolean selfChanges, Uri uri, boolean calledInternally) {
        String fragment = uri.getFragment();
        String tableName = uri.getAuthority();
        Set<String> queryNames = uri.getQueryParameterNames();
        SQLCondition[] columnsChanged = new SQLCondition[queryNames.size()];
        if (!queryNames.isEmpty()) {
            int index = 0;
            for (String key : queryNames) {
                columnsChanged[index] = Condition.column(new NameAlias.Builder(Uri.decode(key)).build()).value(Uri.decode(uri.getQueryParameter(key)));
                index++;
            }
        }
        Class<? extends Model> table = this.registeredTables.get(tableName);
        BaseModel.Action action = BaseModel.Action.valueOf(fragment);
        if (!this.isInTransaction) {
            if (action != null) {
                for (OnModelStateChangedListener modelChangeListener : this.modelChangeListeners) {
                    modelChangeListener.onModelStateChanged(table, action, columnsChanged);
                }
            }
            if (!calledInternally) {
                for (OnTableChangedListener onTableChangeListener : this.onTableChangedListeners) {
                    onTableChangeListener.onTableChanged(table, action);
                }
                return;
            }
            return;
        }
        if (!this.notifyAllUris) {
            action = BaseModel.Action.CHANGE;
            uri = SqlUtils.getNotificationUri(table, action);
        }
        synchronized (this.notificationUris) {
            this.notificationUris.add(uri);
        }
        synchronized (this.tableUris) {
            this.tableUris.add(SqlUtils.getNotificationUri(table, action));
        }
    }
}
