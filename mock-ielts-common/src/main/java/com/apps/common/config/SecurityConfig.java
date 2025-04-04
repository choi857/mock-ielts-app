package com.apps.common.config;

import com.alibaba.nacos.common.utils.HttpMethod;
import com.apps.common.security.JwtAuthenticationFilter;
import com.apps.common.security.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * Spring Security配置类
 * 配置安全策略，包括：
 * 1. 禁用CSRF（因为我们使用JWT）
 * 2. 配置无状态会话
 * 3. 配置公开访问的URL
 * 4. 配置JWT过滤器
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    /**
     * 暂时不做限制
     * @param http
     * @return
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/**").permitAll() // 允许所有请求
                )
                .csrf().disable(); // 如果不需要CSRF保护，可以禁用

        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            // 禁用CSRF
//            .csrf().disable()
//            // 使用无状态会话
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            // 配置请求授权
//            .authorizeRequests()
//            // 允许公开访问的URL
//            .antMatchers("/auth/**", "/error").permitAll()
//            // 其他所有请求需要认证
//            .anyRequest().authenticated()
//            .and()
//            // 添加JWT过滤器
//            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
} 