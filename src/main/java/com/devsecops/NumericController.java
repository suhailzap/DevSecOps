package com.devsecops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@RestController
public class NumericController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String BASE_URL = "http://node-service:5000/plusone";

    private final RestTemplate restTemplate;

    public NumericController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String welcome() {
        return "Kubernetes DevSecOps";
    }

    @GetMapping("/compare/{value}")
    public String compareToFifty(@PathVariable int value) {
        if (value > 50) {
            return "Greater than 50";
        }
        return "Smaller than or equal to 50";
    }

    @GetMapping("/increment/{value}")
    public int increment(@PathVariable int value) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL + "/" + value, String.class);
            String response = responseEntity.getBody();
            logger.info("Value Received in Request: {}", value);
            logger.info("Node Service Response: {}", response);
            // Handle potential null or invalid response
            if (response == null || response.trim().isEmpty()) {
                throw new IllegalStateException("Empty response from node service");
            }
            return Integer.parseInt(response);
        } catch (RestClientException e) {
            logger.error("Failed to call node service at {}: {}", BASE_URL, e.getMessage());
            throw new RuntimeException("Error communicating with node service", e);
        } catch (NumberFormatException e) {
            logger.error("Invalid response from node service: {}", e.getMessage());
            throw new RuntimeException("Invalid number format in response", e);
        }
    }
}