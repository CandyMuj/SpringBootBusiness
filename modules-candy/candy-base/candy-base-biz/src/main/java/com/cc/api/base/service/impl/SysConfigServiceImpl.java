package com.cc.api.base.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cc.api.base.enumc.SysConfigKey;
import com.cc.api.base.enumc.SysConfigType;
import com.cc.api.base.mapper.SysConfigMapper;
import com.cc.api.base.pojo.SysConfig;
import com.cc.api.base.service.ISysConfigService;
import com.cc.api.config.CacheKey;
import com.cc.api.utils.sys.bean.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2022/2/8 10:27
 * @Version 1.0
 */
@Slf4j
@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    @Resource
    private RedisUtil redisUtil;


    @Override
    public <T> T getConfig(String configKey, Class<T> clazz) {
        if (StrUtil.isBlank(configKey)) return null;

        String sysConfigStr = StrUtil.toStringOrNull(redisUtil.hget(CacheKey.SYS_CONFIG, configKey));
        if (StrUtil.isBlank(sysConfigStr)) {
            SysConfig sysConfig = super.getById(configKey);
            if (sysConfig == null) {
                return null;
            }
            if (StrUtil.isBlank(sysConfig.getConfigVal())) {
                return null;
            }
            redisUtil.hset(CacheKey.SYS_CONFIG, configKey, sysConfigStr = JSONUtil.toJsonStr(sysConfig));
        }

        SysConfig sysConfig = JSONUtil.toBean(sysConfigStr, SysConfig.class);
        if (clazz == null) {
            SysConfigType configType = SysConfigType.val(sysConfig.getConfigType());
            if (configType == null) {
                return null;
            }
            clazz = configType.getClazz();
        }
        if (clazz == null) {
            return null;
        }
        if (SysConfigType.JSON.getCode().equals(sysConfig.getConfigType())) {
            return JSONUtil.toBean(sysConfig.getConfigVal(), clazz);
        } else {
            return ReflectUtil.newInstance(clazz, sysConfig.getConfigVal());
        }
    }

    @Override
    public <T> T getConfig(SysConfigKey configKey, Class<T> clazz) {
        if (configKey == null) return null;

        return this.getConfig(configKey.getCode(), clazz);
    }

    @Override
    public <T> T getConfig(String configKey) {
        return this.getConfig(configKey, null);
    }

    @Override
    public <T> T getConfig(SysConfigKey configKey) {
        return this.getConfig(configKey, configKey.getClazz());
    }


    @Override
    public boolean updByKey(String configKey, String configValue) {
        Assert.notBlank(configKey, "key为空或不存在");
        Assert.notBlank(configValue, "val不可为空");

        // 如果能获取枚举，就调用一下参数校验方法
        SysConfigKey keyEnum = SysConfigKey.val(configKey);
        if (keyEnum != null) {
            String val = keyEnum.verify(configValue);
            if (val != null) configValue = val;
        }

        Assert.isTrue(super.lambdaUpdate()
                        .eq(SysConfig::getConfigKey, configKey)
                        .set(SysConfig::getConfigVal, configValue)
                        .update(),
                "配置不存在"
        );

        redisUtil.hdel(CacheKey.SYS_CONFIG, configKey);
        return true;
    }

    @Override
    public boolean updByKey(SysConfigKey configKey, String configValue) {
        Assert.notNull(configKey, "key为空或不存在");

        return this.updByKey(configKey.getCode(), configValue);
    }
}
