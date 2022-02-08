package com.cc.api.base.controller;

import com.cc.api.annotations.ApiVersion;
import com.cc.api.base.service.ISysConfigService;
import com.cc.api.enumc.ApiGroup;
import com.cc.api.pojo.sys.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * @ProjectName SpringBootBusiness
 * @FileName TaskJobController
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/sys/config")
@ApiVersion(ApiGroup.APP)
@Api(tags = "系统配置参数")
public class SysConfigController {
    @Resource
    private ISysConfigService sysConfigService;


    @ApiOperation("获取系统参数")
    @GetMapping("/get")
    public Result<Object> getConfig(
            @RequestParam @NotBlank(message = "参数不可为空") String configKey
    ) {
        return Result.OK(sysConfigService.getConfig(configKey));
    }

}
