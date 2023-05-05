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
 * @Description 添加定时任务注册类，用来增加、删除定时任务。
 * @Author CandyMuj
 * @Date 2021/11/1 17:33
 * @Version 1.0
 */
@Component
public class CronTaskRegistrar implements DisposableBean {
    private final Map<String, JobKey> jobKeyMap = new ConcurrentHashMap<>(16);
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
            if (this.jobKeyMap.containsKey(jobKey.getName())) {
                removeCronTask(jobKey);
            }

            this.jobKeyMap.put(jobKey.getName(), jobKey);
            this.scheduledTasks.put(jobKey, scheduleCronTask(cronTask));
        }
    }

    public void removeCronTask(JobKey jobKey) {
        if (!ModulesCandyConfig.CandyTask.open) return;
        JobKey key = this.jobKeyMap.remove(jobKey.getName());
        if (key == null) return;
        ScheduledTask scheduledTask = this.scheduledTasks.remove(key);
        if (scheduledTask != null) scheduledTask.cancel();
    }

    public void removeCronTask(String jobKey) {
        this.removeCronTask(this.jobKeyMap.get(jobKey));
    }

    private ScheduledTask scheduleCronTask(CronTask cronTask) {
        return new ScheduledTask(
                cronTask,
                this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger())
        );
    }

    public ScheduledTask getTask(JobKey jobKey) {
        return this.scheduledTasks.get(this.jobKeyMap.get(jobKey.getName()));
    }

    public Set<String> jobKeySet() {
        return this.jobKeyMap.keySet();
    }


    @Override
    public void destroy() {
        this.scheduledTasks.values().forEach(ScheduledTask::cancel);
        this.jobKeyMap.clear();
        this.scheduledTasks.clear();
    }
}
