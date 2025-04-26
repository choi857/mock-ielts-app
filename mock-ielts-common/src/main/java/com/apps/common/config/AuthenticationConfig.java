package com.apps.common.config;

import com.apps.common.mapper.UserMapper;
import com.apps.common.security.JwtAuthenticationProvider;
import com.apps.common.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证配置类
 */
@Configuration
public class AuthenticationConfig {

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(
            UserMapper userMapper,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        return new JwtAuthenticationProvider(
                userMapper,
                passwordEncoder,
                userDetailsService,
                jwtUtil
        );
    }
} 