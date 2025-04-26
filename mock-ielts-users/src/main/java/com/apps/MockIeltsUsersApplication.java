// MockIeltsUsersApplication.java
package com.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class MockIeltsUsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockIeltsUsersApplication.class, args);
    }
}