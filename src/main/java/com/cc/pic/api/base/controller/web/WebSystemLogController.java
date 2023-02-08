package com.cc.pic.api.base.controller.web;

import com.cc.pic.api.annotations.Ann;
import com.cc.pic.api.annotations.ApiVersion;
import com.cc.pic.api.base.controller.base.BaseController;
import com.cc.pic.api.enumc.ApiGroup;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.pojo.sys.User;
import com.cc.pic.api.base.pojo.vo.SystemLogVo;
import com.cc.pic.api.base.service.ISystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/09/30 14:58
 * @Version 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/system/log")
@ApiVersion(ApiGroup.ADMIN)
@Api(tags = "系统日志")
public class WebSystemLogController extends BaseController {
    @Resource
    private ISystemLogService systemLogService;


    @Ann
    @ApiOperation("管理端-系统日志查询")
    @PostMapping("/list")
    public Result<List<SystemLogVo>> list(
            @ApiIgnore HttpServletRequest request,
            @ApiParam(required = true, value = "筛选条件") @RequestBody SystemLogVo systemLogVo,
            @ApiIgnore User user
    ) {
        String msg = super.validateAdmin(user);
        if (msg != null) {
            return Result.Error(msg);
        }

        return systemLogService.list(systemLogVo);
    }

}
