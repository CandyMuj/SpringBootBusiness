package com.cc.pic.api.config.sys.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description 在springboot启动时，进行的一些系统初始执行的逻辑
 * CommandLineRunner的方式初始化: 在程序启动成功后调用一次实现了CommandLineRunner接口的run方法，通过order=1指定最先执此类（默认值是一个很大的值，可以点order进去看）
 * @Author CandyMuj
 * @Date 2022/2/8 18:01
 * @Version 1.0
 */
@Slf4j
@Order(1)
@Component
public class SpringInitCommandLine implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        log.info("系统初始化...");

        log.info("系统初始化 完成!");
    }

}
