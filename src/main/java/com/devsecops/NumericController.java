package com.devsecops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/") // Base mapping for all endpoints
public class NumericController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumericController.class);

    private static final String BASE_URL = "http://node-service:5000/plusone"; // Fixed naming

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String welcome() {
        return "Kubernetes DevSecOps";
    }

    @GetMapping("/compare/{value}")
    public String compareToFifty(@PathVariable int value) {
        return (value > 50) ? "Greater than 50" : "Smaller than or equal to 50";
    }

    @GetMapping("/increment/{value}")
    public int increment(@PathVariable int value) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL + '/' + value, String.class);
        String response = responseEntity.getBody();

        LOGGER.info("Value Received in Request - {}", value);
        LOGGER.info("Node Service Response - {}", response);

        return Integer.parseInt(response);
    }
}
