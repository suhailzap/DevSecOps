package com.devsecops;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF protection is enabled by default (Spring Security's default behavior).
            // This is safe because:
            // 1. All exposed endpoints (/increment/*, /, /compare/*) are GET requests, which are not affected by CSRF protection.
            // 2. The app is a backend API called by non-browser clients (e.g., curl, other services, or external clients via Istio).
            // If state-changing endpoints (e.g., POST, PUT, DELETE) are added in the future, clients will need to include a CSRF token.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/increment/**").permitAll() // Allow unauthenticated access to /increment/*
                .requestMatchers("/").permitAll() // Allow unauthenticated access to the root endpoint
                .requestMatchers("/compare/**").permitAll() // Allow unauthenticated access to /compare/*
                .anyRequest().authenticated() // Require authentication for all other endpoints
            )
            .httpBasic(httpBasic -> httpBasic.realmName("Realm")); // Configure Basic Authentication with the same realm as before

        return http.build();
    }
}