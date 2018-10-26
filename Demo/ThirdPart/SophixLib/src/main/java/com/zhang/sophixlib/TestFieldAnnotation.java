package com.zhang.sophixlib;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by admin on 2017/8/16.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME) //运行时注解信息
@Target(ElementType.FIELD)      //只能修饰变量
public @interface TestFieldAnnotation {
    int fieldVer() default 20;
}
