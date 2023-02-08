package com.cc.pic.api.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.pic.api.base.pojo.SystemLog;
import com.cc.pic.api.base.pojo.vo.SystemLogVo;
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
public interface SystemLogMapper extends BaseMapper<SystemLog> {

    Page<SystemLogVo> logList(SystemLogVo systemLogVo);
}
