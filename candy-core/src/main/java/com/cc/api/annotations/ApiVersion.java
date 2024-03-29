package com.cc.api.annotations;

import com.cc.api.enumc.ApiGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 用作swagger分组，可作用于controller类和controller方法中，可同时使用，方法上的注解优先级最大
 * @Author CandyMuj
 * @Date 2020/7/17 17:01
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    // 必填值：swagger文档分组注解-版本号
    ApiGroup.V[] value();

    // 可选值：swagger文档分组注解-所属端（空则取group枚举中配置的默认包限定名进行分组）
    ApiGroup.G[] g() default {};

}
