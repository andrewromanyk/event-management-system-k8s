package ua.edu.ukma.event_management_micro.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {
    static final int PORT = 9090;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server grpcServer(BuildingServer buildingService) {
        return ServerBuilder
                .forPort(PORT)
                .addService(buildingService)
                .build();
    }
}