package ua.edu.ukma.event_management_micro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
		"spring.activemq.broker-url=disabled"
})
class EventManagementMicroApplicationTests {

	@Test
	void contextLoads() {
	}

}
