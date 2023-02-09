package com.cc.pic.api.base.controller;

import com.cc.pic.api.annotations.ApiVersion;
import com.cc.pic.api.enumc.ApiGroup;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.base.controller.base.BaseController;
import com.cc.pic.api.base.service.ISysConfigService;
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
@Api(tags = "系统配置参数")
public class SysConfigController extends BaseController {
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
