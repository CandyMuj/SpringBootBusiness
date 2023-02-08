package com.cc.pic.api.config;

import cn.hutool.core.util.StrUtil;
import com.cc.pic.api.exception.CandyException;

import java.util.Arrays;

/**
 * @Description SpringBoot环境信息
 * @Author CandyMuj
 * @Date 2021/10/19 16:37
 * @Version 1.0
 */
public final class SpringActive {
    // 系统当前使用的环境
    private static Active active;

    /**
     * 初始化此类，请确保只被调用一次
     */
    public static void init(String activeProfile) {
        if (active != null) {
            throw new CandyException("已初始化，勿再次 init");
        }

        Active active = null;
        if (StrUtil.isNotBlank(activeProfile)) {
            active = Arrays.stream(Active.values()).filter(o -> o.name().equals(activeProfile)).findFirst().orElse(null);
        }
        if (active == null) {
            throw new CandyException("未获取到当前系统变量 spring.profiles.active");
        }

        SpringActive.active = active;
    }

    public static Active getActive() {
        if (active == null) {
            throw new CandyException("未获取到系统环境");
        }

        return active;
    }

    /**
     * 当前环境是否属于某个 active
     */
    public static boolean is(Active... o) {
        return Arrays.asList(o).contains(active);
    }

    /**
     * 当前环境是否属于某个系统 system
     */
    public static boolean is(System... o) {
        return Arrays.asList(o).contains(active.getSystem());
    }

    /**
     * 当前环境是否属于 dev 环境
     */
    public static boolean isDev() {
        return active.name().equalsIgnoreCase("dev") || active.name().endsWith("Dev");
    }

    /**
     * 当前环境是否属于 test 环境
     */
    public static boolean isTest() {
        return active.name().equalsIgnoreCase("test") || active.name().endsWith("Test");
    }

    /**
     * 当前环境是否属于 prod 环境
     */
    public static boolean isProd() {
        return active.name().equalsIgnoreCase("prod") || active.name().endsWith("Prod");
    }


    public enum System {
        // 后期根据实际情况，可以随便改名字的
        DEFAULT("default", "默认应用"),
        ;

        private final String name;

        System(String name, String msg) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum Active {
        dev(System.DEFAULT),
        prod(System.DEFAULT),
        ;

        private final System system;

        Active(System system) {
            this.system = system;
        }

        public System getSystem() {
            return this.system;
        }
    }
}
