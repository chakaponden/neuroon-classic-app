package com.raizlabs.android.dbflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Database {
    boolean backupEnabled() default false;

    boolean consistencyCheckEnabled() default false;

    boolean foreignKeysSupported() default false;

    String generatedClassSeparator() default "_";

    boolean inMemory() default false;

    ConflictAction insertConflict() default ConflictAction.NONE;

    String name() default "";

    ConflictAction updateConflict() default ConflictAction.NONE;

    int version();
}
