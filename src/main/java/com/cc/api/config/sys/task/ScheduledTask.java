package com.cc.api.config.sys.task;

import org.springframework.scheduling.config.CronTask;

import java.util.concurrent.ScheduledFuture;

/**
 * @Description ScheduledFuture的包装类。
 * ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果。
 * @Author CandyMuj
 * @Date 2021/11/1 17:03
 * @Version 1.0
 */
public final class ScheduledTask {
    private final CronTask cronTask;
    private final ScheduledFuture<?> future;


    ScheduledTask(CronTask cronTask, ScheduledFuture<?> future) {
        this.cronTask = cronTask;
        this.future = future;
    }

    public CronTask getCronTask() {
        return this.cronTask;
    }


    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
