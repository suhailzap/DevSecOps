package com.devsecops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NumericApplicationTests {

    @Autowired
    private MockMvc mockMvc;

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
        // Mock the WebClient chain
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        // Use lenient mocking to ensure all calls are intercepted
        doReturn(uriSpec).when(webClient).get();
        doReturn(headersSpec).when(uriSpec).uri("/50");
        doReturn(responseSpec).when(headersSpec).retrieve();
        doReturn(Mono.just("51")).when(responseSpec).bodyToMono(String.class);

        this.mockMvc.perform(get("/increment/50")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("51"));
    }
}