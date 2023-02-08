package com.cc.pic.api.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cc.pic.api.base.enumc.LogType;
import com.cc.pic.api.base.mapper.SysLogMapper;
import com.cc.pic.api.base.pojo.SysLog;
import com.cc.pic.api.base.pojo.vo.SysLogVo;
import com.cc.pic.api.base.service.ISysLogService;
import com.cc.pic.api.pojo.sys.Result;
import com.cc.pic.api.utils.DB;
import com.cc.pic.api.utils.IpUtil;
import com.cc.pic.api.utils.sys.YmlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description
 * @Author CandyMuj
 * @Date 2020/09/30 14:58
 * @Version 1.0
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {


    /**
     * 添加日志
     *
     * @param logType       日志类型，枚举值
     * @param userAccountId 用户ID，仅在调用系统接口时可能存在
     * @param describe      操作描述
     * @param restUrl       接口地址
     * @param restParam     接口参数
     * @param oldParam      如果是修改，那么这是修改前的数据
     */
    @Override
    public void ins(LogType logType, Long userAccountId, String describe, String restUrl, String restParam, HttpServletRequest request, String oldParam, Result<?> result) {
        int length = 1000;
        SysLog sysLog = new SysLog();
        sysLog.setLogType(logType.getType());
        sysLog.setUserAccountId(userAccountId);
        sysLog.setDescribe(describe);
        sysLog.setOldParam((StrUtil.isNotBlank(oldParam) && oldParam.length() > length) ? oldParam.substring(0, length - 1) : oldParam);
        sysLog.setAddTime(new Date());

        if (request == null) {
            sysLog.setRestUrl(restUrl);
        } else {
            sysLog.setIp(IpUtil.getRealIP(request));
            sysLog.setRestUrl(request.getRequestURI());
            Map<String, String[]> params = request.getParameterMap();
            if (params != null) {
                params = new HashMap<>(params);
                // 移除sign字段
                params.put(YmlConfig.getString("interface.auth.sign.field"), null);
                // 移除随机字符
                params.put(YmlConfig.getString("interface.auth.sign.nonce"), null);
                // 移除时间戳
                params.put("timespan", null);
                // 把密码删掉，因为传参是明文的不能存
                if (params.get("password") != null) {
                    params.put("password", new String[]{"******"});
                }
                if (params.get("oldpwd") != null) {
                    params.put("oldpwd", new String[]{"******"});
                }
                if (params.get("newpwd") != null) {
                    params.put("newpwd", new String[]{"******"});
                }

                restParam = JSONObject.toJSONString(params);
            }
        }

        if (StrUtil.isNotBlank(restParam)) {
            restParam = restParam.length() > length ? restParam.substring(0, length - 1) : restParam;
            sysLog.setRestParam(restParam);
        }

        if (result != null) {
            Result<?> result1 = new Result<>((Object) null);
            result1.code = result.code;
            result1.curPage = result.curPage;
            result1.totalCount = result.totalCount;
            result1.pageSize = result.pageSize;
            result1.errCode = result.errCode;
            result1.msg = result.msg;
            sysLog.setResult(JSONObject.toJSONString(result1));
        }

        if (!sysLog.insert()) {
            log.error("添加日志失败");
        }
    }

    @Override
    public void ins(LogType logType, String describe, String restUrl, String restParam, String oldParam, Result<?> result) {
        this.ins(logType, null, describe, restUrl, restParam, null, oldParam, result);
    }

    @Override
    public void ins(LogType logType, String describe, String restUrl, String restParam, Result<?> result) {
        this.ins(logType, describe, restUrl, restParam, null, result);
    }

    @Override
    public void ins(LogType logType, String describe, Result<?> result) {
        this.ins(logType, describe, (String) null, (String) null, result);
    }


    @Override
    public void ins(LogType logType, Long userAccountId, String describe, HttpServletRequest request, String oldParam, Result<?> result) {
        this.ins(logType, userAccountId, describe, null, null, request, oldParam, result);
    }

    @Override
    public void ins(LogType logType, Long userAccountId, String describe, HttpServletRequest request, Result<?> result) {
        this.ins(logType, userAccountId, describe, request, null, result);
    }

    @Override
    public void ins(LogType logType, String describe, HttpServletRequest request, String oldParam, Result<?> result) {
        this.ins(logType, null, describe, request, oldParam, result);
    }

    @Override
    public void ins(LogType logType, String describe, HttpServletRequest request, Result<?> result) {
        this.ins(logType, describe, request, null, result);
    }


    @Override
    public void add(LogType logType, Long userAccountId, String describe, String restUrl, String restParam, String oldParam) {
        this.ins(logType, userAccountId, describe, restUrl, restParam, null, oldParam, null);
    }

    @Override
    public void add(LogType logType, String describe, String restUrl, String restParam, String oldParam) {
        this.add(logType, null, describe, restUrl, restParam, oldParam);
    }

    @Override
    public void add(LogType logType, String describe, String restUrl, String restParam) {
        this.add(logType, describe, restUrl, restParam, null);
    }

    @Override
    public void add(LogType logType, String describe) {
        this.add(logType, describe, (String) null, (String) null);
    }

    @Override
    public void add(LogType logType, Long userAccountId, String describe, HttpServletRequest request, String oldParam) {
        this.ins(logType, userAccountId, describe, null, null, request, oldParam, null);
    }

    @Override
    public void add(LogType logType, Long userAccountId, String describe, HttpServletRequest request) {
        this.add(logType, userAccountId, describe, request, null);
    }

    @Override
    public void add(LogType logType, String describe, HttpServletRequest request, String oldParam) {
        this.add(logType, null, describe, request, oldParam);
    }

    @Override
    public void add(LogType logType, String describe, HttpServletRequest request) {
        this.add(logType, describe, request, null);
    }

    /**
     * 系统日志查询
     */
    @Override
    public Result<List<SysLogVo>> list(SysLogVo sysLogVo) {
        return DB.getPageRes(baseMapper.logList(sysLogVo));
    }

}
