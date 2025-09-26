package com.momentum.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序启动类
 *
 * @author itsaxon
 * @version v1.0 2025/09/26
 */
@SpringBootApplication(scanBasePackages = "com.momentum")
@MapperScan("com.momentum.suite.infrastructure.persistent.mapper")
public class MomentumSuiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomentumSuiteApplication.class, args);
    }

}