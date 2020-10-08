package com.raizlabs.android.dbflow.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ForeignKeyReference {
    String columnName();

    Class<?> columnType();

    String foreignKeyColumnName();

    boolean referencedFieldIsPackagePrivate() default false;

    boolean referencedFieldIsPrivate() default false;

    String referencedGetterName() default "";

    String referencedSetterName() default "";
}
