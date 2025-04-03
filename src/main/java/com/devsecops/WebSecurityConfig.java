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
            // Disabling CSRF is safe here because:
            // 1. All exposed endpoints (/increment/*, /, /compare/*) are GET requests, which are not state-changing.
            // 2. The app is a backend API called by non-browser clients (e.g., curl, other services, or external clients via Istio).
            // 3. The service is a ClusterIP, not directly exposed to the internet.
            // Revisit this if state-changing endpoints (e.g., POST, PUT) are added or if browser-based clients are introduced.
            .csrf(csrf -> csrf.disable())
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