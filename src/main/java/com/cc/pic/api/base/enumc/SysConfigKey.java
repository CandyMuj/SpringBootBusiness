package com.cc.pic.api.base.enumc;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cc.pic.api.base.pojo.sysconfig.ISysConfigBase;
import com.cc.pic.api.base.pojo.sysconfig.ScfgExampleConfig;
import com.cc.pic.api.exception.CandyException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 系统内部调用时，使用枚举的方式，避免魔法值
 */
public enum SysConfigKey {
    EXAMPLE_CONFIG("example_config", ScfgExampleConfig.class, "示例配置-实际开发时建议删除"),
    ;


    private final String code;
    private final Class clazz;


    <T> SysConfigKey(String code, Class<T> clazz, String msg) {
        this.code = code;
        this.clazz = clazz;
    }

    public String getCode() {
        return code;
    }

    public <T> Class<T> getClazz() {
        return clazz;
    }

    public static SysConfigKey val(String code) {
        return Arrays.stream(SysConfigKey.values()).filter(e -> e.code.equals(code)).findFirst().orElse(null);
    }


    /**
     * 参数校验
     */
    public String verify(String paramValue) {
        Class<?>[] supClazz = this.clazz.getInterfaces();
        if (ArrayUtil.isEmpty(supClazz) || !ArrayUtil.contains(supClazz, ISysConfigBase.class)) return null;

        try {
            Method verifyRstrMethod = ReflectUtil.getMethod(this.clazz, "verifyRstr");
            if (verifyRstrMethod == null) {
                verifyRstrMethod = ReflectUtil.getMethod(this.clazz, "verifyRstr", Boolean.class);
            }
            if (verifyRstrMethod == null) {
                verifyRstrMethod = ReflectUtil.getMethod(this.clazz, "verify", Boolean.class);
            }
            if (verifyRstrMethod != null) {
                return ReflectUtil.invoke(JSONUtil.toBean(paramValue, clazz), verifyRstrMethod, true).toString();
            }

            ReflectUtil.invoke(JSONUtil.toBean(paramValue, clazz), "verify");
        } catch (Exception e) {
            if (e.getCause() != null) {
                Throwable throwable = e.getCause().getCause();
                if (throwable != null) throw new CandyException(throwable.getMessage());
                throw e;
            }

            // 跳过此异常
            // No such method: [verify]
            String message = e.getMessage();
            if (StrUtil.isBlank(message) || !message.startsWith("No such method")) {
                throw e;
            }
        }

        return null;
    }

}
