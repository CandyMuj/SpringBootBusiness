package com.cc.pic.api.src.utils;

import cn.hutool.core.util.StrUtil;
import com.cc.pic.api.utils.sys.YmlConfig;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName api
 * @FileName RestParams
 * @Description
 * @Author CandyMuj
 * @Date 2021/8/5 14:26
 * @Version 1.0
 */
@Slf4j
public class RestParams {
    // ------------------------------------------------------------------------------ Key定义
    // ----------------------------------- HEADER 参数
    // 将这个接口的随机参数，因为是uuid，就当作是requestId吧
    public static final String SIGN_NONCE = YmlConfig.getString("interface.auth.sign.nonce");


    // ------------------------------------------------------------------------------ 获取方法
    public static String getRequestId(HttpServletRequest request) {
        return getRequestParamVal(request, SIGN_NONCE);
    }


    // ------------------------------------------------------------------------------ 通用方法
    private static String getRequestParamVal(HttpServletRequest request, String key) {
        if (request == null) {
            return null;
        }

        String val = request.getParameter(key);
        return StrUtil.isNotBlank(val) ? val : null;
    }

    private static String getRequestHeaderVal(HttpServletRequest request, String key) {
        if (request == null) {
            return null;
        }

        String val = request.getHeader(key);
        try {
            if (val == null) {
                val = (String) request.getAttribute(key);
            }
        } catch (Exception e) {
            // keep
        }

        return val;
    }

}
