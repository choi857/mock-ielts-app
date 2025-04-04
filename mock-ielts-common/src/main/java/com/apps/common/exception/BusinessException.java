package com.apps.common.exception;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    
    private final Integer code;
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }
    
    public Integer getCode() {
        return code;
    }
} 