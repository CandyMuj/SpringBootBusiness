package com.cc.pic.api.base.pojo.sysconfig;

import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 演示如何使用ISysConfigBase
 * @Author CandyMuj
 * @Date 2023/2/9 14:47
 * @Version 1.0
 */
@Data
@ApiModel("系统配置-示例配置")
public class ScfgExampleConfig implements ISysConfigBase {
    @ApiModelProperty("密码 前端传明文 存储时为密文")
    private String passwd;


    @Override
    public String verifyRstr(boolean adminDosth) {
        // 密码进行加密再存储
        this.passwd = SecureUtil.md5(this.passwd);

        // 仅当调用sysConfigService.updByKey时为true，表示当是通过管理系统修改时触发一些操作
        if (adminDosth) {
            // dosth
        }

        // 返回经过以上逻辑处理后的最终入库的当前对象的json串
        return this.str();
    }

}
