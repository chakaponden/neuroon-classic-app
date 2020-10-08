package com.inteliclinic.lucid;

import android.content.Context;
import com.inteliclinic.lucid.events.NotifyTagEvent;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LucidConfig {
    private static final String TAG = LucidConfig.class.getSimpleName();
    private static LucidConfig mInstance;
    private final ILucidEnv mEnv;
    private Map<String, LucidManager> mManagers = new HashMap();

    public static native int evalTopLevel(long j, long j2, long j3);

    public static native int parseTopLevel(String str, long j);

    public native String stringFromJNI();

    static {
        System.loadLibrary("lucid");
    }

    private LucidConfig(ILucidEnv env) {
        this.mEnv = env;
    }

    public static void init(Context context) {
        init((ILucidConfiguration) LucidConfiguration.readConfig(context));
    }

    private static void init(ILucidConfiguration configuration) {
        if (mInstance == null) {
            mInstance = new LucidConfig(LucidEnv.getEnv(configuration.getRslMap(), configuration.getAppMap(), configuration.getUserMap()));
        }
    }

    public static <T extends LucidManager> LucidConfig getInstance(T manager) {
        if (mInstance == null) {
            throw new UnsupportedOperationException("LucidConfig should be initiated");
        } else if (mInstance.mManagers.containsKey(manager.getLucidDelegateKey())) {
            throw new UnsupportedOperationException("Error while add second instance of the same manager class");
        } else {
            mInstance.mManagers.put(manager.getLucidDelegateKey(), manager);
            return mInstance;
        }
    }

    public void saveConfig(Context context) {
        this.mEnv.getLucidConfiguration().save(context);
    }

    public Map<String, Object> getUserConfig() {
        return this.mEnv.getUserConfiguration();
    }

    public Map<String, Object> getAppConfig() {
        return this.mEnv.getAppConfiguration();
    }

    public void reloadLucid(Context context) {
        reloadLucidInt(LucidConfiguration.readConfig(context));
    }

    public void reloadLucid(ILucidConfiguration configuration) {
        reloadLucidInt(configuration);
    }

    private synchronized void reloadLucidInt(ILucidConfiguration configuration) {
        this.mEnv.deleteEnvironment();
        this.mEnv.parseEnvConfiguration(configuration.getRslMap(), configuration.getAppMap(), configuration.getUserMap());
    }

    public Object get(String key, String managerKey) {
        Object o;
        long outPointerPointer = Sexp.SexpAllocPointerPointer();
        int ret = parseTopLevel(key, outPointerPointer);
        if (1 != ret) {
            if (Sexp.isValidSexp(outPointerPointer)) {
                Sexp.SexpDeletePointer(Sexp.SexpFromTwoToOnePointer(outPointerPointer));
            }
            Sexp.SexpFree(outPointerPointer);
            handleParseError(ret, managerKey, key);
            throw new UnknownError("todo handle parse errors");
        }
        synchronized (this.mEnv) {
            o = lucidEval(Sexp.SexpFromTwoToOnePointer(outPointerPointer), this.mEnv.envForMgrPointer(managerKey));
        }
        if (Sexp.isValidSexp(outPointerPointer)) {
            Sexp.SexpDeletePointer(Sexp.SexpFromTwoToOnePointer(outPointerPointer));
        }
        Sexp.SexpFree(outPointerPointer);
        return o;
    }

    private Object lucidEval(long sPointer, long ePointer) {
        int ret;
        long evaledPointerPointer = Sexp.SexpAllocPointerPointer();
        do {
            ret = evalTopLevel(ePointer, sPointer, evaledPointerPointer);
            if (ret == 2) {
                evalExtern(evaledPointerPointer, ePointer);
                continue;
            }
        } while (ret == 2);
        Sexp.SexpFree(evaledPointerPointer);
        return ret == 1 ? LucidUtils.extractReleaseMemory(sPointer) : handleEvalError(ret);
    }

    private void evalExtern(long sPointerPointer, long ePointer) {
        long nextPointer = Sexp.SexpNextPointerPointer(sPointerPointer);
        if (Sexp.SexpCheckIfIsBroadcast(sPointerPointer)) {
            evalBroadcast(sPointerPointer, ePointer);
        } else if (Sexp.SexpCheckIfIsMgrCall(sPointerPointer)) {
            sPointerPointer = evalMgrCall(sPointerPointer, ePointer);
        }
        Sexp.SexpSetNext(sPointerPointer, nextPointer);
    }

    private long evalMgrCall(long sPointerPointer, long ePointer) {
        Object obj;
        Sexp.SexpDeletePointer(Sexp.listPop(Sexp.SexpFromTwoToOnePointer(sPointerPointer)));
        long outPointer = Sexp.listPop(Sexp.SexpFromTwoToOnePointer(sPointerPointer));
        String mgrName = (String) lucidEval(outPointer, ePointer);
        Sexp.SexpDeletePointer(outPointer);
        if (!this.mEnv.mgrPresentInEnv(mgrName)) {
            Sexp.SexpDeleteAndReuseMem(Sexp.SexpFromTwoToOnePointer(sPointerPointer));
        } else {
            long outPointer2 = Sexp.listPop(Sexp.SexpFromTwoToOnePointer(sPointerPointer));
            String methodKey = (String) lucidEval(outPointer2, ePointer);
            Sexp.SexpDeletePointer(outPointer2);
            long outPointer3 = Sexp.listPop(Sexp.SexpFromTwoToOnePointer(sPointerPointer));
            Object arguments = lucidEval(outPointer3, ePointer);
            Sexp.SexpDeletePointer(outPointer3);
            if (arguments instanceof List) {
                obj = callMgrMethod(mgrName, methodKey, (List) arguments);
            } else {
                List<Object> argList = new ArrayList<>(1);
                argList.add(arguments);
                obj = callMgrMethod(mgrName, methodKey, argList);
            }
            Sexp.SexpDeleteAndReuseMemPointerPointer(sPointerPointer);
            Sexp.pointerEqualsDoublePointer(sPointerPointer, LucidUtils.objectToSexp(obj));
        }
        return sPointerPointer;
    }

    private void evalBroadcast(long sPointerPointer, long ePointer) {
        Float tagValue;
        String tagName = (String) lucidEval(Sexp.SexpCopyNext(sPointerPointer), ePointer);
        Object o = lucidEval(Sexp.SexpCopyNextNext(sPointerPointer), ePointer);
        if (o instanceof Integer) {
            tagValue = Float.valueOf(((float) ((Integer) o).intValue()) / 1.0f);
        } else if (o instanceof Double) {
            tagValue = Float.valueOf(((Double) o).floatValue());
        } else {
            tagValue = (Float) o;
        }
        broadcastTag(tagName, tagValue);
        Sexp.SexpDeleteAndReuseMemPointerPointer(sPointerPointer);
        Sexp.SexpCreateNilPointerPointer(sPointerPointer);
    }

    private Object callMgrMethod(String mgrName, String methodKey, List<Object> args) {
        if (!this.mManagers.containsKey(mgrName)) {
            return null;
        }
        return this.mManagers.get(mgrName).methodWithLucidKey(methodKey, args);
    }

    private void broadcastTag(String tagName, Float tagValue) {
        EventBus.getDefault().post(new NotifyTagEvent(tagName, tagValue));
    }

    private Object handleEvalError(int ret) {
        return null;
    }

    private void handleParseError(int ret, String managerKey, String key) {
    }
}
