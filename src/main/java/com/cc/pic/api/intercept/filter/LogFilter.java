package com.cc.pic.api.intercept.filter;

import com.cc.pic.api.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProJectName APIServer
 * @FileName LogFilter
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
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            log.info("IP : " + IpUtil.getRealIP(request));
            log.info("URL : " + request.getRequestURL().toString());
            log.info("HTTP_METHOD : " + request.getMethod());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
