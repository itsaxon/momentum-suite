package com.momentum.app.listener;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 应用启动监听器，在应用准备就绪后打印启动成功信息。
 *
 * @author Manus (Refined Version)
 */
@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 获取应用上下文和环境信息
        ConfigurableApplicationContext context = event.getApplicationContext();
        Environment environment = context.getEnvironment();

        // 获取应用名称、端口号和上下文路径
        String appName = environment.getProperty("spring.application.name", "MomentumSuite");
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");

        // 使用 hutool 的 StrUtil.isNotBlank 替代 Spring 的 StringUtils.hasText
        if (StrUtil.isNotBlank(contextPath) && !StrUtil.startWith(contextPath, "/")) {
            contextPath = "/" + contextPath;
        }

        // 本地访问地址
        String localAccessUrl = String.format("http://127.0.0.1:%s%s", port, contextPath );

        // 局域网访问地址
        String networkIp = NetUtil.getLocalhostStr();
        String networkAccessUrl = String.format("http://%s:%s%s", networkIp, port, contextPath );

        // 打印最终版的日志格式
        log.info("\n" +
                        "---------------------------------------------------------------------------------------\n" +
                        "  ✅  Application '{}' has started successfully! 🚀\n" +
                        "  \n" +
                        "  ➡️  Local Access:   {}\n" +
                        "  ➡️  Network Access: {}\n" +
                        "---------------------------------------------------------------------------------------",
                appName,
                localAccessUrl,
                networkAccessUrl
        );
    }
}
