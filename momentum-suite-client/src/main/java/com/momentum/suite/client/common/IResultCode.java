package com.momentum.suite.client.common;

/**
 * 统一响应状态码接口 (契约)
 * <p>
 * 所有业务状态码枚举都应实现此接口，以确保它们都能被 ApiResponse.fail() 方法接受。
 *
 * @author itsaxon
 */
public interface IResultCode {

    /**
     * 获取状态码
     *
     * @return 状态码 (int)
     */
    int getCode();

    /**
     * 获取响应消息
     *
     * @return 响应消息 (String)
     */
    String getMessage();
}
