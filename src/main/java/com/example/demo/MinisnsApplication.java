package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "com.example.minisns"})
public class MinisnsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinisnsApplication.class, args);
    }
}

