package com.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 禁用 CSRF 保护（开发环境中可以禁用，生产环境建议启用）
                .cors().and() // 启用跨域配置
                .authorizeRequests()
                .antMatchers("/users/register", "/users/login").permitAll() // 允许注册和登录接口无需认证
                .anyRequest().permitAll(); // 暂时允许所有请求无需认证（开发环境）
    }
}