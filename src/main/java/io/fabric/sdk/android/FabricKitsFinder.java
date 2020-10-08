package io.fabric.sdk.android;

import android.os.SystemClock;
import android.text.TextUtils;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class FabricKitsFinder implements Callable<Map<String, KitInfo>> {
    private static final String FABRIC_BUILD_TYPE_KEY = "fabric-build-type";
    static final String FABRIC_DIR = "fabric/";
    private static final String FABRIC_IDENTIFIER_KEY = "fabric-identifier";
    private static final String FABRIC_VERSION_KEY = "fabric-version";
    final String apkFileName;

    FabricKitsFinder(String apkFileName2) {
        this.apkFileName = apkFileName2;
    }

    public Map<String, KitInfo> call() throws Exception {
        KitInfo kitInfo;
        Map<String, KitInfo> kitInfos = new HashMap<>();
        long startScan = SystemClock.elapsedRealtime();
        int count = 0;
        ZipFile apkFile = loadApkFile();
        Enumeration<? extends ZipEntry> entries = apkFile.entries();
        while (entries.hasMoreElements()) {
            count++;
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.getName().startsWith(FABRIC_DIR) && entry.getName().length() > FABRIC_DIR.length() && (kitInfo = loadKitInfo(entry, apkFile)) != null) {
                kitInfos.put(kitInfo.getIdentifier(), kitInfo);
                Fabric.getLogger().v(Fabric.TAG, String.format("Found kit:[%s] version:[%s]", new Object[]{kitInfo.getIdentifier(), kitInfo.getVersion()}));
            }
        }
        if (apkFile != null) {
            try {
                apkFile.close();
            } catch (IOException e) {
            }
        }
        Fabric.getLogger().v(Fabric.TAG, "finish scanning in " + (SystemClock.elapsedRealtime() - startScan) + " reading:" + count);
        return kitInfos;
    }

    /* JADX INFO: finally extract failed */
    private KitInfo loadKitInfo(ZipEntry fabricFile, ZipFile apk) {
        InputStream inputStream = null;
        try {
            inputStream = apk.getInputStream(fabricFile);
            Properties properties = new Properties();
            properties.load(inputStream);
            String id = properties.getProperty(FABRIC_IDENTIFIER_KEY);
            String version = properties.getProperty(FABRIC_VERSION_KEY);
            String buildType = properties.getProperty(FABRIC_BUILD_TYPE_KEY);
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(version)) {
                throw new IllegalStateException("Invalid format of fabric file," + fabricFile.getName());
            }
            KitInfo kitInfo = new KitInfo(id, version, buildType);
            CommonUtils.closeQuietly(inputStream);
            return kitInfo;
        } catch (IOException ie) {
            Fabric.getLogger().e(Fabric.TAG, "Error when parsing fabric properties " + fabricFile.getName(), ie);
            CommonUtils.closeQuietly(inputStream);
            return null;
        } catch (Throwable th) {
            CommonUtils.closeQuietly(inputStream);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public ZipFile loadApkFile() throws IOException {
        return new ZipFile(this.apkFileName);
    }
}
