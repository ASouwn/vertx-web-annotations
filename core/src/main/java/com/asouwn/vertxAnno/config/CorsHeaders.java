package com.asouwn.vertxAnno.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
public @interface CorsHeaders {
    String[] value() default {};
}
