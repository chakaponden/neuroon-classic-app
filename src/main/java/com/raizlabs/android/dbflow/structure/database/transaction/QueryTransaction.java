package com.raizlabs.android.dbflow.structure.database.transaction;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.List;

public class QueryTransaction<TResult extends Model> implements ITransaction {
    final ModelQueriable<TResult> modelQueriable;
    final QueryResultCallback<TResult> queryResultCallback;
    final QueryResultListCallback<TResult> queryResultListCallback;
    final QueryResultSingleCallback<TResult> queryResultSingleCallback;
    final boolean runResultCallbacksOnSameThread;

    public interface QueryResultCallback<TResult extends Model> {
        void onQueryResult(QueryTransaction queryTransaction, @NonNull CursorResult<TResult> cursorResult);
    }

    public interface QueryResultListCallback<TResult extends Model> {
        void onListQueryResult(QueryTransaction queryTransaction, @Nullable List<TResult> list);
    }

    public interface QueryResultSingleCallback<TResult extends Model> {
        void onSingleQueryResult(QueryTransaction queryTransaction, @Nullable TResult tresult);
    }

    QueryTransaction(Builder<TResult> builder) {
        this.modelQueriable = builder.modelQueriable;
        this.queryResultCallback = builder.queryResultCallback;
        this.queryResultListCallback = builder.queryResultListCallback;
        this.queryResultSingleCallback = builder.queryResultSingleCallback;
        this.runResultCallbacksOnSameThread = builder.runResultCallbacksOnSameThread;
    }

    public void execute(DatabaseWrapper databaseWrapper) {
        final CursorResult<TResult> cursorResult = this.modelQueriable.queryResults();
        if (this.queryResultCallback != null) {
            if (this.runResultCallbacksOnSameThread) {
                this.queryResultCallback.onQueryResult(this, cursorResult);
            } else {
                Transaction.TRANSACTION_HANDLER.post(new Runnable() {
                    public void run() {
                        QueryTransaction.this.queryResultCallback.onQueryResult(QueryTransaction.this, cursorResult);
                    }
                });
            }
        }
        if (this.queryResultListCallback != null) {
            final List<TResult> resultList = cursorResult.toListClose();
            if (this.runResultCallbacksOnSameThread) {
                this.queryResultListCallback.onListQueryResult(this, resultList);
            } else {
                Transaction.TRANSACTION_HANDLER.post(new Runnable() {
                    public void run() {
                        QueryTransaction.this.queryResultListCallback.onListQueryResult(QueryTransaction.this, resultList);
                    }
                });
            }
        }
        if (this.queryResultSingleCallback != null) {
            final TResult result = cursorResult.toModelClose();
            if (this.runResultCallbacksOnSameThread) {
                this.queryResultSingleCallback.onSingleQueryResult(this, result);
            } else {
                Transaction.TRANSACTION_HANDLER.post(new Runnable() {
                    public void run() {
                        QueryTransaction.this.queryResultSingleCallback.onSingleQueryResult(QueryTransaction.this, result);
                    }
                });
            }
        }
    }

    public static final class Builder<TResult extends Model> {
        final ModelQueriable<TResult> modelQueriable;
        QueryResultCallback<TResult> queryResultCallback;
        QueryResultListCallback<TResult> queryResultListCallback;
        QueryResultSingleCallback<TResult> queryResultSingleCallback;
        boolean runResultCallbacksOnSameThread;

        public Builder(@NonNull ModelQueriable<TResult> modelQueriable2) {
            this.modelQueriable = modelQueriable2;
        }

        public Builder<TResult> queryResult(QueryResultCallback<TResult> queryResultCallback2) {
            this.queryResultCallback = queryResultCallback2;
            return this;
        }

        public Builder<TResult> queryListResult(QueryResultListCallback<TResult> queryResultListCallback2) {
            this.queryResultListCallback = queryResultListCallback2;
            return this;
        }

        public Builder<TResult> querySingleResult(QueryResultSingleCallback<TResult> queryResultSingleCallback2) {
            this.queryResultSingleCallback = queryResultSingleCallback2;
            return this;
        }

        public Builder<TResult> runResultCallbacksOnSameThread(boolean runResultCallbacksOnSameThread2) {
            this.runResultCallbacksOnSameThread = runResultCallbacksOnSameThread2;
            return this;
        }

        public QueryTransaction<TResult> build() {
            return new QueryTransaction<>(this);
        }
    }
}
