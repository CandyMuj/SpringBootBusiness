package com.cc.api.config.sys.c;

import cn.hutool.core.util.StrUtil;
import com.cc.api.exception.AuthException;
import com.cc.api.exception.CandyException;
import com.cc.api.exception.ResultException;
import com.cc.api.pojo.sys.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;


/**
 * @Description 全局异常处理
 * @Author CandyMuj
 * @Date 2019/12/25 15:00
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 认证异常处理
     */
    @ExceptionHandler(AuthException.class)
    public Result<?> authException(AuthException e) {
        String msg = e.getMessage();
        return new Result<>(e.getResCode(), (StrUtil.isNotBlank(msg) ? msg : "Authentication failed"));
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(ResultException.class)
    public Result<?> resultException(ResultException e) {
        String msg = e.getMessage();
        return new Result<>(e.getResCode(), (StrUtil.isNotBlank(msg) ? msg : "error"));
    }

    /**
     * 其他自定义异常处理
     */
    @ExceptionHandler(CandyException.class)
    public Result<?> candyException(CandyException e) {
        String msg = e.getMessage();
        return Result.Error(StrUtil.isNotBlank(msg) ? msg : "error");
    }

    /**
     * 方法参数校验 绑定参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> validationException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        return Result.Error(error != null ? error.getDefaultMessage() : "null");
    }

    /**
     * 接口参数校验异常处理
     */
    @ExceptionHandler(ValidationException.class)
    public Result<?> validationException(ValidationException e) {
        String msg = e.getMessage();
        return Result.Error(StrUtil.isNotBlank(msg) ? msg.split(", ")[0].split(": ")[1] : "null");
    }

    /**
     * 其他所有的异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> illegalArgumentException(IllegalArgumentException e) {
        String msg = e.getMessage();
        return Result.Error(StrUtil.isNotBlank(msg) ? msg : "error");
    }


    /**
     * 其他所有的异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<?> exception(Exception e) {
        log.error("software running error...", e);

        String msg = e.getMessage();
        return StrUtil.isNotBlank(msg) ? Result.Error(msg) : Result.ErrorBusy();
    }

}
