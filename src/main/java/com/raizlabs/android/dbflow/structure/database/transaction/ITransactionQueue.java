package com.raizlabs.android.dbflow.structure.database.transaction;

public interface ITransactionQueue {
    void add(Transaction transaction);

    void cancel(Transaction transaction);

    void cancel(String str);

    void quit();

    void startIfNotAlive();
}
