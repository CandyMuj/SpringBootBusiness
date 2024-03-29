package com.cc.api.test;

import com.cc.api.annotations.Ann;
import com.cc.api.pojo.sys.Result;
import com.cc.api.pojo.sys.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2019/12/23 16:19
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "测试专用Controller")
public class TestController {

    @ApiOperation("测试专用接口")
    @RequestMapping("/test")
    public String test(
            @ApiParam(required = true, value = "参数1") @RequestParam(defaultValue = "默认值") String par1,
            @ApiParam(required = false, value = "参数2") String par2,
            @ApiIgnore User user
    ) {
        return "Hello World！";
    }

    @ApiOperation("测试专用接口1")
    @RequestMapping("/test1")
    public Result<?> test1(
            @ApiParam(required = true, value = "参数1") @RequestParam(defaultValue = "默认值") String par1,
            @ApiParam(required = false, value = "参数2") String par2,
            @ApiIgnore User user
    ) {
        return Result.OK();
    }

    @Ann
    @ApiOperation("测试专用接口2")
    @RequestMapping("/test2")
    public Result<?> test2(
            @ApiParam(required = true, value = "参数1") @RequestParam(defaultValue = "默认值") String par1,
            @ApiParam(required = false, value = "参数2") String par2,
            @ApiIgnore User user
    ) {
        return Result.OK();
    }

}
