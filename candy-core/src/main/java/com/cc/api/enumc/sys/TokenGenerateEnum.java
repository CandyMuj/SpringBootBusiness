package com.cc.api.enumc.sys;

import cn.hutool.core.util.StrUtil;

/**
 * @Description token 生成策略;一个用户可能有多个有效的token，但是一个token只对应一个用户
 * 说明一点：不失效并不代表不过期；失效的含义是：在未过期的情况下，手动清除这个token那么就是使其失效，其他的情况就自己悟（比如什么是失效什么是过期如果过期了没失效什么的乱七八糟的东西的）
 * @Author CandyMuj
 * @Date 2019/12/30 17:22
 * @Version 1.0
 */
public enum TokenGenerateEnum {
    // 每次都生成唯一的token；每次获取一个新的token，且之前的token不失效(意味着一个用户只要token未过期 那么同时存在多个有效的token)
    ONLY_ALIVE,
    // 每次都生成唯一的token；每次获取一个新的token，且之前的token失效(意味着在未过期的情况下，同时只有一个token，有且仅有一个有效token)
    ONLY_DEATH,
    // 不生成新的token；如果之前的token已失效才获取新的token(有且仅有一个有效token)
    OLD;


    /**
     * 通过枚举字符串，解析成枚举对象
     * <p>
     * 默认: ONLY_DEATH
     */
    public static TokenGenerateEnum val(String val) {
        if (StrUtil.isNotBlank(val)) {
            for (TokenGenerateEnum e : TokenGenerateEnum.values()) {
                if (e.name().equals(val)) {
                    return e;
                }
            }
        }

        return ONLY_DEATH;
    }

}
