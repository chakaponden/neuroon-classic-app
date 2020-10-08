package com.raizlabs.android.dbflow.structure.database.transaction;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class PriorityTransactionWrapper implements ITransaction, Comparable<PriorityTransactionWrapper> {
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_UI = 5;
    private final int priority;
    private final ITransaction transaction;

    public @interface Priority {
    }

    PriorityTransactionWrapper(Builder builder) {
        if (builder.priority == 0) {
            this.priority = 1;
        } else {
            this.priority = builder.priority;
        }
        this.transaction = builder.transaction;
    }

    public void execute(DatabaseWrapper databaseWrapper) {
        this.transaction.execute(databaseWrapper);
    }

    public int compareTo(@NonNull PriorityTransactionWrapper another) {
        return another.priority - this.priority;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public int priority;
        /* access modifiers changed from: private */
        public final ITransaction transaction;

        public Builder(@NonNull ITransaction transaction2) {
            this.transaction = transaction2;
        }

        public Builder priority(@Priority int priority2) {
            this.priority = priority2;
            return this;
        }

        public PriorityTransactionWrapper build() {
            return new PriorityTransactionWrapper(this);
        }
    }
}
