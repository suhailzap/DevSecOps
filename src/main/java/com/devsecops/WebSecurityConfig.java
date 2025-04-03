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
            .csrf(csrf -> csrf.disable()) // Updated syntax to disable CSRF
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