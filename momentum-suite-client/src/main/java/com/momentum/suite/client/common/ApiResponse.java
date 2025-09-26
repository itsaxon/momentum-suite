package com.momentum.suite.client.common;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用响应封装
 *
 * @author itsaxon
 * @version v1.0 2025/09/26
 */
@Getter
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String message;
    private final T data;
    private final long timestamp;

    // 构造函数保持私有
    private ApiResponse(IResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    private ApiResponse(IResultCode resultCode, T data, String message) {
        this.code = resultCode.getCode();
        this.message = message; // 使用传入的自定义消息
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResultCode.SUCCESS, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, data);
    }

    public static <T> ApiResponse<T> fail(IResultCode resultCode) {
        return new ApiResponse<>(resultCode, null);
    }

    public static <T> ApiResponse<T> failWithEmptyData(IResultCode resultCode, T emptyData) {
        return new ApiResponse<>(resultCode, emptyData);
    }

    public static ApiResponse<java.util.Map<String, Object>> failWithEmptyMap(IResultCode resultCode) {
        return new ApiResponse<>(resultCode, new HashMap<>());
    }

    public static ApiResponse<Map<String, Object>> failWithEmptyMap(IResultCode resultCode, String message) {
        return new ApiResponse<>(resultCode, new HashMap<>(), message);
    }

    public static ApiResponse<java.util.List<?>> failWithEmptyList(IResultCode resultCode) {
        return new ApiResponse<>(resultCode, new java.util.ArrayList<>());
    }

    public static <T> ApiResponse<T> fail(IResultCode resultCode, String message) {
        return new ApiResponse<>(resultCode.getCode(), message, null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
