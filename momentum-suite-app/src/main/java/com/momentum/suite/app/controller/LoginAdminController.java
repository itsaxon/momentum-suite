package com.momentum.suite.app.controller;

import com.momentum.suite.client.view.request.AdminUserCreateRequest;
import com.momentum.suite.client.view.request.LoginAdminRequest;
import com.momentum.suite.client.view.vo.AdminUserCreateVo;
import com.momentum.suite.client.view.vo.LoginAdminVo;
import com.momentum.suite.service.biz.LoginAdminBizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class LoginAdminController {

    private final LoginAdminBizService loginAdminBizService;

    /**
     * 创建一个新的后台管理员用户
     * <p>
     * 注意：在生产环境中，此接口应受到严格的权限保护。
     * </p>
     *
     * @param request 包含用户信息的请求
     * @return 包含新用户ID的响应
     */
    @PostMapping("/createUser")
    public AdminUserCreateVo createUser(@RequestBody @Valid AdminUserCreateRequest request) {
        return loginAdminBizService.createUser(request);
    }

    @PostMapping("/login")
    public LoginAdminVo login(@RequestBody @Validated LoginAdminRequest request) {
        return loginAdminBizService.login(request);
    }

}
