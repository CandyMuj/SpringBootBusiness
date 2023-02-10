package com.cc.api.intercept.filter;

import com.alibaba.fastjson.JSONObject;
import com.cc.api.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @Description 日志过滤器
 * @Author CandyMuj
 * @Date 2019/12/26 14:21
 * @Version 1.0
 * <p>
 * <p>
 * 三大器的执行顺序，经过测试得到的结论
 * 过滤器（filter 全局异常不可捕获）>拦截器（interceptor 全局异常可捕获）>ArgumentResolvers（参数转换)>AOP（全局异常不可捕获）
 */
@Slf4j
@Component
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("============================================================================================");
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            log.debug("URL : {}", request.getRequestURL().toString());
            log.debug("IP : {}-{}", request.getRemoteAddr(), IpUtil.getRealIP(request));
            log.debug("HTTP_METHOD : {}", request.getMethod());
            log.debug("PARAMS : {}", JSONObject.toJSONString(request.getParameterMap()));
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String headerName = enumeration.nextElement();
                log.debug("HEADER : {}   \t{}", headerName, request.getHeader(headerName));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
        log.debug("============================================================================================");
    }

}
