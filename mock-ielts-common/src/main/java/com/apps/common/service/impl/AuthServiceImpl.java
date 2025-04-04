package com.apps.common.service.impl;

import com.apps.common.exception.AuthException;
import com.apps.common.exception.ErrorCode;
import com.apps.common.model.dto.LoginRequest;
import com.apps.common.model.dto.RefreshTokenRequest;
import com.apps.common.service.AuthService;
import com.apps.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Override
    public Map<String, String> generateToken(LoginRequest request) {
        try {
            // 进行身份认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 生成Token
            String token = jwtUtil.generateToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // 返回Token信息
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("refreshToken", refreshToken);
            result.put("expiresIn", String.valueOf(expiration));
            result.put("refreshExpiresIn", String.valueOf(refreshExpiration));
            return result;
        } catch (AuthenticationException e) {
            log.error("认证失败：用户名={}, 原因={}", request.getUsername(), e.getMessage());
            if (e instanceof AuthException) {
                throw e;
            }
            throw new AuthException(ErrorCode.PASSWORD_ERROR);
        }
    }

    @Override
    public Map<String, String> refreshToken(RefreshTokenRequest request) {
        try {
            // 验证refreshToken
            if (!jwtUtil.validateRefreshToken(request.getRefreshToken())) {
                log.error("刷新Token失败：Token已过期或无效");
                throw new AuthException(ErrorCode.TOKEN_EXPIRED);
            }

            // 从refreshToken中获取用户信息
            String username = jwtUtil.extractUsername(request.getRefreshToken());
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, null);

            // 生成新的Token
            String token = jwtUtil.generateToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // 返回新的Token信息
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("refreshToken", refreshToken);
            result.put("expiresIn", String.valueOf(expiration));
            result.put("refreshExpiresIn", String.valueOf(refreshExpiration));
            return result;
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("刷新Token失败：原因={}", e.getMessage());
            throw new AuthException(ErrorCode.SYSTEM_ERROR, "刷新Token失败：" + e.getMessage());
        }
    }
} 