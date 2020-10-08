package io.intercom.com.google.gson;

import java.lang.reflect.Field;

public interface FieldNamingStrategy {
    String translateName(Field field);
}
