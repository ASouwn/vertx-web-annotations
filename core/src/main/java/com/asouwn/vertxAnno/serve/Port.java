package com.asouwn.vertxAnno.serve;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Port {
    int value() default 8080;
}
