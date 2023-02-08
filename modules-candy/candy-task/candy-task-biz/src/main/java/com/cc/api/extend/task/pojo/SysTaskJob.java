package com.cc.api.extend.task.pojo;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cc.api.exception.ResultException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Date 2021/11/02 10:12
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysTaskJob extends Model<SysTaskJob> {
    @TableId
    @ApiModelProperty("任务ID")
    private Integer jobId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("状态（1正常 0暂停）")
    private Integer jobStatus;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("bean 名称")
    private String beanName;
    @ApiModelProperty("cron表达式")
    private String cronExpression;
    @ApiModelProperty("方法名称")
    private String methodName;
    @ApiModelProperty("方法参数")
    private String methodParams;


    public void verify() {
        if (StrUtil.isBlank(this.beanName)) {
            throw new ResultException("bean 名称不可为空");
        }
        if (StrUtil.isBlank(this.methodName)) {
            throw new ResultException("方法名称不可为空");
        }
        if (StrUtil.isBlank(this.cronExpression)) {
            throw new ResultException("cron表达式不可为空");
        }
        if (jobStatus == null || (jobStatus != 1 && jobStatus != 0)) {
            throw new ResultException("bean 名称不可为空");
        }
    }

}
