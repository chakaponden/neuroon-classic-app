package com.inteliclinic.lucid;

import java.util.Map;

public interface ILucidConfiguration {
    Map<String, Object> getAppMap();

    Map<String, Object> getRslMap();

    Map<String, Object> getUserMap();
}
