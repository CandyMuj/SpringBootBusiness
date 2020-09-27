package com.cc.pic.api.annotations;

import java.lang.annotation.*;

/**
 * @ProJectName APIServer
 * @FileName Ann
 * @Description 作用于接口上的注解，一些自定义的配置 如此接口是否需要鉴权;
 * @Author CandyMuj
 * @Date 2019/12/25 16:54
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ann {

    // 此接口是否需要登录鉴权
    boolean au() default true;

}
