package com.cc.api.base.controller;

import com.cc.api.annotations.ApiVersion;
import com.cc.api.base.controller.base.BaseController;
import com.cc.api.base.enumc.LogType;
import com.cc.api.base.enumc.SmsEnum;
import com.cc.api.base.service.IThirdService;
import com.cc.api.enumc.ApiGroup;
import com.cc.api.pojo.sys.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * @Description 第三方服务的一些接口
 * @Author CandyMuj
 * @Date 2020/09/30 14:58
 * @Version 1.0
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/third")
@ApiVersion(g = ApiGroup.G.APP, value = ApiGroup.V.V1)
@Api(tags = "第三方服务")
public class ThirdController extends BaseController {
    @Resource
    private IThirdService thirdService;


    @ApiOperation("短信-发送验证码")
    @PostMapping("/sms/send")
    public Result<String> sms(
            @ApiIgnore HttpServletRequest request,
            @ApiParam(required = true, value = "手机号") @RequestParam @NotBlank(message = "手机号不可为空") String phone,
            @ApiParam(required = true, value = "验证码类型") @RequestParam Integer type
    ) {
        SmsEnum smsEnum = SmsEnum.val(type);
        if (smsEnum == null) {
            return Result.Error("不受支持的验证码类型");
        }

        return thirdService.sms(request, smsEnum, phone);
    }

    @ApiOperation("短信-校验验证码 此接口可能不会用到，验证写在业务接口中")
    @PostMapping("/sms/check")
    public Result<?> check(
            @ApiIgnore HttpServletRequest request,
            @ApiParam(required = true, value = "手机号") @RequestParam @NotBlank(message = "手机号不可为空") String phone,
            @ApiParam(required = true, value = "验证码") @RequestParam @NotBlank(message = "验证码不可为空") String smsCode,
            @ApiParam(required = true, value = "验证码类型") @RequestParam Integer type
    ) {
        SmsEnum smsEnum = SmsEnum.val(type);
        if (smsEnum == null) {
            return Result.Error("不受支持的验证码类型");
        }

        Result<?> result = thirdService.check(request, smsEnum, phone, smsCode);
        sysLogService.ins(LogType.IFACES, "短信-校验验证码", request, result);
        return result;
    }

    @ApiOperation("生成图形验证码")
    @GetMapping("/img/code")
    public void createImgCode(
            @ApiIgnore HttpServletResponse response,
            @ApiParam(required = true, value = "图片验证码id") @RequestParam @NotBlank(message = "图片验证码id不可为空") String imgCodeId
    ) {
        thirdService.createImgCode(response, imgCodeId);
    }

}
