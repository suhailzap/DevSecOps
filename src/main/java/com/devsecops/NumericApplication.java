package com.devsecops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NumericApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumericApplication.class, args); // Entry point, typically not tested directly
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}