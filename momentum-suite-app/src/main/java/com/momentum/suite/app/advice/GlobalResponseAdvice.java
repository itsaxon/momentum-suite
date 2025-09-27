package com.momentum.suite.app.advice;

import cn.hutool.json.JSONUtil;
import com.momentum.suite.client.common.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应结果处理器
 *
 * @author itsaxon
 * @version v1.0 2025/09/26
 */
@RestControllerAdvice(basePackages = "com.momentum.suite.app.controller" )
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().isAssignableFrom(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return ApiResponse.success();
        }
        if (body instanceof ApiResponse) {
            return body;
        }
        if (body instanceof String) {
            return JSONUtil.toJsonStr(ApiResponse.success(body));
        }
        return ApiResponse.success(body);
    }
}
