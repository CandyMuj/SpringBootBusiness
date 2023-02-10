package com.cc.api.config.sys.task;

import cn.hutool.core.util.StrUtil;
import com.cc.api.exception.CandyException;
import lombok.Getter;

/**
 * @Description 定时任务在内存中的唯一标识
 * @Author CandyMuj
 * @Date 2021/11/4 16:39
 * @Version 1.0
 */
@Getter
public class JobKey {
    // 定时任务名称
    private final String name;
    // 定时任务分组
    private final String group;


    public JobKey(String name, String group) {
        if (StrUtil.isBlank(name)) {
            throw new CandyException("Name cannot be null.");
        }
        this.name = "CANDY_JOB_".concat(name);
        if (StrUtil.isBlank(group)) {
            this.group = "DEFAULT";
        } else {
            this.group = group;
        }
    }

    public JobKey(String name) {
        this(name, null);
    }


    public static JobKey jobKey(String name) {
        return new JobKey(name);
    }

    public static JobKey jobKey(String name, String group) {
        return new JobKey(name, group);
    }

    public static JobKey jobKey(Long jobId) {
        return new JobKey(jobId.toString());
    }

    public static JobKey jobKey(Long jobId, String group) {
        return new JobKey(jobId.toString(), group);
    }

    public static JobKey jobKey(Integer jobId) {
        return new JobKey(jobId.toString());
    }

    public static JobKey jobKey(Integer jobId, String group) {
        return new JobKey(jobId.toString(), group);
    }


    @Override
    public String toString() {
        return this.group + "." + this.name;
    }
}
