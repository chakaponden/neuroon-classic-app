package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class MetaDataStore {
    private static final String KEYDATA_SUFFIX = "keys";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String METADATA_EXT = ".meta";
    private static final String USERDATA_SUFFIX = "user";
    private static final Charset UTF_8 = Charset.forName(HttpRequest.CHARSET_UTF8);
    private final File filesDir;

    public MetaDataStore(File filesDir2) {
        this.filesDir = filesDir2;
    }

    public void writeUserData(String sessionId, UserMetaData data) {
        File f = getUserDataFileForSession(sessionId);
        Writer writer = null;
        try {
            String userDataString = userDataToJson(data);
            Writer writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), UTF_8));
            try {
                writer2.write(userDataString);
                writer2.flush();
                CommonUtils.closeOrLog(writer2, "Failed to close user metadata file.");
                Writer writer3 = writer2;
            } catch (Exception e) {
                e = e;
                writer = writer2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "Error serializing user metadata.", e);
                    CommonUtils.closeOrLog(writer, "Failed to close user metadata file.");
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.closeOrLog(writer, "Failed to close user metadata file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                writer = writer2;
                CommonUtils.closeOrLog(writer, "Failed to close user metadata file.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Error serializing user metadata.", e);
            CommonUtils.closeOrLog(writer, "Failed to close user metadata file.");
        }
    }

    public UserMetaData readUserData(String sessionId) {
        File f = getUserDataFileForSession(sessionId);
        if (!f.exists()) {
            return UserMetaData.EMPTY;
        }
        InputStream is = null;
        try {
            InputStream is2 = new FileInputStream(f);
            try {
                UserMetaData jsonToUserData = jsonToUserData(CommonUtils.streamToString(is2));
                CommonUtils.closeOrLog(is2, "Failed to close user metadata file.");
                return jsonToUserData;
            } catch (Exception e) {
                e = e;
                is = is2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "Error deserializing user metadata.", e);
                    CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                    return UserMetaData.EMPTY;
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Error deserializing user metadata.", e);
            CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
            return UserMetaData.EMPTY;
        }
    }

    public void writeKeyData(String sessionId, Map<String, String> keyData) {
        File f = getKeysFileForSession(sessionId);
        Writer writer = null;
        try {
            String keyDataString = keysDataToJson(keyData);
            Writer writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), UTF_8));
            try {
                writer2.write(keyDataString);
                writer2.flush();
                CommonUtils.closeOrLog(writer2, "Failed to close key/value metadata file.");
                Writer writer3 = writer2;
            } catch (Exception e) {
                e = e;
                writer = writer2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "Error serializing key/value metadata.", e);
                    CommonUtils.closeOrLog(writer, "Failed to close key/value metadata file.");
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.closeOrLog(writer, "Failed to close key/value metadata file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                writer = writer2;
                CommonUtils.closeOrLog(writer, "Failed to close key/value metadata file.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Error serializing key/value metadata.", e);
            CommonUtils.closeOrLog(writer, "Failed to close key/value metadata file.");
        }
    }

    public Map<String, String> readKeyData(String sessionId) {
        File f = getKeysFileForSession(sessionId);
        if (!f.exists()) {
            return Collections.emptyMap();
        }
        InputStream is = null;
        try {
            InputStream is2 = new FileInputStream(f);
            try {
                Map<String, String> jsonToKeysData = jsonToKeysData(CommonUtils.streamToString(is2));
                CommonUtils.closeOrLog(is2, "Failed to close user metadata file.");
                return jsonToKeysData;
            } catch (Exception e) {
                e = e;
                is = is2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "Error deserializing user metadata.", e);
                    CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                    return Collections.emptyMap();
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                is = is2;
                CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Error deserializing user metadata.", e);
            CommonUtils.closeOrLog(is, "Failed to close user metadata file.");
            return Collections.emptyMap();
        }
    }

    private File getUserDataFileForSession(String sessionId) {
        return new File(this.filesDir, sessionId + "user" + METADATA_EXT);
    }

    private File getKeysFileForSession(String sessionId) {
        return new File(this.filesDir, sessionId + KEYDATA_SUFFIX + METADATA_EXT);
    }

    private static UserMetaData jsonToUserData(String json) throws JSONException {
        JSONObject dataObj = new JSONObject(json);
        return new UserMetaData(valueOrNull(dataObj, KEY_USER_ID), valueOrNull(dataObj, KEY_USER_NAME), valueOrNull(dataObj, KEY_USER_EMAIL));
    }

    private static String userDataToJson(final UserMetaData userData) throws JSONException {
        return new JSONObject() {
            {
                put(MetaDataStore.KEY_USER_ID, userData.id);
                put(MetaDataStore.KEY_USER_NAME, userData.name);
                put(MetaDataStore.KEY_USER_EMAIL, userData.email);
            }
        }.toString();
    }

    private static Map<String, String> jsonToKeysData(String json) throws JSONException {
        JSONObject dataObj = new JSONObject(json);
        Map<String, String> keyData = new HashMap<>();
        Iterator<String> keyIter = dataObj.keys();
        while (keyIter.hasNext()) {
            String key = keyIter.next();
            keyData.put(key, valueOrNull(dataObj, key));
        }
        return keyData;
    }

    private static String keysDataToJson(Map<String, String> keyData) throws JSONException {
        return new JSONObject(keyData).toString();
    }

    private static String valueOrNull(JSONObject json, String key) {
        if (!json.isNull(key)) {
            return json.optString(key, (String) null);
        }
        return null;
    }
}
