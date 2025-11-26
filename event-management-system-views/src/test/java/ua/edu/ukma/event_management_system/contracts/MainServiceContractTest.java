package ua.edu.ukma.event_management_system.contracts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ua.edu.ukma.event_management_system.clients.EventClient;
import ua.edu.ukma.event_management_system.clients.UserClient;
import ua.edu.ukma.event_management_system.dto.EventDto;
import ua.edu.ukma.event_management_system.dto.UserDto;
import ua.edu.ukma.event_management_system.dto.UserRole;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "ua.edu.ukma:event-management-main:+:stubs:8081"
)
@TestPropertySource(properties = {
        "feign.client.user.url=http://localhost:8081",
        "feign.client.event.url=http://localhost:8081",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false"
})
class MainServiceContractTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private EventClient eventClient;


    @Test
    void shouldReturnUserByUsername() {
        ResponseEntity<UserDto> response = userClient.getUser("user");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        UserDto user = response.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void shouldReturnUserById() {
        ResponseEntity<UserDto> response = userClient.getUserById(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        UserDto user = response.getBody();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getUserRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void shouldReturnAllUsers() {
        List<UserDto> users = userClient.getAllUsers();

        assertThat(users).isNotNull();
        assertThat(users.isEmpty()).isFalse();
        assertThat(users.stream().anyMatch(u -> u.getUsername().equals("admin"))).isTrue();
    }


    @Test
    void shouldReturnEventById() {
        EventDto event = eventClient.getEventById(1L);

        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getEventTitle()).isEqualTo("Sample Event");
        assertThat(event.getBuildingId()).isEqualTo(1L);
        assertThat(event.getPrice()).isEqualTo(50.0);
    }

    @Test
    void shouldReturnAllEvents() {
        List<EventDto> events = eventClient.getAllEvents();

        assertThat(events).isNotNull();
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.getFirst().getEventTitle()).isEqualTo("Sample Event");
    }

    @Test
    void shouldReturnAllRelevantEvents() {
        List<EventDto> events = eventClient.getAllRelevantEvents();

        assertThat(events).isNotNull();
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.getFirst().getEventTitle()).isEqualTo("Sample Event");
    }
}