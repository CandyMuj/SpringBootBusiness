package com.cc.api.extend.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.api.enumc.Enable;
import com.cc.api.extend.task.pojo.SysTaskJob;
import com.cc.api.pojo.sys.Result;

import java.util.List;

/**
 * @ProjectName SpringBootBusiness
 * @FileName ITaskJobService
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
public interface ISysTaskJobService extends IService<SysTaskJob> {

    Result<?> addOrUpd(SysTaskJob taskJob);

    Result<?> delete(Integer jobId);

    Result<?> switchTask(Integer jobId);

    List<SysTaskJob> getSysJobListByStatus(Enable enable);
}
