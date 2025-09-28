package com.momentum.suite.app.controller;

import com.momentum.suite.client.view.request.LoginAppMobileRequest;
import com.momentum.suite.client.view.vo.LoginAppMobileVo;
import com.momentum.suite.service.biz.LoginAppMobileBizService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/app")
@RequiredArgsConstructor
public class LoginAppController {

    private final LoginAppMobileBizService loginAppMobileBizService;

    @PostMapping("/login")
    public LoginAppMobileVo login(@RequestBody @Validated LoginAppMobileRequest request) {
        return loginAppMobileBizService.login(request);
    }

}
