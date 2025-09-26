package com.momentum.suite.client.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录成功视图对象 (VO)
 * <p>
 * 封装了登录成功后需要返回给前端的核心凭证和信息。
 *
 * @author itsaxon
 */
@Data
@Accessors(chain = true)
public class LoginSuccessVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌 (Access Token)
     * 用于后续接口调用的身份认证。
     */
    private String token;

    /**
     * 用户唯一编号 (UserID)
     */
    private String userID;

    // 还可以根据需要添加其他信息，例如：
    // /**
    //  * 刷新令牌 (Refresh Token)，用于在访问令牌过期后获取新的令牌
    //  */
    // private String refreshToken;
    //
    // /**
    //  * 令牌过期时间 (单位：秒)
    //  */
    // private Long expiresIn;
}
