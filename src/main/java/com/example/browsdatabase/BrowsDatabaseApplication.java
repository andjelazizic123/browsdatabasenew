package com.example.browsdatabase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BrowsDatabaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrowsDatabaseApplication.class, args);
    }
}
