package com.apps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(basePackages =  "com.apps.mapper" )
@SpringBootApplication
public class MockIeltsSpeakListenApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockIeltsSpeakListenApplication.class, args);
    }
}