package io.fabric.sdk.android.services.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class IdManager {
    private static final String BAD_ANDROID_ID = "9774d56d682e549c";
    public static final String COLLECT_DEVICE_IDENTIFIERS = "com.crashlytics.CollectDeviceIdentifiers";
    public static final String COLLECT_USER_IDENTIFIERS = "com.crashlytics.CollectUserIdentifiers";
    public static final String DEFAULT_VERSION_NAME = "0.0";
    private static final String FORWARD_SLASH_REGEX = Pattern.quote(Condition.Operation.DIVISION);
    private static final Pattern ID_PATTERN = Pattern.compile("[^\\p{Alnum}]");
    private static final String PREFKEY_INSTALLATION_UUID = "crashlytics.installation.id";
    AdvertisingInfo advertisingInfo;
    AdvertisingInfoProvider advertisingInfoProvider;
    private final Context appContext;
    private final String appIdentifier;
    private final String appInstallIdentifier;
    private final boolean collectHardwareIds;
    private final boolean collectUserIds;
    boolean fetchedAdvertisingInfo;
    private final ReentrantLock installationIdLock = new ReentrantLock();
    private final InstallerPackageNameProvider installerPackageNameProvider;
    private final Collection<Kit> kits;

    public enum DeviceIdentifierType {
        WIFI_MAC_ADDRESS(1),
        BLUETOOTH_MAC_ADDRESS(2),
        FONT_TOKEN(53),
        ANDROID_ID(100),
        ANDROID_DEVICE_ID(101),
        ANDROID_SERIAL(102),
        ANDROID_ADVERTISING_ID(103);
        
        public final int protobufIndex;

        private DeviceIdentifierType(int pbufIndex) {
            this.protobufIndex = pbufIndex;
        }
    }

    public IdManager(Context appContext2, String appIdentifier2, String appInstallIdentifier2, Collection<Kit> kits2) {
        if (appContext2 == null) {
            throw new IllegalArgumentException("appContext must not be null");
        } else if (appIdentifier2 == null) {
            throw new IllegalArgumentException("appIdentifier must not be null");
        } else if (kits2 == null) {
            throw new IllegalArgumentException("kits must not be null");
        } else {
            this.appContext = appContext2;
            this.appIdentifier = appIdentifier2;
            this.appInstallIdentifier = appInstallIdentifier2;
            this.kits = kits2;
            this.installerPackageNameProvider = new InstallerPackageNameProvider();
            this.advertisingInfoProvider = new AdvertisingInfoProvider(appContext2);
            this.collectHardwareIds = CommonUtils.getBooleanResourceValue(appContext2, COLLECT_DEVICE_IDENTIFIERS, true);
            if (!this.collectHardwareIds) {
                Fabric.getLogger().d(Fabric.TAG, "Device ID collection disabled for " + appContext2.getPackageName());
            }
            this.collectUserIds = CommonUtils.getBooleanResourceValue(appContext2, COLLECT_USER_IDENTIFIERS, true);
            if (!this.collectUserIds) {
                Fabric.getLogger().d(Fabric.TAG, "User information collection disabled for " + appContext2.getPackageName());
            }
        }
    }

    @Deprecated
    public String createIdHeaderValue(String apiKey, String packageName) {
        return "";
    }

    public boolean canCollectUserIds() {
        return this.collectUserIds;
    }

    private String formatId(String id) {
        if (id == null) {
            return null;
        }
        return ID_PATTERN.matcher(id).replaceAll("").toLowerCase(Locale.US);
    }

    public String getAppInstallIdentifier() {
        String appInstallId = this.appInstallIdentifier;
        if (appInstallId != null) {
            return appInstallId;
        }
        SharedPreferences prefs = CommonUtils.getSharedPrefs(this.appContext);
        String appInstallId2 = prefs.getString(PREFKEY_INSTALLATION_UUID, (String) null);
        if (appInstallId2 == null) {
            return createInstallationUUID(prefs);
        }
        return appInstallId2;
    }

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public String getOsVersionString() {
        return getOsDisplayVersionString() + Condition.Operation.DIVISION + getOsBuildVersionString();
    }

    public String getOsDisplayVersionString() {
        return removeForwardSlashesIn(Build.VERSION.RELEASE);
    }

    public String getOsBuildVersionString() {
        return removeForwardSlashesIn(Build.VERSION.INCREMENTAL);
    }

    public String getModelName() {
        return String.format(Locale.US, "%s/%s", new Object[]{removeForwardSlashesIn(Build.MANUFACTURER), removeForwardSlashesIn(Build.MODEL)});
    }

    private String removeForwardSlashesIn(String s) {
        return s.replaceAll(FORWARD_SLASH_REGEX, "");
    }

    public String getDeviceUUID() {
        if (!this.collectHardwareIds) {
            return "";
        }
        String toReturn = getAndroidId();
        if (toReturn != null) {
            return toReturn;
        }
        SharedPreferences prefs = CommonUtils.getSharedPrefs(this.appContext);
        String toReturn2 = prefs.getString(PREFKEY_INSTALLATION_UUID, (String) null);
        if (toReturn2 == null) {
            return createInstallationUUID(prefs);
        }
        return toReturn2;
    }

    private String createInstallationUUID(SharedPreferences prefs) {
        this.installationIdLock.lock();
        try {
            String uuid = prefs.getString(PREFKEY_INSTALLATION_UUID, (String) null);
            if (uuid == null) {
                uuid = formatId(UUID.randomUUID().toString());
                prefs.edit().putString(PREFKEY_INSTALLATION_UUID, uuid).commit();
            }
            return uuid;
        } finally {
            this.installationIdLock.unlock();
        }
    }

    public Map<DeviceIdentifierType, String> getDeviceIdentifiers() {
        Map<DeviceIdentifierType, String> ids = new HashMap<>();
        for (Kit kit : this.kits) {
            if (kit instanceof DeviceIdentifierProvider) {
                for (Map.Entry<DeviceIdentifierType, String> entry : ((DeviceIdentifierProvider) kit).getDeviceIdentifiers().entrySet()) {
                    putNonNullIdInto(ids, entry.getKey(), entry.getValue());
                }
            }
        }
        putNonNullIdInto(ids, DeviceIdentifierType.ANDROID_ID, getAndroidId());
        putNonNullIdInto(ids, DeviceIdentifierType.ANDROID_ADVERTISING_ID, getAdvertisingId());
        return Collections.unmodifiableMap(ids);
    }

    public String getInstallerPackageName() {
        return this.installerPackageNameProvider.getInstallerPackageName(this.appContext);
    }

    /* access modifiers changed from: package-private */
    public synchronized AdvertisingInfo getAdvertisingInfo() {
        if (!this.fetchedAdvertisingInfo) {
            this.advertisingInfo = this.advertisingInfoProvider.getAdvertisingInfo();
            this.fetchedAdvertisingInfo = true;
        }
        return this.advertisingInfo;
    }

    public Boolean isLimitAdTrackingEnabled() {
        AdvertisingInfo advertisingInfo2;
        if (!this.collectHardwareIds || (advertisingInfo2 = getAdvertisingInfo()) == null) {
            return null;
        }
        return Boolean.valueOf(advertisingInfo2.limitAdTrackingEnabled);
    }

    public String getAdvertisingId() {
        AdvertisingInfo advertisingInfo2;
        if (!this.collectHardwareIds || (advertisingInfo2 = getAdvertisingInfo()) == null) {
            return null;
        }
        return advertisingInfo2.advertisingId;
    }

    private void putNonNullIdInto(Map<DeviceIdentifierType, String> idMap, DeviceIdentifierType idKey, String idValue) {
        if (idValue != null) {
            idMap.put(idKey, idValue);
        }
    }

    public String getAndroidId() {
        if (!this.collectHardwareIds) {
            return null;
        }
        String androidId = Settings.Secure.getString(this.appContext.getContentResolver(), "android_id");
        if (!BAD_ANDROID_ID.equals(androidId)) {
            return formatId(androidId);
        }
        return null;
    }

    @Deprecated
    public String getTelephonyId() {
        return null;
    }

    @Deprecated
    public String getWifiMacAddress() {
        return null;
    }

    @Deprecated
    public String getBluetoothMacAddress() {
        return null;
    }

    @Deprecated
    public String getSerialNumber() {
        return null;
    }
}
