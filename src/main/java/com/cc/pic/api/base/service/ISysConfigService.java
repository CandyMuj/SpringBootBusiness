package com.cc.pic.api.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.pic.api.base.enumc.SysConfigKey;
import com.cc.pic.api.base.pojo.SysConfig;

/**
 * @ProjectName SpringBootBusiness
 * @FileName ITaskJobService
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
public interface ISysConfigService extends IService<SysConfig> {

    <T> T getConfig(String configKey, Class<T> clazz);

    <T> T getConfig(SysConfigKey configKey, Class<T> clazz);

    <T> T getConfig(String configKey);

    boolean updByKey(String configKey, String configValue);

    boolean updByKey(SysConfigKey configKey, String configValue);
}
