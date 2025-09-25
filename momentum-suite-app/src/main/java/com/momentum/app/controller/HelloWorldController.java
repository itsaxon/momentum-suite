package com.momentum.app.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.momentum.app.common.ApiResponse;
import com.momentum.app.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;

    /**
     * 成功示例：返回值是业务对象 Dict，会被自动包装。
     */
    @GetMapping("/world")
    public Dict sayHello() {
        log.info("Request received for /hello/world endpoint.");
        return Dict.create()
                .set("greeting", "Hello, World! Welcome to Momentum Suite!")
                .set("activeProfile", activeProfile)
                .set("serverTime", DateUtil.now());
    }

}
