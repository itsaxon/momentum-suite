package com.momentum.suite.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 全局配置
 * <p>
 * 用于注册拦截器、配置跨域、消息转换器等。
 * </p>
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

     @Override
     public void addCorsMappings(CorsRegistry registry) {
         // 配置跨域
         registry.addMapping("/**")
                 .allowedOrigins("*")
                 .allowedMethods("GET", "POST", "PUT", "DELETE")
                 .maxAge(3600);
     }
}
