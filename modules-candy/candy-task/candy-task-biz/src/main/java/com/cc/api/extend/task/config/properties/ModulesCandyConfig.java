package com.cc.api.extend.task.config.properties;

import com.cc.api.utils.sys.YmlConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description 自定义模块/扩展组件的配置 实体类
 * 具体配置的含义，详见 application_cc.yml 中的注释
 * @Author CandyMuj
 * @Date 2021/11/3 15:20
 * @Version 1.0
 */
public class ModulesCandyConfig {

    /**
     * 动态定时任务模块配置
     */
    public static class CandyTask {
        public static final boolean open = YmlConfig.getBooleanValue("modules-candy.candy-task.open");
        public static final boolean cluster = YmlConfig.getBooleanValue("modules-candy.candy-task.cluster");
        public static final long clusterCheckInterval = Optional.ofNullable(YmlConfig.getLong("modules-candy.candy-task.clusterCheckInterval")).orElse(15000L);

        // 动态定时任务更新时间
        public static final Map<String, Date> lastUpd = new HashMap<>();
    }

}
