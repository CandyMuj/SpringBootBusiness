package com.cc.api.intercept.aop;

import com.cc.api.utils.sys.HttpContextUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/3/26 16:23
 * @Version 1.0
 */
@Aspect
@Component
public class AutoPageAop {

    /**
     * 过滤分页拦截
     *
     * @param point
     * @return
     */
    @Before("execution(com.github.pagehelper.Page com.cc.api..mapper.*Mapper.*(..))")
    public void beforeMethod(JoinPoint point) {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        if (request == null) return;
        // 页码
        String pageNum = request.getParameter("pageNum");
        // 每页显示条数
        String pageSize = request.getParameter("pageSize");
        if (pageNum == null || pageSize == null) {
            String pager = request.getHeader("pager");
            if (StringUtils.isNotBlank(pager)) {
                String[] temp = pager.split(",");
                if (temp.length == 2) {
                    pageNum = temp[0];
                    pageSize = temp[1];
                }
            }
        }
        if (pageNum == null) {
            pageNum = "1";
        }
        if (pageSize == null) {
            pageSize = "20";
        }
        if (StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
            PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        }
    }

}
