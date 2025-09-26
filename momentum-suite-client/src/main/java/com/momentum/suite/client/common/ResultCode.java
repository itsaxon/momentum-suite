package com.momentum.suite.client.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

    // ================= 通用状态码 =================
    SUCCESS(200, "success"),
    FAILURE(500, "failure"),

    // ================= 参数错误 1001-1999 =================
    PARAM_VALID_ERROR(1001, "参数校验失败"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    // ================= 用户错误 2001-2999 =================
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_AUTH_ERROR(2002, "用户认证失败或令牌无效"),
    USER_NOT_FOUND(2003, "用户不存在"),
    USER_HAS_EXISTED(2004, "用户已存在"),
    USER_ACCOUNT_DISABLED(2005, "账户已被禁用"), // <-- 新增的状态码
    USER_PASSWORD_ERROR(2006, "用户名或密码错误"),

    // ================= 业务错误 3001-3999 =================
    RESOURCE_NOT_FOUND(3001, "请求的资源不存在"),

    // ================= 系统错误 5001-5999 =================
    SERVICE_UNAVAILABLE(5001, "服务暂不可用，请稍后重试"),
    INTERNAL_SERVER_ERROR(5002, "服务器内部错误");


    private final int code;
    private final String message;
}
