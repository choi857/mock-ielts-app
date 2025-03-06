package com.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 允许所有来源访问
                registry.addMapping("/**") // 对所有路径启用跨域支持
                        .allowedOriginPatterns("*") // 允许所有来源（暂时不限制）
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                        .allowedHeaders("*") // 允许所有头部信息
                        .allowCredentials(true); // 允许携带身份验证信息（如 Cookie）
            }
        };
    }
}