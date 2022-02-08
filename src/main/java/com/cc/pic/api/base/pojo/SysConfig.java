package com.cc.pic.api.base.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @FileName SysConfig
 * @Description 由 https://github.com/CandyMuj/MyBatisPlusGenerator 自动生成！
 * @Author CandyMuj
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig extends Model<SysConfig> {
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("key")
    private String configKey;
    @ApiModelProperty("值说明，取值范围")
    private String configDesc;
    @ApiModelProperty("value")
    private String configVal;
    @ApiModelProperty("中文描述")
    private String configName;
    @ApiModelProperty("参数数据类型 1string 2json 3int 4long...")
    private Integer configType;

}
