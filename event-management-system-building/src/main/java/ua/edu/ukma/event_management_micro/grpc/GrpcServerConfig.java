package ua.edu.ukma.event_management_micro.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.io.File;

import static io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth.REQUIRE;

@Configuration
public class GrpcServerConfig {
    static final int PORT = 9090;

    @Value("${grpc.server.mtls.enabled:false}")
    private boolean mtlsEnabled;

    @Value("${grpc.server.cert-chain:certs/server.cert.pem}")
    private String certChainPath;

    @Value("${grpc.server.private-key:certs/server.key.pem}")
    private String privateKeyPath;

    @Value("${grpc.server.trust-cert:certs/ca.cert.pem}")
    private String trustCertPath;

    @ConditionalOnProperty(
            name = "grpc.start.enabled",
            havingValue = "true")
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server grpcServer(BuildingServer buildingService) throws SSLException {
        File certChain = new File(certChainPath);
        File key = new File(privateKeyPath);
        File trustCert = new File(trustCertPath);

        SslContext sslContext = GrpcSslContexts.forServer(certChain, key)
                .trustManager(trustCert)
                .clientAuth(REQUIRE)
                .build();

        return NettyServerBuilder.forPort(PORT)
                .sslContext(sslContext)
                .addService(buildingService)
                .build();
    }
}