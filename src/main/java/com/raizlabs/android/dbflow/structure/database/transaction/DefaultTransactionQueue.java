package com.raizlabs.android.dbflow.structure.database.transaction;

import android.os.Looper;
import android.os.Process;
import com.raizlabs.android.dbflow.config.FlowLog;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class DefaultTransactionQueue extends Thread implements ITransactionQueue {
    private boolean isQuitting = false;
    private final LinkedBlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();

    public DefaultTransactionQueue(String name) {
        super(name);
    }

    public void run() {
        Looper.prepare();
        Process.setThreadPriority(10);
        while (true) {
            try {
                Transaction transaction = this.queue.take();
                if (!this.isQuitting) {
                    transaction.executeSync();
                }
            } catch (InterruptedException e) {
                synchronized (this) {
                    if (this.isQuitting) {
                        synchronized (this.queue) {
                            this.queue.clear();
                            return;
                        }
                    }
                }
            }
        }
    }

    public void add(Transaction runnable) {
        synchronized (this.queue) {
            if (!this.queue.contains(runnable)) {
                this.queue.add(runnable);
            }
        }
    }

    public void cancel(Transaction runnable) {
        synchronized (this.queue) {
            if (this.queue.contains(runnable)) {
                this.queue.remove(runnable);
            }
        }
    }

    public void cancel(String tag) {
        synchronized (this.queue) {
            Iterator<Transaction> it = this.queue.iterator();
            while (it.hasNext()) {
                Transaction next = it.next();
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
        synchronized (this) {
            this.isQuitting = true;
        }
        interrupt();
    }
}
