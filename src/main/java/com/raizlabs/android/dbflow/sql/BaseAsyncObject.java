package com.raizlabs.android.dbflow.sql;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

public class BaseAsyncObject<TAsync> {
    /* access modifiers changed from: private */
    public Transaction currentTransaction;
    private final DatabaseDefinition databaseDefinition;
    private final Transaction.Error error = new Transaction.Error() {
        public void onError(Transaction transaction, Throwable error) {
            if (BaseAsyncObject.this.errorCallback != null) {
                BaseAsyncObject.this.errorCallback.onError(transaction, error);
            }
            BaseAsyncObject.this.onError(transaction, error);
            Transaction unused = BaseAsyncObject.this.currentTransaction = null;
        }
    };
    /* access modifiers changed from: private */
    public Transaction.Error errorCallback;
    private final Transaction.Success success = new Transaction.Success() {
        public void onSuccess(Transaction transaction) {
            if (BaseAsyncObject.this.successCallback != null) {
                BaseAsyncObject.this.successCallback.onSuccess(transaction);
            }
            BaseAsyncObject.this.onSuccess(transaction);
            Transaction unused = BaseAsyncObject.this.currentTransaction = null;
        }
    };
    /* access modifiers changed from: private */
    public Transaction.Success successCallback;

    public BaseAsyncObject(Class<? extends Model> modelClass) {
        this.databaseDefinition = FlowManager.getDatabaseForTable(modelClass);
    }

    public TAsync error(Transaction.Error errorCallback2) {
        this.errorCallback = errorCallback2;
        return this;
    }

    public TAsync success(Transaction.Success success2) {
        this.successCallback = success2;
        return this;
    }

    public void cancel() {
        if (this.currentTransaction != null) {
            this.currentTransaction.cancel();
        }
    }

    /* access modifiers changed from: protected */
    public void executeTransaction(ITransaction transaction) {
        cancel();
        this.currentTransaction = this.databaseDefinition.beginTransactionAsync(transaction).error(this.error).success(this.success).build();
        this.currentTransaction.execute();
    }

    /* access modifiers changed from: protected */
    public void onError(Transaction transaction, Throwable error2) {
    }

    /* access modifiers changed from: protected */
    public void onSuccess(Transaction transaction) {
    }
}
