package com.cc.pic.api.base.controller;

import com.cc.pic.api.annotations.ApiVersion;
import com.cc.pic.api.base.service.ISysConfigService;
import com.cc.pic.api.enumc.ApiGroup;
import com.cc.pic.api.pojo.sys.Result;
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
import java.util.HashMap;
import java.util.Map;
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
@RequestMapping("/sys/config")
@ApiVersion(g = ApiGroup.G.APP, v = ApiGroup.V.V1)
@Api(tags = "系统配置")
public class SysConfigController {
    @Resource
    private ISysConfigService sysConfigService;


    @ApiOperation("获取系统配置")
    @GetMapping("/one")
    public Result<Object> getConfig(
            @RequestParam @NotBlank(message = "参数不可为空") String configKey
    ) {
        return Result.OK(sysConfigService.getConfig(configKey));
    }

    @ApiOperation("获取系统配置-批量")
    @PostMapping("/bat")
    public Result<Map<String, Object>> getConfigs(
            @ApiParam(required = true, value = "配置key串") @RequestBody
            @NotNull(message = "参数为空") @Size(min = 1, message = "参数为空") Set<String> configKeys
    ) {
        // 由于内部使用的是redis，因此直接用循环获取的方式；因为如果改内部为批量，反而麻烦许多，没必要的
        Map<String, Object> map = new HashMap<>();
        for (String configKey : configKeys) {
            Object o = sysConfigService.getConfig(configKey);
            if (o == null) continue;
            map.put(configKey, o);
        }

        return Result.OK(map);
    }

}
