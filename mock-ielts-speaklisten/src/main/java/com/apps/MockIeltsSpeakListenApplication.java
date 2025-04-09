package com.apps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan(basePackages =  "com.apps.mapper" )
@SpringBootApplication
@EnableAsync
public class MockIeltsSpeakListenApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockIeltsSpeakListenApplication.class, args);
    }
}