package com.momentum.suite.client.view.vo;

import lombok.Data;

/**
 * 手机登录响应
 *
 * @author itsaxon
 * @version v1.0 2025/09/27
 */
@Data
public class LoginAppMobileVo {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 用户唯一编号
     */
    private String userId;

}
