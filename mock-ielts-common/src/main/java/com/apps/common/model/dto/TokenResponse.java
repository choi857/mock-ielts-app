package com.apps.common.model.dto;

import lombok.Data;

/**
 * Token响应DTO
 */
@Data
public class TokenResponse {
    /**
     * 访问令牌
     */
    private String token;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌过期时间（毫秒）
     */
    private Long expiresIn;

    /**
     * 刷新令牌过期时间（毫秒）
     */
    private Long refreshExpiresIn;
} 