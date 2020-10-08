package com.raizlabs.android.dbflow.runtime;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

public abstract class BaseContentProvider extends ContentProvider {
    protected DatabaseDefinition database;
    protected Class<? extends DatabaseHolder> moduleClass;

    public interface PropertyConverter {
        IProperty fromName(String str);
    }

    /* access modifiers changed from: protected */
    public abstract int bulkInsert(Uri uri, ContentValues contentValues);

    /* access modifiers changed from: protected */
    public abstract String getDatabaseName();

    protected BaseContentProvider() {
    }

    protected BaseContentProvider(Class<? extends DatabaseHolder> databaseHolderClass) {
        this.moduleClass = databaseHolderClass;
    }

    public boolean onCreate() {
        if (this.moduleClass == null) {
            return true;
        }
        FlowManager.initModule(this.moduleClass);
        return true;
    }

    public int bulkInsert(@NonNull final Uri uri, @NonNull final ContentValues[] values) {
        final int[] count = {0};
        getDatabase().executeTransaction(new ITransaction() {
            public void execute(DatabaseWrapper databaseWrapper) {
                for (ContentValues contentValues : values) {
                    int[] iArr = count;
                    iArr[0] = iArr[0] + BaseContentProvider.this.bulkInsert(uri, contentValues);
                }
            }
        });
        getContext().getContentResolver().notifyChange(uri, (ContentObserver) null);
        return count[0];
    }

    /* access modifiers changed from: protected */
    public DatabaseDefinition getDatabase() {
        if (this.database == null) {
            this.database = FlowManager.getDatabase(getDatabaseName());
        }
        return this.database;
    }
}
