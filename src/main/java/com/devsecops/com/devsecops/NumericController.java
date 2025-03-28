package com.devsecops.com.devsecops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class NumericController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebClient webClient;

    @Autowired
    public NumericController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/")
    public String welcome() {
        return "Kubernetes DevSecOps";
    }

    @GetMapping("/compare/{value}")
    public String compareToFifty(@PathVariable int value) {
        if (value > 50) {
            return "Greater than 50";
        } else {
            return "Smaller than or equal to 50";
        }
    }

    @GetMapping("/increment/{value}")
    public int increment(@PathVariable int value) {
        String response = webClient.get()
                .uri("/plusone/{value}", value)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        logger.info("Value Received in Request - {}", value);
        logger.info("Node Service Response - {}", response);
        return Integer.parseInt(response);
    }
}