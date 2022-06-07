package com.liujiabin.common.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    //注解中的方法
    long expire() default 60 * 1000;

    String name() default "";

}