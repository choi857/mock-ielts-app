package com.apps.common.service;

import com.apps.common.model.dto.LoginRequest;
import com.apps.common.model.dto.RefreshTokenRequest;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 生成Token
     *
     * @param request 登录请求
     * @return Token信息
     */
    Map<String, String> generateToken(LoginRequest request);
    
    /**
     * 刷新Token
     *
     * @param request 刷新Token请求
     * @return 新的Token信息
     */
    Map<String, String> refreshToken(RefreshTokenRequest request);
} 