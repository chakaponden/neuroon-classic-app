package com.raizlabs.android.dbflow.annotation;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Column {
    Collate collate() default Collate.NONE;

    String defaultValue() default "";

    boolean excludeFromToModelMethod() default false;

    String getterName() default "";

    int length() default -1;

    String name() default "";

    String setterName() default "";

    Class<? extends TypeConverter> typeConverter() default TypeConverter.class;
}
