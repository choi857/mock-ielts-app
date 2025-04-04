package com.apps.common.service;

import com.apps.common.model.dto.LoginRequest;
import com.apps.common.model.dto.RefreshTokenRequest;
import com.apps.common.service.impl.AuthServiceImpl;
import com.apps.common.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private RefreshTokenRequest refreshTokenRequest;
    private Authentication authentication;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // 设置JWT配置
        ReflectionTestUtils.setField(authService, "expiration", 86400000L);
        ReflectionTestUtils.setField(authService, "refreshExpiration", 604800000L);

        // 准备测试数据
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("testRefreshToken");

        // 模拟认证对象
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // 模拟用户详情
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void generateToken_Success() {
        // 模拟认证成功
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn("testToken");
        when(jwtUtil.generateRefreshToken(authentication)).thenReturn("testRefreshToken");

        // 执行测试
        Map<String, String> result = authService.generateToken(loginRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals("testToken", result.get("token"));
        assertEquals("testRefreshToken", result.get("refreshToken"));
        assertEquals("86400000", result.get("expiresIn"));
        assertEquals("604800000", result.get("refreshExpiresIn"));

        // 验证方法调用
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(authentication);
        verify(jwtUtil).generateRefreshToken(authentication);
    }

    @Test
    void generateToken_AuthenticationFailure() {
        // 模拟认证失败
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("认证失败"));

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> authService.generateToken(loginRequest));
    }

    @Test
    void refreshToken_Success() {
        // 模拟刷新Token验证成功
        when(jwtUtil.validateRefreshToken(refreshTokenRequest.getRefreshToken())).thenReturn(true);
        when(jwtUtil.extractUsername(refreshTokenRequest.getRefreshToken())).thenReturn("testUser");
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("newToken");
        when(jwtUtil.generateRefreshToken(any(Authentication.class))).thenReturn("newRefreshToken");

        // 执行测试
        Map<String, String> result = authService.refreshToken(refreshTokenRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals("newToken", result.get("token"));
        assertEquals("newRefreshToken", result.get("refreshToken"));
        assertEquals("86400000", result.get("expiresIn"));
        assertEquals("604800000", result.get("refreshExpiresIn"));

        // 验证方法调用
        verify(jwtUtil).validateRefreshToken(refreshTokenRequest.getRefreshToken());
        verify(jwtUtil).extractUsername(refreshTokenRequest.getRefreshToken());
        verify(jwtUtil).generateToken(any(Authentication.class));
        verify(jwtUtil).generateRefreshToken(any(Authentication.class));
    }

    @Test
    void refreshToken_InvalidToken() {
        // 模拟刷新Token验证失败
        when(jwtUtil.validateRefreshToken(refreshTokenRequest.getRefreshToken())).thenReturn(false);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> authService.refreshToken(refreshTokenRequest));
    }
} 