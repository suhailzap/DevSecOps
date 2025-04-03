package com.devsecops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(username = "user", roles = {"USER"}) // Simulates an authenticated user
class NumericApplicationTests {  // Package-private visibility, correct for JUnit 5

    private MockMvc mockMvc;

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    // Setup MockMvc manually since we're not using @WebMvcTest
    // Setup MockMvc manually since we're not using @WebMvcTest
    @Autowired
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Inject the mocked RestTemplate into the application context if needed
    }
    @Test
    void mainMethodRunsWithoutException() {
        NumericApplication.main(new String[]{});
        // No assertion needed; ensures it runs without throwing an exception
    }

    @Test
    void welcomeMessage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Kubernetes DevSecOps"));
    }

    @Test
    void smallerThanOrEqualToFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Smaller than or equal to 50"));
    }

    @Test
    void greaterThanFiftyMessage() throws Exception {
        this.mockMvc.perform(get("/compare/51"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Greater than 50"));
    }

    @Test
    void incrementValue() throws Exception {
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("51"));

        this.mockMvc.perform(get("/increment/50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("51"));
    }
}