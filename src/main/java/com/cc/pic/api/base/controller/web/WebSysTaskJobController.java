package com.cc.pic.api.base.controller.web;

import com.cc.pic.api.annotations.Ann;
import com.cc.pic.api.annotations.ApiVersion;
import com.cc.pic.api.enumc.ApiGroup;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.pojo.sys.User;
import com.cc.pic.api.base.controller.base.BaseController;
import com.cc.pic.api.base.pojo.SysTaskJob;
import com.cc.pic.api.base.service.ISysTaskJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/sys/task_job")
@ApiVersion(ApiGroup.ADMIN)
@Api(tags = "动态定时任务配置")
public class WebSysTaskJobController extends BaseController {
    @Resource
    private ISysTaskJobService sysTaskJobService;


    @Ann
    @ApiOperation("管理端-新增或更新定时任务")
    @PostMapping("/addOrUpd")
    public Result<?> addOrUpd(
            @ApiParam(required = false, value = "任务ID 更新时传入") @RequestParam(required = false) Integer jobId,
            @ApiParam(required = true, value = "bean 名称") @RequestParam String beanName,
            @ApiParam(required = true, value = "方法名称") @RequestParam String methodName,
            @ApiParam(required = false, value = "方法参数") @RequestParam(required = false) String methodParams,
            @ApiParam(required = true, value = "cron表达式") @RequestParam String cronExpression,
            @ApiParam(required = false, value = "备注") @RequestParam(required = false) String remark,
            @ApiParam(required = true, value = "状态（1正常 0暂停）") @RequestParam Integer jobStatus,
            @ApiIgnore User user
    ) {
        String msg = super.validateAdmin(user);
        if (msg != null) {
            return Result.Error(msg);
        }

        SysTaskJob taskJob = new SysTaskJob();
        taskJob.setJobId(jobId);
        taskJob.setBeanName(beanName);
        taskJob.setMethodName(methodName);
        taskJob.setMethodParams(methodParams);
        taskJob.setCronExpression(cronExpression);
        taskJob.setRemark(remark);
        taskJob.setJobStatus(jobStatus);
        taskJob.verify();

        return sysTaskJobService.addOrUpd(taskJob);
    }

    @Ann
    @ApiOperation("管理端-删除任务")
    @PostMapping("/delete")
    public Result<?> delete(
            @ApiParam(required = true, value = "任务ID") @RequestParam Integer jobId,
            @ApiIgnore User user
    ) {
        String msg = super.validateAdmin(user);
        if (msg != null) {
            return Result.Error(msg);
        }

        return sysTaskJobService.delete(jobId);
    }

    @Ann
    @ApiOperation("管理端-启动/停止任务")
    @PostMapping("/switch")
    public Result<?> switchTask(
            @ApiParam(required = true, value = "任务ID") @RequestParam Integer jobId,
            @ApiIgnore User user
    ) {
        String msg = super.validateAdmin(user);
        if (msg != null) {
            return Result.Error(msg);
        }

        return sysTaskJobService.switchTask(jobId);
    }

}
