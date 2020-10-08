package io.fabric.sdk.android.services.settings;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.DeliveryMechanism;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Settings {
    public static final String SETTINGS_CACHE_FILENAME = "com.crashlytics.settings.json";
    private static final String SETTINGS_URL_FORMAT = "https://settings.crashlytics.com/spi/v2/platforms/android/apps/%s/settings";
    private boolean initialized;
    private SettingsController settingsController;
    private final AtomicReference<SettingsData> settingsData;
    private final CountDownLatch settingsDataLatch;

    public interface SettingsAccess<T> {
        T usingSettings(SettingsData settingsData);
    }

    static class LazyHolder {
        /* access modifiers changed from: private */
        public static final Settings INSTANCE = new Settings();

        LazyHolder() {
        }
    }

    public static Settings getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Settings() {
        this.settingsData = new AtomicReference<>();
        this.settingsDataLatch = new CountDownLatch(1);
        this.initialized = false;
    }

    /* Debug info: failed to restart local var, previous not found, register: 30 */
    public synchronized Settings initialize(Kit kit, IdManager idManager, HttpRequestFactory httpRequestFactory, String versionCode, String versionName, String urlEndpoint) {
        Settings settings;
        if (this.initialized) {
            settings = this;
        } else {
            if (this.settingsController == null) {
                Context context = kit.getContext();
                String appIdentifier = idManager.getAppIdentifier();
                String apiKey = new ApiKey().getValue(context);
                String installerPackageName = idManager.getInstallerPackageName();
                CurrentTimeProvider currentTimeProvider = new SystemCurrentTimeProvider();
                SettingsJsonTransform settingsJsonTransform = new DefaultSettingsJsonTransform();
                DefaultCachedSettingsIo defaultCachedSettingsIo = new DefaultCachedSettingsIo(kit);
                String iconHash = CommonUtils.getAppIconHashOrNull(context);
                this.settingsController = new DefaultSettingsController(kit, new SettingsRequest(apiKey, idManager.getModelName(), idManager.getOsBuildVersionString(), idManager.getOsDisplayVersionString(), idManager.getAdvertisingId(), idManager.getAppInstallIdentifier(), idManager.getAndroidId(), CommonUtils.createInstanceIdFrom(CommonUtils.resolveBuildId(context)), versionName, versionCode, DeliveryMechanism.determineFrom(installerPackageName).getId(), iconHash), currentTimeProvider, settingsJsonTransform, defaultCachedSettingsIo, new DefaultSettingsSpiCall(kit, urlEndpoint, String.format(Locale.US, SETTINGS_URL_FORMAT, new Object[]{appIdentifier}), httpRequestFactory));
            }
            this.initialized = true;
            settings = this;
        }
        return settings;
    }

    public void clearSettings() {
        this.settingsData.set((Object) null);
    }

    public void setSettingsController(SettingsController settingsController2) {
        this.settingsController = settingsController2;
    }

    public <T> T withSettings(SettingsAccess<T> access, T defaultValue) {
        SettingsData settingsData2 = this.settingsData.get();
        return settingsData2 == null ? defaultValue : access.usingSettings(settingsData2);
    }

    public SettingsData awaitSettingsData() {
        try {
            this.settingsDataLatch.await();
            return this.settingsData.get();
        } catch (InterruptedException e) {
            Fabric.getLogger().e(Fabric.TAG, "Interrupted while waiting for settings data.");
            return null;
        }
    }

    public synchronized boolean loadSettingsData() {
        SettingsData settingsData2;
        settingsData2 = this.settingsController.loadSettingsData();
        setSettingsData(settingsData2);
        return settingsData2 != null;
    }

    public synchronized boolean loadSettingsSkippingCache() {
        SettingsData settingsData2;
        settingsData2 = this.settingsController.loadSettingsData(SettingsCacheBehavior.SKIP_CACHE_LOOKUP);
        setSettingsData(settingsData2);
        if (settingsData2 == null) {
            Fabric.getLogger().e(Fabric.TAG, "Failed to force reload of settings from Crashlytics.", (Throwable) null);
        }
        return settingsData2 != null;
    }

    private void setSettingsData(SettingsData settingsData2) {
        this.settingsData.set(settingsData2);
        this.settingsDataLatch.countDown();
    }
}
