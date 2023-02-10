package com.cc.api.config.sys.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Description Runnable接口实现类，被定时任务线程池调用，用来执行指定bean里面的方法。
 * @Author CandyMuj
 * @Date 2021/11/1 17:04
 * @Version 1.0
 */
@Slf4j
public class SchedulingRunnable implements Runnable {
    private final String beanName;
    private final String methodName;
    private final String params;


    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName, methodName, null);
    }

    public SchedulingRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        log.info("定时任务开始执行 - bean：{} 方法：{} 参数：{}", beanName, methodName, params);
        TimeInterval timer = DateUtil.timer();
        try {
            Object target = SpringUtil.getBean(beanName);

            Method method;
            if (StringUtils.isNotEmpty(params)) {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            log.error(StrUtil.format("定时任务执行异常 - bean：{} 方法：{} 参数：{} msg：{}", beanName, methodName, params, ex.getMessage()), ex);
        } finally {
            log.info("定时任务执行结束 - bean：{} 方法：{} 参数：{} 耗时：{} 毫秒", beanName, methodName, params, timer.interval());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }

        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }

        return Objects.hash(beanName, methodName, params);
    }
}