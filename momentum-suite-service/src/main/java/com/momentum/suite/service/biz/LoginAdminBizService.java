package com.momentum.suite.service.biz;

// ... 其他 import ...

import com.momentum.suite.client.exception.BusinessException;
import com.momentum.suite.client.view.request.AdminUserCreateRequest;
import com.momentum.suite.client.view.request.LoginAdminRequest;
import com.momentum.suite.client.view.vo.AdminUserCreateVo;
import com.momentum.suite.client.view.vo.LoginAdminVo;
import com.momentum.suite.infrastructure.persistent.po.AdminUserInfoPO;
import com.momentum.suite.infrastructure.security.JwtTokenProvider;
import com.momentum.suite.service.AdminUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAdminBizService {

    private final AdminUserInfoService adminUserInfoService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder; // 【关键】注入密码编码器

    /**
     * 创建一个新的后台管理员用户
     *
     * @param request 包含用户信息的请求
     * @return 包含新用户ID的VO
     */
    public AdminUserCreateVo createUser(AdminUserCreateRequest request) {
        log.info("开始创建新的后台管理员，用户名: {}", request.getUsername());

        // 1. 检查用户名是否已存在
        boolean exists = adminUserInfoService.lambdaQuery()
                .eq(AdminUserInfoPO::getUsername, request.getUsername())
                .exists();

        if (exists) {
            log.warn("创建用户失败，用户名 '{}' 已存在", request.getUsername());
            throw new BusinessException("用户名已存在");
        }

        // 2. 【核心】对明文密码进行加密
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("密码已成功加密");

        // 3. 构建 PO 对象
        AdminUserInfoPO newUser = new AdminUserInfoPO();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(encodedPassword); // 存储加密后的密码
        newUser.setNickname(request.getNickname());
        // 其他字段（如 enable, del_flag）会由数据库默认值或自动填充处理器处理

        // 4. 保存到数据库
        adminUserInfoService.save(newUser);
        log.info("新用户 '{}' 创建成功，用户ID: {}", newUser.getUsername(), newUser.getId());

        // 5. 封装并返回结果
        return new AdminUserCreateVo().setUserId(String.valueOf(newUser.getId()));
    }

    public LoginAdminVo login(LoginAdminRequest request) {
        log.info("开始处理后台管理端登录请求，用户名: {}", request.getUsername());

        // 1. 【关键】调用 AuthenticationManager.authenticate() 进行认证
        // Spring Security 会自动调用我们上面写的 AdminUserDetailsServiceImpl 和 PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 如果上面的代码没有抛出异常，说明认证成功
        log.info("用户 '{}' 认证成功", request.getUsername());

        // 2. 将认证信息存入 SecurityContext (可选，但推荐)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 根据用户名查询完整的用户信息，以获取用户ID
        AdminUserInfoPO adminUser = adminUserInfoService.lambdaQuery()
                        .eq(AdminUserInfoPO::getUsername, request.getUsername())
                .one();

        // 4. 生成我们自己系统的 JWT
        String token = jwtTokenProvider.generateToken(adminUser.getId(), adminUser.getUsername(), "admin");
        log.info("为后台用户 '{}' 生成 Token 成功", request.getUsername());

        // 5. 封装并返回结果
        LoginAdminVo vo = new LoginAdminVo();
        vo.setToken(token);
        vo.setUserID(String.valueOf(adminUser.getId()));

        return vo;
    }
}
