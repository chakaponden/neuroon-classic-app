package com.inteliclinic.neuroon.views;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class AnnotationContainer {
    public static final int THEME_JET_LAG = 1;
    public static final int THEME_NORMAL = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {
    }

    private AnnotationContainer() {
    }
}
