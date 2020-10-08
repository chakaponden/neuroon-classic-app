package com.raizlabs.android.dbflow.runtime;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransactionQueue;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

public abstract class BaseTransactionManager {
    private DBBatchSaveQueue saveQueue;
    private final ITransactionQueue transactionQueue;

    public BaseTransactionManager(@NonNull ITransactionQueue transactionQueue2, @NonNull DatabaseDefinition databaseDefinition) {
        this.transactionQueue = transactionQueue2;
        this.saveQueue = new DBBatchSaveQueue(databaseDefinition);
        checkQueue();
    }

    public DBBatchSaveQueue getSaveQueue() {
        try {
            if (!this.saveQueue.isAlive()) {
                this.saveQueue.start();
            }
        } catch (IllegalThreadStateException i) {
            FlowLog.logError(i);
        }
        return this.saveQueue;
    }

    public ITransactionQueue getQueue() {
        return this.transactionQueue;
    }

    public void checkQueue() {
        getQueue().startIfNotAlive();
    }

    public void stopQueue() {
        getQueue().quit();
    }

    public void addTransaction(Transaction transaction) {
        getQueue().add(transaction);
    }

    public void cancelTransaction(Transaction transaction) {
        getQueue().cancel(transaction);
    }
}
