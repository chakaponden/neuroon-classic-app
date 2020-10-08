package com.raizlabs.android.dbflow.annotation.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentUri {

    public static class ContentType {
        public static final String VND_MULTIPLE = "vnd.android.cursor.dir/";
        public static final String VND_SINGLE = "vnd.android.cursor.item/";
    }

    public @interface PathSegment {
        String column();

        int segment();
    }

    boolean deleteEnabled() default true;

    boolean insertEnabled() default true;

    String path();

    boolean queryEnabled() default true;

    PathSegment[] segments() default {};

    String type();

    boolean updateEnabled() default true;
}
