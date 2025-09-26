package com.momentum.suite.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


/**
 * 移动登录请求
 *
 * @author itsaxon
 * @version v1.0 2025/09/26
 */
@Data
public class MobileLoginRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 终端类型
     */
    @NotBlank(message = "终端类型不能为空")
    private String platformId;

}

