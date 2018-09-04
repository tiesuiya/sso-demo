package com.demo.crm.annotation;

import java.lang.annotation.*;

/**
 * 自定义安全注解
 *
 * @Author: lihong
 * @Date: 2018/9/4
 * @Description
 */
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
    String value() default "";
}