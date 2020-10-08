package io.fabric.sdk.android.services.concurrency;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public abstract class PriorityAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements Dependency<Task>, PriorityProvider, Task, DelegateProvider {
    private final PriorityTask priorityTask = new PriorityTask();

    public final void executeOnExecutor(ExecutorService exec, Params... params) {
        super.executeOnExecutor(new ProxyExecutor(exec, this), params);
    }

    public int compareTo(Object another) {
        return Priority.compareTo(this, another);
    }

    public void addDependency(Task task) {
        if (getStatus() != AsyncTask.Status.PENDING) {
            throw new IllegalStateException("Must not add Dependency after task is running");
        }
        ((Dependency) ((PriorityProvider) getDelegate())).addDependency(task);
    }

    public Collection<Task> getDependencies() {
        return ((Dependency) ((PriorityProvider) getDelegate())).getDependencies();
    }

    public boolean areDependenciesMet() {
        return ((Dependency) ((PriorityProvider) getDelegate())).areDependenciesMet();
    }

    public Priority getPriority() {
        return ((PriorityProvider) getDelegate()).getPriority();
    }

    public void setFinished(boolean finished) {
        ((Task) ((PriorityProvider) getDelegate())).setFinished(finished);
    }

    public boolean isFinished() {
        return ((Task) ((PriorityProvider) getDelegate())).isFinished();
    }

    public void setError(Throwable throwable) {
        ((Task) ((PriorityProvider) getDelegate())).setError(throwable);
    }

    public Throwable getError() {
        return ((Task) ((PriorityProvider) getDelegate())).getError();
    }

    public <T extends Dependency<Task> & PriorityProvider & Task> T getDelegate() {
        return this.priorityTask;
    }

    private static class ProxyExecutor<Result> implements Executor {
        private final Executor executor;
        /* access modifiers changed from: private */
        public final PriorityAsyncTask task;

        public ProxyExecutor(Executor ex, PriorityAsyncTask task2) {
            this.executor = ex;
            this.task = task2;
        }

        public void execute(Runnable command) {
            this.executor.execute(new PriorityFutureTask<Result>(command, (Object) null) {
                public <T extends Dependency<Task> & PriorityProvider & Task> T getDelegate() {
                    return ProxyExecutor.this.task;
                }
            });
        }
    }
}
