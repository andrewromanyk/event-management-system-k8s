package ua.edu.ukma.event_management_micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;
import org.springframework.scheduling.annotation.EnableAsync;

@Modulith(sharedModules = "core")
@SpringBootApplication
@EnableAsync
public class EventManagementMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementMicroApplication.class, args);
	}

}
