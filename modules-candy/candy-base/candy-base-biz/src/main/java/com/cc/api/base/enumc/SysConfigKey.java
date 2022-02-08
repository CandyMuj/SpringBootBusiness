package com.cc.api.base.enumc;

import java.util.Arrays;

/**
 * 系统内部调用时，使用枚举的方式，避免魔法值
 */
public enum SysConfigKey {
    ;

    private final String code;

    SysConfigKey(String code, String msg) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SysConfigKey val(String code) {
        return Arrays.stream(SysConfigKey.values()).filter(e -> e.code.equals(code)).findFirst().orElse(null);
    }

}
