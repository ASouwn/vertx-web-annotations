package com.asouwn.vertxAnno.vertical;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VerticalNum {
    int value() default 1;
}
