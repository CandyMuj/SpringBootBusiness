package com.cc.pic.api.config.sys;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProJectName api
 * @FileName MybatisPlusConfig
 * @Description
 * @Author CandyMuj
 * @Date 2020/3/26 16:14
 * @Version 1.0
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus 的分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
