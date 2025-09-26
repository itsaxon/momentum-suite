package com.momentum.app.advice;

import com.momentum.suite.client.common.ApiResponse;
import com.momentum.suite.client.common.ResultCode;
import com.momentum.suite.client.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局异常处理程序
 *
 * @author itsaxon
 * @version v1.0 2025/09/26
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.momentum.app.controller")
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常 (BusinessException)
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Map<String, Object>> handleBusinessException(BusinessException ex) {
        log.warn("业务异常 (BusinessException): code={}, message={}", ex.getResultCode().getCode(), ex.getResultCode().getMessage());
        return ApiResponse.failWithEmptyMap(ex.getResultCode());
    }

    /**
     * 处理参数校验异常 (MethodArgumentNotValidException)
     * 通常由 @Valid 注解触发
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        // 只获取第一个校验不通过的字段的错误信息
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.warn("参数校验异常 (ValidationException): {}", errorMessage);
        return ApiResponse.failWithEmptyMap(ResultCode.PARAM_VALID_ERROR, errorMessage);
    }

    /**
     * 处理所有未被捕获的其他异常 (Exception)
     * 这是最后的防线
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Map<String, Object>> handleUnexpectedException(Exception ex) {
        log.error("系统未知异常 (UnexpectedException): ", ex);
        return ApiResponse.failWithEmptyMap(ResultCode.SERVICE_UNAVAILABLE);
    }
}
