package com.momentum.suite.client.view.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 手机登录请求
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Data
public class LoginAdminRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
