package com.momentum.app;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Momentum Suite 应用程序启动类
 * <p>
 * {@code @SpringBootApplication} 是一个复合注解，它包含了：
 * <ul>
 *     <li>{@code @SpringBootConfiguration}: 标记该类为 Spring Boot 的配置类。</li>
 *     <li>{@code @EnableAutoConfiguration}: 启用 Spring Boot 的自动配置机制。</li>
 *     <li>{@code @ComponentScan}: 自动扫描该类所在包及其子包下的所有 Spring 组件。
 *     因为该类位于 'com.momentum.app' 包下，所以它会自动扫描 'com.momentum.app.controller' 等子包。</li>
 * </ul>
 *
 * @author liushuozhen
 */
@SpringBootApplication
@Slf4j
public class MomentumSuiteApplication {

    /**
     * Java 应用程序的主入口点。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MomentumSuiteApplication.class, args);
    }

}