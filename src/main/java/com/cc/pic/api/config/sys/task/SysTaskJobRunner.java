package com.cc.pic.api.config.sys.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cc.pic.api.config.ModulesCandyConfig;
import com.cc.pic.api.enumc.Enable;
import com.cc.pic.api.base.pojo.SysTaskJob;
import com.cc.pic.api.base.service.ISysTaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ProjectName SpringBootBusiness-single
 * @FileName SysTaskJobRunner
 * @Description 实现了CommandLineRunner接口的SysJobRunner类，当spring boot项目启动完成后，加载数据库里状态为正常的定时任务。
 * @Author CandyMuj
 * @Date 2021/11/2 13:38
 * @Version 1.0
 */
@Slf4j
@Service
public class SysTaskJobRunner implements CommandLineRunner {
    @Resource
    private ISysTaskJobService sysTaskJobService;
    @Resource
    private CronTaskRegistrar cronTaskRegistrar;


    @Override
    public void run(String... args) {
        if (!ModulesCandyConfig.CandyTask.open) {
            log.info("定时任务已关闭[不初始化定时任务]...");
            return;
        }
        // 初始加载数据库里状态为正常的定时任务
        List<SysTaskJob> jobList = sysTaskJobService.getSysJobListByStatus(Enable.ENABLE);
        if (CollUtil.isNotEmpty(jobList)) {
            for (SysTaskJob job : jobList) {
                JobKey jobKey = JobKey.jobKey(job.getJobId());
                cronTaskRegistrar.addCronTask(
                        jobKey,
                        new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams()),
                        job.getCronExpression()
                );
                ModulesCandyConfig.CandyTask.lastUpd.put(jobKey, job.getUpdateTime());
            }
        }
        log.info("定时任务已加载完毕... {}", jobList.size());
        this.listenerCheck();
    }


    /**
     * 监听定时任务是否发生了变更 进行同步
     * <p>
     * 1. 数据库不存在 定时任务中存在 移除定时任务
     * 2. 数据库中存在 定时任务中不存在 新增定时任务
     * 3. 都存在 判断更新时间是否一致，若不一致则执行更新
     */
    private void listenerCheck() {
        if (!ModulesCandyConfig.CandyTask.cluster) {
            log.info("已关闭集群模式[不加载同步检查逻辑]...");
            return;
        }
        JobKey listenerJobKey = JobKey.jobKey("listenerCheck");
        long interval = (long) (Math.ceil(ModulesCandyConfig.CandyTask.clusterCheckInterval / 1000f));
        cronTaskRegistrar.addCronTask(listenerJobKey, () -> {
            // 从数据库获取：仅获取启用（正常）状态的定时任务列表
            Map<JobKey, SysTaskJob> dataBaseEnableJobMap = sysTaskJobService.getSysJobListByStatus(Enable.ENABLE).stream().collect(
                    Collectors.toMap(job -> JobKey.jobKey(job.getJobId()), Function.identity())
            );
            Set<JobKey> jobKeySet = cronTaskRegistrar.jobKeySet();
            // 循环结束后，剩下的就是：数据库中不存在，定时任务中存在 需要移除的定时任务
            Set<JobKey> notInDataBaseJobKey = new HashSet<>(jobKeySet);
            for (Map.Entry<JobKey, SysTaskJob> entry : dataBaseEnableJobMap.entrySet()) {
                JobKey jobKey = entry.getKey();
                SysTaskJob taskJob = entry.getValue();

                // 3. 都存在 判断更新时间是否一致，若不一致则执行更新
                if (jobKeySet.contains(jobKey)) {
                    notInDataBaseJobKey.remove(jobKey);
                    // 一致，不执行更新
                    if (taskJob.getUpdateTime().equals(ModulesCandyConfig.CandyTask.lastUpd.get(jobKey))) {
                        continue;
                    }
                }
                // 2. 数据库中存在 定时任务中不存在 新增定时任务
                // 不一致，下方执行更新定时任务（如果存在内部会先删除再新增）
                cronTaskRegistrar.addCronTask(
                        jobKey,
                        new SchedulingRunnable(taskJob.getBeanName(), taskJob.getMethodName(), taskJob.getMethodParams()),
                        taskJob.getCronExpression()
                );

                // 更新更新时间
                ModulesCandyConfig.CandyTask.lastUpd.put(jobKey, taskJob.getUpdateTime());
            }

            // 1. 数据库不存在 定时任务中存在 移除定时任务
            if (notInDataBaseJobKey.size() == 0) {
                return;
            }
            for (JobKey jobKey : notInDataBaseJobKey) {
                // 监听定时 别打自己给移除了
                if (jobKey.equals(listenerJobKey)) {
                    continue;
                }
                cronTaskRegistrar.removeCronTask(jobKey);
                ModulesCandyConfig.CandyTask.lastUpd.remove(jobKey);
            }
        }, StrUtil.format("0/{} * * * * ?", interval));
        log.info("已开启集群模式[监听加载完毕]...");
    }

}