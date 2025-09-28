package com.momentum.suite.service.security;

import com.momentum.suite.infrastructure.persistent.po.AdminUserInfoPO;
import com.momentum.suite.service.AdminUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 后台管理用户 UserDetailsService 实现
 * <p>
 * 负责从数据库加载用户信息，供 Spring Security 进行认证。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    private final AdminUserInfoService adminUserInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名从数据库查询用户信息
        AdminUserInfoPO adminUser = adminUserInfoService.lambdaQuery()
                .eq(AdminUserInfoPO::getUsername, username)
                .one();

        // 2. 如果用户不存在，抛出异常
        if (adminUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 3. 如果用户被禁用，也可以在这里抛出异常 (可选，但推荐)
        if (!adminUser.getEnable()) {
            throw new UsernameNotFoundException("用户已被禁用: " + username);
        }

        // 4. 将我们自己的 PO 对象转换成 Spring Security 的 UserDetails 对象
        // 参数：用户名、加密后的密码、权限列表（这里先用空列表）
        return new User(adminUser.getUsername(), adminUser.getPassword(), Collections.emptyList());
    }
}
