package com.raizlabs.android.dbflow.structure.database.transaction;

import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.database.transaction.PriorityTransactionWrapper;
import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityTransactionQueue extends Thread implements ITransactionQueue {
    private boolean isQuitting = false;
    private final PriorityBlockingQueue<PriorityEntry<Transaction>> queue = new PriorityBlockingQueue<>();

    public PriorityTransactionQueue(String name) {
        super(name);
    }

    public void run() {
        Looper.prepare();
        Process.setThreadPriority(10);
        while (true) {
            try {
                this.queue.take().entry.executeSync();
            } catch (InterruptedException e) {
                if (this.isQuitting) {
                    synchronized (this.queue) {
                        this.queue.clear();
                        return;
                    }
                }
            }
        }
    }

    public void add(Transaction transaction) {
        synchronized (this.queue) {
            PriorityEntry<Transaction> priorityEntry = new PriorityEntry<>(transaction);
            if (!this.queue.contains(priorityEntry)) {
                this.queue.add(priorityEntry);
            }
        }
    }

    public void cancel(Transaction transaction) {
        synchronized (this.queue) {
            PriorityEntry<Transaction> priorityEntry = new PriorityEntry<>(transaction);
            if (this.queue.contains(priorityEntry)) {
                this.queue.remove(priorityEntry);
            }
        }
    }

    public void cancel(String tag) {
        synchronized (this.queue) {
            Iterator<PriorityEntry<Transaction>> it = this.queue.iterator();
            while (it.hasNext()) {
                Transaction next = it.next().entry;
                if (next.name() != null && next.name().equals(tag)) {
                    it.remove();
                }
            }
        }
    }

    public void startIfNotAlive() {
        synchronized (this) {
            if (!isAlive()) {
                try {
                    start();
                } catch (IllegalThreadStateException i) {
                    FlowLog.log(FlowLog.Level.E, (Throwable) i);
                }
            }
        }
    }

    public void quit() {
        this.isQuitting = true;
        interrupt();
    }

    private void throwInvalidTransactionType(Transaction transaction) {
        throw new IllegalArgumentException("Transaction of type:" + (transaction != null ? transaction.transaction().getClass() : "Unknown") + " should be of type PriorityTransactionWrapper");
    }

    class PriorityEntry<E extends Transaction> implements Comparable<PriorityEntry<Transaction>> {
        final E entry;
        final PriorityTransactionWrapper transactionWrapper;

        public PriorityEntry(E entry2) {
            this.entry = entry2;
            if (entry2.transaction() instanceof PriorityTransactionWrapper) {
                this.transactionWrapper = (PriorityTransactionWrapper) entry2.transaction();
            } else {
                this.transactionWrapper = new PriorityTransactionWrapper.Builder(entry2.transaction()).build();
            }
        }

        public E getEntry() {
            return this.entry;
        }

        public int compareTo(@NonNull PriorityEntry<Transaction> another) {
            return this.transactionWrapper.compareTo(another.transactionWrapper);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PriorityEntry<?> that = (PriorityEntry) o;
            if (this.transactionWrapper != null) {
                return this.transactionWrapper.equals(that.transactionWrapper);
            }
            if (that.transactionWrapper != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            if (this.transactionWrapper != null) {
                return this.transactionWrapper.hashCode();
            }
            return 0;
        }
    }
}
