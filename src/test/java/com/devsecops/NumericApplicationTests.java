package com.devsecops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NumericController.class)
class NumericApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("deprecation") // Suppress if tool insists on deprecation
    @MockBean
    private WebClient webClient;

    @Test
    void welcomeMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Kubernetes DevSecOps"));
    }

    @Test
    void smallerThanOrEqualToFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/50")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Smaller than or equal to 50"));
    }

    @Test
    void greaterThanFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/51")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Greater than 50"));
    }

    @Test
    void incrementValue() throws Exception {
        when(webClient.get()
                .uri("/plusone/{value}", 50)
                .retrieve()
                .bodyToMono(String.class))
                .thenReturn(Mono.just("51"));

        this.mockMvc.perform(get("/increment/50")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("51"));
    }
}