package com.momentum.app.controller;

import com.momentum.suite.client.dto.LoginSuccessVO;
import com.momentum.suite.client.dto.MobileLoginRequest;
import com.momentum.suite.client.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserFacade userFacade;

    @PostMapping("/app/login")
    public LoginSuccessVO loginByMobile(@RequestBody @Validated MobileLoginRequest request) {
        return userFacade.loginByMobile(request);
    }
}