package io.fabric.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import io.fabric.sdk.android.ActivityLifecycleManager;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.PriorityThreadPoolExecutor;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class Fabric {
    static final boolean DEFAULT_DEBUGGABLE = false;
    static final Logger DEFAULT_LOGGER = new DefaultLogger();
    static final String ROOT_DIR = ".Fabric";
    public static final String TAG = "Fabric";
    static volatile Fabric singleton;
    private WeakReference<Activity> activity;
    private ActivityLifecycleManager activityLifecycleManager;
    private final Context context;
    final boolean debuggable;
    private final ExecutorService executorService;
    private final IdManager idManager;
    /* access modifiers changed from: private */
    public final InitializationCallback<Fabric> initializationCallback;
    /* access modifiers changed from: private */
    public AtomicBoolean initialized = new AtomicBoolean(false);
    private final InitializationCallback<?> kitInitializationCallback;
    private final Map<Class<? extends Kit>, Kit> kits;
    final Logger logger;
    private final Handler mainHandler;

    public static class Builder {
        private String appIdentifier;
        private String appInstallIdentifier;
        private final Context context;
        private boolean debuggable;
        private Handler handler;
        private InitializationCallback<Fabric> initializationCallback;
        private Kit[] kits;
        private Logger logger;
        private PriorityThreadPoolExecutor threadPoolExecutor;

        public Builder(Context context2) {
            if (context2 == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context2.getApplicationContext();
        }

        public Builder kits(Kit... kits2) {
            if (this.kits != null) {
                throw new IllegalStateException("Kits already set.");
            }
            this.kits = kits2;
            return this;
        }

        @Deprecated
        public Builder executorService(ExecutorService executorService) {
            return this;
        }

        public Builder threadPoolExecutor(PriorityThreadPoolExecutor threadPoolExecutor2) {
            if (threadPoolExecutor2 == null) {
                throw new IllegalArgumentException("PriorityThreadPoolExecutor must not be null.");
            } else if (this.threadPoolExecutor != null) {
                throw new IllegalStateException("PriorityThreadPoolExecutor already set.");
            } else {
                this.threadPoolExecutor = threadPoolExecutor2;
                return this;
            }
        }

        @Deprecated
        public Builder handler(Handler handler2) {
            return this;
        }

        public Builder logger(Logger logger2) {
            if (logger2 == null) {
                throw new IllegalArgumentException("Logger must not be null.");
            } else if (this.logger != null) {
                throw new IllegalStateException("Logger already set.");
            } else {
                this.logger = logger2;
                return this;
            }
        }

        public Builder appIdentifier(String appIdentifier2) {
            if (appIdentifier2 == null) {
                throw new IllegalArgumentException("appIdentifier must not be null.");
            } else if (this.appIdentifier != null) {
                throw new IllegalStateException("appIdentifier already set.");
            } else {
                this.appIdentifier = appIdentifier2;
                return this;
            }
        }

        public Builder appInstallIdentifier(String appInstallIdentifier2) {
            if (appInstallIdentifier2 == null) {
                throw new IllegalArgumentException("appInstallIdentifier must not be null.");
            } else if (this.appInstallIdentifier != null) {
                throw new IllegalStateException("appInstallIdentifier already set.");
            } else {
                this.appInstallIdentifier = appInstallIdentifier2;
                return this;
            }
        }

        public Builder debuggable(boolean enabled) {
            this.debuggable = enabled;
            return this;
        }

        public Builder initializationCallback(InitializationCallback<Fabric> initializationCallback2) {
            if (initializationCallback2 == null) {
                throw new IllegalArgumentException("initializationCallback must not be null.");
            } else if (this.initializationCallback != null) {
                throw new IllegalStateException("initializationCallback already set.");
            } else {
                this.initializationCallback = initializationCallback2;
                return this;
            }
        }

        public Fabric build() {
            Map<Class<? extends Kit>, Kit> kitMap;
            if (this.threadPoolExecutor == null) {
                this.threadPoolExecutor = PriorityThreadPoolExecutor.create();
            }
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper());
            }
            if (this.logger == null) {
                if (this.debuggable) {
                    this.logger = new DefaultLogger(3);
                } else {
                    this.logger = new DefaultLogger();
                }
            }
            if (this.appIdentifier == null) {
                this.appIdentifier = this.context.getPackageName();
            }
            if (this.initializationCallback == null) {
                this.initializationCallback = InitializationCallback.EMPTY;
            }
            if (this.kits == null) {
                kitMap = new HashMap<>();
            } else {
                kitMap = Fabric.getKitMap(Arrays.asList(this.kits));
            }
            return new Fabric(this.context, kitMap, this.threadPoolExecutor, this.handler, this.logger, this.debuggable, this.initializationCallback, new IdManager(this.context, this.appIdentifier, this.appInstallIdentifier, kitMap.values()));
        }
    }

    static Fabric singleton() {
        if (singleton != null) {
            return singleton;
        }
        throw new IllegalStateException("Must Initialize Fabric before using singleton()");
    }

    Fabric(Context context2, Map<Class<? extends Kit>, Kit> kits2, PriorityThreadPoolExecutor threadPoolExecutor, Handler mainHandler2, Logger logger2, boolean debuggable2, InitializationCallback callback, IdManager idManager2) {
        this.context = context2;
        this.kits = kits2;
        this.executorService = threadPoolExecutor;
        this.mainHandler = mainHandler2;
        this.logger = logger2;
        this.debuggable = debuggable2;
        this.initializationCallback = callback;
        this.kitInitializationCallback = createKitInitializationCallback(kits2.size());
        this.idManager = idManager2;
    }

    public static Fabric with(Context context2, Kit... kits2) {
        if (singleton == null) {
            synchronized (Fabric.class) {
                if (singleton == null) {
                    setFabric(new Builder(context2).kits(kits2).build());
                }
            }
        }
        return singleton;
    }

    public static Fabric with(Fabric fabric) {
        if (singleton == null) {
            synchronized (Fabric.class) {
                if (singleton == null) {
                    setFabric(fabric);
                }
            }
        }
        return singleton;
    }

    private static void setFabric(Fabric fabric) {
        singleton = fabric;
        fabric.init();
    }

    public Fabric setCurrentActivity(Activity activity2) {
        this.activity = new WeakReference<>(activity2);
        return this;
    }

    public Activity getCurrentActivity() {
        if (this.activity != null) {
            return (Activity) this.activity.get();
        }
        return null;
    }

    private void init() {
        setCurrentActivity(extractActivity(this.context));
        this.activityLifecycleManager = new ActivityLifecycleManager(this.context);
        this.activityLifecycleManager.registerCallbacks(new ActivityLifecycleManager.Callbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Fabric.this.setCurrentActivity(activity);
            }

            public void onActivityStarted(Activity activity) {
                Fabric.this.setCurrentActivity(activity);
            }

            public void onActivityResumed(Activity activity) {
                Fabric.this.setCurrentActivity(activity);
            }
        });
        initializeKits(this.context);
    }

    public String getVersion() {
        return "1.3.12.127";
    }

    public String getIdentifier() {
        return "io.fabric.sdk.android:fabric";
    }

    /* access modifiers changed from: package-private */
    public void initializeKits(Context context2) {
        StringBuilder initInfo;
        Future<Map<String, KitInfo>> installedKitsFuture = getKitsFinderFuture(context2);
        Collection<Kit> providedKits = getKits();
        Onboarding onboarding = new Onboarding(installedKitsFuture, providedKits);
        List<Kit> kits2 = new ArrayList<>(providedKits);
        Collections.sort(kits2);
        onboarding.injectParameters(context2, this, InitializationCallback.EMPTY, this.idManager);
        for (Kit kit : kits2) {
            kit.injectParameters(context2, this, this.kitInitializationCallback, this.idManager);
        }
        onboarding.initialize();
        if (getLogger().isLoggable(TAG, 3)) {
            initInfo = new StringBuilder("Initializing ").append(getIdentifier()).append(" [Version: ").append(getVersion()).append("], with the following kits:\n");
        } else {
            initInfo = null;
        }
        for (Kit kit2 : kits2) {
            kit2.initializationTask.addDependency((Task) onboarding.initializationTask);
            addAnnotatedDependencies(this.kits, kit2);
            kit2.initialize();
            if (initInfo != null) {
                initInfo.append(kit2.getIdentifier()).append(" [Version: ").append(kit2.getVersion()).append("]\n");
            }
        }
        if (initInfo != null) {
            getLogger().d(TAG, initInfo.toString());
        }
    }

    /* access modifiers changed from: package-private */
    public void addAnnotatedDependencies(Map<Class<? extends Kit>, Kit> kits2, Kit dependentKit) {
        DependsOn dependsOn = dependentKit.dependsOnAnnotation;
        if (dependsOn != null) {
            for (Class<?> dependency : dependsOn.value()) {
                if (dependency.isInterface()) {
                    for (Kit kit : kits2.values()) {
                        if (dependency.isAssignableFrom(kit.getClass())) {
                            dependentKit.initializationTask.addDependency((Task) kit.initializationTask);
                        }
                    }
                } else if (kits2.get(dependency) == null) {
                    throw new UnmetDependencyException("Referenced Kit was null, does the kit exist?");
                } else {
                    dependentKit.initializationTask.addDependency((Task) kits2.get(dependency).initializationTask);
                }
            }
        }
    }

    private Activity extractActivity(Context context2) {
        if (context2 instanceof Activity) {
            return (Activity) context2;
        }
        return null;
    }

    public ActivityLifecycleManager getActivityLifecycleManager() {
        return this.activityLifecycleManager;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public Collection<Kit> getKits() {
        return this.kits.values();
    }

    public static <T extends Kit> T getKit(Class<T> cls) {
        return (Kit) singleton().kits.get(cls);
    }

    public static Logger getLogger() {
        if (singleton == null) {
            return DEFAULT_LOGGER;
        }
        return singleton.logger;
    }

    public static boolean isDebuggable() {
        if (singleton == null) {
            return false;
        }
        return singleton.debuggable;
    }

    public static boolean isInitialized() {
        return singleton != null && singleton.initialized.get();
    }

    public String getAppIdentifier() {
        return this.idManager.getAppIdentifier();
    }

    public String getAppInstallIdentifier() {
        return this.idManager.getAppInstallIdentifier();
    }

    /* access modifiers changed from: private */
    public static Map<Class<? extends Kit>, Kit> getKitMap(Collection<? extends Kit> kits2) {
        HashMap<Class<? extends Kit>, Kit> map = new HashMap<>(kits2.size());
        addToKitMap(map, kits2);
        return map;
    }

    private static void addToKitMap(Map<Class<? extends Kit>, Kit> map, Collection<? extends Kit> kits2) {
        for (Kit kit : kits2) {
            map.put(kit.getClass(), kit);
            if (kit instanceof KitGroup) {
                addToKitMap(map, ((KitGroup) kit).getKits());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public InitializationCallback<?> createKitInitializationCallback(final int size) {
        return new InitializationCallback() {
            final CountDownLatch kitInitializedLatch = new CountDownLatch(size);

            public void success(Object o) {
                this.kitInitializedLatch.countDown();
                if (this.kitInitializedLatch.getCount() == 0) {
                    Fabric.this.initialized.set(true);
                    Fabric.this.initializationCallback.success(Fabric.this);
                }
            }

            public void failure(Exception exception) {
                Fabric.this.initializationCallback.failure(exception);
            }
        };
    }

    /* access modifiers changed from: package-private */
    public Future<Map<String, KitInfo>> getKitsFinderFuture(Context context2) {
        return getExecutorService().submit(new FabricKitsFinder(context2.getPackageCodePath()));
    }
}
