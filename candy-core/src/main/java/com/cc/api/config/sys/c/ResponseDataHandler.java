package com.cc.api.config.sys.c;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.cc.api.config.Configc;
import com.cc.api.pojo.sys.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Description 针对返回值为Result类型的数据进行处理
 * @Author CandyMuj
 * @Date 2019/12/27 17:04
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ResponseDataHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getParameterType().equals(Result.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o != null) {
            log.debug("handle responseData...");
            return JSONUtil.parseObj(JSONUtil.toJsonStr(o, JSONConfig.create().setDateFormat(Configc.DEFAULT_DATEFORMAT)));
        }

        return null;
    }

}
