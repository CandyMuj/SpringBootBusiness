package com.cc.api.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.api.base.pojo.SysLog;
import com.cc.api.base.pojo.vo.SysLogVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/09/30 14:58
 * @Version 1.0
 */
@Mapper
@Component
public interface SysLogMapper extends BaseMapper<SysLog> {

    Page<SysLogVo> logList(SysLogVo sysLogVo);
}
