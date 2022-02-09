package com.cc.pic.api.pojo.sys;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cc.pic.api.utils.DB;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.cc.pic.api.config.StatusCode.FAIL;
import static com.cc.pic.api.config.StatusCode.SUCCESS;

/**
 * @ProJectName APIServer
 * @FileName Result
 * @Description
 * @Author CandyMuj
 * @Date 2019/12/25 11:49
 * @Version 1.0
 */
@Data
@Slf4j
public class Result<T> {
    @ApiModelProperty(value = "成功标识 布尔类型")
    public boolean success;
    @ApiModelProperty(value = "成功标识 1成功 0失败")
    public int code;
    @ApiModelProperty(value = "返回错误代码")
    public String errCode;
    @ApiModelProperty(value = "返回处理消息")
    public String msg;
    @ApiModelProperty(value = "返回数据对象")
    public T data;
    @ApiModelProperty(value = "扩展数据")
    public Map<String, Object> extData;

    // --------------------- 分页相关
    @ApiModelProperty(value = "分页：当前页")
    public Long curPage;
    @ApiModelProperty(value = "分页：总数据数")
    public Long totalCount;
    @ApiModelProperty(value = "分页：每页的数据数量")
    public Long pageSize;
    @ApiModelProperty(value = "分页：是否还有下一页")
    public Boolean hasNext;


    private Result() {
    }

    public Result(T data) {
        this(SUCCESS, data, null);
    }

    public Result(int code) {
        this(code, null);
    }

    public Result(T data, String msg) {
        this(SUCCESS, data, msg);
    }

    public Result(int code, String msg) {
        this(code, null, msg);
    }

    public Result(Throwable e) {
        this(FAIL, null, e.getMessage());
    }

    public Result(int code, T data, String msg) {
        this(code, data, null, msg);
    }

    public Result(int code, T data, String errCode, String msg) {
        this.success = (SUCCESS == code);
        this.code = code;
        this.data = data;
        this.errCode = errCode;
        this.msg = msg;

        this.printLog();
    }


    public void hasNextPage() {
        this.setHasNext((long) Math.ceil(this.totalCount / (double) this.pageSize) > this.curPage);
    }

    private void printLog() {
        if (StrUtil.isNotBlank(this.msg)) {
            log.info("Result Msg ===> {}", this.msg);
        }
    }


    public static <T> Result<T> OK() {
        return new Result<>(SUCCESS);
    }

    public static <T> Result<T> OK(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> OK(String msg) {
        return new Result<>(SUCCESS, msg);
    }

    public static <T> Result<T> OK(T data, String msg) {
        return new Result<>(SUCCESS, data, msg);
    }

    public static <T> Result<T> Error() {
        return Error("系统错误");
    }

    public static <T> Result<T> ErrorBusy() {
        return Error("服务器忙，请稍后再试...");
    }

    public static <T> Result<T> Error(String msg) {
        return Error((T) null, msg);
    }

    public static <T> Result<T> Error(T data, String msg) {
        return Error(data, null, msg);
    }

    public static <T> Result<T> Error(String errCode, String msg) {
        return Error(null, errCode, msg);
    }

    public static <T> Result<T> Error(T data, String errCode, String msg) {
        DB.setRollbackOnly();
        // 另一种spring提供的抛出异常的方法，用来更方便的抛出异常，使事务可以捕获，执行回滚
        // 但是在我这个框架里面不能用这种方式，此框架无法做到统一处理，因为在aop中也进行了error的返回那么就是死循环了（不停地进这个方法，不停地向上抛异常）
        // 如果用这种抛异常的方式，那么去掉方法体中的try catch catch (Exception e) { throw e;}继续向上抛，目的是让spring事务捕获这个异常
        // Assert.isTrue(false, msg);
        // throw new RuntimeException(msg);
        return new Result<>(FAIL, data, errCode, msg);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
