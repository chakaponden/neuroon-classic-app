package io.fabric.sdk.android;

import android.content.Context;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.Task;
import java.io.File;
import java.util.Collection;

public abstract class Kit<Result> implements Comparable<Kit> {
    Context context;
    final DependsOn dependsOnAnnotation = ((DependsOn) getClass().getAnnotation(DependsOn.class));
    Fabric fabric;
    IdManager idManager;
    InitializationCallback<Result> initializationCallback;
    InitializationTask<Result> initializationTask = new InitializationTask<>(this);

    /* access modifiers changed from: protected */
    public abstract Result doInBackground();

    public abstract String getIdentifier();

    public abstract String getVersion();

    /* access modifiers changed from: package-private */
    public void injectParameters(Context context2, Fabric fabric2, InitializationCallback<Result> callback, IdManager idManager2) {
        this.fabric = fabric2;
        this.context = new FabricContext(context2, getIdentifier(), getPath());
        this.initializationCallback = callback;
        this.idManager = idManager2;
    }

    /* access modifiers changed from: package-private */
    public final void initialize() {
        this.initializationTask.executeOnExecutor(this.fabric.getExecutorService(), null);
    }

    /* access modifiers changed from: protected */
    public boolean onPreExecute() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
    }

    /* access modifiers changed from: protected */
    public IdManager getIdManager() {
        return this.idManager;
    }

    public Context getContext() {
        return this.context;
    }

    public Fabric getFabric() {
        return this.fabric;
    }

    public String getPath() {
        return ".Fabric" + File.separator + getIdentifier();
    }

    public int compareTo(Kit another) {
        if (containsAnnotatedDependency(another)) {
            return 1;
        }
        if (another.containsAnnotatedDependency(this)) {
            return -1;
        }
        if (hasAnnotatedDependency() && !another.hasAnnotatedDependency()) {
            return 1;
        }
        if (hasAnnotatedDependency() || !another.hasAnnotatedDependency()) {
            return 0;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean containsAnnotatedDependency(Kit target) {
        if (hasAnnotatedDependency()) {
            for (Class<?> dep : this.dependsOnAnnotation.value()) {
                if (dep.isAssignableFrom(target.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean hasAnnotatedDependency() {
        return this.dependsOnAnnotation != null;
    }

    /* access modifiers changed from: protected */
    public Collection<Task> getDependencies() {
        return this.initializationTask.getDependencies();
    }
}
