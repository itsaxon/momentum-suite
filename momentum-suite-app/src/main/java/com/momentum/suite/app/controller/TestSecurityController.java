package com.momentum.suite.app.controller;

import com.momentum.suite.client.common.context.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/security")
@RequiredArgsConstructor
public class TestSecurityController {

    @GetMapping
    public String login() {
        Long userId = UserContextHolder.getUserId();

        String username = UserContextHolder.getUsername();

        return userId + ":" + username;
    }

}
