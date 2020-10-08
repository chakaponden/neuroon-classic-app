package com.crashlytics.android.answers;

import android.content.Context;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.IdManager;
import java.util.Map;
import java.util.UUID;

class SessionMetadataCollector {
    private final Context context;
    private final IdManager idManager;
    private final String versionCode;
    private final String versionName;

    public SessionMetadataCollector(Context context2, IdManager idManager2, String versionCode2, String versionName2) {
        this.context = context2;
        this.idManager = idManager2;
        this.versionCode = versionCode2;
        this.versionName = versionName2;
    }

    public SessionEventMetadata getMetadata() {
        Map<IdManager.DeviceIdentifierType, String> deviceIdentifiers = this.idManager.getDeviceIdentifiers();
        String buildId = CommonUtils.resolveBuildId(this.context);
        String osVersion = this.idManager.getOsVersionString();
        String deviceModel = this.idManager.getModelName();
        return new SessionEventMetadata(this.idManager.getAppIdentifier(), UUID.randomUUID().toString(), this.idManager.getAppInstallIdentifier(), deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ID), deviceIdentifiers.get(IdManager.DeviceIdentifierType.ANDROID_ADVERTISING_ID), this.idManager.isLimitAdTrackingEnabled(), deviceIdentifiers.get(IdManager.DeviceIdentifierType.FONT_TOKEN), buildId, osVersion, deviceModel, this.versionCode, this.versionName);
    }
}
