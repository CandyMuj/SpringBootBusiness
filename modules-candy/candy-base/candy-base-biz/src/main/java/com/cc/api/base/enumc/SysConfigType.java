package com.cc.api.base.enumc;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public enum SysConfigType {
    STRING(1, String.class),
    JSON(2, JSONObject.class),
    INTEGER(3, Integer.class),
    LONG(4, Long.class),
    BOOL(5, Boolean.class),
    ;

    private final Integer code;
    private final Class clazz;

    <T> SysConfigType(Integer code, Class<T> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public Integer getCode() {
        return code;
    }

    public <T> Class<T> getClazz() {
        return clazz;
    }

    public static SysConfigType val(Integer code) {
        return Arrays.stream(SysConfigType.values()).filter(e -> e.code.equals(code)).findFirst().orElse(null);
    }

}
