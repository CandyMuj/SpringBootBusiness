package com.cc.pic.api.config.sys.task;

import cn.hutool.core.collection.CollUtil;
import com.cc.pic.api.enumc.Enable;
import com.cc.pic.api.src.pojo.sys.SysTaskJob;
import com.cc.pic.api.src.service.sys.ISysTaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        // 初始加载数据库里状态为正常的定时任务
        List<SysTaskJob> jobList = sysTaskJobService.getSysJobListByStatus(Enable.ENABLE);
        if (CollUtil.isNotEmpty(jobList)) {
            for (SysTaskJob job : jobList) {
                cronTaskRegistrar.addCronTask(new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams()), job.getCronExpression());
            }
        }
        log.info("定时任务已加载完毕... {}", jobList.size());
    }
}