package com.momentum.app.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预定义响应状态码枚举
 * <p>
 * 实现了 IResultCode 接口，作为通用的状态码集合。
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

    // --- 通用成功 ---
    SUCCESS(200, "success"),

    // --- 通用失败 (1000-1999) ---
    FAILURE(1001, "操作失败"),
    PARAM_VALID_ERROR(1002, "参数校验失败"),
    RESOURCE_NOT_FOUND(1003, "资源未找到"),
    SERVICE_UNAVAILABLE(1004, "服务暂不可用"),

    // --- 用户相关 (2000-2999) ---
    USER_NOT_LOGIN(2001, "用户未登录或凭证已过期"),
    USER_AUTH_ERROR(2002, "用户认证失败，账号或密码错误"),
    USER_ACCESS_DENIED(2003, "用户无权访问此资源"),
    USER_ACCOUNT_LOCKED(2004, "用户账号已被锁定"),

    // --- 业务相关 (3000-...) ---
    ORDER_CREATE_FAIL(3001, "订单创建失败");

    private final int code;
    private final String message;
}
