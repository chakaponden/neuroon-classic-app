package com.raizlabs.android.dbflow.runtime;

import android.os.Looper;
import android.os.Process;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import java.util.ArrayList;
import java.util.Collection;

public class DBBatchSaveQueue extends Thread {
    private static final int MODEL_SAVE_SIZE = 50;
    private static final int sMODEL_SAVE_CHECK_TIME = 30000;
    private DatabaseDefinition databaseDefinition;
    private final Transaction.Error errorCallback = new Transaction.Error() {
        public void onError(Transaction transaction, Throwable error) {
            if (DBBatchSaveQueue.this.errorListener != null) {
                DBBatchSaveQueue.this.errorListener.onError(transaction, error);
            }
        }
    };
    /* access modifiers changed from: private */
    public Transaction.Error errorListener;
    private boolean isQuitting = false;
    private long modelSaveCheckTime = 30000;
    private int modelSaveSize = 50;
    private final ProcessModelTransaction.ProcessModel modelSaver = new ProcessModelTransaction.ProcessModel() {
        public void processModel(Model model) {
            model.save();
        }
    };
    private final ArrayList<Model> models;
    private final Transaction.Success successCallback = new Transaction.Success() {
        public void onSuccess(Transaction transaction) {
            if (DBBatchSaveQueue.this.successListener != null) {
                DBBatchSaveQueue.this.successListener.onSuccess(transaction);
            }
        }
    };
    /* access modifiers changed from: private */
    public Transaction.Success successListener;

    DBBatchSaveQueue(DatabaseDefinition databaseDefinition2) {
        super("DBBatchSaveQueue");
        this.databaseDefinition = databaseDefinition2;
        this.models = new ArrayList<>();
    }

    public void setModelSaveSize(int mModelSaveSize) {
        this.modelSaveSize = mModelSaveSize;
    }

    public void setModelSaveCheckTime(long time) {
        this.modelSaveCheckTime = time;
    }

    public void setErrorListener(Transaction.Error errorListener2) {
        this.errorListener = errorListener2;
    }

    public void setSuccessListener(Transaction.Success successListener2) {
        this.successListener = successListener2;
    }

    public void run() {
        ArrayList<Model> tmpModels;
        super.run();
        Looper.prepare();
        Process.setThreadPriority(10);
        do {
            synchronized (this.models) {
                tmpModels = new ArrayList<>(this.models);
                this.models.clear();
            }
            if (tmpModels.size() > 0) {
                this.databaseDefinition.beginTransactionAsync(new ProcessModelTransaction.Builder(this.modelSaver).addAll(tmpModels).build()).success(this.successCallback).error(this.errorCallback).build().execute();
            }
            try {
                Thread.sleep(this.modelSaveCheckTime);
            } catch (InterruptedException e) {
                FlowLog.log(FlowLog.Level.I, "DBRequestQueue Batch interrupted to start saving");
            }
        } while (!this.isQuitting);
    }

    public void purgeQueue() {
        interrupt();
    }

    public void add(Model inModel) {
        synchronized (this.models) {
            this.models.add(inModel);
            if (this.models.size() > this.modelSaveSize) {
                interrupt();
            }
        }
    }

    public void addAll(Collection<Model> list) {
        synchronized (this.models) {
            this.models.addAll(list);
            if (this.models.size() > this.modelSaveSize) {
                interrupt();
            }
        }
    }

    public void remove(Model outModel) {
        synchronized (this.models) {
            this.models.remove(outModel);
        }
    }

    public void removeAll(Collection<Model> outCollection) {
        synchronized (this.models) {
            this.models.removeAll(outCollection);
        }
    }

    public void quit() {
        this.isQuitting = true;
    }
}
