package ua.edu.ukma.event_management_micro.contracts.building;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.stubrunner.spring.cloud.StubRunnerSpringCloudAutoConfiguration;
import org.springframework.cloud.contract.verifier.messaging.jms.ContractVerifierJmsConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ua.edu.ukma.event_management_micro.core.client.BuildingClient;
import ua.edu.ukma.event_management_micro.core.dto.BuildingDto;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "ua.edu.ukma:event-management-building:+:stubs:8082"
)
@TestPropertySource(properties = {
        "feign.client.building.url=http://localhost:8082",
        "spring.jms.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.cloud.contract.verifier.messaging.jms.enabled=false"
})
@EnableAutoConfiguration(exclude = {
        JmsAutoConfiguration.class
})
public class BuildingClientContractTest {

    @Autowired
    private BuildingClient buildingClient;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8082";

    @Test
    public void shouldGetBuildingById() {
        ResponseEntity<BuildingDto> response = buildingClient.getBuildingById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    public void shouldReturnAllBuildings() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BuildingDto[]> response = restTemplate.exchange(
                baseUrl + "/api/building",
                org.springframework.http.HttpMethod.GET,
                entity,
                BuildingDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void shouldCreateNewBuilding() {
        BuildingDto newBuilding = new BuildingDto();
        newBuilding.setAddress("New Building Address");
        newBuilding.setHourlyRate(75);
        newBuilding.setAreaM2(600);
        newBuilding.setCapacity(150);
        newBuilding.setDescription("A brand new building for events");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<BuildingDto> entity = new HttpEntity<>(newBuilding, headers);

        ResponseEntity<BuildingDto> response = restTemplate.postForEntity(
                baseUrl + "/api/building",
                entity,
                BuildingDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    public void shouldUpdateBuilding() {
        // Given
        BuildingDto updatedBuilding = new BuildingDto();
        updatedBuilding.setAddress("Updated Address");
        updatedBuilding.setHourlyRate(80);
        updatedBuilding.setAreaM2(550);
        updatedBuilding.setCapacity(120);
        updatedBuilding.setDescription("Updated description");

        // When
        restTemplate.put(
                baseUrl + "/api/building/1",
                updatedBuilding
        );

    }

    @Test
    public void shouldDeleteBuilding() {
        restTemplate.delete(baseUrl + "/api/building/1");
    }


    @Test
    public void shouldReturnBuildingsByCapacity() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<BuildingDto[]> response = restTemplate.exchange(
                baseUrl + "/api/building?capacity=100",
                org.springframework.http.HttpMethod.GET,
                entity,
                BuildingDto[].class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(Arrays.stream(response.getBody()))
                .allMatch(b -> b.getCapacity() >= 100);
    }

    @Test
    public void shouldReturn404ForNonExistentBuilding() {
        // When/Then
        assertThatThrownBy(() ->
                buildingClient.getBuildingById(999999L)
        )
                .hasMessageContaining("404");
    }
}