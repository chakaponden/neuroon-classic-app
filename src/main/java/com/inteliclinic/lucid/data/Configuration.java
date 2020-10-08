package com.inteliclinic.lucid.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class Configuration {
    private LinkedHashMap<String, Object> configuration;
    private int version;

    public Configuration() {
    }

    public Configuration(Map<String, Object> configuration2) {
        if (configuration2 instanceof LinkedHashMap) {
            this.configuration = (LinkedHashMap) configuration2;
        } else {
            this.configuration = new LinkedHashMap<>(configuration2);
        }
    }

    public Configuration(Map<String, Object> configuration2, int version2) {
        this(configuration2);
        this.version = version2;
    }

    public Map<String, Object> getConfiguration() {
        return this.configuration;
    }

    public int getVersion() {
        return this.version;
    }
}
