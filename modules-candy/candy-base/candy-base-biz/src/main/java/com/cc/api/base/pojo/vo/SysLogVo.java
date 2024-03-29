package com.cc.api.base.pojo.vo;

import com.cc.api.base.pojo.SysLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/09/30 14:58
 * @Version 1.0
 */
@Data
@ApiModel("系统日志表")
public class SysLogVo extends SysLog {
    @ApiModelProperty("筛选：账号")
    private String account;
    @ApiModelProperty("筛选：开始时间")
    private Date startDate;
    @ApiModelProperty("筛选：结束时间")
    private Date endDate;

}
