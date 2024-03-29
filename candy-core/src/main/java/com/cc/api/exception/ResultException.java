package com.cc.api.exception;

import com.cc.api.config.StatusCode;

/**
 * @Description 业务处理自定义异常，主要用于事务回滚
 * 想了下这个不能喝result直接集成，不能直接通过result来抛出此事务做回滚，因为有些场景就是需要处理失败后的逻辑，否则就只有抓异常了，那样反而不灵活
 * 这个异常就留着吧，全局也处理了的，但基本不会用此异常，当需要手动通过抛异常回滚事务的情况，建议使用断言
 * 这个异常也还是留着吧，万一用到了呢
 * @Author CandyMuj
 * @Date 2020/6/30 17:50
 * @Version 1.0
 */
public class ResultException extends RuntimeException {
    private Integer resCode = StatusCode.FAIL;


    public ResultException(String msg, Throwable t) {
        super(msg, t);
    }

    public ResultException(String msg) {
        super(msg);
    }

    public ResultException(String msg, Integer resCode) {
        super(msg);
        if (resCode != null) {
            this.resCode = resCode;
        }
    }

    public int getResCode() {
        return resCode;
    }
}
