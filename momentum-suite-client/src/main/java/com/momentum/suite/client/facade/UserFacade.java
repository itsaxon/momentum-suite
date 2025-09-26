package com.momentum.suite.client.facade;

import com.momentum.suite.client.dto.LoginSuccessVO;
import com.momentum.suite.client.dto.MobileLoginRequest;

/**
 * 用户服务门面接口
 */
public interface UserFacade {

    /**
     * 处理手机号验证码登录
     * @param request 包含登录信息的请求对象
     * @return 登录成功后的凭证和用户信息
     */
    LoginSuccessVO loginByMobile(MobileLoginRequest request);

}
