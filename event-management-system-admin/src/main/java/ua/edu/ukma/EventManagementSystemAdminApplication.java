package ua.edu.ukma;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class EventManagementSystemAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementSystemAdminApplication.class, args);
	}

}
