package com.cc.api.base.controller.web;

import com.cc.api.annotations.Ann;
import com.cc.api.annotations.ApiVersion;
import com.cc.api.base.controller.base.BaseController;
import com.cc.api.base.pojo.vo.SysLogVo;
import com.cc.api.enumc.ApiGroup;
import com.cc.api.pojo.sys.Result;
import com.cc.api.pojo.sys.User;
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
@RequestMapping("/admin/sys/log")
@ApiVersion(g = ApiGroup.G.ADMIN, value = ApiGroup.V.V1)
@Api(tags = "系统日志")
public class WebSysLogController extends BaseController {


    @Ann
    @ApiOperation("管理端-系统日志查询")
    @PostMapping("/list")
    public Result<List<SysLogVo>> list(
            @ApiParam(required = true, value = "筛选条件") @RequestBody SysLogVo sysLogVo,
            @ApiIgnore User user
    ) {
        super.validateAdmin(user);

        return sysLogService.list(sysLogVo);
    }

}
