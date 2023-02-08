package com.cc.api.extend.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cc.api.enumc.Enable;
import com.cc.api.extend.task.config.CronTaskRegistrar;
import com.cc.api.extend.task.config.JobKey;
import com.cc.api.extend.task.config.SchedulingRunnable;
import com.cc.api.extend.task.mapper.SysTaskJobMapper;
import com.cc.api.extend.task.pojo.SysTaskJob;
import com.cc.api.extend.task.service.ISysTaskJobService;
import com.cc.api.pojo.sys.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
@Slf4j
@Service
public class SysTaskJobServiceImpl extends ServiceImpl<SysTaskJobMapper, SysTaskJob> implements ISysTaskJobService {
    @Resource
    private CronTaskRegistrar cronTaskRegistrar;


    /**
     * 新增或更新定时任务
     */
    @Override
    @Transactional
    public Result<?> addOrUpd(SysTaskJob taskJob) {
        // 更新
        if (taskJob.getId() != null) {
            SysTaskJob oldJob = super.getById(taskJob.getId());
            if (oldJob == null) {
                return Result.Error("配置不存在");
            }
            taskJob.setUpdateTime(new Date());
            if (!taskJob.updateById()) {
                return Result.Error("更新失败");
            }

            // 动态调整定时任务
            // 若原来存在正常状态的任务，则先移除再添加
            if (oldJob.getJobStatus() == 1) {
                cronTaskRegistrar.removeCronTask(JobKey.jobKey(oldJob.getId()));
            }
            if (taskJob.getJobStatus() == 1) {
                cronTaskRegistrar.addCronTask(
                        JobKey.jobKey(taskJob.getId()),
                        new SchedulingRunnable(taskJob.getBeanName(), taskJob.getMethodName(), taskJob.getMethodParams()),
                        taskJob.getCronExpression()
                );
            }
        }
        // 新增
        else {
            taskJob.setCreateTime(new Date());
            taskJob.setUpdateTime(taskJob.getCreateTime());
            if (!taskJob.insert()) {
                return Result.Error("新增失败");
            }

            // 动态调整定时任务
            if (taskJob.getJobStatus() == 1) {
                cronTaskRegistrar.addCronTask(
                        JobKey.jobKey(taskJob.getId()),
                        new SchedulingRunnable(taskJob.getBeanName(), taskJob.getMethodName(), taskJob.getMethodParams()),
                        taskJob.getCronExpression()
                );
            }
        }

        return Result.OK();
    }

    /**
     * 删除任务
     */
    @Override
    @Transactional
    public Result<?> delete(Integer jobId) {
        SysTaskJob taskJob = super.getById(jobId);
        if (taskJob == null) {
            return Result.Error("任务不存在");
        }
        if (!taskJob.deleteById()) {
            return Result.Error("删除失败");
        }

        // 动态调整定时任务
        if (taskJob.getJobStatus() == 1) {
            cronTaskRegistrar.removeCronTask(JobKey.jobKey(taskJob.getId()));
        }

        return Result.OK();
    }

    /**
     * 启动/停止任务
     */
    @Override
    @Transactional
    public Result<?> switchTask(Integer jobId) {
        SysTaskJob taskJob = super.getById(jobId);
        if (taskJob == null) {
            return Result.Error("任务不存在");
        }

        taskJob.setJobStatus(taskJob.getJobStatus() == 0 ? 1 : 0);
        if (!taskJob.updateById()) {
            return Result.Error("更新状态失败");
        }

        // 动态调整定时任务
        // 开启任务
        if (taskJob.getJobStatus() == 1) {
            cronTaskRegistrar.addCronTask(
                    JobKey.jobKey(taskJob.getId()),
                    new SchedulingRunnable(taskJob.getBeanName(), taskJob.getMethodName(), taskJob.getMethodParams()),
                    taskJob.getCronExpression()
            );
        }
        // 停止任务
        else {
            cronTaskRegistrar.removeCronTask(JobKey.jobKey(taskJob.getId()));
        }

        return Result.OK();
    }

    /**
     * 通过状态获取定时任务列表
     */
    @Override
    public List<SysTaskJob> getSysJobListByStatus(Enable enable) {
        return super.lambdaQuery().eq(SysTaskJob::getJobStatus, enable.getCode()).list();
    }

}
