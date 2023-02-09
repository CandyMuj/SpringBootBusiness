package com.cc.api.base.pojo.sysconfig;

import cn.hutool.json.JSONUtil;

import java.io.Serializable;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2022/4/14 15:55
 * @Version 1.0
 */
public interface ISysConfigBase extends Serializable {
    long serialVersionUID = 1L;


    /**
     * 参数校验
     * 这几个以verify开头的方法可根据需要实现多个，一般来说实现一个即可，调用优先级请查看{{@link com.cc.api.base.enumc.SysConfigKey#verify}}中的逻辑
     */
    default void verify() {
    }

    /**
     * @param adminDosth 有些逻辑仅在=true的时候执行  如管理系统更新是需要一些逻辑操作
     */
    default void verify(boolean adminDosth) {
    }

    /**
     * 参数校验，如果对对象进行了相关的修改，则用此校验方法，返回修改后的数据字符串
     */
    default String verifyRstr() {
        return null;
    }

    default String verifyRstr(boolean adminDosth) {
        return null;
    }

    default String str() {
        return JSONUtil.toJsonStr(this);
    }
}
