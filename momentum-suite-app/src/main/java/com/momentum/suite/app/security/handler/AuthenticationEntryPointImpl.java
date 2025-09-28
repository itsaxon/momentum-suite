package com.momentum.suite.app.security.handler;

import cn.hutool.json.JSONUtil;
import com.momentum.suite.client.common.ApiResponse;
import com.momentum.suite.client.common.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 认证入口点实现
 * <p>
 * 当一个未认证的用户尝试访问需要权限的资源时，此方法被调用。
 * 我们覆盖默认行为，返回一个自定义的 JSON 响应，而不是重定向到登录页。
 * </p>
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 设置响应状态码为 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应内容类型为 JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 设置字符编码
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 构建统一的 API 响应体
        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.failWithEmptyMap(ResultCode.USER_AUTH_ERROR, "认证失败，请先登录");

        // 将响应体写入输出流
        response.getWriter().write(JSONUtil.toJsonStr(apiResponse));
    }
}
