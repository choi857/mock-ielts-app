package com.apps.common.exception;

/**
 * 错误码枚举
 */
public enum ErrorCode {
    // 认证相关错误码
    USER_NOT_FOUND(401, "用户不存在，请检查用户名是否正确"),
    PASSWORD_ERROR(401, "密码错误，请重新输入"),
    TOKEN_EXPIRED(401, "登录已过期，请重新登录"),
    TOKEN_INVALID(401, "无效的登录凭证，请重新登录"),
    
    // 请求参数相关错误码
    REQUEST_BODY_EMPTY(400, "请求体不能为空，请检查请求参数"),
    REQUEST_PARAM_ERROR(400, "请求参数格式错误，请检查请求体格式"),
    PARAM_VALIDATION_ERROR(400, "参数验证失败，请检查输入参数"),
    
    // 权限相关错误码
    ACCESS_DENIED(403, "没有权限访问该资源，请联系管理员"),
    
    // 系统错误码
    SYSTEM_ERROR(500, "系统异常，请联系管理员处理");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 