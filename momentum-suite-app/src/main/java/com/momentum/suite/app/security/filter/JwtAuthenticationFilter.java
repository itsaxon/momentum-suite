package com.momentum.suite.app.security.filter;

import com.momentum.suite.client.common.context.UserContextHolder;
import com.momentum.suite.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * <p>
 * 唯一负责处理 JWT 认证和用户上下文设置的组件。
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. 从请求头获取 Token
            String token = resolveToken(request);

            // 2. 如果 Token 存在且有效，则进行处理
            if (StringUtils.hasText(token)) {
                Claims claims = jwtTokenProvider.parseToken(token);
                if (!jwtTokenProvider.isTokenExpired(claims)) {
                    // 3. 解析用户信息
                    Long userId = claims.get("user_id", Long.class);
                    String username = claims.get("username", String.class);

                    // 4. 【关键】同时设置两个上下文
                    // 4.1 放入我们自己的业务上下文，供 Service/Domain 层使用
                    UserContextHolder.setUserId(userId);
                    UserContextHolder.setUsername(username);
                    log.debug("Set UserContextHolder: userId={}, username={}", userId, username);

                    // 4.2 放入 Spring Security 的安全上下文，用于框架的授权判断
                    UserDetails userDetails = new User(username, "", Collections.emptyList());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Set SecurityContextHolder for user: {}", username);
                }
            }
            // 5. 无论是否认证成功，都继续执行过滤器链
            filterChain.doFilter(request, response);

        } finally {
            UserContextHolder.clear();
            log.debug("UserContextHolder cleared.");
        }
    }

    /**
     * 从 HttpServletRequest 中解析出 Bearer Token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
