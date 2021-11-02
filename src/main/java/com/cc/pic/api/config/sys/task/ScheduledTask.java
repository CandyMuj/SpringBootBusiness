package com.cc.pic.api.config.sys.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @ProjectName SpringBootBusiness
 * @FileName ScheduledTask
 * @Description ScheduledFuture的包装类。
 * ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果。
 * @Author CandyMuj
 * @Date 2021/11/1 17:03
 * @Version 1.0
 */
public final class ScheduledTask {
    volatile ScheduledFuture<?> future;

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
