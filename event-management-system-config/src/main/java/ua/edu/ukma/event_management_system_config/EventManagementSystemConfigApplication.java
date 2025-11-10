package ua.edu.ukma.event_management_system_config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class EventManagementSystemConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementSystemConfigApplication.class, args);
	}

}
