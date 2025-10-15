package ua.edu.ukma.event_management_micro.core;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.event_management_micro.core.dto.LoginRequest;

@Service
public class CoreService {

    @Setter
    private String JwtToken;
    private RestTemplate restTemplate;

    @Value("${secret.name}")
    private String name;
    @Value("${secret.pass}")
    private String pass;

    @Autowired
    public CoreService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CoreService() {}

    private void requestJwtToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(name);
        loginRequest.setPassword(pass);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api-login", loginRequest, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            this.JwtToken = response.getBody();
        } else {
            throw new RuntimeException("Failed to get JWT token");
        }
    }

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 500),
            retryFor = RestClientException.class)
    public ResponseEntity<?> callWithToken(String endPoint, HttpMethod method, Class<?> responseType) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + JwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                endPoint,
                method,
                entity,
                responseType
        );
    }

    @Recover
    public ResponseEntity<?> recover(RestClientException e, String endPoint, HttpMethod method, Class<?> responseType) {
        requestJwtToken();
        return callWithToken(endPoint, method, responseType);
    }

}
