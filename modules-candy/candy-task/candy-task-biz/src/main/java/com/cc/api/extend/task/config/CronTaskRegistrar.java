package com.cc.api.extend.task.config;

import com.cc.api.extend.task.config.properties.ModulesCandyConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName SpringBootBusiness
 * @FileName CronTaskRegistrar
 * @Description 添加定时任务注册类，用来增加、删除定时任务。
 * @Author CandyMuj
 * @Date 2021/11/1 17:33
 * @Version 1.0
 */
@Component
public class CronTaskRegistrar implements DisposableBean {
    private final Map<JobKey, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);
    @Resource
    private TaskScheduler taskScheduler;


    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }


    public void addCronTask(JobKey jobKey, Runnable task, String cronExpression) {
        addCronTask(jobKey, new CronTask(task, cronExpression));
    }

    public void addCronTask(JobKey jobKey, CronTask cronTask) {
        if (!ModulesCandyConfig.CandyTask.open) {
            return;
        }
        if (cronTask != null) {
            if (this.scheduledTasks.containsKey(jobKey)) {
                removeCronTask(jobKey);
            }

            this.scheduledTasks.put(jobKey, scheduleCronTask(cronTask));
        }
    }

    public void removeCronTask(JobKey jobKey) {
        if (!ModulesCandyConfig.CandyTask.open) {
            return;
        }
        ScheduledTask scheduledTask = this.scheduledTasks.remove(jobKey);
        if (scheduledTask != null)
            scheduledTask.cancel();
    }

    private ScheduledTask scheduleCronTask(CronTask cronTask) {
        return new ScheduledTask(
                cronTask,
                this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger())
        );
    }

    public ScheduledTask getTask(JobKey jobKey) {
        return this.scheduledTasks.get(jobKey);
    }

    public Set<JobKey> jobKeySet() {
        return this.scheduledTasks.keySet();
    }


    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }

        this.scheduledTasks.clear();
    }
}
