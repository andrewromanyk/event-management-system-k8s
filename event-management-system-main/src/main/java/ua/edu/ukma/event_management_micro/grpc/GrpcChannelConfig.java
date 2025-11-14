package ua.edu.ukma.event_management_micro.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.edu.ukma.event_management_system_building.grpc.BuildingServiceGrpc;

@Configuration
public class GrpcChannelConfig {
    static final String ADDRESS = "localhost";
    static final int PORT = 9090;

    @Bean
    public ManagedChannel buildingChannel() {
        // Creates TCP client
        return ManagedChannelBuilder
                .forAddress(ADDRESS, PORT)
                .usePlaintext()
                .build();
    }

    @Bean
    public BuildingServiceGrpc.BuildingServiceBlockingStub buildingBlockingStub(
            ManagedChannel buildingChannel
    ) {
        return BuildingServiceGrpc.newBlockingStub(buildingChannel);
    }

    @Bean
    public BuildingServiceGrpc.BuildingServiceStub buildingAsyncStub(
            ManagedChannel buildingChannel
    ) {
        return BuildingServiceGrpc.newStub(buildingChannel);
    }
}
