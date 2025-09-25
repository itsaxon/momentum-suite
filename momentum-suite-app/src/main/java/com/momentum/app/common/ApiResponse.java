package com.momentum.app.common;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * 通用 API 响应封装类
 *
 * @param <T> 响应数据的类型
 * @author Manus
 */
@Getter
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String message;
    private final T data;
    private final long timestamp;

    // 私有构造函数，接收任何实现了 IResultCode 接口的对象
    private ApiResponse(IResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // 私有构造函数，用于自定义消息
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toEpochMilli();
    }

    // --- 静态工厂方法 ---

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResultCode.SUCCESS, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, data);
    }

    /**
     * 核心优势：此方法可以接收任何实现了 IResultCode 接口的枚举实例。
     * 例如：ApiResponse.fail(ResultCode.FAILURE) 或 ApiResponse.fail(OrderResultCode.ORDER_NOT_FOUND)
     */
    public static <T> ApiResponse<T> fail(IResultCode resultCode) {
        return new ApiResponse<>(resultCode, null);
    }

    public static <T> ApiResponse<T> fail(IResultCode resultCode, String message) {
        return new ApiResponse<>(resultCode.getCode(), message, null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
