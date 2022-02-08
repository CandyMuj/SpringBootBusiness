package com.cc.pic.api.config.sys.init;

import com.cc.pic.api.config.SpringActive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ProjectName SpringBootBusiness
 * @FileName SpringInitConstruct
 * @Description 在springboot启动时，进行的一些系统初始执行的逻辑
 * PostConstruct的方式初始化: @PostConstruct注解的方法将会在依赖注入完成后被自动调用。
 * @Author CandyMuj
 * @Date 2022/2/8 17:53
 * @Version 1.0
 */
@Slf4j
@Component
public class SpringInitConstruct {
    @Resource
    private Environment environment;


    @PostConstruct
    private void initSpringActive() {
        // 初始化环境配置文件
        SpringActive.init(environment.getActiveProfiles()[0]);
    }

}
