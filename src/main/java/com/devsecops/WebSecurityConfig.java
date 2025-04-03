package com.devsecops;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF protection is enabled by default, no need to disable it
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll() // Adjust this based on your needs
                .anyRequest().authenticated()
            );

        return http.build();
    }
}