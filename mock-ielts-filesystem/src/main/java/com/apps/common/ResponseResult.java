package com.apps.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {
    // 获取日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ResponseResult.class);

    private String status; // 成功或失败状态
    private String message; // 返回消息
    private Integer code; // HTTP 状态码
    private T data; // 返回数据

    // 成功响应
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>("success", "操作成功", HttpStatus.OK.value(), null);
    }

    public static <T> ResponseResult<T> success(String message) {
        return new ResponseResult<>("success", message, HttpStatus.OK.value(), null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>("success", "操作成功", HttpStatus.OK.value(), data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>("success", message, HttpStatus.OK.value(), data);
    }

    public static <T> ResponseResult<T> success(String message, Integer code, T data) {
        return new ResponseResult<>("success", message, code, data);
    }

    // 错误响应
    public static <T> ResponseResult<T> fail() {
        logger.warn("操作失败");
        return new ResponseResult<>("error", "操作失败", HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>("error", message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }

    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return new ResponseResult<>("error", message, code, null);
    }

    public static <T> ResponseResult<T> fail(String message, Integer code) {
        return new ResponseResult<>("error", message, code, null);
    }

    // 允许自定义状态和代码
    public static <T> ResponseResult<T> custom(String status, String message, Integer code, T data) {
        return new ResponseResult<>(status, message, code, data);
    }
}