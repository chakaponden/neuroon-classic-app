package com.inteliclinic.lucid;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public final class LucidEnv implements ILucidEnv {
    static final String APP_SECTION = "App";
    static final String RSL_SECTION = "Rsl";
    private static final String TAG = LucidEnv.class.getSimpleName();
    static final String USER_SECTION = "User";
    private Map<String, Long> appManagersPointers;
    private long envPointer;
    private Map<String, Long> userManagersPointers;

    private static native long allocAssocPointerPointer();

    private static native long assocFromTwoToOnePointer(long j);

    private static native void bindToAssoc(long j, String str, long j2, long j3);

    private static native void cleanAssoc(long j);

    private static native void cleanEnvironment(long j);

    public static native long createBool(long j, boolean z);

    public static native long createDouble(long j, double d);

    private static native long createEmptyEnvironment();

    public static native void createIntArray(long j, int[] iArr, int i);

    public static native long createInteger(long j, int i);

    public static native long createKeyword(long j, String str);

    public static native long createLong(long j, long j2);

    public static native void createSpecial(long j, int i);

    public static native long createString(long j, String str);

    private static native void freeAssocPointerPointer(long j);

    private static native void freeEnviroment(long j);

    private static native long getAppAssoc(long j);

    private static native long getAssocHhNextPointer(long j);

    private static native long getAssocHhPrevPointer(long j);

    private static native String getAssocKey(long j);

    private static native long getAssocSexpPointer(long j);

    private static native long getLsl();

    private static native long getLucidVersion();

    private static native long getRslAssoc(long j);

    private static native long getUserAssoc(long j);

    private static native void setAssocPointer(long j, long j2);

    private static native void setEnvApp(long j, long j2);

    private static native long setEnvAppMgr(long j, long j2);

    private static native void setEnvLsl(long j, long j2);

    private static native void setEnvRsl(long j, long j2);

    private static native void setEnvUser(long j, long j2);

    private static native long setEnvUserMgr(long j, long j2);

    private LucidEnv() {
    }

    public static ILucidEnv getEnv(Map<String, Object> rslConfiguration, Map<String, Object> appConfiguration, Map<String, Object> userConfiguration) {
        LucidEnv env = new LucidEnv();
        env.parseEnvConfiguration(rslConfiguration, appConfiguration, userConfiguration);
        return env;
    }

    public void parseEnvConfiguration(Map<String, Object> rslConfiguration, Map<String, Object> appConfiguration, Map<String, Object> userConfiguration) {
        if ((this.appManagersPointers == null || this.appManagersPointers.size() <= 0) && (this.userManagersPointers == null || this.userManagersPointers.size() <= 0)) {
            if (this.envPointer == 0) {
                this.envPointer = createEmptyEnvironment();
            }
            setEnvLsl(this.envPointer, getLsl());
            parseEnvLocal(rslConfiguration, appConfiguration, userConfiguration);
            return;
        }
        throw new UnsupportedOperationException("Lucid environment should be clean when parsing environment configuration.");
    }

    public void deleteEnvironment() {
        cleanEnvironment(this.envPointer);
        cleanMgrs();
        freeEnviroment(this.envPointer);
        this.envPointer = 0;
    }

    private void cleanMgrs() {
        cleanManagers(this.appManagersPointers);
        cleanManagers(this.userManagersPointers);
    }

    private void cleanManagers(Map<String, Long> managersPointers) {
        if (managersPointers != null) {
            for (Long assocPointerPointer : managersPointers.values()) {
                cleanAssoc(assocPointerPointer.longValue());
            }
            managersPointers.clear();
        }
    }

    public long envForMgrPointer(String managerKey) {
        if (getUserManagerPointer(managerKey) == 0) {
            long userManagerPointerPointer = allocAssocPointerPointer();
            setAssocPointer(userManagerPointerPointer, 0);
            setUserManagerPointer(managerKey, userManagerPointerPointer);
        }
        setEnvUserMgr(this.envPointer, getUserManagerPointer(managerKey));
        if (getAppManagerPointer(managerKey) == 0) {
            long appManagerPointerPointer = allocAssocPointerPointer();
            setAssocPointer(appManagerPointerPointer, 0);
            setAppManagerPointer(managerKey, appManagerPointerPointer);
        }
        setEnvAppMgr(this.envPointer, getAppManagerPointer(managerKey));
        return this.envPointer;
    }

    public synchronized LucidConfiguration getLucidConfiguration() {
        return new LucidConfiguration(getAppConfiguration(), getUserConfiguration());
    }

    private Map<String, Object> getRslConfiguration() {
        return crystalizeAssoc(getRslAssoc(this.envPointer));
    }

    public synchronized Map<String, Object> getAppConfiguration() {
        Map<String, Object> appSection;
        appSection = crystalizeAssoc(getAppAssoc(this.envPointer));
        if (this.appManagersPointers != null) {
            for (Map.Entry<String, Long> appMgr : this.appManagersPointers.entrySet()) {
                appSection.put(appMgr.getKey(), crystalizeAssoc(appMgr.getValue().longValue()));
            }
        }
        return appSection;
    }

    public synchronized Map<String, Object> getUserConfiguration() {
        Map<String, Object> userSection;
        userSection = crystalizeAssoc(getUserAssoc(this.envPointer));
        if (this.userManagersPointers != null) {
            for (Map.Entry<String, Long> userMgr : this.userManagersPointers.entrySet()) {
                userSection.put(userMgr.getKey(), crystalizeAssoc(userMgr.getValue().longValue()));
            }
        }
        return userSection;
    }

    private Map<String, Object> crystalizeAssoc(long assocPointerPointer) {
        long tempAssocPointer;
        Object extractSexp;
        Map<String, Object> map = new HashMap<>();
        if (assocPointerPointer != 0) {
            long tempAssocPointer2 = assocFromTwoToOnePointer(assocPointerPointer);
            if (tempAssocPointer2 != 0) {
                for (int backwards = 0; backwards < 2; backwards++) {
                    while (tempAssocPointer != 0) {
                        long sPointer = getAssocSexpPointer(tempAssocPointer);
                        String symbolName = getAssocKey(tempAssocPointer);
                        switch (Sexp.SexpGetType(sPointer)) {
                            case 7:
                            case 8:
                            case 9:
                                extractSexp = LucidUtils.extractSexp(sPointer);
                                break;
                            default:
                                extractSexp = Sexp.SexpPrintSexp(sPointer);
                                break;
                        }
                        map.put(symbolName, extractSexp);
                        if (backwards != 0) {
                            tempAssocPointer = getAssocHhPrevPointer(tempAssocPointer);
                        } else {
                            tempAssocPointer = getAssocHhNextPointer(tempAssocPointer);
                        }
                    }
                    tempAssocPointer2 = getAssocHhPrevPointer(assocFromTwoToOnePointer(assocPointerPointer));
                }
            }
        }
        return map;
    }

    private long getAppManagerPointer(String managerKey) {
        if (this.appManagersPointers == null || !this.appManagersPointers.containsKey(managerKey)) {
            return 0;
        }
        return this.appManagersPointers.get(managerKey).longValue();
    }

    private void setAppManagerPointer(String managerKey, long managerPointer) {
        if (this.appManagersPointers == null) {
            this.appManagersPointers = new HashMap();
        }
        this.appManagersPointers.put(managerKey, Long.valueOf(managerPointer));
    }

    private long getUserManagerPointer(String managerKey) {
        if (this.userManagersPointers == null || !this.userManagersPointers.containsKey(managerKey)) {
            return 0;
        }
        return this.userManagersPointers.get(managerKey).longValue();
    }

    private void setUserManagerPointer(String managerKey, long managerPointer) {
        if (this.userManagersPointers == null) {
            this.userManagersPointers = new HashMap();
        }
        this.userManagersPointers.put(managerKey, Long.valueOf(managerPointer));
    }

    public boolean mgrPresentInEnv(String mgrName) {
        return (this.appManagersPointers != null && this.appManagersPointers.containsKey(mgrName)) || (this.userManagersPointers != null && this.userManagersPointers.containsKey(mgrName));
    }

    private void parseEnvLocal(Map<String, Object> rslMap, Map<String, Object> appMap, Map<String, Object> userMap) {
        if (rslMap != null) {
            parseEnvRsl(rslMap);
            if (appMap != null) {
                parseEnvApp(appMap);
                if (userMap != null) {
                    paserEnvUser(userMap);
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException();
        }
        throw new UnsupportedOperationException();
    }

    private void parseEnvRsl(Map<String, Object> rslMap) {
        long assocPointerPointer = parseEnvLocalNode(RSL_SECTION, rslMap);
        setEnvRsl(this.envPointer, assocPointerPointer);
        freeAssocPointerPointer(assocPointerPointer);
    }

    private void parseEnvApp(Map<String, Object> appMap) {
        long assocPointerPointer = parseEnvLocalNode(APP_SECTION, appMap);
        setEnvApp(this.envPointer, assocPointerPointer);
        freeAssocPointerPointer(assocPointerPointer);
    }

    private void paserEnvUser(Map<String, Object> userMap) {
        long assocPointerPointer = parseEnvLocalNode(USER_SECTION, userMap);
        setEnvUser(this.envPointer, assocPointerPointer);
        freeAssocPointerPointer(assocPointerPointer);
    }

    private long parseEnvLocalNode(String section, Map<String, Object> map) {
        long assocPointerPointer = allocAssocPointerPointer();
        setAssocPointer(assocPointerPointer, 0);
        for (String key : map.keySet()) {
            Object object = map.get(key);
            int ret = 0;
            long sPointerPointer = Sexp.SexpAllocPointerPointer();
            if (object instanceof Boolean) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createBool(sPointerPointer, ((Boolean) object).booleanValue());
                ret = 1;
            } else if (object instanceof Integer) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createInteger(sPointerPointer, ((Integer) object).intValue());
                ret = 1;
            } else if (object instanceof Long) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createLong(sPointerPointer, ((Long) object).longValue());
                ret = 1;
            } else if (object instanceof Double) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createDouble(sPointerPointer, ((Double) object).doubleValue());
                ret = 1;
            } else if (object instanceof String) {
                ret = LucidConfig.parseTopLevel((String) object, sPointerPointer);
            } else if (object instanceof Map) {
                if (RSL_SECTION.equals(section)) {
                    Log.w(TAG, "Rsl shouldn't contain subsections");
                } else {
                    parseMgrEnv(key, (Map) object, section);
                }
            }
            if (ret != 1) {
                Log.w(TAG, "Parser error " + ret);
            }
            bindToAssoc(assocPointerPointer, key, Sexp.SexpFromTwoToOnePointer(sPointerPointer), 0);
            Sexp.SexpFree(sPointerPointer);
        }
        return assocPointerPointer;
    }

    private void parseMgrEnv(String managerKey, Map<String, Object> map, String section) {
        long assocPointerPointer = allocAssocPointerPointer();
        setAssocPointer(assocPointerPointer, 0);
        for (String key : map.keySet()) {
            Object object = map.get(key);
            int ret = 0;
            long sPointerPointer = Sexp.SexpAllocPointerPointer();
            if (object instanceof Boolean) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createBool(sPointerPointer, ((Boolean) object).booleanValue());
                ret = 1;
            } else if (object instanceof Integer) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createInteger(sPointerPointer, ((Integer) object).intValue());
                ret = 1;
            } else if (object instanceof Long) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createLong(sPointerPointer, ((Long) object).longValue());
                ret = 1;
            } else if (object instanceof Double) {
                Sexp.SexpAllocAndCreateNilPointerPointer(sPointerPointer);
                createDouble(sPointerPointer, ((Double) object).doubleValue());
                ret = 1;
            } else if (object instanceof String) {
                ret = LucidConfig.parseTopLevel((String) object, sPointerPointer);
            } else if (object instanceof Map) {
                throw new UnsupportedOperationException("Manager shouldn't contain subsections");
            }
            if (ret == 1) {
                bindToAssoc(assocPointerPointer, key, Sexp.SexpFromTwoToOnePointer(sPointerPointer), 0);
                Sexp.SexpFree(sPointerPointer);
            }
        }
        if (APP_SECTION.equals(section)) {
            setAppManagerPointer(managerKey, assocPointerPointer);
        } else if (USER_SECTION.equals(section)) {
            setUserManagerPointer(managerKey, assocPointerPointer);
        }
    }

    public static long getVersion() {
        return getLucidVersion();
    }
}
