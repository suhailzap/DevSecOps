package com.devsecops;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Removed unused import
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}  