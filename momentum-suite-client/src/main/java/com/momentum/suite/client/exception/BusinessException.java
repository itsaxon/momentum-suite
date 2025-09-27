package com.momentum.suite.client.exception;

import com.momentum.suite.client.common.IResultCode;
import com.momentum.suite.client.common.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final IResultCode resultCode;

    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(IResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public BusinessException(String message) {
        super(message);
        this.resultCode = new IResultCode() {
            @Override
            public int getCode() {
                return ResultCode.FAILURE.getCode();
            }
            @Override
            public String getMessage() {
                return message;
            }
        };
    }
}
