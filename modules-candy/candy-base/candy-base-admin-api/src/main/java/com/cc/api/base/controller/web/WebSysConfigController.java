package com.cc.api.base.controller.web;

import cn.hutool.core.lang.Assert;
import com.cc.api.annotations.Ann;
import com.cc.api.annotations.ApiVersion;
import com.cc.api.base.pojo.SysConfig;
import com.cc.api.base.service.ISysConfigService;
import com.cc.api.enumc.ApiGroup;
import com.cc.api.pojo.sys.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/sys/config")
@ApiVersion(g = ApiGroup.G.ADMIN, v = ApiGroup.V.V1)
@Api(tags = "系统配置")
public class WebSysConfigController {
    @Resource
    private ISysConfigService sysConfigService;


    @ApiOperation("获取系统配置")
    @GetMapping("/one")
    public Result<SysConfig> getConfig(
            @RequestParam @NotBlank(message = "参数不可为空") String configKey
    ) {
        return Result.OK(sysConfigService.getById(configKey));
    }

    @ApiOperation("获取系统配置-批量")
    @PostMapping("/bat")
    public Result<List<SysConfig>> getConfigs(
            @ApiParam(required = true, value = "配置key串") @RequestBody
            @NotNull(message = "参数为空") @Size(min = 1, message = "参数为空") Set<String> configKeys
    ) {
        return Result.OK(sysConfigService.listByIds(configKeys));
    }

    @Ann
    @ApiOperation("更新系统配置")
    @PostMapping("/upd")
    public Result<?> upd(@RequestBody SysConfig sysConfig) {
        sysConfigService.updByKey(sysConfig.getConfigKey(), sysConfig.getConfigVal());
        return Result.OK();
    }

    @Ann
    @ApiOperation("更新系统配置-批量")
    @PostMapping("/upd/bat")
    public Result<?> updBat(@RequestBody List<SysConfig> sysConfigs) {
        Assert.notEmpty(sysConfigs, "配置不可为空");

        sysConfigs.forEach(o -> sysConfigService.updByKey(o.getConfigKey(), o.getConfigVal()));
        return Result.OK();
    }

}
