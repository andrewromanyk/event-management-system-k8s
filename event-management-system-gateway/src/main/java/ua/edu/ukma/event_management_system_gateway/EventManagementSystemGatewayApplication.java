package ua.edu.ukma.event_management_system_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EventManagementSystemGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementSystemGatewayApplication.class, args);
	}

}
