package ua.edu.ukma.event_management_micro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
		"spring.activemq.broker-url=disabled"
})
class EventManagementMicroApplicationTests {

	@Test
	@DirtiesContext
	void contextLoads() {
		// Test to ensure the application context loads successfully
	}

}
