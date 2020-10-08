package com.crashlytics.android.beta;

import android.annotation.SuppressLint;
import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.settings.BetaSettingsData;
import java.util.concurrent.atomic.AtomicBoolean;

abstract class AbstractCheckForUpdatesController implements UpdatesController {
    static final long LAST_UPDATE_CHECK_DEFAULT = 0;
    static final String LAST_UPDATE_CHECK_KEY = "last_update_check";
    private static final long MILLIS_PER_SECOND = 1000;
    private Beta beta;
    private BetaSettingsData betaSettings;
    private BuildProperties buildProps;
    private Context context;
    private CurrentTimeProvider currentTimeProvider;
    private final AtomicBoolean externallyReady;
    private HttpRequestFactory httpRequestFactory;
    private IdManager idManager;
    private final AtomicBoolean initialized;
    private long lastCheckTimeMillis;
    private PreferenceStore preferenceStore;

    public AbstractCheckForUpdatesController() {
        this(false);
    }

    public AbstractCheckForUpdatesController(boolean externallyReady2) {
        this.initialized = new AtomicBoolean();
        this.lastCheckTimeMillis = LAST_UPDATE_CHECK_DEFAULT;
        this.externallyReady = new AtomicBoolean(externallyReady2);
    }

    public void initialize(Context context2, Beta beta2, IdManager idManager2, BetaSettingsData betaSettings2, BuildProperties buildProps2, PreferenceStore preferenceStore2, CurrentTimeProvider currentTimeProvider2, HttpRequestFactory httpRequestFactory2) {
        this.context = context2;
        this.beta = beta2;
        this.idManager = idManager2;
        this.betaSettings = betaSettings2;
        this.buildProps = buildProps2;
        this.preferenceStore = preferenceStore2;
        this.currentTimeProvider = currentTimeProvider2;
        this.httpRequestFactory = httpRequestFactory2;
        if (signalInitialized()) {
            checkForUpdates();
        }
    }

    /* access modifiers changed from: protected */
    public boolean signalExternallyReady() {
        this.externallyReady.set(true);
        return this.initialized.get();
    }

    /* access modifiers changed from: package-private */
    public boolean signalInitialized() {
        this.initialized.set(true);
        return this.externallyReady.get();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"CommitPrefEdits"})
    public void checkForUpdates() {
        synchronized (this.preferenceStore) {
            if (this.preferenceStore.get().contains(LAST_UPDATE_CHECK_KEY)) {
                this.preferenceStore.save(this.preferenceStore.edit().remove(LAST_UPDATE_CHECK_KEY));
            }
        }
        long currentTimeMillis = this.currentTimeProvider.getCurrentTimeMillis();
        long updateCheckDelayMillis = ((long) this.betaSettings.updateSuspendDurationSeconds) * MILLIS_PER_SECOND;
        Fabric.getLogger().d(Beta.TAG, "Check for updates delay: " + updateCheckDelayMillis);
        Fabric.getLogger().d(Beta.TAG, "Check for updates last check time: " + getLastCheckTimeMillis());
        long nextCheckTimeMillis = getLastCheckTimeMillis() + updateCheckDelayMillis;
        Fabric.getLogger().d(Beta.TAG, "Check for updates current time: " + currentTimeMillis + ", next check time: " + nextCheckTimeMillis);
        if (currentTimeMillis >= nextCheckTimeMillis) {
            try {
                performUpdateCheck();
            } finally {
                setLastCheckTimeMillis(currentTimeMillis);
            }
        } else {
            Fabric.getLogger().d(Beta.TAG, "Check for updates next check time was not passed");
        }
    }

    private void performUpdateCheck() {
        Fabric.getLogger().d(Beta.TAG, "Performing update check");
        new CheckForUpdatesRequest(this.beta, this.beta.getOverridenSpiEndpoint(), this.betaSettings.updateUrl, this.httpRequestFactory, new CheckForUpdatesResponseTransform()).invoke(new ApiKey().getValue(this.context), this.idManager.getDeviceIdentifiers().get(IdManager.DeviceIdentifierType.FONT_TOKEN), this.buildProps);
    }

    /* access modifiers changed from: package-private */
    public void setLastCheckTimeMillis(long time) {
        this.lastCheckTimeMillis = time;
    }

    /* access modifiers changed from: package-private */
    public long getLastCheckTimeMillis() {
        return this.lastCheckTimeMillis;
    }
}
