package com.cc.pic.api.src.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cc.pic.api.enumc.Enable;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.src.pojo.sys.SysTaskJob;

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
