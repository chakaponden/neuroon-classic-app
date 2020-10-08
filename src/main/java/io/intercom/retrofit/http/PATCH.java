package io.intercom.retrofit.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@RestMethod(hasBody = true, value = "PATCH")
@Retention(RetentionPolicy.RUNTIME)
public @interface PATCH {
    String value();
}
