package com.cc.api.config;

import com.cc.api.base.enumc.SmsEnum;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2019/12/27 17:33
 * @Version 1.0
 */
public class CacheKey {

    /**
     * 上方是关于系统或全局的配置 （若只需要框架，此处的配置不建议删除，建议只修改，视具体情况而定）
     */
    // redis缓存前缀，避免后期同一个redis有多个程序使用，使用一个父级进行区分
    public static final String REDIS_PREFIX = "one:";

    // ****************** 关于用户token相关的key
    // key:token val：user
    public static final String AUTH_TOKEN_USER = REDIS_PREFIX + "auth:token_user:";
    // key:userId val：token(若是多个则是一个列表)
    public static final String AUTH_USER_TOKEN = REDIS_PREFIX + "auth:user_token:";

    /**
     * 下方写关于自己业务的一些配置，避免建过多的重复文件，造成冗余 （若只需要框架，此处的配置可全部删除，可视具体情况保留需要的）
     */
    // 系统配置
    public static final String SYS_CONFIG = REDIS_PREFIX + "sys:config";
    // 短信验证码缓存前缀
    private static final String SMS_CODE = REDIS_PREFIX + "vercode:sms:";
    // 图形验证码缓存前缀
    private static final String IMG_CODE = REDIS_PREFIX + "vercode:img:";


    public static String smsCode(String phone, SmsEnum smsEnum) {
        return SMS_CODE.concat(smsEnum.getKey()).concat(":").concat(phone);
    }

    public static String imgCode(String imgCodeId) {
        return IMG_CODE.concat(imgCodeId);
    }

}
