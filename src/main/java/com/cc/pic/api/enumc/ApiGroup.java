package com.cc.pic.api.enumc;

/**
 * @Description swagger文档分组注解配置
 * @Author CandyMuj
 * @Date 2020/7/17 16:47
 * @Version 1.0
 */
public enum ApiGroup {
    APP("应用端 1.0"),
    ADMIN("管理员端 1.0"),
    ;


    private String name;

    ApiGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
