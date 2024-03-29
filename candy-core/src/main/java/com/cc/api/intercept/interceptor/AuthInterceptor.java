package com.cc.api.intercept.interceptor;

import cn.hutool.core.util.StrUtil;
import com.cc.api.annotations.Ann;
import com.cc.api.config.SpringActive;
import com.cc.api.config.StatusCode;
import com.cc.api.exception.AuthException;
import com.cc.api.pojo.sys.User;
import com.cc.api.utils.Methodc;
import com.cc.api.utils.sys.AuthUtil;
import com.cc.api.utils.sys.YmlConfig;
import com.cc.api.utils.sys.bean.JwtTokenFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.cc.api.config.SecurityConstants.REQ_HEADER;

/**
 * @Description 接口请求时认证校验
 * @Author CandyMuj
 * @Date 2019/12/26 10:39
 * @Version 1.0
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Resource
    private JwtTokenFactory jwtTokenFactory;


    private static final String SIGN_FIELD = YmlConfig.getString("interface.auth.sign.field");
    private static final String SIGN_NONCE = YmlConfig.getString("interface.auth.sign.nonce");
    private static final String SIGN_TIMESPAN = YmlConfig.getString("interface.auth.sign.timespan");
    private static final Long SIGN_LIMITED = YmlConfig.getLong("interface.auth.sign.limited");

    // 将接口鉴权uri的鉴权结果做一个缓存，不用每次都去循环配置内的排除鉴权项
    private static final Map<String, Boolean> INTERFACE_EXCLUDE_RES = new HashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(REQ_HEADER);


        boolean token = true;
        if (handler instanceof HandlerMethod) {
            // 参数签名校验
            if (!exclude(request.getRequestURI())) {
                Map<String, String> params = new HashMap<>();
                request.getParameterMap().forEach((k, v) -> {
                    params.put(k, v[0]);
                });
                if (StrUtil.isBlank(params.get(SIGN_NONCE))) {
                    throw new AuthException("缺少必要参数[".concat(SIGN_NONCE).concat("]"), StatusCode.FAIL);
                }
                if (StrUtil.isBlank(params.get(SIGN_TIMESPAN))) {
                    throw new AuthException("缺少必要参数[".concat(SIGN_TIMESPAN).concat("]"), StatusCode.FAIL);
                }
                if (!Methodc.generateSignature(params).equals(params.get(SIGN_FIELD))) {
                    throw new AuthException("非法请求", StatusCode.FAIL);
                }
                String verTimespan = verTimespan(Long.valueOf(params.get(SIGN_TIMESPAN)));
                if (verTimespan != null) {
                    throw new AuthException(verTimespan, StatusCode.FAIL);
                }
            }


            // 接口及用户鉴权
            HandlerMethod h = (HandlerMethod) handler;
            if (!Arrays.asList(h.getMethod().getParameterTypes()).contains(User.class)) {
                token = false;
            }

            // 获取自定义注解
            Ann ann = h.getMethod().getDeclaredAnnotation(Ann.class);
            if (ann != null) {
                // 验证此接口是否需要鉴权,且token是否有效
                if (ann.au()) {
                    log.debug("AUTH : true");

                    User user = jwtTokenFactory.validateToken(AuthUtil.getToken(authorization));
                    if (user == null) {
                        log.debug("AUTH : validation failed");
                        throw new AuthException("validation failed");
                    }
                } else {
                    log.debug("AUTH : false");
                }
            } else {
                log.debug("AUTH : false");
            }
        } else {
            token = false;
        }

        // 进行接口鉴权 及 格式验证
        // 接口鉴权和方法内的鉴权不冲突，即使接口鉴权通过了，如果是一个方法，还是得看这个方法的认证是否通过才能最终确定是否有权访问
        if (!auth(request.getRequestURI(), authorization) && !token) {
            log.debug("AUTH : interface auth validation failed");
            throw new AuthException("interface auth validation failed");
        }

        log.debug("AUTH : validation success");
        return true;
    }


    /**
     * 验证时间戳 - 可有效的避免抓包，伪造请求
     * 这将要求用户的系统时间与后端程序所在服务器的系统时间差在设定的允许的时间段内
     * 若不需要此功能，可使此方法直接返回null，其他地方不需要更改，上方该验证就验证，前端该传就还是传，不影响的。只需要这里返回null就可以移除此校验了，无需更改过多的代码逻辑
     * <p>
     * 此处若依然存在用户与系统时间不一致的问题（需确定用户确实是真实的系统时间，否则这种请求本就不被允许）
     * * 可以规定前端传递的时间戳为 yyy-mm-dd hh:mm:ss 不要毫秒的时间戳（不要直接用new date().getTime获取完整的时间戳）
     * * 后台在此处验证时同理
     * 通过此方式，获取指定格式的时间戳（若判断时间为分钟，那么至少日期格式到分钟，若为小时，格式则到小时，其他情况同理）
     * 建议根据毫秒判断，格式到秒。如此能够最大限度的规避错误
     *
     * @param timespan
     * @return
     */
    private String verTimespan(Long timespan) {
        long diff = System.currentTimeMillis() - timespan;

        // 判断传入的时间戳与当前系统时间相差是否小于一分钟
        if (diff < -SIGN_LIMITED) {
            return "错误的时间戳：超前的请求";
        }
        // 判断传入的时间戳与当前系统时间相差是否超过一分钟
        if (diff > SIGN_LIMITED) {
            return "错误的时间戳：过时的请求";
        }

        return null;
    }

    /**
     * 接口鉴权
     *
     * @return true: 成功
     */
    private boolean auth(String uri, String authorization) {
        boolean res = exclude(uri);

        if (!res) {
            // 先验证是否是basictoken，如果是则校验是否正确；若不是才会执行后面的bearertoken校验，因为用的是||符号，此时校验通过也意味着接口鉴权成功，而且是处于登录状态的成功
            // 而且鉴权返回成功与否，到上一层的时候也依然会返回403错误，因此此处理方法可以保证登录后的token依然有失效的验证处理
            if ((AuthUtil.realAuthSplit(authorization) && AuthUtil.getAuthToken().equals(AuthUtil.getAuthToken(authorization))) || jwtTokenFactory.validateToken(AuthUtil.getToken(authorization)) != null) {
                res = true;
            } else {
                res = false;
            }
        }

        return res;
    }

    /**
     * 验证是否在排除列表，若是直接鉴权成功
     */
    private boolean exclude(String uri) {
        if (SpringActive.isDev()) return true;

        Boolean res = INTERFACE_EXCLUDE_RES.get(uri);
        if (res == null) {
            res = false;

            List<String> exclude = YmlConfig.getList("interface.auth.exclude");
            for (String str : exclude) {
                if (str.contains("*")) {
                    str = str.replace("*", ".*");
                    Pattern pattern = Pattern.compile(str);
                    res = pattern.matcher(uri).matches();
                } else {
                    res = str.equals(uri);
                }

                if (res) {
                    break;
                }
            }

            INTERFACE_EXCLUDE_RES.put(uri, res);
        }

        return res;
    }

}
