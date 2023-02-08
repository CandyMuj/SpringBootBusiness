package com.cc.api.base.service;


import com.cc.api.base.enumc.SmsEnum;
import com.cc.api.base.pojo.restpo.OperatorInfo;
import com.cc.api.pojo.sys.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/10/9 9:31
 * @Version 1.0
 */
public interface IThirdService {

    /**
     * 根据手机号获取运营商信息
     *
     * @param phone 手机号
     */
    OperatorInfo ispInfo(String phone);

    Result<String> sms(HttpServletRequest request, SmsEnum smsEnum, String phone);

    Result<?> check(HttpServletRequest request, SmsEnum smsEnum, String phone, String smsCode);

    void createImgCode(HttpServletResponse response, String imgCodeId);

    boolean checkImgCode(String imgCodeId, String imgCode);
}
