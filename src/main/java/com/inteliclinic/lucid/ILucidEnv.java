package com.inteliclinic.lucid;

import java.util.Map;

public interface ILucidEnv {
    void deleteEnvironment();

    long envForMgrPointer(String str);

    Map<String, Object> getAppConfiguration();

    LucidConfiguration getLucidConfiguration();

    Map<String, Object> getUserConfiguration();

    boolean mgrPresentInEnv(String str);

    void parseEnvConfiguration(Map<String, Object> map, Map<String, Object> map2, Map<String, Object> map3);
}
