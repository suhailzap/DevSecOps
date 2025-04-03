package com.devsecops;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NumericApplicationTest {  // Package-private visibility for JUnit 5

    @Test
    void mainMethodRunsWithoutException() {
        NumericApplication.main(new String[]{});
        // No assertion needed; just ensures it runs without throwing an exception
    }
}