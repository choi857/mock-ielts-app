package com;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages =  "com.apps.mapper" )
//@ComponentScan(basePackages = {"com.apps.controller", "com.apps.mapper","com.apps.service","com.apps.config"})
@SpringBootApplication
public class MockIeltsTopicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockIeltsTopicApplication.class, args);
    }

}
