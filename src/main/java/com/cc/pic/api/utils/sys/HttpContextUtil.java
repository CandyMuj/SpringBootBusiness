package com.cc.pic.api.utils.sys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description http相关的工具
 * @Author CandyMuj
 * @Date 2023/2/3 14:08
 * @Version 1.0
 */
@Slf4j
public class HttpContextUtil {

    public static HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) return null;
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }

}
