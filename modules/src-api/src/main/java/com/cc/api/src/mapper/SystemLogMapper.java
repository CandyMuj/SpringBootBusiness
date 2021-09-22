package com.cc.api.src.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cc.api.src.pojo.SystemLog;
import com.cc.api.src.pojo.vo.SystemLogVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @ProjectName SpringBootBusiness
 * @FileName SystemLogMapper
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
