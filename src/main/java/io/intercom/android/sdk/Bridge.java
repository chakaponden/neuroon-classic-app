package io.intercom.android.sdk;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import io.intercom.android.sdk.api.Api;
import io.intercom.android.sdk.api.DeDuper;
import io.intercom.android.sdk.api.MainThreadBus;
import io.intercom.android.sdk.api.PollingManager;
import io.intercom.android.sdk.identity.IdentityStore;
import io.intercom.android.sdk.lifecycles.Lifecycles;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.metrics.MetricsStore;
import io.intercom.android.sdk.preview.PreviewDisplayManager;
import io.intercom.android.sdk.preview.PreviewManager;
import io.intercom.com.squareup.otto.Bus;
import io.intercom.com.squareup.otto.ThreadEnforcer;

@TargetApi(14)
public class Bridge {
    private static Bridge instance;
    private final Api api;
    private final Context appContext;
    private final MainThreadBus bus = new MainThreadBus(ThreadEnforcer.ANY);
    private final IdentityStore identityStore = new IdentityStore(this.appContext);
    private final MetricsStore metricsStore = new MetricsStore(this.appContext);
    private final NexusWrapper nexusClient = new NexusWrapper();
    private final PollingManager poller = new PollingManager();
    private final PreviewManager previewManager;
    private final DeDuper superDeDuper = new DeDuper();

    Bridge(Application application, String apiKey, String appId) {
        boolean z;
        this.appContext = application.getApplicationContext();
        Context context = this.appContext;
        if (!this.identityStore.hasIntercomid()) {
            z = true;
        } else {
            z = false;
        }
        this.api = new Api(context, appId, z);
        application.registerActivityLifecycleCallbacks(new Lifecycles(this.api, this.superDeDuper));
        this.previewManager = new PreviewManager(application, this.bus, this.poller, new PreviewDisplayManager());
        this.nexusClient.setLoggingEnabled(false);
        this.identityStore.setAppIdentity(apiKey, appId);
    }

    public static synchronized void init(Application application) {
        synchronized (Bridge.class) {
            if (instance == null) {
                IntercomLogger.INTERNAL("bridge", "initialising bridge");
                IdentityStore idStore = new IdentityStore(application);
                initWithAppCredentials(application, idStore.getApiKey(), idStore.getAppId());
            }
        }
    }

    public static synchronized void initWithAppCredentials(Application application, String apiKey, String appId) {
        synchronized (Bridge.class) {
            if (instance == null) {
                IntercomLogger.INTERNAL("bridge", "initialising bridge");
                instance = new Bridge(application, apiKey, appId);
            }
        }
    }

    public static Api getApi() {
        return instance.api();
    }

    public static Bus getBus() {
        return instance.bus();
    }

    public static PollingManager getPoller() {
        return instance.poller();
    }

    public static IdentityStore getIdentityStore() {
        return instance.identityStore();
    }

    public static MetricsStore getMetricsStore() {
        return instance.metricsStore();
    }

    public static NexusWrapper getNexusClient() {
        return instance.nexusClient();
    }

    public static Context getContext() {
        return instance.context();
    }

    public static PreviewManager getPreviewManager() {
        return instance.previewManager();
    }

    public static DeDuper getDeDuper() {
        return instance.deDuper();
    }

    static void setBridge(Bridge bridge) {
        instance = bridge;
    }

    public Api api() {
        this.api.configureRequestSynchronicity(this.appContext, this.identityStore.hasIntercomid());
        return this.api;
    }

    public Bus bus() {
        return this.bus;
    }

    public PollingManager poller() {
        return this.poller;
    }

    public IdentityStore identityStore() {
        return this.identityStore;
    }

    public MetricsStore metricsStore() {
        return this.metricsStore;
    }

    public NexusWrapper nexusClient() {
        return this.nexusClient;
    }

    public Context context() {
        return this.appContext;
    }

    public PreviewManager previewManager() {
        return this.previewManager;
    }

    public DeDuper deDuper() {
        return this.superDeDuper;
    }
}
