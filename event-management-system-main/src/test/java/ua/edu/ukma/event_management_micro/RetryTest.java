package ua.edu.ukma.event_management_micro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.event_management_micro.core.CoreService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableRetry
@TestPropertySource(properties = {
        "secret.name=testuser",
        "secret.pass=testpass"
})
class RetryTest {

    @Autowired
    private CoreService coreService;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void shouldRetryCallWithTokenOnRestClientException() {
        coreService.setJwtToken("test-token");

        when(restTemplate.exchange(
                eq("http://test-endpoint"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class)))
                .thenThrow(new RestClientException("Connection failed"))
                .thenThrow(new RestClientException("Timeout"))
                .thenReturn(ResponseEntity.ok("Success"));

        ResponseEntity<?> result = coreService.callWithToken(
                "http://test-endpoint",
                HttpMethod.GET,
                String.class
        );

        verify(restTemplate, times(3)).exchange(
                eq("http://test-endpoint"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class)
        );

        Assertions.assertEquals("Success", result.getBody());
    }
}