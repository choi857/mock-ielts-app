package com.apps.common.security;

import com.apps.common.exception.AuthException;
import com.apps.common.exception.ErrorCode;
import com.apps.common.mapper.UserMapper;
import com.apps.common.model.entity.User;
import com.apps.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * JWT认证提供者
 * 用于处理JWT令牌的认证
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationProvider(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            // 从数据库获取用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                log.error("认证失败：用户不存在 - {}", username);
                throw new AuthException(ErrorCode.USER_NOT_FOUND);
            }

            // 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.error("认证失败：密码错误 - {}", username);
                throw new AuthException(ErrorCode.PASSWORD_ERROR);
            }

            // 加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 创建认证令牌
            return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("认证失败：{}", e.getMessage());
            throw new AuthException(ErrorCode.SYSTEM_ERROR, "认证失败：" + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
} 