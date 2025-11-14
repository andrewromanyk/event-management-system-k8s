package ua.edu.ukma.event_management_micro.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.edu.ukma.event_management_system_building.grpc.BuildingServiceGrpc;

import javax.net.ssl.SSLException;
import java.io.File;

@Configuration
public class GrpcChannelConfig {
    static final String ADDRESS = "localhost";
    static final int PORT = 9090;

    @Value("${grpc.client.mtls.enabled:false}")
    private boolean mtlsEnabled;

    @Value("${grpc.client.trust-cert:certs/ca.cert.pem}")
    private String trustCertPath;

    @Value("${grpc.client.cert:certs/client.cert.pem}")
    private String clientCertPath;

    @Value("${grpc.client.key:certs/client.key.pem}")
    private String clientKeyPath;


    @Bean
    public ManagedChannel buildingChannel() throws SSLException {
        if (mtlsEnabled) {
            System.out.println("Creating gRPC channel with mTLS to " + ADDRESS + ":" + PORT);

            SslContext sslContext = GrpcSslContexts.forClient()
                    .trustManager(new File(trustCertPath))
                    .keyManager(new File(clientCertPath), new File(clientKeyPath))
                    .build();

            return NettyChannelBuilder.forAddress(ADDRESS, PORT)
                    .sslContext(sslContext)
                    .build();
        } else {
            System.out.println("Creating gRPC channel WITHOUT mTLS to " + ADDRESS + ":" + PORT);
            return NettyChannelBuilder.forAddress(ADDRESS, PORT)
                    .usePlaintext()
                    .build();
        }
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
