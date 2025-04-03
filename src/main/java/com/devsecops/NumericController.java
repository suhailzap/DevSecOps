package com.devsecops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

// Custom exception class
class NodeServiceException extends RuntimeException {
    public NodeServiceException(String message) {
        super(message);
    }

    public NodeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

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
        String url = BASE_URL + "/" + value;
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            String response = responseEntity.getBody();
            logger.info("Value Received in Request: {}", value);
            logger.info("Node Service Response: {}", response);
            // Handle potential null or invalid response
            if (response == null || response.trim().isEmpty()) {
                throw new NodeServiceException("Empty or null response from node service for value: " + value);
            }
            return Integer.parseInt(response);
        } catch (RestClientException e) {
            logger.error("Failed to call node service at URL: {} for value: {}", url, value, e); // Enhanced logging
            throw new NodeServiceException("Failed to increment value " + value + " due to node service communication error", e);
        } catch (NumberFormatException e) {
            logger.error("Invalid response from node service at URL: {} for value: {}, response: {}", url, value, e.getMessage());
            throw new NodeServiceException("Invalid number format in response for value " + value, e);
        }
    }
}