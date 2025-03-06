//package com.apps.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // 禁用 CSRF 保护
//                .cors().and() // 启用跨域配置
//                .authorizeRequests()
//                .antMatchers("/users/register", "/users/login", "/topic/reading/**").permitAll() // 允许注册、登录和 /topic/reading 相关路径
//                .anyRequest().authenticated() // 其他请求需要认证
//                .and()
//                .httpBasic(); // 如果需要基本认证
//    }
//}