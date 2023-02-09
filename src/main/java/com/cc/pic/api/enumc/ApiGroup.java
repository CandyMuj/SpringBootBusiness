package com.cc.pic.api.enumc;

/**
 * @Description swagger文档分组注解配置
 * @Author CandyMuj
 * @Date 2020/7/17 16:47
 * @Version 1.0
 */
public class ApiGroup {

    /**
     * 版本号
     */
    public enum V {
        V1("1.0.0", true),
        ;


        private final String name;
        private final boolean show;

        V(String name, boolean show) {
            this.name = name;
            this.show = show;
        }

        public String getName() {
            return name;
        }

        public boolean isShow() {
            return show;
        }
    }

    /**
     * 客户端
     */
    public enum G {
        COMMON("通用", true),
        APP("应用端", true),
        ADMIN("管理员端", true),
        ;


        private final String name;
        private final boolean show;

        G(String name, boolean show) {
            this.name = name;
            this.show = show;
        }

        public String getName() {
            return name;
        }

        public boolean isShow() {
            return show;
        }
    }

}
