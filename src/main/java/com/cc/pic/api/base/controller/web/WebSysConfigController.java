package com.cc.pic.api.base.controller.web;

import cn.hutool.core.util.StrUtil;
import com.cc.pic.api.annotations.ApiVersion;
import com.cc.pic.api.base.pojo.SysConfig;
import com.cc.pic.api.base.service.ISysConfigService;
import com.cc.pic.api.enumc.ApiGroup;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.base.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/sys/config")
@ApiVersion(ApiGroup.ADMIN)
@Api(tags = "系统配置参数")
public class WebSysConfigController extends BaseController {
    @Resource
    private ISysConfigService sysConfigService;


    @ApiOperation("根据id获取")
    @GetMapping("/get")
    public Result<SysConfig> getConfig(
            @RequestParam @NotBlank(message = "参数不可为空") String configKey
    ) {
        return Result.OK(sysConfigService.getById(configKey));
    }

    @ApiOperation("更新")
    @PostMapping("/upd")
    public Result<?> upd(@RequestBody SysConfig sysConfig) {
        if (sysConfig.getConfigKey() == null) {
            return Result.Error("key不可为空");
        }
        if (StrUtil.isBlank(sysConfig.getConfigVal())) {
            return Result.Error("val不可为空");
        }

        sysConfig.updateById();
        return Result.OK();
    }

}
