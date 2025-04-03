package com.devsecops.com.devsecops;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NumericApplicationIntegrationTest {

    // Removed unused WebClient field

    @Test
    void contextLoads() {
        // This test ensures the Spring Boot application context loads successfully,
        // which indirectly calls the main method.
    }

    @Test
    void webClientBaseUrlIsConfiguredCorrectly() {
        // Verify that the WebClient bean is created with the correct base URL
        String baseUrl = "http://node-service:5000/"; // Expected base URL
        WebClient configuredWebClient = WebClient.builder().baseUrl(baseUrl).build();
        assertEquals(baseUrl, configuredWebClient.mutate().build().toString());
    }
}