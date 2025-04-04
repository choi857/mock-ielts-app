package com.apps.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private Authentication authentication;
    private UserDetails userDetails;
    private String secret;
    private Long expiration;
    private Long refreshExpiration;

    @BeforeEach
    void setUp() {
        // 设置JWT配置
        secret = "your-secret-key-must-be-at-least-32-characters-long";
        expiration = 86400000L; // 24小时
        refreshExpiration = 604800000L; // 7天

        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", refreshExpiration);

        // 初始化JWT工具类
        jwtUtil.init();

        // 模拟认证对象
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // 模拟用户详情
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void generateToken_Success() {
        // 执行测试
        String token = jwtUtil.generateToken(authentication);

        // 验证结果
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals("testUser", jwtUtil.extractUsername(token));
    }

    @Test
    void generateRefreshToken_Success() {
        // 执行测试
        String refreshToken = jwtUtil.generateRefreshToken(authentication);

        // 验证结果
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
        assertEquals("testUser", jwtUtil.extractUsername(refreshToken));
    }

    @Test
    void validateToken_Success() {
        // 生成token
        String token = jwtUtil.generateToken(authentication);

        // 验证token
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        // 验证结果
        assertTrue(isValid);
    }

    @Test
    void validateToken_InvalidUsername() {
        // 生成token
        String token = jwtUtil.generateToken(authentication);

        // 修改用户详情
        when(userDetails.getUsername()).thenReturn("differentUser");

        // 验证token
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        // 验证结果
        assertFalse(isValid);
    }

    @Test
    void validateRefreshToken_Success() {
        // 生成刷新token
        String refreshToken = jwtUtil.generateRefreshToken(authentication);

        // 验证刷新token
        boolean isValid = jwtUtil.validateRefreshToken(refreshToken);

        // 验证结果
        assertTrue(isValid);
    }

    @Test
    void validateRefreshToken_InvalidToken() {
        // 验证无效的刷新token
        boolean isValid = jwtUtil.validateRefreshToken("invalid.token.string");

        // 验证结果
        assertFalse(isValid);
    }

    @Test
    void extractExpiration_Success() {
        // 生成token
        String token = jwtUtil.generateToken(authentication);

        // 提取过期时间
        Date expirationDate = jwtUtil.extractExpiration(token);

        // 验证结果
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
} 