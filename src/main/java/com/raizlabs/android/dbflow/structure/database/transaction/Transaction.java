package com.raizlabs.android.dbflow.structure.database.transaction;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;

public final class Transaction {
    static final Handler TRANSACTION_HANDLER = new Handler(Looper.getMainLooper());
    final DatabaseDefinition databaseDefinition;
    final Error errorCallback;
    final String name;
    final boolean runCallbacksOnSameThread;
    final boolean shouldRunInTransaction;
    final Success successCallback;
    final ITransaction transaction;

    public interface Error {
        void onError(Transaction transaction, Throwable th);
    }

    public interface Success {
        void onSuccess(Transaction transaction);
    }

    Transaction(Builder builder) {
        this.databaseDefinition = builder.databaseDefinition;
        this.errorCallback = builder.errorCallback;
        this.successCallback = builder.successCallback;
        this.transaction = builder.transaction;
        this.name = builder.name;
        this.shouldRunInTransaction = builder.shouldRunInTransaction;
        this.runCallbacksOnSameThread = builder.runCallbacksOnSameThread;
    }

    public Error error() {
        return this.errorCallback;
    }

    public Success success() {
        return this.successCallback;
    }

    public ITransaction transaction() {
        return this.transaction;
    }

    public String name() {
        return this.name;
    }

    public void execute() {
        this.databaseDefinition.getTransactionManager().addTransaction(this);
    }

    public void cancel() {
        this.databaseDefinition.getTransactionManager().cancelTransaction(this);
    }

    public void executeSync() {
        try {
            if (this.shouldRunInTransaction) {
                this.databaseDefinition.executeTransaction(this.transaction);
            } else {
                this.transaction.execute(this.databaseDefinition.getWritableDatabase());
            }
            if (this.successCallback == null) {
                return;
            }
            if (this.runCallbacksOnSameThread) {
                this.successCallback.onSuccess(this);
            } else {
                TRANSACTION_HANDLER.post(new Runnable() {
                    public void run() {
                        Transaction.this.successCallback.onSuccess(Transaction.this);
                    }
                });
            }
        } catch (Throwable throwable) {
            if (this.errorCallback == null) {
                throw new RuntimeException("An exception occurred while executing a transaction", throwable);
            } else if (this.runCallbacksOnSameThread) {
                this.errorCallback.onError(this, throwable);
            } else {
                TRANSACTION_HANDLER.post(new Runnable() {
                    public void run() {
                        Transaction.this.errorCallback.onError(Transaction.this, throwable);
                    }
                });
            }
        }
    }

    public static final class Builder {
        @NonNull
        final DatabaseDefinition databaseDefinition;
        Error errorCallback;
        String name;
        /* access modifiers changed from: private */
        public boolean runCallbacksOnSameThread;
        boolean shouldRunInTransaction = true;
        Success successCallback;
        final ITransaction transaction;

        public Builder(@NonNull ITransaction transaction2, @NonNull DatabaseDefinition databaseDefinition2) {
            this.transaction = transaction2;
            this.databaseDefinition = databaseDefinition2;
        }

        public Builder error(Error errorCallback2) {
            this.errorCallback = errorCallback2;
            return this;
        }

        public Builder success(Success successCallback2) {
            this.successCallback = successCallback2;
            return this;
        }

        public Builder name(String name2) {
            this.name = name2;
            return this;
        }

        public Builder shouldRunInTransaction(boolean shouldRunInTransaction2) {
            this.shouldRunInTransaction = shouldRunInTransaction2;
            return this;
        }

        public Builder runCallbacksOnSameThread(boolean runCallbacksOnSameThread2) {
            this.runCallbacksOnSameThread = runCallbacksOnSameThread2;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
