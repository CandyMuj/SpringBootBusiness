package com.cc.api.utils;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.api.pojo.sys.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description 关于数据库操作相关的工具
 * @Author CandyMuj
 * @Date 2020/4/11 12:43
 * @Version 1.0
 */
@Slf4j
public class DB {

    /**
     * 手动回滚事务
     */
    public static void setRollbackOnly() {
        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.debug("当前事务回滚成功");
        } catch (NoTransactionException e) {
            log.debug("当前没有事务存在");
        }
    }

    /**
     * 通过orderby的map对象，解析出orderby的字符串
     * 由于排序是有权重的，因此返回的语句会按照map顺序生成
     * <p>
     * key: 排序字段
     * val: 是否升序 默认：true
     */
    public static String orderByStr(Map<String, Boolean> orderMap) {
        List<String> strList = new ArrayList<>();
        for (Map.Entry<String, Boolean> e : orderMap.entrySet()) {
            strList.add(e.getKey().concat(Optional.ofNullable(e.getValue()).orElse(true) ? " ASC" : " DESC"));
        }
        return strList.size() != 0 ? "ORDER BY ".concat(CollUtil.join(strList, ",")) : "";
    }

    /**
     * mybatis-plus 传入page解析成result对象返回
     */
    public static <T> Result<List<T>> getPageRes(Page<T> page) {
        Result<List<T>> result = new Result<>(page.getRecords());
        result.curPage = page.getCurrent();
        result.pageSize = page.getSize();
        result.totalCount = page.getTotal();
        result.hasNextPage();
        return result;
    }

    /**
     * pagehelper 根据pageinfo解析成Result
     */
    public static <T> Result<List<T>> getPageRes(PageInfo<T> pageInfo) {
        Result<List<T>> result = new Result<>(pageInfo.getList());
        result.curPage = (long) pageInfo.getPageNum();
        result.pageSize = (long) pageInfo.getPageSize();
        result.totalCount = pageInfo.getTotal();
        result.hasNextPage();
        return result;
    }

    public static <T> Result<List<T>> getPageRes(List<T> data) {
        return getPageRes(new PageInfo<>(data));
    }

    /**
     * pagehelper 传入page解析成result对象返回
     */
    public static <T> Result<List<T>> getPageRes(com.github.pagehelper.Page<T> page) {
        Result<List<T>> result = new Result<>(page.getResult());
        result.curPage = (long) page.getPageNum();
        result.pageSize = (long) page.getPageSize();
        result.totalCount = page.getTotal();
        result.hasNextPage();
        return result;
    }

    public static <T> Result<List<T>> getPageRes(org.springframework.data.domain.Page<T> page) {
        Result<List<T>> result = new Result<>(page.getContent());
        result.curPage = (long) page.getPageable().getPageNumber();
        result.pageSize = (long) page.getPageable().getPageSize();
        result.totalCount = page.getTotalElements();
        result.hasNextPage();
        return result;
    }

}
