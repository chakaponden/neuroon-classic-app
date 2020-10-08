package com.crashlytics.android.ndk;

import android.content.Context;
import android.content.pm.PackageManager;
import com.crashlytics.android.core.internal.models.BinaryImageData;
import com.crashlytics.android.core.internal.models.CustomAttributeData;
import com.crashlytics.android.core.internal.models.DeviceData;
import com.crashlytics.android.core.internal.models.SessionEventData;
import com.crashlytics.android.core.internal.models.SignalData;
import com.crashlytics.android.core.internal.models.ThreadData;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonCrashDataParser {
    private static final String CUSTOM_KEY_JSON_SESSION = "json_session";
    private static final String DATA_DIR = "/data";
    private static final BinaryImageData[] EMPTY_BINARY_IMAGES = new BinaryImageData[0];
    private static final ThreadData.FrameData[] EMPTY_FRAMES = new ThreadData.FrameData[0];
    private static final ThreadData[] EMPTY_THREADS = new ThreadData[0];
    static final String KEY_AVAILABLE_INTERNAL_STORAGE = "available_internal_storage";
    static final String KEY_AVAILABLE_PHYSICAL_MEMORY = "available_physical_memory";
    static final String KEY_BATTERY_CAPACITY = "battery";
    static final String KEY_BATTERY_VELOCITY = "battery_velocity";
    static final String KEY_CRASHED = "crashed";
    static final String KEY_FRAMES = "frames";
    static final String KEY_MAPS = "maps";
    static final String KEY_ORIENTATION = "orientation";
    static final String KEY_PC = "pc";
    static final String KEY_PROXIMITY_ENABLED = "proximity_enabled";
    static final String KEY_SIG_CODE = "sig_code";
    static final String KEY_SIG_NAME = "sig_name";
    static final String KEY_SI_ADDR = "si_addr";
    static final String KEY_SYMBOL = "symbol";
    static final String KEY_THREADS = "threads";
    static final String KEY_THREAD_NAME = "name";
    static final String KEY_TIME = "time";
    static final String KEY_TOTAL_INTERNAL_STORAGE = "total_internal_storage";
    static final String KEY_TOTAL_PHYSICAL_MEMORY = "total_physical_memory";
    private static final String TAG = "JsonCrashDataParser";
    private final FileIdStrategy fileIdStrategy;

    public JsonCrashDataParser() {
        this(new Sha1FileIdStrategy());
    }

    JsonCrashDataParser(FileIdStrategy fileIdStrategy2) {
        this.fileIdStrategy = fileIdStrategy2;
    }

    public SessionEventData parseCrashEventData(String jsonString) throws JSONException {
        JSONObject jsonCrash = new JSONObject(jsonString);
        return new SessionEventData(jsonCrash.optLong(KEY_TIME), parseSignalData(jsonCrash), parseThreadData(jsonCrash), parseBinaryImageData(jsonCrash), parseCustomAttributes(jsonString), parseDeviceData(jsonCrash));
    }

    public DeviceData parseDeviceData(JSONObject jsonCrash) {
        return new DeviceData(jsonCrash.optInt(KEY_ORIENTATION), jsonCrash.optLong(KEY_TOTAL_PHYSICAL_MEMORY), jsonCrash.optLong(KEY_TOTAL_INTERNAL_STORAGE), jsonCrash.optLong(KEY_AVAILABLE_PHYSICAL_MEMORY), jsonCrash.optLong(KEY_AVAILABLE_INTERNAL_STORAGE), jsonCrash.optInt(KEY_BATTERY_CAPACITY), jsonCrash.optInt(KEY_BATTERY_VELOCITY, 1), jsonCrash.optBoolean(KEY_PROXIMITY_ENABLED, false));
    }

    public SignalData parseSignalData(JSONObject jsonCrash) {
        return new SignalData(jsonCrash.optString(KEY_SIG_NAME, ""), jsonCrash.optString(KEY_SIG_CODE, ""), jsonCrash.optLong(KEY_SI_ADDR));
    }

    public BinaryImageData[] parseBinaryImageData(JSONObject jsonCrash) {
        JSONArray entries = jsonCrash.optJSONArray(KEY_MAPS);
        if (entries == null) {
            return EMPTY_BINARY_IMAGES;
        }
        List<BinaryImageData> binaryImages = new LinkedList<>();
        for (int i = 0; i < entries.length(); i++) {
            ProcMapEntry mapInfo = ProcMapEntryParser.parse(entries.optString(i));
            if (mapInfo != null) {
                String path = mapInfo.path;
                try {
                    binaryImages.add(new BinaryImageData(mapInfo.address, mapInfo.size, path, this.fileIdStrategy.getId(getLibraryFile(path))));
                } catch (IOException e) {
                    Fabric.getLogger().d(TAG, "Could not generate ID for file " + mapInfo.path, e);
                }
            }
        }
        return (BinaryImageData[]) binaryImages.toArray(new BinaryImageData[binaryImages.size()]);
    }

    public ThreadData[] parseThreadData(JSONObject jsonCrash) {
        JSONArray jsonThreads = jsonCrash.optJSONArray(KEY_THREADS);
        if (jsonThreads == null) {
            return EMPTY_THREADS;
        }
        int len = jsonThreads.length();
        ThreadData[] threads = new ThreadData[len];
        for (int i = 0; i < len; i++) {
            JSONObject jsonThread = jsonThreads.optJSONObject(i);
            String threadName = jsonThread.optString(KEY_THREAD_NAME);
            int threadImportance = jsonThread.optBoolean(KEY_CRASHED) ? 4 : 0;
            threads[i] = new ThreadData(threadName, threadImportance, parseFrameData(jsonThread, threadImportance));
        }
        return threads;
    }

    public ThreadData.FrameData[] parseFrameData(JSONObject jsonThread, int threadImportance) {
        String symbol;
        JSONArray jsonFrames = jsonThread.optJSONArray(KEY_FRAMES);
        if (jsonFrames == null) {
            return EMPTY_FRAMES;
        }
        int len = jsonFrames.length();
        ThreadData.FrameData[] frames = new ThreadData.FrameData[len];
        for (int i = 0; i < len; i++) {
            JSONObject frame = jsonFrames.optJSONObject(i);
            long pc = frame.optLong(KEY_PC);
            String maybeSymbol = frame.optString(KEY_SYMBOL);
            if (maybeSymbol == null) {
                symbol = "";
            } else {
                symbol = maybeSymbol;
            }
            frames[i] = new ThreadData.FrameData(pc, symbol, "", 0, threadImportance);
        }
        return frames;
    }

    public CustomAttributeData[] parseCustomAttributes(String json) {
        return new CustomAttributeData[]{new CustomAttributeData(CUSTOM_KEY_JSON_SESSION, json)};
    }

    private static File getLibraryFile(String path) {
        File libFile = new File(path);
        if (!libFile.exists()) {
            return correctDataPath(libFile);
        }
        return libFile;
    }

    private static File correctDataPath(File missingFile) {
        if (!missingFile.getAbsolutePath().startsWith(DATA_DIR)) {
            return missingFile;
        }
        try {
            Context context = CrashlyticsNdk.getInstance().getContext();
            return new File(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).nativeLibraryDir, missingFile.getName());
        } catch (PackageManager.NameNotFoundException e) {
            Fabric.getLogger().e(TAG, "Error getting ApplicationInfo", e);
            return missingFile;
        }
    }
}
